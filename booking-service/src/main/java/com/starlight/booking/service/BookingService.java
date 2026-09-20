package com.starlight.booking.service;

import com.starlight.booking.dto.BookingRequest;
import com.starlight.booking.dto.BookingResponse;
import com.starlight.booking.dto.CancelBookingRequest;

import java.util.List;

public interface BookingService {
    BookingResponse createBooking(BookingRequest request, Long userId);
    BookingResponse cancelBooking(Long bookingId, CancelBookingRequest request, Long currentUserId, boolean isAdmin);
    BookingResponse getBookingById(Long id);
    BookingResponse getBookingByReference(String reference);
    List<BookingResponse> getUserBookings(Long userId);
    List<BookingResponse> getAllBookings();
}
