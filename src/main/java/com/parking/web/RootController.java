package com.parking.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class RootController {

    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> root() {
        return ResponseEntity.ok(Map.of(
                "app", "ParkSmart API",
                "status", "running",
                "version", "1.0.0",
                "endpoints", Map.of(
                        "park",        "POST /api/park",
                        "leave",       "POST /api/leave/{licensePlate}",
                        "payment",     "POST /api/payments/card | /cash",
                        "inspections", "GET  /api/inspections/lot/{id}",
                        "admin",       "POST /api/admin/inspections"
                )
        ));
    }

    @GetMapping("/favicon.ico")
    public ResponseEntity<Void> favicon() {
        return ResponseEntity.noContent().build();
    }
}

