
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
    status VARCHAR(20) NOT NULL DEFAULT 'AVAILABLE',
    CONSTRAINT chk_property_status CHECK (status IN ('AVAILABLE', 'RENTED', 'ARCHIVED')),
    CONSTRAINT chk_property_availability CHECK (available = (status = 'AVAILABLE')),
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

-- Favorites table
CREATE TABLE favorites (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    property_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_favorite_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE,
    CONSTRAINT fk_favorite_property
        FOREIGN KEY (property_id)
        REFERENCES properties(id)
        ON DELETE CASCADE,
    CONSTRAINT unique_user_property UNIQUE (user_id, property_id)
);

CREATE INDEX idx_favorites_user_id ON favorites(user_id);
CREATE INDEX idx_favorites_property_id ON favorites(property_id);

-- Reviews table
CREATE TABLE reviews (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    property_id BIGINT NOT NULL,
    rating INTEGER NOT NULL CHECK (rating >= 1 AND rating <= 5),
    comment VARCHAR(1000),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_review_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE,
    CONSTRAINT fk_review_property
        FOREIGN KEY (property_id)
        REFERENCES properties(id)
        ON DELETE CASCADE,
    CONSTRAINT unique_user_property_review UNIQUE (user_id, property_id)
);

CREATE INDEX idx_reviews_property_id ON reviews(property_id);

-- Bookings table
CREATE TABLE bookings (
    id BIGSERIAL PRIMARY KEY,
    property_id BIGINT NOT NULL,
    home_seeker_id BIGINT NOT NULL,
    scheduled_date TIMESTAMP NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    message TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_booking_property
        FOREIGN KEY (property_id)
        REFERENCES properties(id)
        ON DELETE CASCADE,
    CONSTRAINT fk_booking_seeker
        FOREIGN KEY (home_seeker_id)
        REFERENCES users(id)
        ON DELETE CASCADE
);

CREATE INDEX idx_bookings_property_id ON bookings(property_id);
CREATE INDEX idx_bookings_seeker_id ON bookings(home_seeker_id);
CREATE INDEX idx_bookings_status ON bookings(status);
