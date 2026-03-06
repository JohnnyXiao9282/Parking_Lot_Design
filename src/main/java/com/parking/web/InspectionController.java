package com.parking.web;

import com.parking.entity.InspectionRecord.InspectionStatus;
import com.parking.service.IInspectionService;
import com.parking.web.dto.InspectionRecordResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/inspections")
public class InspectionController {

    private final IInspectionService inspectionService;

    public InspectionController(@Qualifier("userInspectionService") IInspectionService inspectionService) {
        this.inspectionService = inspectionService;
    }

    /**
     * Get an inspection record by its ID.
     * GET /api/inspections/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<InspectionRecordResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(new InspectionRecordResponse(inspectionService.getInspectionById(id)));
    }

    /**
     * Get all inspections for a parking lot.
     * GET /api/inspections/lot/{parkingLotId}
     */
    @GetMapping("/lot/{parkingLotId}")
    public ResponseEntity<List<InspectionRecordResponse>> getByParkingLot(@PathVariable Long parkingLotId) {
        List<InspectionRecordResponse> responses = inspectionService.getInspectionsByParkingLot(parkingLotId)
                .stream()
                .map(InspectionRecordResponse::new)
                .toList();
        return ResponseEntity.ok(responses);
    }

    /**
     * Get the latest inspections for a parking lot.
     * GET /api/inspections/lot/{parkingLotId}/latest
     */
    @GetMapping("/lot/{parkingLotId}/latest")
    public ResponseEntity<List<InspectionRecordResponse>> getLatestByParkingLot(@PathVariable Long parkingLotId) {
        List<InspectionRecordResponse> responses = inspectionService.getLatestInspections(parkingLotId)
                .stream()
                .map(InspectionRecordResponse::new)
                .toList();
        return ResponseEntity.ok(responses);
    }


    /**
     * Get all inspections by status.
     * GET /api/inspections/status/{status}
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<InspectionRecordResponse>> getByStatus(@PathVariable InspectionStatus status) {
        List<InspectionRecordResponse> responses = inspectionService.getInspectionsByStatus(status)
                .stream()
                .map(InspectionRecordResponse::new)
                .toList();
        return ResponseEntity.ok(responses);
    }

    /**
     * Get all inspections within a date range.
     * GET /api/inspections/range?start=...&end=...
     */
    @GetMapping("/range")
    public ResponseEntity<List<InspectionRecordResponse>> getByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        List<InspectionRecordResponse> responses = inspectionService.getInspectionsByDateRange(start, end)
                .stream()
                .map(InspectionRecordResponse::new)
                .toList();
        return ResponseEntity.ok(responses);
    }
}

