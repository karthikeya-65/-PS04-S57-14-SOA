package com.starlight.room.config;

import com.starlight.room.entity.Hotel;
import com.starlight.room.entity.Room;
import com.starlight.room.entity.RoomStatus;
import com.starlight.room.entity.RoomType;
import com.starlight.room.repository.HotelRepository;
import com.starlight.room.repository.RoomRepository;
import com.starlight.room.repository.RoomTypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final HotelRepository hotelRepository;
    private final RoomTypeRepository roomTypeRepository;
    private final RoomRepository roomRepository;

    @Override
    public void run(String... args) {
        if (hotelRepository.count() == 0) {
            log.info("Seeding default Spring Boot hotel and room inventory data...");

            // 1. Hotel 1: Malibu
            Hotel azure = Hotel.builder()
                    .name("Starlight Azure Bay Resort")
                    .description("Ultra luxury oceanfront resort with private beach and infinity cliffside pools.")
                    .address("101 Azure Coast Blvd")
                    .city("Malibu")
                    .country("USA")
                    .starRating(5)
                    .contactEmail("concierge@starlightazure.com")
                    .contactPhone("+1-310-555-0144")
                    .roomTypes(new ArrayList<>())
                    .build();
            Hotel savedAzure = hotelRepository.save(azure);

            RoomType oceanVilla = RoomType.builder()
                    .hotel(savedAzure)
                    .name("Oceanfront King Villa")
                    .description("Private pool and panoramic Pacific Ocean views.")
                    .basePricePerNight(new BigDecimal("450.00"))
                    .maxOccupancy(2)
                    .totalInventory(10)
                    .amenities("WiFi,Private Pool,King Bed,Jacuzzi,Breakfast Included")
                    .isActive(true)
                    .build();
            RoomType savedOceanVilla = roomTypeRepository.save(oceanVilla);

            RoomType familySuite = RoomType.builder()
                    .hotel(savedAzure)
                    .name("Executive Family Suite")
                    .description("Spacious 2-bedroom suite with coastal patio and full kitchenette.")
                    .basePricePerNight(new BigDecimal("650.00"))
                    .maxOccupancy(4)
                    .totalInventory(8)
                    .amenities("WiFi,2 King Beds,Kitchenette,Ocean View,Balcony")
                    .isActive(true)
                    .build();
            RoomType savedFamilySuite = roomTypeRepository.save(familySuite);

            // 2. Hotel 2: Aspen
            Hotel alpine = Hotel.builder()
                    .name("Starlight Alpine Grand")
                    .description("Serene mountain ski resort with panoramic snow peaks and fireside spa.")
                    .address("45 Alpine Peak Rd")
                    .city("Aspen")
                    .country("USA")
                    .starRating(5)
                    .contactEmail("stay@starlightalpine.com")
                    .contactPhone("+1-970-555-0182")
                    .roomTypes(new ArrayList<>())
                    .build();
            Hotel savedAlpine = hotelRepository.save(alpine);

            RoomType alpineChalet = RoomType.builder()
                    .hotel(savedAlpine)
                    .name("Alpine Summit Chalet")
                    .description("Fireplace and heated stone floors overlooking the slopes.")
                    .basePricePerNight(new BigDecimal("520.00"))
                    .maxOccupancy(2)
                    .totalInventory(12)
                    .amenities("WiFi,Fireplace,Heated Floors,Ski-in/Ski-out,Sauna")
                    .isActive(true)
                    .build();
            RoomType savedAlpineChalet = roomTypeRepository.save(alpineChalet);

            // 3. Hotel 3: Honolulu
            Hotel emerald = Hotel.builder()
                    .name("Starlight Emerald Coast")
                    .description("Tropical beachfront sanctuary with private lagoon and tiki cabanas.")
                    .address("500 Palm Boulevard")
                    .city("Honolulu")
                    .country("USA")
                    .starRating(5)
                    .contactEmail("aloha@starlightemerald.com")
                    .contactPhone("+1-808-555-0199")
                    .roomTypes(new ArrayList<>())
                    .build();
            Hotel savedEmerald = hotelRepository.save(emerald);

            RoomType royalPenthouse = RoomType.builder()
                    .hotel(savedEmerald)
                    .name("Royal Penthouse Suite")
                    .description("Top-floor penthouse with 360-degree ocean views and personal butler.")
                    .basePricePerNight(new BigDecimal("850.00"))
                    .maxOccupancy(4)
                    .totalInventory(5)
                    .amenities("WiFi,Private Pool,Helipad Access,Butler Service,Ocean View")
                    .isActive(true)
                    .build();
            RoomType savedRoyalPenthouse = roomTypeRepository.save(royalPenthouse);

            // Physical room units
            for (int i = 1; i <= 3; i++) {
                roomRepository.save(Room.builder()
                        .roomType(savedOceanVilla)
                        .roomNumber("VILLA-" + (100 + i))
                        .floor(1)
                        .status(RoomStatus.AVAILABLE)
                        .build());

                roomRepository.save(Room.builder()
                        .roomType(savedAlpineChalet)
                        .roomNumber("CHALET-" + (200 + i))
                        .floor(2)
                        .status(RoomStatus.AVAILABLE)
                        .build());
            }

            log.info("Default Spring Boot hotels, room types, and rooms seeded successfully!");
        }
    }
}
