package com.parking.service;

import com.parking.entity.InspectionRecord;
import com.parking.entity.InspectionRecord.InspectionStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface IInspectionService {
    List<InspectionRecord> getInspectionsByParkingLot(Long parkingLotId);
    List<InspectionRecord> getLatestInspections(Long parkingLotId);
    List<InspectionRecord> getInspectionsByStatus(InspectionStatus status);
    List<InspectionRecord> getInspectionsByDateRange(LocalDateTime start, LocalDateTime end);
    InspectionRecord getInspectionById(Long id);
}



