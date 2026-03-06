package com.parking.web.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class ConductInspectionRequest {

    @NotNull(message = "Parking lot ID is required")
    @Positive(message = "Parking lot ID must be positive")
    private Long parkingLotId;

    @NotNull(message = "Admin ID is required")
    @Positive(message = "Admin ID must be positive")
    private Long adminId;

    private String notes;
}

