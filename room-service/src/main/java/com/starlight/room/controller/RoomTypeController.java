package com.starlight.room.controller;

import com.starlight.room.dto.ApiResponse;
import com.starlight.room.dto.CreateRoomTypeRequest;
import com.starlight.room.dto.RoomTypeDto;
import com.starlight.room.service.RoomTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/room-types")
@RequiredArgsConstructor
public class RoomTypeController {

    private final RoomTypeService roomTypeService;

    @PostMapping
    public ResponseEntity<ApiResponse<RoomTypeDto>> createRoomType(@Valid @RequestBody CreateRoomTypeRequest request) {
        RoomTypeDto dto = roomTypeService.createRoomType(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Room type created successfully", dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RoomTypeDto>> getRoomTypeById(@PathVariable("id") Long id) {
        RoomTypeDto dto = roomTypeService.getRoomTypeById(id);
        return ResponseEntity.ok(ApiResponse.ok(dto));
    }

    @GetMapping("/hotel/{hotelId}")
    public ResponseEntity<ApiResponse<List<RoomTypeDto>>> getRoomTypesByHotelId(@PathVariable("hotelId") Long hotelId) {
        List<RoomTypeDto> dtos = roomTypeService.getRoomTypesByHotelId(hotelId);
        return ResponseEntity.ok(ApiResponse.ok(dtos));
    }
}
