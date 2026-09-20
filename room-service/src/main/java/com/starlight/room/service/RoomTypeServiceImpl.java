package com.starlight.room.service;

import com.starlight.room.dto.CreateRoomTypeRequest;
import com.starlight.room.dto.RoomSearchResultDto;
import com.starlight.room.dto.RoomTypeDto;
import com.starlight.room.entity.Hotel;
import com.starlight.room.entity.RoomType;
import com.starlight.room.exception.ResourceNotFoundException;
import com.starlight.room.repository.HotelRepository;
import com.starlight.room.repository.RoomTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomTypeServiceImpl implements RoomTypeService {

    private final RoomTypeRepository roomTypeRepository;
    private final HotelRepository hotelRepository;

    @Override
    @Transactional
    public RoomTypeDto createRoomType(CreateRoomTypeRequest request) {
        Hotel hotel = hotelRepository.findById(request.getHotelId())
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with ID: " + request.getHotelId()));

        RoomType roomType = RoomType.builder()
                .hotel(hotel)
                .name(request.getName())
                .description(request.getDescription())
                .basePricePerNight(request.getBasePricePerNight())
                .maxOccupancy(request.getMaxOccupancy())
                .totalInventory(request.getTotalInventory())
                .amenities(request.getAmenities())
                .isActive(true)
                .build();

        RoomType saved = roomTypeRepository.save(roomType);
        return mapToDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public RoomTypeDto getRoomTypeById(Long id) {
        RoomType roomType = roomTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room type not found with ID: " + id));
        return mapToDto(roomType);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoomTypeDto> getRoomTypesByHotelId(Long hotelId) {
        return roomTypeRepository.findByHotelId(hotelId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoomSearchResultDto> searchRoomTypes(String city, BigDecimal minPrice, BigDecimal maxPrice, Integer guests, Integer starRating) {
        return roomTypeRepository.searchRoomTypes(city, minPrice, maxPrice, guests, starRating).stream()
                .map(rt -> RoomSearchResultDto.builder()
                        .roomTypeId(rt.getId())
                        .roomTypeName(rt.getName())
                        .description(rt.getDescription())
                        .basePricePerNight(rt.getBasePricePerNight())
                        .maxOccupancy(rt.getMaxOccupancy())
                        .totalInventory(rt.getTotalInventory())
                        .amenities(rt.getAmenities())
                        .hotelId(rt.getHotel().getId())
                        .hotelName(rt.getHotel().getName())
                        .hotelCity(rt.getHotel().getCity())
                        .hotelCountry(rt.getHotel().getCountry())
                        .starRating(rt.getHotel().getStarRating())
                        .build())
                .collect(Collectors.toList());
    }

    private RoomTypeDto mapToDto(RoomType rt) {
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
