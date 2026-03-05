package com.parking.web;

import com.parking.entity.Level;
import com.parking.repository.LevelRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/spots")
public class SpotAvailabilityController {

    private final LevelRepository levelRepository;

    public SpotAvailabilityController(LevelRepository levelRepository) {
        this.levelRepository = levelRepository;
    }

    /**
     * Returns available spot counts split by level type.
     * GET /api/spots/availability
     * Response:
     * {
     *   "smallCar":  { "available": 198, "total": 200 },
     *   "largeCar":  { "available": 99,  "total": 100 }
     * }
     */
    @GetMapping("/availability")
    public ResponseEntity<Map<String, Object>> getAvailability() {
        List<Level> smallLevels = levelRepository.findByIsSmallCarLevel(true);
        List<Level> largeLevels = levelRepository.findByIsSmallCarLevel(false);

        int smallAvail = smallLevels.stream().mapToInt(Level::getAvailableSpots).sum();
        int smallTotal = smallLevels.stream().mapToInt(Level::getTotalSpots).sum();

        int largeAvail = largeLevels.stream().mapToInt(Level::getAvailableSpots).sum();
        int largeTotal = largeLevels.stream().mapToInt(Level::getTotalSpots).sum();

        return ResponseEntity.ok(Map.of(
                "smallCar", Map.of("available", smallAvail, "total", smallTotal),
                "largeCar", Map.of("available", largeAvail, "total", largeTotal)
        ));
    }
}

