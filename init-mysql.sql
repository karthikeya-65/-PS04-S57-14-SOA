-- ==========================================================
-- Starlight Stays & Resorts - MySQL Database Initialization
-- ==========================================================

-- 1. USER SERVICE DATABASE
CREATE DATABASE IF NOT EXISTS starlight_user_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE starlight_user_db;

CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    phone_number VARCHAR(20),
    role VARCHAR(30) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 2. ROOM SERVICE DATABASE
CREATE DATABASE IF NOT EXISTS starlight_room_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE starlight_room_db;

CREATE TABLE IF NOT EXISTS hotels (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(120) NOT NULL,
    description TEXT,
    address VARCHAR(255) NOT NULL,
    city VARCHAR(80) NOT NULL,
    country VARCHAR(80) NOT NULL,
    star_rating INT,
    contact_email VARCHAR(100),
    contact_phone VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_hotel_city (city),
    INDEX idx_hotel_country (country)
);

CREATE TABLE IF NOT EXISTS room_types (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    hotel_id BIGINT NOT NULL,
    name VARCHAR(80) NOT NULL,
    description TEXT,
    base_price_per_night DECIMAL(10, 2) NOT NULL,
    max_occupancy INT NOT NULL,
    total_inventory INT NOT NULL,
    amenities TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (hotel_id) REFERENCES hotels(id) ON DELETE CASCADE,
    INDEX idx_room_type_price (base_price_per_night)
);

CREATE TABLE IF NOT EXISTS rooms (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    room_type_id BIGINT NOT NULL,
    room_number VARCHAR(20) NOT NULL,
    floor INT,
    status VARCHAR(20) NOT NULL DEFAULT 'AVAILABLE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (room_type_id) REFERENCES room_types(id) ON DELETE CASCADE
);

-- Seed Initial Hotels and Room Types
INSERT INTO hotels (name, description, address, city, country, star_rating, contact_email, contact_phone)
VALUES 
('Starlight Azure Bay Resort', 'Ultra luxury oceanfront resort with private beach', '101 Azure Coast Blvd', 'Malibu', 'USA', 5, 'concierge@starlightazure.com', '+1-310-555-0144'),
('Starlight Alpine Grand', 'Serene mountain ski resort with panoramic snow peaks', '45 Alpine Peak Rd', 'Aspen', 'USA', 5, 'stay@starlightalpine.com', '+1-970-555-0182');

INSERT INTO room_types (hotel_id, name, description, base_price_per_night, max_occupancy, total_inventory, amenities, is_active)
VALUES
(1, 'Oceanfront King Villa', 'Private pool and ocean views', 450.00, 2, 10, 'WiFi,Private Pool,King Bed,Jacuzzi,Breakfast Included', TRUE),
(1, 'Executive Family Suite', 'Spacious 2-bedroom suite with coastal patio', 650.00, 4, 8, 'WiFi,2 King Beds,Kitchenette,Ocean View,Balcony', TRUE),
(2, 'Alpine Summit Chalet', 'Fireplace and heated floors overlooking slopes', 520.00, 2, 12, 'WiFi,Fireplace,Heated Floors,Ski-in/Ski-out,Sauna', TRUE);

-- 3. BOOKING SERVICE DATABASE
CREATE DATABASE IF NOT EXISTS starlight_booking_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE starlight_booking_db;

CREATE TABLE IF NOT EXISTS bookings (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    booking_reference VARCHAR(36) NOT NULL UNIQUE,
    user_id BIGINT NOT NULL,
    hotel_id BIGINT NOT NULL,
    room_type_id BIGINT NOT NULL,
    check_in_date DATE NOT NULL,
    check_out_date DATE NOT NULL,
    num_rooms INT NOT NULL DEFAULT 1,
    num_guests INT NOT NULL,
    total_amount DECIMAL(10, 2) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'CONFIRMED',
    special_requests TEXT,
    cancellation_reason VARCHAR(255),
    cancelled_at TIMESTAMP NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_booking_user (user_id),
    INDEX idx_booking_dates (check_in_date, check_out_date),
    INDEX idx_booking_room_type (room_type_id)
);
