-- Create database if not exists (without dropping)
CREATE DATABASE IF NOT EXISTS se2midterm;

-- Use the database
USE se2midterm;

-- Create User table (only if not exists)
CREATE TABLE IF NOT EXISTS user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100),
    phone_number VARCHAR(20),
    birthdate DATE,
    full_name VARCHAR(100),
    address TEXT,
    age INT,
    gender VARCHAR(20),
    avatar_url VARCHAR(255),
    preferred_payment_method VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert sample users only if they don't exist
INSERT IGNORE INTO user (username, password, email) VALUES
    ('nam', '123', 'nam@example.com'),
    ('thanh', '123', 'thanh@example.com');

-- Create Product table (only if not exists)
CREATE TABLE IF NOT EXISTS products (
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

-- Insert sample data with updated image_path
INSERT INTO products (title, price, original_price, discount_percentage, source, weight, size, product_type, description, image_path) VALUES
                                                                                                                                          ('Decorative Kitchen Set', 40.00, 50.00, 20, 'Home Decor', '3 kg', '30x25x20 cm', 'Home Decor', 'Elegant kitchen set with modern design', '/images/products/Decorative Kitchen Set.jpg'),
                                                                                                                                          ('Kitchen Utensil Set', 33.00, 35.00, 6, 'Kitchen Pro', '2.5 kg', '25x20x15 cm', 'Kitchenware', 'Complete set of high-quality kitchen utensils', '/images/products/Kitchen Utensil Set.jpg'),
                                                                                                                                          ('Digital Weighing Scale', 44.00, 45.00, 2, 'Tech Measure', '1 kg', '20x15x5 cm', 'Kitchen Appliances', 'Precise digital kitchen scale with LCD display', '/images/products/Digital Weighing Scale.jpg'),
                                                                                                                                          ('Thermal Water Bottle', 22.50, 25.00, 10, 'Hydro Tech', '0.5 kg', '25x7x7 cm', 'Drinkware', 'Insulated water bottle to keep drinks hot or cold', '/images/products/Thermal Water Bottle.jpg'),
                                                                                                                                          ('Kitchen Blender', 89.99, NULL, NULL, 'Brand X', '2.5 kg', '25x20x30 cm', 'Kitchen Appliances', 'Powerful blender for smoothies and food processing', '/images/products/Kitchen Blender.jpg'),
                                                                                                                                          ('Ceramic Cookware Set', 129.99, NULL, NULL, 'Luxury Kitchens', '5 kg', '40x30x20 cm', 'Kitchenware', 'Premium ceramic cookware set with non-stick coating', '/images/products/Ceramic Cookware Set.jpg'),
                                                                                                                                          ('Coffee Maker', 79.99, NULL, NULL, 'Coffee Tech', '3 kg', '30x25x35 cm', 'Kitchen Appliances', 'Programmable coffee maker with built-in grinder', '/images/products/Coffee Maker.jpg'),
                                                                                                                                          ('Air Fryer', 99.99, NULL, NULL, 'Healthy Cook', '4 kg', '35x30x40 cm', 'Kitchen Appliances', 'Low-oil air fryer for healthier cooking', '/images/products/Air Fryer.jpg'),
                                                                                                                                          ('Toaster', 39.99, NULL, NULL, 'Home Appliances', '2 kg', '25x20x25 cm', 'Kitchen Appliances', 'Wide-slot toaster with multiple browning settings', '/images/products/Toaster.jpg'),
                                                                                                                                          ('Rice Cooker', 69.99, NULL, NULL, 'Kitchen Pro', '3.5 kg', '30x30x30 cm', 'Kitchen Appliances', 'Multi-function rice cooker with steam function', '/images/products/Rice Cooker.jpg'),
                                                                                                                                          ('Microwave Oven', 119.99, NULL, NULL, 'Tech Kitchen', '15 kg', '50x40x30 cm', 'Kitchen Appliances', 'Large capacity microwave with smart cooking programs', '/images/products/Microwave Oven.jpg');

-- Add Fashion Products
INSERT INTO products (title, price, original_price, discount_percentage, source, weight, size, product_type, description, image_path) VALUES
-- Men's Clothing
('Men\'s Casual T-Shirt', 19.99, 24.99, 20, 'Fashion Brand', '0.2 kg', 'M, L, XL', 'Men\'s Clothing', 'Comfortable cotton t-shirt with modern design', '/images/products/Mens Casual TShirt.jpg'),
('Men\'s Slim Fit Jeans', 39.99, 49.99, 20, 'Denim Co', '0.5 kg', '30, 32, 34, 36', 'Men\'s Clothing', 'Stylish slim fit jeans for casual wear', '/images/products/Mens Slim Fit Jeans.jpg'),
('Men\'s Formal Shirt', 34.99, NULL, NULL, 'Business Wear', '0.3 kg', 'M, L, XL', 'Men\'s Clothing', 'Classic formal shirt for business and special occasions', '/images/products/Mens Formal Shirt.jpg'),
('Men\'s Hooded Sweatshirt', 45.99, 59.99, 23, 'Urban Style', '0.6 kg', 'M, L, XL, XXL', 'Men\'s Clothing', 'Warm and comfortable hooded sweatshirt for casual outings', '/images/products/Mens Hooded Sweatshirt.jpg'),
('Men\'s Sport Jacket', 79.99, 99.99, 20, 'Sport Elite', '0.8 kg', 'M, L, XL', 'Men\'s Clothing', 'Lightweight sport jacket perfect for outdoor activities', '/images/products/Mens Sport Jacket.jpg'),

-- Women's Clothing
('Women\'s Summer Dress', 29.99, 39.99, 25, 'Fashion Nova', '0.3 kg', 'S, M, L', 'Women\'s Clothing', 'Elegant summer dress with floral pattern', '/images/products/Womens Summer Dress.jpg'),
('Women\'s Skinny Jeans', 44.99, 54.99, 18, 'Denim Co', '0.4 kg', '26, 28, 30, 32', 'Women\'s Clothing', 'Classic skinny jeans with stretch comfort', '/images/products/Womens Skinny Jeans.jpg'),
('Women\'s Blouse', 24.99, NULL, NULL, 'Elegant Style', '0.2 kg', 'S, M, L', 'Women\'s Clothing', 'Stylish blouse for casual and formal occasions', '/images/products/Womens Blouse.jpg'),
('Women\'s Cardigan', 49.99, 59.99, 17, 'Cozy Wear', '0.5 kg', 'S, M, L, XL', 'Women\'s Clothing', 'Soft and warm cardigan for everyday wear', '/images/products/Womens Cardigan.jpg'),
('Women\'s Maxi Skirt', 39.99, 49.99, 20, 'Elegant Style', '0.4 kg', 'S, M, L', 'Women\'s Clothing', 'Flowy maxi skirt perfect for summer', '/images/products/Womens Maxi Skirt.jpg'),

-- Accessories
('Leather Belt', 19.99, 24.99, 20, 'Accessory Co', '0.2 kg', '80-100 cm', 'Accessories', 'Genuine leather belt with classic buckle', '/images/products/Leather Belt.jpg'),
('Fashion Sunglasses', 29.99, 39.99, 25, 'Eyewear Co', '0.1 kg', 'One Size', 'Accessories', 'UV protection sunglasses with stylish frame', '/images/products/Fashion Sunglasses.jpg'),
('Wool Scarf', 24.99, NULL, NULL, 'Winter Wear', '0.2 kg', '180x30 cm', 'Accessories', 'Soft wool scarf to keep you warm in winter', '/images/products/Wool Scarf.jpg'),
('Leather Wallet', 34.99, 44.99, 22, 'Accessory Co', '0.15 kg', '12x9 cm', 'Accessories', 'Genuine leather wallet with multiple card slots', '/images/products/Leather Wallet.jpg'),
('Fashion Watch', 79.99, 99.99, 20, 'Time Co', '0.2 kg', 'One Size', 'Accessories', 'Stylish analog watch with leather strap', '/images/products/Fashion Watch.jpg');

-- Add more Fashion Products
INSERT INTO products (title, price, original_price, discount_percentage, source, weight, size, product_type, description, image_path) VALUES
-- Men's Clothing
('Canvas Sneakers', 49.99, 59.99, 17, 'Footwear Co', '0.4 kg', '40-45', 'Men\'s Clothing', 'Comfortable canvas sneakers for everyday wear', '/images/products/Canvas Sneakers.jpg'),

-- Women's Clothing
('Women\'s Formal Blazer', 69.99, 89.99, 22, 'Business Fashion', '0.7 kg', 'S, M, L', 'Women\'s Clothing', 'Elegant formal blazer for professional settings', '/images/products/Womens Formal Blazer.jpg');

-- Add more Home Decor products
INSERT INTO products (title, price, original_price, discount_percentage, source, weight, size, product_type, description, image_path) VALUES
('Wall Art Canvas', 59.99, 79.99, 25, 'Art Deco', '1.2 kg', '60x40 cm', 'Home Decor', 'Beautiful wall art canvas for living room decoration', 'https://placehold.co/600x400?text=Wall+Art+Canvas'),
('Decorative Throw Pillows', 34.99, 44.99, 22, 'Cozy Home', '0.5 kg', '45x45 cm', 'Home Decor', 'Set of 2 decorative throw pillows with modern patterns', 'https://placehold.co/600x400?text=Decorative+Pillows'),
('Table Centerpiece', 29.99, NULL, NULL, 'Elegant Living', '0.8 kg', '30x20x15 cm', 'Home Decor', 'Elegant table centerpiece for dining room decoration', 'https://placehold.co/600x400?text=Table+Centerpiece'),
('Floating Wall Shelves', 49.99, 59.99, 17, 'Modern Spaces', '2.5 kg', '80x20x5 cm', 'Home Decor', 'Set of 3 floating wall shelves with modern design', 'https://placehold.co/600x400?text=Floating+Shelves');

-- Add more Drinkware products
INSERT INTO products (title, price, original_price, discount_percentage, source, weight, size, product_type, description, image_path) VALUES
('Glass Tumbler Set', 39.99, 49.99, 20, 'Crystal Clear', '1.2 kg', '350ml x 6', 'Drinkware', 'Set of 6 elegant glass tumblers for everyday use', 'https://placehold.co/600x400?text=Glass+Tumblers'),
('Ceramic Coffee Mugs', 29.99, 34.99, 14, 'Morning Brew', '1.5 kg', '400ml x 4', 'Drinkware', 'Set of 4 ceramic coffee mugs with modern design', 'https://placehold.co/600x400?text=Coffee+Mugs'),
('Stainless Steel Travel Mug', 24.99, NULL, NULL, 'On The Go', '0.3 kg', '500ml', 'Drinkware', 'Insulated stainless steel travel mug for hot and cold beverages', 'https://placehold.co/600x400?text=Travel+Mug'),
('Wine Glass Set', 54.99, 69.99, 21, 'Fine Dining', '1.8 kg', '350ml x 6', 'Drinkware', 'Set of 6 crystal wine glasses for special occasions', 'https://placehold.co/600x400?text=Wine+Glasses');
