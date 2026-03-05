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
     * Release a car from its parking spot by license plate.
     * POST /api/leave/{licensePlate}
     *
     * @param licensePlate the license plate of the car to release
     * @return success status
     */
    @PostMapping("/{licensePlate}")
    public ResponseEntity<Map<String, Object>> leave(@PathVariable String licensePlate) {
        leaveService.leave(licensePlate);
        return ResponseEntity.ok(Map.of(
                "licensePlate", licensePlate,
                "success", true,
                "message", "Car successfully left the parking spot"
        ));
    }
}

