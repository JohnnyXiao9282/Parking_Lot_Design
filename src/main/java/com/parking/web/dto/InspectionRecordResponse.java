package com.parking.web.dto;

import com.parking.entity.InspectionRecord;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class InspectionRecordResponse {

    private final Long id;
    private final Long parkingLotId;
    private final String parkingLotName;
    private final Long adminId;
    private final String adminUsername;
    private final LocalDateTime inspectionTime;
    private final int totalSpots;
    private final int occupiedSpots;
    private final int availableSpots;
    private final double occupancyRate;
    private final String status;
    private final String notes;

    public InspectionRecordResponse(InspectionRecord record) {
        this.id = record.getId();
        this.parkingLotId = record.getParkingLot().getId();
        this.parkingLotName = record.getParkingLot().getName();
        this.adminId = record.getInspector().getId();
        this.adminUsername = record.getInspector().getUsername();
        this.inspectionTime = record.getInspectionTime();
        this.totalSpots = record.getTotalSpots();
        this.occupiedSpots = record.getOccupiedSpots();
        this.availableSpots = record.getAvailableSpots();
        this.occupancyRate = record.getOccupancyRate();
        this.status = record.getStatus().name();
        this.notes = record.getNotes();
    }
}

