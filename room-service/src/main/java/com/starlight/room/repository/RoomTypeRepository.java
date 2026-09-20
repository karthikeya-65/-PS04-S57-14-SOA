package com.starlight.room.repository;

import com.starlight.room.entity.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface RoomTypeRepository extends JpaRepository<RoomType, Long> {

    List<RoomType> findByHotelId(Long hotelId);

    @Query("SELECT rt FROM RoomType rt JOIN rt.hotel h WHERE " +
            "(:city IS NULL OR LOWER(h.city) LIKE LOWER(CONCAT('%', :city, '%'))) AND " +
            "(:minPrice IS NULL OR rt.basePricePerNight >= :minPrice) AND " +
            "(:maxPrice IS NULL OR rt.basePricePerNight <= :maxPrice) AND " +
            "(:guests IS NULL OR rt.maxOccupancy >= :guests) AND " +
            "(:starRating IS NULL OR h.starRating >= :starRating) AND " +
            "rt.isActive = true")
    List<RoomType> searchRoomTypes(
            @Param("city") String city,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            @Param("guests") Integer guests,
            @Param("starRating") Integer starRating
    );
}
