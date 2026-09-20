package com.starlight.room;

import com.starlight.room.dto.CreateHotelRequest;
import com.starlight.room.dto.CreateRoomTypeRequest;
import com.starlight.room.dto.HotelDto;
import com.starlight.room.dto.RoomSearchResultDto;
import com.starlight.room.dto.RoomTypeDto;
import com.starlight.room.service.HotelService;
import com.starlight.room.service.RoomTypeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("dev")
@Transactional
class RoomServiceApplicationTests {

    @Autowired
    private HotelService hotelService;

    @Autowired
    private RoomTypeService roomTypeService;

    @Test
    void testCreateHotelAndSearchRooms() {
        HotelDto hotel = hotelService.createHotel(CreateHotelRequest.builder()
                .name("Starlight Grand Resort")
                .description("Luxury beachfront resort")
                .address("100 Ocean Drive")
                .city("Miami")
                .country("USA")
                .starRating(5)
                .contactEmail("miami@starlight.com")
                .contactPhone("+1-305-555-0199")
                .build());

        assertNotNull(hotel);
        assertNotNull(hotel.getId());

        RoomTypeDto roomType = roomTypeService.createRoomType(CreateRoomTypeRequest.builder()
                .hotelId(hotel.getId())
                .name("Deluxe Oceanfront Suite")
                .description("King bed with panoramic ocean views")
                .basePricePerNight(new BigDecimal("350.00"))
                .maxOccupancy(2)
                .totalInventory(10)
                .amenities("WiFi,King Bed,Ocean View,Balcony,Breakfast")
                .build());

        assertNotNull(roomType);
        assertEquals("Deluxe Oceanfront Suite", roomType.getName());

        List<RoomSearchResultDto> results = roomTypeService.searchRoomTypes(
                "Miami",
                new BigDecimal("200.00"),
                new BigDecimal("500.00"),
                2,
                4
        );

        assertFalse(results.isEmpty());
        assertEquals("Miami", results.get(0).getHotelCity());
        assertEquals("Deluxe Oceanfront Suite", results.get(0).getRoomTypeName());
    }
}
