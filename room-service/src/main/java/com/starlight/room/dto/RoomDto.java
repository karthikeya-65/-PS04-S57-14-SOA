package com.starlight.room.dto;

import com.starlight.room.entity.RoomStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomDto {
    private Long id;
    private Long roomTypeId;
    private String roomTypeName;
    private String roomNumber;
    private Integer floor;
    private RoomStatus status;
}
