package com.parking.service;

import com.parking.entity.Admin;
import com.parking.entity.InspectionRecord;
import com.parking.entity.InspectionRecord.InspectionStatus;
import com.parking.entity.ParkingLot;
import com.parking.exception.InvalidDateRangeException;
import com.parking.exception.ResourceNotFoundException;
import com.parking.repository.AdminRepository;
import com.parking.repository.InspectionRecordRepository;
import com.parking.repository.ParkingLotRepository;
import com.parking.repository.ParkingSpotRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class InspectionServiceImpl implements IAdminInspectionService {

    private final InspectionRecordRepository inspectionRecordRepository;
    private final ParkingLotRepository parkingLotRepository;
    private final AdminRepository adminRepository;
    private final ParkingSpotRepository parkingSpotRepository;

    public InspectionServiceImpl(InspectionRecordRepository inspectionRecordRepository,
                                 ParkingLotRepository parkingLotRepository,
                                 AdminRepository adminRepository,
                                 ParkingSpotRepository parkingSpotRepository) {
        this.inspectionRecordRepository = inspectionRecordRepository;
        this.parkingLotRepository = parkingLotRepository;
        this.adminRepository = adminRepository;
        this.parkingSpotRepository = parkingSpotRepository;
    }

    // ─── IAdminInspectionService ─────────────────────────────────────────────────

    @Transactional
    @Override
    public InspectionRecord conductInspection(Long parkingLotId, Long adminId, String notes) {
        ParkingLot parkingLot = parkingLotRepository.findById(parkingLotId)
                .orElseThrow(() -> new ResourceNotFoundException("Parking lot not found: " + parkingLotId));

        Admin inspector = adminRepository.findById(adminId)
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found: " + adminId));

        int totalSpots = parkingSpotRepository.countByParkingLotId(parkingLotId);
        int occupiedSpots = parkingSpotRepository.countOccupiedByParkingLotId(parkingLotId);

        int availableSpots = totalSpots - occupiedSpots;
        InspectionStatus status = determineStatus(totalSpots, occupiedSpots);

        InspectionRecord record = new InspectionRecord();
        record.setParkingLot(parkingLot);
        record.setInspector(inspector);
        record.setInspectionTime(LocalDateTime.now());
        record.setTotalSpots(totalSpots);
        record.setOccupiedSpots(occupiedSpots);
        record.setAvailableSpots(availableSpots);
        record.setStatus(status);
        record.setNotes(notes);

        return inspectionRecordRepository.save(record);
    }

    @Transactional
    @Override
    public InspectionRecord updateInspection(Long id, InspectionStatus newStatus, String newNotes) {
        InspectionRecord record = getInspectionById(id);
        if (newStatus != null) record.setStatus(newStatus);
        if (newNotes != null) record.setNotes(newNotes);
        return inspectionRecordRepository.save(record);
    }

    @Transactional
    @Override
    public void deleteInspection(Long id) {
        if (!inspectionRecordRepository.existsById(id)) {
            throw new ResourceNotFoundException("Inspection record not found: " + id);
        }
        inspectionRecordRepository.deleteById(id);
    }

    // ─── IInspectionService ──────────────────────────────────────────────────────

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
            throw new InvalidDateRangeException("Start time must be before end time");
        }
        return inspectionRecordRepository.findByInspectionTimeBetween(start, end);
    }

    @Override
    public InspectionRecord getInspectionById(Long id) {
        return inspectionRecordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inspection record not found: " + id));
    }

    // ─── Private Helpers ─────────────────────────────────────────────────────────

    private InspectionStatus determineStatus(int total, int occupied) {
        if (total == 0) return InspectionStatus.FAILED;
        double occupancyRate = (double) occupied / total * 100;
        if (occupancyRate >= 90.0) return InspectionStatus.NEEDS_MAINTENANCE;
        return InspectionStatus.PASSED;
    }
}
