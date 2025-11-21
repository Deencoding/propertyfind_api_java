-- This file runs automatically when PostgreSQL container starts for the first time

-- Create enum type for user roles
CREATE TYPE user_role AS ENUM ('ADMIN', 'HOME_SEEKER', 'HOME_PROVIDER');

-- Users table
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    full_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    phone_number VARCHAR(20),
    registered_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    active BOOLEAN NOT NULL DEFAULT true,
    role user_role NOT NULL DEFAULT 'HOME_SEEKER'
);

-- Create indexes for users table
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_role ON users(role);
CREATE INDEX idx_users_active ON users(active);

-- Properties table
CREATE TABLE properties (
    id BIGSERIAL PRIMARY KEY,
    description TEXT,
    title VARCHAR(255) NOT NULL,
    address VARCHAR(500) NOT NULL,
    city VARCHAR(100) NOT NULL,
    state VARCHAR(100) NOT NULL,
    country VARCHAR(100) NOT NULL,
    price_per_year DECIMAL(15, 2) NOT NULL,
    bedroom INTEGER NOT NULL DEFAULT 0,
    bathroom INTEGER NOT NULL DEFAULT 0,
    area DOUBLE PRECISION NOT NULL,
    image_urls TEXT[],
    available BOOLEAN NOT NULL DEFAULT true,
    listed_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    provider_id BIGINT NOT NULL,
    CONSTRAINT fk_provider
        FOREIGN KEY (provider_id)
        REFERENCES users(id)
        ON DELETE CASCADE
);

-- Create indexes for properties table
CREATE INDEX idx_properties_provider_id ON properties(provider_id);
CREATE INDEX idx_properties_city ON properties(city);
CREATE INDEX idx_properties_state ON properties(state);
CREATE INDEX idx_properties_available ON properties(available);
CREATE INDEX idx_properties_price ON properties(price_per_year);
CREATE INDEX idx_properties_bedroom ON properties(bedroom);
CREATE INDEX idx_properties_bathroom ON properties(bathroom);

-- Create a function to automatically update the updated_at timestamp
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

-- Create triggers to auto-update updated_at
CREATE TRIGGER update_users_updated_at BEFORE UPDATE ON users
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_properties_updated_at BEFORE UPDATE ON properties
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
