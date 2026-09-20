package com.starlight.room.repository;

import com.starlight.room.entity.Room;
import com.starlight.room.entity.RoomStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findByRoomTypeId(Long roomTypeId);
    long countByRoomTypeIdAndStatus(Long roomTypeId, RoomStatus status);
}
