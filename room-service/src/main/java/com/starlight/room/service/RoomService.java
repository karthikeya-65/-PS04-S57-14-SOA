package com.starlight.room.service;

import com.starlight.room.dto.CreateRoomRequest;
import com.starlight.room.dto.RoomDto;

import java.util.List;

public interface RoomService {
    RoomDto createRoom(CreateRoomRequest request);
    List<RoomDto> getRoomsByRoomTypeId(Long roomTypeId);
}
