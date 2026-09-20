package com.starlight.booking;

import com.starlight.booking.client.RoomServiceClient;
import com.starlight.booking.client.UserServiceClient;
import com.starlight.booking.dto.*;
import com.starlight.booking.dto.client.RoomTypeDto;
import com.starlight.booking.dto.client.UserDto;
import com.starlight.booking.entity.BookingStatus;
import com.starlight.booking.service.BookingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;

@SpringBootTest
@ActiveProfiles("dev")
@Transactional
class BookingServiceApplicationTests {

    @Autowired
    private BookingService bookingService;

    @MockBean
    private RoomServiceClient roomServiceClient;

    @MockBean
    private UserServiceClient userServiceClient;

    @BeforeEach
    void setUp() {
        RoomTypeDto mockRoomType = RoomTypeDto.builder()
                .id(1L)
                .hotelId(1L)
                .hotelName("Starlight Grand")
                .name("Deluxe Suite")
                .basePricePerNight(new BigDecimal("200.00"))
                .maxOccupancy(2)
                .totalInventory(5)
                .isActive(true)
                .build();

        UserDto mockUser = UserDto.builder()
                .id(1L)
                .username("guest1")
                .email("guest1@starlight.com")
                .role("ROLE_GUEST")
                .build();

        Mockito.when(roomServiceClient.getRoomTypeById(anyLong()))
                .thenReturn(ApiResponse.ok(mockRoomType));

        Mockito.when(userServiceClient.getUserById(anyLong()))
                .thenReturn(ApiResponse.ok(mockUser));
    }

    @Test
    void testCreateAndCancelBooking() {
        LocalDate checkIn = LocalDate.now().plusDays(5);
        LocalDate checkOut = LocalDate.now().plusDays(8);

        BookingRequest request = BookingRequest.builder()
                .hotelId(1L)
                .roomTypeId(1L)
                .checkInDate(checkIn)
                .checkOutDate(checkOut)
                .numRooms(1)
                .numGuests(2)
                .specialRequests("High floor please")
                .build();

        BookingResponse response = bookingService.createBooking(request, 1L);
        assertNotNull(response);
        assertNotNull(response.getBookingReference());
        assertEquals(BookingStatus.CONFIRMED, response.getStatus());
        assertEquals(new BigDecimal("600.00"), response.getTotalAmount()); // 3 nights * $200

        // Cancel booking
        CancelBookingRequest cancelRequest = CancelBookingRequest.builder()
                .cancellationReason("Change of travel plans")
                .build();

        BookingResponse cancelled = bookingService.cancelBooking(response.getId(), cancelRequest, 1L, false);
        assertEquals(BookingStatus.CANCELLED, cancelled.getStatus());
        assertEquals("Change of travel plans", cancelled.getCancellationReason());
        assertNotNull(cancelled.getCancelledAt());
    }
}
