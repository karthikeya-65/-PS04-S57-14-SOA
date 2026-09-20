package com.starlight.room.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomSearchResultDto {
    private Long roomTypeId;
    private String roomTypeName;
    private String description;
    private BigDecimal basePricePerNight;
    private Integer maxOccupancy;
    private Integer totalInventory;
    private String amenities;
    private Long hotelId;
    private String hotelName;
    private String hotelCity;
    private String hotelCountry;
    private Integer starRating;
}
