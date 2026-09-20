package com.starlight.room.service;

import com.starlight.room.dto.CreateRoomRequest;
import com.starlight.room.dto.RoomDto;
import com.starlight.room.entity.Room;
import com.starlight.room.entity.RoomStatus;
import com.starlight.room.entity.RoomType;
import com.starlight.room.exception.ResourceNotFoundException;
import com.starlight.room.repository.RoomRepository;
import com.starlight.room.repository.RoomTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final RoomTypeRepository roomTypeRepository;

    @Override
    @Transactional
    public RoomDto createRoom(CreateRoomRequest request) {
        RoomType roomType = roomTypeRepository.findById(request.getRoomTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("Room type not found with ID: " + request.getRoomTypeId()));

        Room room = Room.builder()
                .roomType(roomType)
                .roomNumber(request.getRoomNumber())
                .floor(request.getFloor())
                .status(request.getStatus() != null ? request.getStatus() : RoomStatus.AVAILABLE)
                .build();

        Room saved = roomRepository.save(room);
        return mapToDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoomDto> getRoomsByRoomTypeId(Long roomTypeId) {
        return roomRepository.findByRoomTypeId(roomTypeId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private RoomDto mapToDto(Room room) {
        return RoomDto.builder()
                .id(room.getId())
                .roomTypeId(room.getRoomType().getId())
                .roomTypeName(room.getRoomType().getName())
                .roomNumber(room.getRoomNumber())
                .floor(room.getFloor())
                .status(room.getStatus())
                .build();
    }
}
