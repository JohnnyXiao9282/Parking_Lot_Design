package com.parking.config;

import com.parking.entity.*;
import com.parking.repository.AdminRepository;
import com.parking.repository.ParkingLotRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private final ParkingLotRepository parkingLotRepository;
    private final AdminRepository adminRepository;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
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

