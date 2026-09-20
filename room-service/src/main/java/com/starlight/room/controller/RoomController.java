package com.starlight.room.controller;

import com.starlight.room.dto.ApiResponse;
import com.starlight.room.dto.CreateRoomRequest;
import com.starlight.room.dto.RoomDto;
import com.starlight.room.dto.RoomSearchResultDto;
import com.starlight.room.service.RoomService;
import com.starlight.room.service.RoomTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;
    private final RoomTypeService roomTypeService;

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<RoomSearchResultDto>>> searchRooms(
            @RequestParam(name = "city", required = false) String city,
            @RequestParam(name = "minPrice", required = false) BigDecimal minPrice,
            @RequestParam(name = "maxPrice", required = false) BigDecimal maxPrice,
            @RequestParam(name = "guests", required = false) Integer guests,
            @RequestParam(name = "starRating", required = false) Integer starRating) {
        List<RoomSearchResultDto> results = roomTypeService.searchRoomTypes(city, minPrice, maxPrice, guests, starRating);
        return ResponseEntity.ok(ApiResponse.ok(results));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<RoomDto>> createRoom(@Valid @RequestBody CreateRoomRequest request) {
        RoomDto dto = roomService.createRoom(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Room created successfully", dto));
    }

    @GetMapping("/type/{roomTypeId}")
    public ResponseEntity<ApiResponse<List<RoomDto>>> getRoomsByRoomTypeId(@PathVariable("roomTypeId") Long roomTypeId) {
        List<RoomDto> dtos = roomService.getRoomsByRoomTypeId(roomTypeId);
        return ResponseEntity.ok(ApiResponse.ok(dtos));
    }
}
