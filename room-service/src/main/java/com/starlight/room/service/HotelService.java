package com.starlight.room.service;

import com.starlight.room.dto.CreateHotelRequest;
import com.starlight.room.dto.HotelDto;

import java.util.List;

public interface HotelService {
    HotelDto createHotel(CreateHotelRequest request);
    HotelDto getHotelById(Long id);
    List<HotelDto> getAllHotels(String city, String country);
}
