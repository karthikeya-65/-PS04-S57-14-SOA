package com.starlight.room.service;

import com.starlight.room.dto.CreateHotelRequest;
import com.starlight.room.dto.HotelDto;
import com.starlight.room.dto.RoomTypeDto;
import com.starlight.room.entity.Hotel;
import com.starlight.room.entity.RoomType;
import com.starlight.room.exception.ResourceNotFoundException;
import com.starlight.room.repository.HotelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService {

    private final HotelRepository hotelRepository;

    @Override
    @Transactional
    public HotelDto createHotel(CreateHotelRequest request) {
        Hotel hotel = Hotel.builder()
                .name(request.getName())
                .description(request.getDescription())
                .address(request.getAddress())
                .city(request.getCity())
                .country(request.getCountry())
                .starRating(request.getStarRating())
                .contactEmail(request.getContactEmail())
                .contactPhone(request.getContactPhone())
                .build();

        Hotel saved = hotelRepository.save(hotel);
        return mapToDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public HotelDto getHotelById(Long id) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with ID: " + id));
        return mapToDto(hotel);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HotelDto> getAllHotels(String city, String country) {
        List<Hotel> hotels;
        if (StringUtils.hasText(city)) {
            hotels = hotelRepository.findByCityIgnoreCase(city);
        } else if (StringUtils.hasText(country)) {
            hotels = hotelRepository.findByCountryIgnoreCase(country);
        } else {
            hotels = hotelRepository.findAll();
        }
        return hotels.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    private HotelDto mapToDto(Hotel hotel) {
        List<RoomTypeDto> roomTypeDtos = hotel.getRoomTypes() != null
                ? hotel.getRoomTypes().stream().map(this::mapRoomTypeToDto).collect(Collectors.toList())
                : Collections.emptyList();

        return HotelDto.builder()
                .id(hotel.getId())
                .name(hotel.getName())
                .description(hotel.getDescription())
                .address(hotel.getAddress())
                .city(hotel.getCity())
                .country(hotel.getCountry())
                .starRating(hotel.getStarRating())
                .contactEmail(hotel.getContactEmail())
                .contactPhone(hotel.getContactPhone())
                .roomTypes(roomTypeDtos)
                .createdAt(hotel.getCreatedAt())
                .build();
    }

    private RoomTypeDto mapRoomTypeToDto(RoomType rt) {
        return RoomTypeDto.builder()
                .id(rt.getId())
                .hotelId(rt.getHotel().getId())
                .hotelName(rt.getHotel().getName())
                .name(rt.getName())
                .description(rt.getDescription())
                .basePricePerNight(rt.getBasePricePerNight())
                .maxOccupancy(rt.getMaxOccupancy())
                .totalInventory(rt.getTotalInventory())
                .amenities(rt.getAmenities())
                .isActive(rt.getIsActive())
                .createdAt(rt.getCreatedAt())
                .build();
    }
}
