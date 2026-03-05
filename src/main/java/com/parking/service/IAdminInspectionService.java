package com.parking.service;

import com.parking.entity.InspectionRecord;
import com.parking.entity.InspectionRecord.InspectionStatus;

import java.util.List;

public interface IAdminInspectionService extends IInspectionService {
    InspectionRecord conductInspection(Long parkingLotId, Long adminId, String notes);
    InspectionRecord updateInspection(Long id, InspectionStatus newStatus, String newNotes);
    void deleteInspection(Long id);
    List<InspectionRecord> getInspectionsByAdmin(Long adminId);
}

