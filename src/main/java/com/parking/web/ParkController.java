package com.parking.web;

import com.parking.entity.ParkingSpot;
import com.parking.service.IParkService;
import com.parking.web.dto.ParkRequest;
import com.parking.web.dto.ParkingSpotResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/park")
public class ParkController {

    private final IParkService parkService;

    public ParkController(IParkService parkService) {
        this.parkService = parkService;
    }

    /**
     * Register a car on arrival and assign it a parking spot.
     * If the car (by license plate) already exists in the system, it is reused.
     * POST /api/park
     *
     * @param request license plate, make, model, car type, hourly rate
     * @return the assigned ParkingSpot as a DTO
     */
    @PostMapping
    public ResponseEntity<ParkingSpotResponse> park(@Valid @RequestBody ParkRequest request) {
        ParkingSpot spot = parkService.park(request);
        return ResponseEntity.ok(new ParkingSpotResponse(spot));
    }
}

