-- Create database
CREATE DATABASE IF NOT EXISTS ecommerce;
USE ecommerce;

-- Create user (allow connections from any host)
CREATE USER IF NOT EXISTS "admin"@"%" IDENTIFIED BY "adminpassword";
GRANT ALL PRIVILEGES ON ecommerce.* TO "admin"@"%";
FLUSH PRIVILEGES;

-- Drop tables in order to safely rebuild (handles foreign keys)
DROP TABLE IF EXISTS products_tags;
DROP TABLE IF EXISTS products;
DROP TABLE IF EXISTS tags;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS order_items;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS reviews;
DROP TABLE IF EXISTS discounts;

-- Create tables

-- Users table 
CREATE TABLE users (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    email           VARCHAR(255) UNIQUE NOT NULL,
    password        VARCHAR(255) NOT NULL,
    first_name      VARCHAR(255) NOT NULL,
    last_name       VARCHAR(255) NOT NULL,
    role            ENUM("ADMIN", "CUSTOMER") NOT NULL DEFAULT "CUSTOMER",
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Products table
CREATE TABLE products (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    name            VARCHAR(255) NOT NULL,
    price           DECIMAL(10, 2) NOT NULL,
    description     TEXT,
    quantity        INT NOT NULL DEFAULT 0 CHECK (quantity >= 0),
    image_path      VARCHAR(512)
);

-- Tags table
CREATE TABLE tags (
    id      INT AUTO_INCREMENT PRIMARY KEY,
    name    VARCHAR(50) UNIQUE NOT NULL
);

-- Junction table for products and tags
CREATE TABLE products_tags (
    product_id  INT NOT NULL,
    tag_id      INT NOT NULL,

    PRIMARY KEY (product_id, tag_id),
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
    FOREIGN KEY (tag_id) REFERENCES tags(id) ON DELETE CASCADE
);

-- Reviews table
CREATE TABLE reviews (
    id                  INT AUTO_INCREMENT PRIMARY KEY,
    product_id          INT NOT NULL,
    customer_email      VARCHAR(64) NOT NULL,
    rating              INT NOT NULL CHECK (rating BETWEEN 1 AND 5),
    comment             VARCHAR(1000),
    created_at          TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (product_id) 
        REFERENCES products(id) 
        ON DELETE CASCADE,
    FOREIGN KEY (customer_email) 
        REFERENCES users(email) 
        ON DELETE CASCADE
);

-- Discounts table
CREATE TABLE discounts (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(32) UNIQUE NOT NULL,
    type        ENUM("PERCENTAGE", "FIXED") NOT NULL,
    discount    DECIMAL(10,2) NOT NULL,
    active      BOOLEAN NOT NULL DEFAULT TRUE
);

-- Orders table 
CREATE TABLE orders (
    id                  SERIAL PRIMARY KEY,
    confirmation        VARCHAR(64) UNIQUE NOT NULL,
    customer_name       VARCHAR(255) NOT NULL,
    customer_email      VARCHAR(255) NOT NULL,
    order_placed        TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    sub_total           DECIMAL(10,2) NOT NULL,
    shipping_fee        DECIMAL(10,2) NOT NULL,
    tax                 DECIMAL(10,2) NOT NULL,
    total               DECIMAL(10,2) NOT NULL,
    shipping_address    TEXT NOT NULL,
    status           ENUM("RECEIVED", "PROCESSED", "SHIPPED", "DELIVERED", "CANCELLED") NOT NULL DEFAULT "RECEIVED"
);

-- Order Items table 
CREATE TABLE order_items (
    id              SERIAL PRIMARY KEY,
    order_conf      VARCHAR(64) NOT NULL,
    product_id      INTEGER NOT NULL REFERENCES products(id),
    unit_price      DECIMAL(10,2) NOT NULL,
    quantity        INT NOT NULL,
    line_total      DECIMAL(10,2) NOT NULL,

    FOREIGN KEY (order_conf)
        REFERENCES orders(confirmation)
        ON DELETE CASCADE
);

-- Seed tables

-- Seed users
INSERT INTO users (email, password, first_name, last_name, role) VALUES 
("user", "pass", "test", "customer", "CUSTOMER"),
("admin", "pass", "test", "admin", "ADMIN"),
("user@test.com", "pass", "user", "customer", "CUSTOMER"),
("user2@test.com", "pass", "user2", "customer", "CUSTOMER"),
("user3@test.com", "pass", "user3", "customer", "CUSTOMER"),
("user5@test.com", "pass", "user5", "customer", "CUSTOMER");

-- Seed discounts
INSERT INTO discounts (name, type, discount) VALUES
("FALLFUN10", "PERCENTAGE", 0.10),
("TAKE5", "FIXED", 5.0);

-- Seed products
INSERT INTO products (name, price, description, quantity, image_path) VALUES
("Wireless Mouse", 19.99, "A comfortable wireless mouse with long battery life.", 50, "/images/products/mouse.jpg"),
("Java Programming Book", 39.95, "Comprehensive guide to modern Java programming.", 30, "/images/products/java_book.jpg"),
("Graphic T-Shirt", 15.99, "Soft cotton t-shirt with a custom graphic print.", 100, "/images/products/tshirt.jpg"),
("Bluetooth Speaker", 49.99, "Portable speaker with high-quality sound and waterproof design.", 25, "/images/products/speaker.jpg"),
("Board Game", 29.50, "Fun for the whole family - 2 to 6 players.", 40, "/images/products/board_game.jpg"),
("Stainless Steel Water Bottle", 12.49, "Insulated water bottle that keeps drinks cold for 24 hours.", 60, "/images/products/water_bottle.jpg"),
("Noise-Cancelling Headphones", 89.99, "Over-ear headphones with active noise cancellation.", 20, "/images/products/headphones.jpg"),
("Cotton Hoodie", 34.99, "Warm and soft hoodie available in multiple colors.", 75, "/images/products/hoodie.jpg"),
("LED Desk Lamp", 24.95, "Adjustable LED lamp with brightness settings.", 40, "/images/products/desk_lamp.jpg"),
("Puzzle Set (1000 pieces)", 18.50, "Challenging puzzle with vibrant artwork.", 50, "/images/products/puzzle.jpg"),
("Laptop Backpack", 45.00, "Durable backpack with padded laptop compartment.", 30, "/images/products/backpack.jpg"),
("Scented Candle", 14.99, "Soy wax candle with a refreshing lavender scent.", 80, "/images/products/candle.jpg"),
("Yoga Mat", 21.99, "Non-slip yoga mat with carrying strap.", 55, "/images/products/yoga_mat.jpg"),
("USB-C Charging Cable", 9.99, "Durable and fast-charging USB-C cable (1 meter).", 120, "/images/products/usb_cable.jpg"),
("Plush Teddy Bear", 16.99, "Soft and cuddly teddy bear for kids.", 65, "/images/products/teddy_bear.jpg"),
("Ceramic Coffee Mug", 9.99, "Dishwasher-safe mug with a minimalist design.", 90, "/images/products/coffee_mug.jpg"),
("Wireless Keyboard", 29.99, "Compact Bluetooth keyboard with quiet keys.", 40, "/images/products/keyboard.jpg"),
("Running Shoes", 59.99, "Lightweight running shoes with breathable mesh.", 35, "/images/products/running_shoes.jpg"),
("Throw Blanket", 22.99, "Soft microfiber throw blanket for couches or beds.", 60, "/images/products/throw_blanket.jpg"),
("Digital Alarm Clock", 18.49, "LED display alarm clock with USB charging port.", 45, "/images/products/alarm_clock.jpg"),
("Sketchbook", 12.99, "Hardcover sketchbook with premium drawing paper.", 70, "/images/products/sketchbook.jpg"),
("Mini Tripod", 14.99, "Portable tripod for smartphones and cameras.", 55, "/images/products/tripod.jpg"),
("Portable Charger", 27.99, "10,000mAh power bank with fast charging support.", 50, "/images/products/power_bank.jpg"),
("Stainless Steel Knife Set", 49.95, "5-piece kitchen knife set with ergonomic handles.", 25, "/images/products/knife_set.jpg"),
("Desk Organizer", 17.50, "Multi-compartment organizer for office supplies.", 80, "/images/products/desk_organizer.jpg"),
("Winter Gloves", 14.99, "Warm insulated gloves for cold weather.", 65, "/images/products/winter_gloves.jpg"),
("Notebook Pack (3)", 9.49, "Set of 3 lined notebooks for journaling or school.", 95, "/images/products/notebooks.jpg"),
("LED Light Strip", 19.99, "Color-changing LED strip with remote control.", 55, "/images/products/light_strip.jpg");

-- Seed tags
INSERT INTO tags (name) VALUES
("electronics"), ("books"), ("clothing"), ("home"), ("toys"), ("accessories"), ("fitness"),
("lighting"), ("bags"), ("candles"), ("kids");

-- Seed product-tag relationships
INSERT INTO products_tags (product_id, tag_id) VALUES
-- 1 Wireless Mouse
(1, 1), -- electronics
(1, 6), -- accessories

-- 2 Java Programming Book
(2, 2), -- books

-- 3 Graphic T-Shirt
(3, 3), -- clothing

-- 4 Bluetooth Speaker
(4, 1), -- electronics
(4, 6), -- accessories

-- 5 Board Game
(5, 5), -- toys
(5, 11), -- kids

-- 6 Stainless Steel Water Bottle
(6, 6), -- accessories
(6, 7), -- fitness

-- 7 Noise-Cancelling Headphones
(7, 1), -- electronics
(7, 6), -- accessories

-- 8 Cotton Hoodie
(8, 3), -- clothing

-- 9 LED Desk Lamp
(9, 8), -- lighting
(9, 4), -- home

-- 10 Puzzle Set (1000 pieces)
(10, 5), -- toys
(10, 11), -- kids

-- 11 Laptop Backpack
(11, 9), -- bags
(11, 6), -- accessories

-- 12 Scented Candle
(12, 10), -- candles
(12, 4),  -- home

-- 13 Yoga Mat
(13, 7), -- fitness

-- 14 USB-C Charging Cable
(14, 1), -- electronics
(14, 6), -- accessories

-- 15 Plush Teddy Bear
(15, 5), -- toys
(15, 11), -- kids

-- 16 Ceramic Coffee Mug
(16, 4), -- home

-- 17 Wireless Keyboard
(17, 1), -- electronics
(17, 6), -- accessories

-- 18 Running Shoes
(18, 3), -- clothing
(18, 7), -- fitness

-- 19 Throw Blanket
(19, 4), -- home

-- 20 Digital Alarm Clock
(20, 8), -- lighting
(20, 4), -- home

-- 21 Sketchbook
(21, 2), -- books
(21, 11), -- kids

-- 22 Mini Tripod
(22, 6), -- accessories
(22, 1), -- electronics

-- 23 Portable Charger
(23, 1), -- electronics
(23, 6), -- accessories

-- 24 Stainless Steel Knife Set
(24, 4), -- home

-- 25 Desk Organizer
(25, 4), -- home

-- 26 Winter Gloves
(26, 3), -- clothing
(26, 6), -- accessories

-- 27 Notebook Pack (3)
(27, 2), -- books
(27, 11), -- kids

-- 28 LED Light Strip
(28, 8), -- lighting
(28, 4); -- home

-- Seed reviews
INSERT INTO reviews (product_id, customer_email, rating, comment) VALUES
("1", "user@test.com", 5, "Works great!"),
("1", "user2@test.com", 5, "I use it every day."),
("1", "user3@test.com", 4, "It's a mouse, I guess..."),
("3", "user@test.com", 1, "I WANT MY MONEY BACK!"),
("6", "user@test.com", 3, "It holds my water and doesn't leak! But I can't open it back up :("),
("6", "user5@test.com", 2, "Makes my water taste too wet!");