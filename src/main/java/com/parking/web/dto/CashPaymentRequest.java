package com.parking.web.dto;

import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CashPaymentRequest {

    @Positive(message = "Car ID must be positive")
    private Long carId;

    @Positive(message = "Amount must be positive")
    private double amount;

    @Positive(message = "Cash received must be positive")
    private double cashReceived;
}

