package com.parking.web;

import com.parking.service.ILeaveService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/leave")
public class LeaveController {

    private final ILeaveService leaveService;

    public LeaveController(ILeaveService leaveService) {
        this.leaveService = leaveService;
    }

    /**
     * Release a car from its parking spot.
     * POST /api/leave/{carId}
     *
     * @param carId the ID of the car to release
     * @return success status
     */
    @PostMapping("/{carId}")
    public ResponseEntity<Map<String, Object>> leave(@PathVariable Long carId) {
        leaveService.leave(carId);
        return ResponseEntity.ok(Map.of(
                "carId", carId,
                "success", true,
                "message", "Car successfully left the parking spot"
        ));
    }
}

