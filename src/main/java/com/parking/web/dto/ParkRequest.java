package com.parking.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ParkRequest {

    @NotBlank(message = "License plate is required")
    private String licensePlate;

    @NotBlank(message = "Make is required")
    private String make;

    @NotBlank(message = "Model is required")
    private String model;

    @NotNull(message = "Car type is required: SMALL or LARGE")
    private CarType carType;

    private int hourlyRate = 5; // default rate, can be overridden

    public enum CarType {
        SMALL, LARGE
    }
}

