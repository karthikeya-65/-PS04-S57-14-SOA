package com.starlight.booking.service;

import com.starlight.booking.dto.AvailabilityRequest;
import com.starlight.booking.dto.AvailabilityResponse;

public interface AvailabilityService {
    AvailabilityResponse checkAvailability(AvailabilityRequest request);
    boolean isAvailable(Long roomTypeId, java.time.LocalDate checkInDate, java.time.LocalDate checkOutDate, int requestedRooms);
}
