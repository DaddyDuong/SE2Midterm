DROP DATABASE IF EXISTS se2midterm;
CREATE DATABASE IF NOT EXISTS se2midterm;
-- Use the database
USE se2midterm;
-- Create User table
CREATE TABLE IF NOT EXISTS user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100),
    phone_number VARCHAR(20),
    birthdate DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
-- Insert some sample users (for testing)
INSERT INTO user (username, password, email) VALUES
('nam', '123', 'nam@example.com'),
('thanh', '123', 'thanh@example.com');

CREATE TABLE products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    price DOUBLE NOT NULL,
    original_price DOUBLE,
    discount_percentage INT,
    source VARCHAR(255),
    weight VARCHAR(100),
    size VARCHAR(100),
    product_type VARCHAR(100),
    description TEXT,
    image_path VARCHAR(255)
);
-- Insert sample data for Deals of the Month
INSERT INTO products (title, price, original_price, discount_percentage, source, weight, size, product_type, description, image_path) VALUES 
('Decorative Kitchen Set', 40.00, 50.00, 20, 'Home Decor', '3 kg', '30x25x20 cm', 'Home Decor', 'Elegant kitchen set with modern design', '/images/products/product1.jpg'),
('Kitchen Utensil Set', 33.00, 35.00, 6, 'Kitchen Pro', '2.5 kg', '25x20x15 cm', 'Kitchenware', 'Complete set of high-quality kitchen utensils', '/images/products/product2.jpg'),
('Digital Weighing Scale', 44.00, 45.00, 2, 'Tech Measure', '1 kg', '20x15x5 cm', 'Kitchen Appliances', 'Precise digital kitchen scale with LCD display', '/images/products/product3.jpg'),
('Thermal Water Bottle', 22.50, 25.00, 10, 'Hydro Tech', '0.5 kg', '25x7x7 cm', 'Drinkware', 'Insulated water bottle to keep drinks hot or cold', '/images/products/product4.jpg'),
-- Insert sample data for Shop Now
('Kitchen Blender', 89.99, NULL, NULL, 'Brand X', '2.5 kg', '25x20x30 cm', 'Kitchen Appliances', 'Powerful blender for smoothies and food processing', '/images/products/product5.jpg'),
('Ceramic Cookware Set', 129.99, NULL, NULL, 'Luxury Kitchens', '5 kg', '40x30x20 cm', 'Kitchenware', 'Premium ceramic cookware set with non-stick coating', '/images/products/product6.jpg'),
('Coffee Maker', 79.99, NULL, NULL, 'Coffee Tech', '3 kg', '30x25x35 cm', 'Kitchen Appliances', 'Programmable coffee maker with built-in grinder', '/images/products/product7.jpg'),
('Air Fryer', 99.99, NULL, NULL, 'Healthy Cook', '4 kg', '35x30x40 cm', 'Kitchen Appliances', 'Low-oil air fryer for healthier cooking', '/images/products/product8.jpg'),
('Toaster', 39.99, NULL, NULL, 'Home Appliances', '2 kg', '25x20x25 cm', 'Kitchen Appliances', 'Wide-slot toaster with multiple browning settings', '/images/products/product9.jpg'),
('Rice Cooker', 69.99, NULL, NULL, 'Kitchen Pro', '3.5 kg', '30x30x30 cm', 'Kitchen Appliances', 'Multi-function rice cooker with steam function', '/images/products/product10.jpg'),
('Microwave Oven', 119.99, NULL, NULL, 'Tech Kitchen', '15 kg', '50x40x30 cm', 'Kitchen Appliances', 'Large capacity microwave with smart cooking programs', '/images/products/product11.jpg');