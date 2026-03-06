package com.parking.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CardPaymentRequest {

    @Positive(message = "Car ID must be positive")
    private Long carId;

    @Positive(message = "Amount must be positive")
    private double amount;

    @NotBlank(message = "Card number is required")
    private String cardNumber;
}

