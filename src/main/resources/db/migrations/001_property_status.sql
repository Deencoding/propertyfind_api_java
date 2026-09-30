-- Run once on an existing database before deploying the status-aware application.
BEGIN;
ALTER TABLE properties ADD COLUMN status VARCHAR(20);
UPDATE properties SET status = CASE WHEN available THEN 'AVAILABLE' ELSE 'RENTED' END;
ALTER TABLE properties ALTER COLUMN status SET DEFAULT 'AVAILABLE';
ALTER TABLE properties ALTER COLUMN status SET NOT NULL;
ALTER TABLE properties ADD CONSTRAINT chk_property_status
    CHECK (status IN ('AVAILABLE', 'RENTED', 'ARCHIVED'));
ALTER TABLE properties ADD CONSTRAINT chk_property_availability
    CHECK (available = (status = 'AVAILABLE'));
COMMIT;
