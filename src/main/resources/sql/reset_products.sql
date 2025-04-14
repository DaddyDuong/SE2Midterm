-- Use the database
USE se2midterm;

-- Drop and recreate products table
DROP TABLE IF EXISTS products;

-- Create Product table
CREATE TABLE products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL UNIQUE,
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

-- System will re-run config.sql with the spring.sql.init.schema-locations property 