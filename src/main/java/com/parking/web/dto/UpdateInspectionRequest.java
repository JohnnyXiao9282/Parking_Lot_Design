package com.parking.web.dto;

import com.parking.entity.InspectionRecord.InspectionStatus;
import lombok.Data;

@Data
public class UpdateInspectionRequest {

    private InspectionStatus status;
    private String notes;
}

