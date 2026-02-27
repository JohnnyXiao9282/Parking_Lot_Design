package com.parking.service;

import com.parking.entity.InspectionRecord;
import com.parking.entity.InspectionRecord.InspectionStatus;
import com.parking.repository.InspectionRecordRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class InspectionServiceUserImpl implements IInspectionService {

    private final InspectionRecordRepository inspectionRecordRepository;

    public InspectionServiceUserImpl(InspectionRecordRepository inspectionRecordRepository) {
        this.inspectionRecordRepository = inspectionRecordRepository;
    }

    @Override
    public List<InspectionRecord> getInspectionsByParkingLot(Long parkingLotId) {
        return inspectionRecordRepository.findByParkingLotId(parkingLotId);
    }

    @Override
    public List<InspectionRecord> getLatestInspections(Long parkingLotId) {
        return inspectionRecordRepository.findLatestInspectionsByParkingLot(parkingLotId);
    }

    @Override
    public List<InspectionRecord> getInspectionsByAdmin(Long adminId) {
        return inspectionRecordRepository.findByInspectorId(adminId);
    }

    @Override
    public List<InspectionRecord> getInspectionsByStatus(InspectionStatus status) {
        return inspectionRecordRepository.findByStatus(status);
    }

    @Override
    public List<InspectionRecord> getInspectionsByDateRange(LocalDateTime start, LocalDateTime end) {
        if (start.isAfter(end)) {
            throw new RuntimeException("Start time must be before end time");
        }
        return inspectionRecordRepository.findByInspectionTimeBetween(start, end);
    }

    @Override
    public InspectionRecord getInspectionById(Long id) {
        return inspectionRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inspection record not found: " + id));
    }
}

