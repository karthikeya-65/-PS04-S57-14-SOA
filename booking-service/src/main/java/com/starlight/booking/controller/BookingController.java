package com.starlight.booking.controller;

import com.starlight.booking.dto.*;
import com.starlight.booking.service.AvailabilityService;
import com.starlight.booking.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;
    private final AvailabilityService availabilityService;

    @PostMapping
    public ResponseEntity<ApiResponse<BookingResponse>> createBooking(
            @Valid @RequestBody BookingRequest request,
            @RequestHeader(value = "X-User-Id", required = false) String userIdHeader,
            @RequestParam(value = "userId", required = false) Long userIdParam) {

        Long effectiveUserId = resolveUserId(userIdHeader, userIdParam);
        BookingResponse response = bookingService.createBooking(request, effectiveUserId);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Booking confirmed successfully", response));
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<BookingResponse>> cancelBooking(
            @PathVariable("id") Long id,
            @Valid @RequestBody CancelBookingRequest request,
            @RequestHeader(value = "X-User-Id", required = false) String userIdHeader,
            @RequestHeader(value = "X-User-Roles", required = false) String rolesHeader,
            @RequestParam(value = "userId", required = false) Long userIdParam) {

        Long effectiveUserId = resolveUserId(userIdHeader, userIdParam);
        boolean isAdmin = StringUtils.hasText(rolesHeader) && rolesHeader.contains("ROLE_ADMIN");

        BookingResponse response = bookingService.cancelBooking(id, request, effectiveUserId, isAdmin);
        return ResponseEntity.ok(ApiResponse.ok("Booking cancelled successfully", response));
    }

    @PostMapping("/check-availability")
    public ResponseEntity<ApiResponse<AvailabilityResponse>> checkAvailability(
            @Valid @RequestBody AvailabilityRequest request) {
        AvailabilityResponse response = availabilityService.checkAvailability(request);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BookingResponse>> getBookingById(@PathVariable("id") Long id) {
        BookingResponse response = bookingService.getBookingById(id);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @GetMapping("/reference/{reference}")
    public ResponseEntity<ApiResponse<BookingResponse>> getBookingByReference(@PathVariable("reference") String reference) {
        BookingResponse response = bookingService.getBookingByReference(reference);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @GetMapping("/my-bookings")
    public ResponseEntity<ApiResponse<List<BookingResponse>>> getMyBookings(
            @RequestHeader(value = "X-User-Id", required = false) String userIdHeader,
            @RequestParam(value = "userId", required = false) Long userIdParam) {

        Long effectiveUserId = resolveUserId(userIdHeader, userIdParam);
        List<BookingResponse> responses = bookingService.getUserBookings(effectiveUserId);
        return ResponseEntity.ok(ApiResponse.ok(responses));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<BookingResponse>>> getAllBookings() {
        List<BookingResponse> responses = bookingService.getAllBookings();
        return ResponseEntity.ok(ApiResponse.ok(responses));
    }

    private Long resolveUserId(String header, Long param) {
        if (StringUtils.hasText(header)) {
            try {
                return Long.parseLong(header);
            } catch (NumberFormatException ignored) {}
        }
        if (param != null) {
            return param;
        }
        return 1L; // default guest ID if not passed in dev/testing mode
    }
}
