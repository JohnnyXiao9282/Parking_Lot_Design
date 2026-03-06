package com.parking.web;

import com.parking.service.IAdminInspectionService;
import com.parking.web.dto.ConductInspectionRequest;
import com.parking.web.dto.InspectionRecordResponse;
import com.parking.web.dto.UpdateInspectionRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/inspections")
public class AdminController {

    private final IAdminInspectionService adminInspectionService;

    public AdminController(IAdminInspectionService adminInspectionService) {
        this.adminInspectionService = adminInspectionService;
    }

    /**
     * Conduct a new inspection on a parking lot.
     * POST /api/admin/inspections
     */
    @PostMapping
    public ResponseEntity<InspectionRecordResponse> conductInspection(
            @Valid @RequestBody ConductInspectionRequest request) {
        InspectionRecordResponse response = new InspectionRecordResponse(
                adminInspectionService.conductInspection(
                        request.getParkingLotId(),
                        request.getAdminId(),
                        request.getNotes()
                )
        );
        return ResponseEntity.ok(response);
    }

    /**
     * Update status or notes of an existing inspection.
     * PATCH /api/admin/inspections/{id}
     */
    @PatchMapping("/{id}")
    public ResponseEntity<InspectionRecordResponse> updateInspection(
            @PathVariable Long id,
            @RequestBody UpdateInspectionRequest request) {
        InspectionRecordResponse response = new InspectionRecordResponse(
                adminInspectionService.updateInspection(
                        id,
                        request.getStatus(),
                        request.getNotes()
                )
        );
        return ResponseEntity.ok(response);
    }

    /**
     * Delete an inspection record.
     * DELETE /api/admin/inspections/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteInspection(@PathVariable Long id) {
        adminInspectionService.deleteInspection(id);
        return ResponseEntity.ok(Map.of(
                "id", id,
                "success", true,
                "message", "Inspection record deleted successfully"
        ));
    }

    /**
     * Get all inspections conducted by a specific admin.
     * GET /api/admin/inspections/by-admin/{adminId}
     */
    @GetMapping("/by-admin/{adminId}")
    public ResponseEntity<List<InspectionRecordResponse>> getByAdmin(@PathVariable Long adminId) {
        List<InspectionRecordResponse> responses = adminInspectionService.getInspectionsByAdmin(adminId)
                .stream()
                .map(InspectionRecordResponse::new)
                .toList();
        return ResponseEntity.ok(responses);
    }
}

