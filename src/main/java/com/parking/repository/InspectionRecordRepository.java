package com.parking.repository;

import com.parking.entity.InspectionRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface InspectionRecordRepository extends JpaRepository<InspectionRecord, Long> {

    List<InspectionRecord> findByParkingLotId(Long parkingLotId);

    List<InspectionRecord> findByInspectorId(Long inspectorId);

    List<InspectionRecord> findByStatus(InspectionRecord.InspectionStatus status);

    List<InspectionRecord> findByInspectionTimeBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT ir FROM InspectionRecord ir WHERE ir.parkingLot.id = :parkingLotId ORDER BY ir.inspectionTime DESC")
    List<InspectionRecord> findLatestInspectionsByParkingLot(Long parkingLotId);
}

