package com.parking.web;

import com.parking.entity.Car;
import com.parking.entity.ParkingSpot;
import com.parking.repository.CarRepository;
import com.parking.repository.LevelRepository;
import com.parking.repository.ParkingSpotRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/spots")
public class SpotAvailabilityController {

    private final LevelRepository levelRepository;
    private final ParkingSpotRepository parkingSpotRepository;
    private final CarRepository carRepository;

    public SpotAvailabilityController(LevelRepository levelRepository,
                                      ParkingSpotRepository parkingSpotRepository,
                                      CarRepository carRepository) {
        this.levelRepository = levelRepository;
        this.parkingSpotRepository = parkingSpotRepository;
        this.carRepository = carRepository;
    }

    /** GET /api/spots/availability */
    @GetMapping("/availability")
    public ResponseEntity<Map<String, Object>> getAvailability() {
        List<ParkingSpot> smallSpots = parkingSpotRepository.findByIsSmallCarSpotOrderBySpotNumberAsc(true);
        List<ParkingSpot> largeSpots = parkingSpotRepository.findByIsSmallCarSpotOrderBySpotNumberAsc(false);

        long smallAvail = smallSpots.stream().filter(ps -> !ps.isOccupied()).count();
        long largeAvail = largeSpots.stream().filter(ps -> !ps.isOccupied()).count();

        return ResponseEntity.ok(Map.of(
                "smallCar", Map.of("available", smallAvail, "total", smallSpots.size()),
                "largeCar", Map.of("available", largeAvail, "total", largeSpots.size())
        ));
    }

    /**
     * GET /api/spots/floor?small=true
     * Returns every spot on the small-car floor (small=true) or large-car floor (small=false).
     * Response: [ { "spotId": 1, "spotNumber": 1, "occupied": false }, ... ]
     */
    @GetMapping("/floor")
    public ResponseEntity<List<Map<String, Object>>> getFloorSpots(
            @RequestParam(defaultValue = "true") boolean small) {

        List<Map<String, Object>> spots = parkingSpotRepository
                .findByIsSmallCarSpotOrderBySpotNumberAsc(small)
                .stream()
                .map(spot -> Map.<String, Object>of(
                        "spotId",     spot.getId(),
                        "spotNumber", spot.getSpotNumber(),
                        "occupied",   spot.isOccupied()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(spots);
    }

    /**
     * GET /api/spots/preview?small=true
     * Returns the next available spot (read-only, nothing is saved).
     */
    @GetMapping("/preview")
    public ResponseEntity<Map<String, Object>> previewNextSpot(
            @RequestParam(defaultValue = "true") boolean small) {

        return parkingSpotRepository
                .findByIsSmallCarSpotOrderBySpotNumberAsc(small)
                .stream()
                .filter(ps -> !ps.isOccupied())
                .findFirst()
                .map(spot -> ResponseEntity.ok(Map.<String, Object>of(
                        "spotId",     spot.getId(),
                        "spotNumber", spot.getSpotNumber()
                )))
                .orElseGet(() -> ResponseEntity.ok(Map.<String, Object>of(
                        "spotId", -1, "spotNumber", -1
                )));
    }

    /**
     * GET /api/spots/quote/{licensePlate}
     * Returns car info + amount due for a parked car — read-only, nothing is saved.
     */
    @GetMapping("/quote/{licensePlate}")
    public ResponseEntity<?> getQuote(@PathVariable String licensePlate) {
        Car car = carRepository.findByLicensePlate(licensePlate).orElse(null);

        if (car == null) {
            return ResponseEntity.status(404)
                    .body(Map.of("message", "Car not found: " + licensePlate));
        }
        if (!car.isParked()) {
            return ResponseEntity.status(409)
                    .body(Map.of("message", "Car is not currently parked: " + licensePlate));
        }

        int hours = car.getParkedHours() != null ? car.getParkedHours() : 1;
        double amount = (double) car.getHourlyRate() * hours;

        return ResponseEntity.ok(Map.of(
                "carId",        car.getId(),
                "licensePlate", car.getLicensePlate(),
                "make",         car.getMake(),
                "model",        car.getModel(),
                "hourlyRate",   car.getHourlyRate(),
                "hours",        hours,
                "amountDue",    amount
        ));
    }
}

