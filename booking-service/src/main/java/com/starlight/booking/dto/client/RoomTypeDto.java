package com.starlight.booking.dto.client;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomTypeDto {
    private Long id;
    private Long hotelId;
    private String hotelName;
    private String name;
    private String description;
    private BigDecimal basePricePerNight;
    private Integer maxOccupancy;
    private Integer totalInventory;
    private String amenities;
    private Boolean isActive;
}
