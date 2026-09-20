package com.starlight.room.controller;

import com.starlight.room.dto.ApiResponse;
import com.starlight.room.dto.CreateHotelRequest;
import com.starlight.room.dto.HotelDto;
import com.starlight.room.service.HotelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hotels")
@RequiredArgsConstructor
public class HotelController {

    private final HotelService hotelService;

    @PostMapping
    public ResponseEntity<ApiResponse<HotelDto>> createHotel(@Valid @RequestBody CreateHotelRequest request) {
        HotelDto dto = hotelService.createHotel(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Hotel registered successfully", dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<HotelDto>> getHotelById(@PathVariable("id") Long id) {
        HotelDto dto = hotelService.getHotelById(id);
        return ResponseEntity.ok(ApiResponse.ok(dto));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<HotelDto>>> getAllHotels(
            @RequestParam(name = "city", required = false) String city,
            @RequestParam(name = "country", required = false) String country) {
        List<HotelDto> dtos = hotelService.getAllHotels(city, country);
        return ResponseEntity.ok(ApiResponse.ok(dtos));
    }
}
