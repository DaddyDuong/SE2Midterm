-- Use the database
USE se2midterm;

-- Add missing profile fields to the user table
ALTER TABLE user
ADD COLUMN IF NOT EXISTS full_name VARCHAR(100),
ADD COLUMN IF NOT EXISTS address TEXT,
ADD COLUMN IF NOT EXISTS age INT,
ADD COLUMN IF NOT EXISTS gender VARCHAR(20),
ADD COLUMN IF NOT EXISTS avatar_url VARCHAR(255),
ADD COLUMN IF NOT EXISTS preferred_payment_method VARCHAR(50);

-- Update existing users with some default values
UPDATE user
SET full_name = username,
    gender = 'Not specified'
 