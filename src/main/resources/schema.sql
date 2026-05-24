
-- Users table
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    full_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255),
    phone_number VARCHAR(20),
    registered_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    active BOOLEAN NOT NULL DEFAULT true,
    role VARCHAR(20) NOT NULL DEFAULT 'HOME_SEEKER'
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
