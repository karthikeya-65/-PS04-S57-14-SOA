package com.starlight.room.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
    private LocalDateTime createdAt;
}
