package com.parking.config;

import com.parking.entity.*;
import com.parking.repository.AdminRepository;
import com.parking.repository.ParkingLotRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private final ParkingLotRepository parkingLotRepository;
    private final AdminRepository adminRepository;
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void run(ApplicationArguments args) {
        migrateEnumColumns();   // commits immediately in its own transaction
        backfillParkedSince();  // fix parked cars with no timestamp
        seedData();             // separate transaction for JPA operations
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void backfillParkedSince() {
        try {
            int updated = jdbcTemplate.update(
                "UPDATE cars SET parked_since = NOW() - INTERVAL '1 hour' " +
                "WHERE is_parked = true AND parked_since IS NULL"
            );
            if (updated > 0) {
                log.info("[DataInitializer] Backfilled parked_since for {} car(s) — set to 1 hour ago.", updated);
            }
        } catch (Exception e) {
            log.debug("[DataInitializer] backfillParkedSince skipped: {}", e.getMessage());
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void migrateEnumColumns() {
        try {
            jdbcTemplate.execute(
                "ALTER TABLE admins ALTER COLUMN role TYPE VARCHAR(20) USING role::text"
            );
            log.info("[DataInitializer] Migrated admins.role to VARCHAR.");
        } catch (Exception e) {
            log.debug("[DataInitializer] admins.role already VARCHAR or migration skipped: {}", e.getMessage());
        }
        try {
            jdbcTemplate.execute(
                "ALTER TABLE inspection_records ALTER COLUMN status TYPE VARCHAR(30) USING status::text"
            );
            log.info("[DataInitializer] Migrated inspection_records.status to VARCHAR.");
        } catch (Exception e) {
            log.debug("[DataInitializer] inspection_records.status already VARCHAR or migration skipped: {}", e.getMessage());
        }
    }

    @Transactional
    public void seedData() {
        seedParkingLot();
        seedAdmin();
    }

    // ── Parking Lot ───────────────────────────────────────────────────────────────
    private void seedParkingLot() {
        if (parkingLotRepository.existsByName("ParkSmart Main Lot")) {
            log.info("[DataInitializer] Parking lot already seeded – skipping.");
            return;
        }

        ParkingLot lot = new ParkingLot();
        lot.setName("ParkSmart Main Lot");
        lot.setLocation("123 Main Street");

        // Level 1 – Small cars (10 spots)
        Level level1 = buildLevel(1, true, 200, lot);

        // Level 2 – Large cars (10 spots)
        Level level2 = buildLevel(2, false, 100, lot);

        lot.addLevel(level1);
        lot.addLevel(level2);

        parkingLotRepository.save(lot);
        log.info("[DataInitializer] Seeded parking lot '{}' with 2 levels (300 spots total).",
                lot.getName());
    }

    private Level buildLevel(int levelNumber, boolean isSmallCarLevel,
                             int spotCount, ParkingLot lot) {
        Level level = new Level();
        level.setLevelNumber(levelNumber);
        level.setSmallCarLevel(isSmallCarLevel);
        level.setTotalSpots(spotCount);
        level.setAvailableSpots(spotCount);
        level.setParkingLot(lot);

        for (int i = 1; i <= spotCount; i++) {
            ParkingSpot spot = new ParkingSpot();
            spot.setSpotNumber(i);
            spot.setSmallCarSpot(isSmallCarLevel);
            spot.setOccupied(false);
            level.addParkingSpot(spot);
        }
        return level;
    }

    // ── Default Admin ─────────────────────────────────────────────────────────────
    private void seedAdmin() {
        if (adminRepository.existsByUsername("admin")) {
            log.info("[DataInitializer] Default admin already exists – skipping.");
            return;
        }

        Admin admin = new Admin();
        admin.setUsername("admin");
        admin.setPassword("123456");   // NOTE: plain-text for dev only – hash in production
        admin.setFullName("System Administrator");
        admin.setEmail("julian151719@gmail.com");
        admin.setRole(Admin.Role.SUPER_ADMIN);

        adminRepository.save(admin);
        log.info("[DataInitializer] Seeded default admin (username: admin).");
    }
}

