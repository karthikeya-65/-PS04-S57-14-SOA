package com.starlight.booking.repository;

import com.starlight.booking.entity.Booking;
import com.starlight.booking.entity.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    Optional<Booking> findByBookingReference(String bookingReference);

    List<Booking> findByUserIdOrderByCreatedAtDesc(Long userId);

    @Query("SELECT COALESCE(SUM(b.numRooms), 0) FROM Booking b " +
           "WHERE b.roomTypeId = :roomTypeId " +
           "AND b.checkInDate < :checkOutDate " +
           "AND b.checkOutDate > :checkInDate " +
           "AND b.status IN (:activeStatuses)")
    int countOverlappingBookedRooms(
            @Param("roomTypeId") Long roomTypeId,
            @Param("checkInDate") LocalDate checkInDate,
            @Param("checkOutDate") LocalDate checkOutDate,
            @Param("activeStatuses") Collection<BookingStatus> activeStatuses
    );
}
