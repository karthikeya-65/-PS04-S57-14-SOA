package com.starlight.booking.service;

import com.starlight.booking.client.RoomServiceClient;
import com.starlight.booking.dto.ApiResponse;
import com.starlight.booking.dto.AvailabilityRequest;
import com.starlight.booking.dto.AvailabilityResponse;
import com.starlight.booking.dto.client.RoomTypeDto;
import com.starlight.booking.entity.BookingStatus;
import com.starlight.booking.exception.InvalidBookingException;
import com.starlight.booking.exception.ResourceNotFoundException;
import com.starlight.booking.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AvailabilityServiceImpl implements AvailabilityService {

    private final BookingRepository bookingRepository;
    private final RoomServiceClient roomServiceClient;

    private static final List<BookingStatus> ACTIVE_STATUSES = List.of(
            BookingStatus.CONFIRMED,
            BookingStatus.PENDING,
            BookingStatus.CHECKED_IN
    );

    @Override
    @Transactional(readOnly = true)
    public AvailabilityResponse checkAvailability(AvailabilityRequest request) {
        validateDates(request.getCheckInDate(), request.getCheckOutDate());

        ApiResponse<RoomTypeDto> roomTypeResponse = roomServiceClient.getRoomTypeById(request.getRoomTypeId());
        if (roomTypeResponse == null || roomTypeResponse.getData() == null) {
            throw new ResourceNotFoundException("Room type not found with ID: " + request.getRoomTypeId());
        }

        RoomTypeDto roomType = roomTypeResponse.getData();
        int totalInventory = roomType.getTotalInventory() != null ? roomType.getTotalInventory() : 0;

        int bookedRooms = bookingRepository.countOverlappingBookedRooms(
                request.getRoomTypeId(),
                request.getCheckInDate(),
                request.getCheckOutDate(),
                ACTIVE_STATUSES
        );

        int availableRooms = Math.max(0, totalInventory - bookedRooms);
        boolean isAvailable = availableRooms > 0;

        return AvailabilityResponse.builder()
                .roomTypeId(roomType.getId())
                .roomTypeName(roomType.getName())
                .checkInDate(request.getCheckInDate())
                .checkOutDate(request.getCheckOutDate())
                .totalInventory(totalInventory)
                .bookedRooms(bookedRooms)
                .availableRooms(availableRooms)
                .isAvailable(isAvailable)
                .pricePerNight(roomType.getBasePricePerNight())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isAvailable(Long roomTypeId, LocalDate checkInDate, LocalDate checkOutDate, int requestedRooms) {
        validateDates(checkInDate, checkOutDate);

        ApiResponse<RoomTypeDto> roomTypeResponse = roomServiceClient.getRoomTypeById(roomTypeId);
        if (roomTypeResponse == null || roomTypeResponse.getData() == null) {
            throw new ResourceNotFoundException("Room type not found with ID: " + roomTypeId);
        }

        int totalInventory = roomTypeResponse.getData().getTotalInventory() != null
                ? roomTypeResponse.getData().getTotalInventory()
                : 0;

        int bookedRooms = bookingRepository.countOverlappingBookedRooms(
                roomTypeId,
                checkInDate,
                checkOutDate,
                ACTIVE_STATUSES
        );

        int availableRooms = totalInventory - bookedRooms;
        return availableRooms >= requestedRooms;
    }

    private void validateDates(LocalDate checkIn, LocalDate checkOut) {
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
}
