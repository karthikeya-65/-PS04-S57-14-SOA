package com.starlight.booking.service;

import com.starlight.booking.client.RoomServiceClient;
import com.starlight.booking.client.UserServiceClient;
import com.starlight.booking.dto.ApiResponse;
import com.starlight.booking.dto.BookingRequest;
import com.starlight.booking.dto.BookingResponse;
import com.starlight.booking.dto.CancelBookingRequest;
import com.starlight.booking.dto.client.HotelDto;
import com.starlight.booking.dto.client.RoomTypeDto;
import com.starlight.booking.dto.client.UserDto;
import com.starlight.booking.entity.Booking;
import com.starlight.booking.entity.BookingStatus;
import com.starlight.booking.exception.InvalidBookingException;
import com.starlight.booking.exception.ResourceNotFoundException;
import com.starlight.booking.exception.RoomNotAvailableException;
import com.starlight.booking.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final AvailabilityService availabilityService;
    private final RoomServiceClient roomServiceClient;
    private final UserServiceClient userServiceClient;

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public synchronized BookingResponse createBooking(BookingRequest request, Long userId) {
        validateBookingDates(request.getCheckInDate(), request.getCheckOutDate());

        // 1. Verify User exists
        try {
            ApiResponse<UserDto> userResponse = userServiceClient.getUserById(userId);
            if (userResponse == null || userResponse.getData() == null) {
                throw new ResourceNotFoundException("User not found with ID: " + userId);
            }
        } catch (Exception e) {
            log.warn("UserService check failed or skipped: {}", e.getMessage());
        }

        // 2. Fetch and verify Room Type
        ApiResponse<RoomTypeDto> roomTypeResponse = roomServiceClient.getRoomTypeById(request.getRoomTypeId());
        if (roomTypeResponse == null || roomTypeResponse.getData() == null) {
            throw new ResourceNotFoundException("Room type not found with ID: " + request.getRoomTypeId());
        }
        RoomTypeDto roomType = roomTypeResponse.getData();

        if (request.getNumGuests() > (roomType.getMaxOccupancy() * request.getNumRooms())) {
            throw new InvalidBookingException("Number of guests exceeds total room capacity (" +
                    (roomType.getMaxOccupancy() * request.getNumRooms()) + " max)");
        }

        // 3. Real-time availability check
        boolean available = availabilityService.isAvailable(
                request.getRoomTypeId(),
                request.getCheckInDate(),
                request.getCheckOutDate(),
                request.getNumRooms()
        );

        if (!available) {
            throw new RoomNotAvailableException("No rooms available for " + roomType.getName() +
                    " between " + request.getCheckInDate() + " and " + request.getCheckOutDate());
        }

        // 4. Calculate pricing
        long nights = ChronoUnit.DAYS.between(request.getCheckInDate(), request.getCheckOutDate());
        BigDecimal totalAmount = roomType.getBasePricePerNight()
                .multiply(BigDecimal.valueOf(nights))
                .multiply(BigDecimal.valueOf(request.getNumRooms()));

        // 5. Generate reference
        String bookingReference = "SLS-" + LocalDate.now().getYear() + "-" +
                UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        Booking booking = Booking.builder()
                .bookingReference(bookingReference)
                .userId(userId)
                .hotelId(request.getHotelId())
                .roomTypeId(request.getRoomTypeId())
                .checkInDate(request.getCheckInDate())
                .checkOutDate(request.getCheckOutDate())
                .numRooms(request.getNumRooms())
                .numGuests(request.getNumGuests())
                .totalAmount(totalAmount)
                .status(BookingStatus.CONFIRMED)
                .specialRequests(request.getSpecialRequests())
                .build();

        Booking savedBooking = bookingRepository.save(booking);

        return mapToResponse(savedBooking, roomType.getName(), null);
    }

    @Override
    @Transactional
    public BookingResponse cancelBooking(Long bookingId, CancelBookingRequest request, Long currentUserId, boolean isAdmin) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with ID: " + bookingId));

        if (!isAdmin && !booking.getUserId().equals(currentUserId)) {
            throw new InvalidBookingException("You are not authorized to cancel this booking");
        }

        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new InvalidBookingException("Booking is already cancelled");
        }

        if (booking.getStatus() == BookingStatus.CHECKED_IN || booking.getStatus() == BookingStatus.CHECKED_OUT) {
            throw new InvalidBookingException("Cannot cancel booking with status: " + booking.getStatus());
        }

        if (LocalDate.now().isAfter(booking.getCheckInDate())) {
            throw new InvalidBookingException("Cannot cancel reservation after check-in date has passed");
        }

        booking.setStatus(BookingStatus.CANCELLED);
        booking.setCancellationReason(request.getCancellationReason());
        booking.setCancelledAt(LocalDateTime.now());

        Booking updated = bookingRepository.save(booking);
        return mapToResponse(updated, null, null);
    }

    @Override
    @Transactional(readOnly = true)
    public BookingResponse getBookingById(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with ID: " + id));
        return mapToResponse(booking, null, null);
    }

    @Override
    @Transactional(readOnly = true)
    public BookingResponse getBookingByReference(String reference) {
        Booking booking = bookingRepository.findByBookingReference(reference)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with reference: " + reference));
        return mapToResponse(booking, null, null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponse> getUserBookings(Long userId) {
        return bookingRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(b -> mapToResponse(b, null, null))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponse> getAllBookings() {
        return bookingRepository.findAll().stream()
                .map(b -> mapToResponse(b, null, null))
                .collect(Collectors.toList());
    }

    private void validateBookingDates(LocalDate checkIn, LocalDate checkOut) {
        if (checkIn == null || checkOut == null) {
            throw new InvalidBookingException("Check-in and check-out dates are required");
        }
        if (!checkOut.isAfter(checkIn)) {
            throw new InvalidBookingException("Check-out date must be strictly after check-in date");
        }
        if (checkIn.isBefore(LocalDate.now())) {
            throw new InvalidBookingException("Check-in date cannot be in the past");
        }
    }

    private BookingResponse mapToResponse(Booking booking, String roomTypeName, String hotelName) {
        return BookingResponse.builder()
                .id(booking.getId())
                .bookingReference(booking.getBookingReference())
                .userId(booking.getUserId())
                .hotelId(booking.getHotelId())
                .hotelName(hotelName)
                .roomTypeId(booking.getRoomTypeId())
                .roomTypeName(roomTypeName)
                .checkInDate(booking.getCheckInDate())
                .checkOutDate(booking.getCheckOutDate())
                .numRooms(booking.getNumRooms())
                .numGuests(booking.getNumGuests())
                .totalAmount(booking.getTotalAmount())
                .status(booking.getStatus())
                .specialRequests(booking.getSpecialRequests())
                .cancellationReason(booking.getCancellationReason())
                .cancelledAt(booking.getCancelledAt())
                .createdAt(booking.getCreatedAt())
                .build();
    }
}
