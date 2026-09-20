package com.starlight.room.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HotelDto {
    private Long id;
    private String name;
    private String description;
    private String address;
    private String city;
    private String country;
    private Integer starRating;
    private String contactEmail;
    private String contactPhone;
    private List<RoomTypeDto> roomTypes;
    private LocalDateTime createdAt;
}
