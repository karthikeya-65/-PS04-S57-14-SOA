package com.starlight.booking.client;

import com.starlight.booking.dto.ApiResponse;
import com.starlight.booking.dto.client.HotelDto;
import com.starlight.booking.dto.client.RoomTypeDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "room-service")
public interface RoomServiceClient {

    @GetMapping("/api/room-types/{id}")
    ApiResponse<RoomTypeDto> getRoomTypeById(@PathVariable("id") Long id);

    @GetMapping("/api/hotels/{id}")
    ApiResponse<HotelDto> getHotelById(@PathVariable("id") Long id);
}
