-- ============================================================
--  ParkSmart – PostgreSQL Schema
--  Generated from Java entity classes
-- ============================================================

-- ----------------------------------------------------------------
-- Drop tables in reverse dependency order (idempotent re-run)
-- ----------------------------------------------------------------
DROP TABLE IF EXISTS card_payments       CASCADE;
DROP TABLE IF EXISTS cash_payments       CASCADE;
DROP TABLE IF EXISTS inspection_records  CASCADE;
DROP TABLE IF EXISTS parking_spots       CASCADE;
DROP TABLE IF EXISTS levels              CASCADE;
DROP TABLE IF EXISTS parking_lots        CASCADE;
DROP TABLE IF EXISTS cars                CASCADE;
DROP TABLE IF EXISTS admins              CASCADE;

-- ----------------------------------------------------------------
-- ENUM types
-- ----------------------------------------------------------------
DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'admin_role') THEN
        CREATE TYPE admin_role AS ENUM ('ADMIN', 'SUPER_ADMIN');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'inspection_status') THEN
        CREATE TYPE inspection_status AS ENUM ('PASSED', 'FAILED', 'NEEDS_MAINTENANCE');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'car_type') THEN
        CREATE TYPE car_type AS ENUM ('SMALL', 'LARGE');
    END IF;
END$$;

-- ----------------------------------------------------------------
-- admins
--   Maps to: com.parking.entity.Admin
-- ----------------------------------------------------------------
CREATE TABLE admins (
    id          BIGSERIAL       PRIMARY KEY,
    username    TEXT            NOT NULL UNIQUE,
    password    TEXT            NOT NULL,
    full_name   TEXT            NOT NULL,
    email       TEXT            UNIQUE,
    role        admin_role      NOT NULL DEFAULT 'ADMIN',
    created_at  TIMESTAMP       NOT NULL DEFAULT NOW(),
    last_login  TIMESTAMP
);

-- ----------------------------------------------------------------
-- parking_lots
--   Maps to: com.parking.entity.ParkingLot
-- ----------------------------------------------------------------
CREATE TABLE parking_lots (
    id          BIGSERIAL   PRIMARY KEY,
    name        TEXT        NOT NULL,
    location    TEXT
);

-- ----------------------------------------------------------------
-- levels
--   Maps to: com.parking.entity.Level
--   Belongs to a ParkingLot; owns many ParkingSpots
-- ----------------------------------------------------------------
CREATE TABLE levels (
    id                  BIGSERIAL   PRIMARY KEY,
    level_number        INTEGER     NOT NULL,
    total_spots         INTEGER     NOT NULL,
    available_spots     INTEGER     NOT NULL,
    is_small_car_level  BOOLEAN     NOT NULL DEFAULT FALSE,
    parking_lot_id      BIGINT      NOT NULL
        REFERENCES parking_lots(id) ON DELETE CASCADE
);

-- ----------------------------------------------------------------
-- parking_spots
--   Maps to: com.parking.entity.ParkingSpot
--   Belongs to a Level
-- ----------------------------------------------------------------
CREATE TABLE parking_spots (
    id                  BIGSERIAL   PRIMARY KEY,
    spot_number         INTEGER     NOT NULL,
    is_small_car_spot   BOOLEAN     NOT NULL DEFAULT FALSE,
    is_occupied         BOOLEAN     NOT NULL DEFAULT FALSE,
    level_id            BIGINT      NOT NULL
        REFERENCES levels(id) ON DELETE CASCADE
);

-- ----------------------------------------------------------------
-- cars
--   Maps to: com.parking.entity.Car (abstract, SINGLE_TABLE strategy)
--   Subclasses: SmallCar (car_type = 'SMALL'), LargeCar (car_type = 'LARGE')
-- ----------------------------------------------------------------
CREATE TABLE cars (
    id              BIGSERIAL   PRIMARY KEY,
    car_type        car_type    NOT NULL,                  -- discriminator column
    make            TEXT        NOT NULL,
    model           TEXT        NOT NULL,
    license_plate   TEXT        NOT NULL,
    hourly_rate     INTEGER     NOT NULL,
    is_parked       BOOLEAN     NOT NULL DEFAULT FALSE,
    parked_hours    INTEGER
);

-- ----------------------------------------------------------------
-- inspection_records
--   Maps to: com.parking.entity.InspectionRecord
--   References: parking_lots, admins
-- ----------------------------------------------------------------
CREATE TABLE inspection_records (
    id                  BIGSERIAL           PRIMARY KEY,
    parking_lot_id      BIGINT              NOT NULL
        REFERENCES parking_lots(id) ON DELETE CASCADE,
    admin_id            BIGINT              NOT NULL
        REFERENCES admins(id),
    inspection_time     TIMESTAMP           NOT NULL DEFAULT NOW(),
    total_spots         INTEGER             NOT NULL,
    occupied_spots      INTEGER             NOT NULL,
    available_spots     INTEGER             NOT NULL,
    status              inspection_status   NOT NULL,
    notes               TEXT
);

-- ----------------------------------------------------------------
-- card_payments
--   Maps to: com.parking.service.CardPayment
--   References: cars
-- ----------------------------------------------------------------
CREATE TABLE card_payments (
    id                  BIGSERIAL   PRIMARY KEY,
    car_id              BIGINT      NOT NULL
        REFERENCES cars(id),
    amount              NUMERIC(10, 2) NOT NULL,
    card_number         TEXT        NOT NULL,   -- store last 4 digits only
    transaction_id      TEXT        NOT NULL,
    successful          BOOLEAN     NOT NULL DEFAULT FALSE,
    payment_timestamp   TIMESTAMP   NOT NULL DEFAULT NOW()
);

-- ----------------------------------------------------------------
-- cash_payments
--   Maps to: com.parking.service.CashPayment
--   References: cars
-- ----------------------------------------------------------------
CREATE TABLE cash_payments (
    id                  BIGSERIAL   PRIMARY KEY,
    car_id              BIGINT      NOT NULL
        REFERENCES cars(id),
    amount              NUMERIC(10, 2) NOT NULL,
    cash_received       NUMERIC(10, 2) NOT NULL,
    change_given        NUMERIC(10, 2) NOT NULL DEFAULT 0,
    successful          BOOLEAN     NOT NULL DEFAULT FALSE,
    payment_timestamp   TIMESTAMP   NOT NULL DEFAULT NOW()
);

-- ----------------------------------------------------------------
-- Indexes for common query patterns
-- ----------------------------------------------------------------
CREATE INDEX idx_levels_parking_lot         ON levels(parking_lot_id);
CREATE INDEX idx_spots_level                ON parking_spots(level_id);
CREATE INDEX idx_spots_occupied             ON parking_spots(is_occupied);
CREATE INDEX idx_spots_small_car            ON parking_spots(is_small_car_spot);
CREATE INDEX idx_cars_license_plate         ON cars(license_plate);
CREATE INDEX idx_inspection_parking_lot     ON inspection_records(parking_lot_id);
CREATE INDEX idx_inspection_admin           ON inspection_records(admin_id);
CREATE INDEX idx_inspection_time            ON inspection_records(inspection_time DESC);
CREATE INDEX idx_card_payments_car          ON card_payments(car_id);
CREATE INDEX idx_cash_payments_car          ON cash_payments(car_id);

