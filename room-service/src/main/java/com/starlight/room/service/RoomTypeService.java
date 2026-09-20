package com.starlight.room.service;

import com.starlight.room.dto.CreateRoomTypeRequest;
import com.starlight.room.dto.RoomSearchResultDto;
import com.starlight.room.dto.RoomTypeDto;

import java.math.BigDecimal;
import java.util.List;

public interface RoomTypeService {
    RoomTypeDto createRoomType(CreateRoomTypeRequest request);
    RoomTypeDto getRoomTypeById(Long id);
    List<RoomTypeDto> getRoomTypesByHotelId(Long hotelId);
    List<RoomSearchResultDto> searchRoomTypes(String city, BigDecimal minPrice, BigDecimal maxPrice, Integer guests, Integer starRating);
}
