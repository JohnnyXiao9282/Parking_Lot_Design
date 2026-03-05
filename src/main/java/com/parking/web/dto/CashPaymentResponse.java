package com.parking.web.dto;

import com.parking.entity.CashPayment;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CashPaymentResponse {

    private final Long id;
    private final Long carId;
    private final String licensePlate;
    private final double amount;
    private final double cashReceived;
    private final double changeGiven;
    private final boolean successful;
    private final LocalDateTime paymentTimestamp;

    public CashPaymentResponse(CashPayment payment) {
        this.id = payment.getId();
        this.carId = payment.getCar().getId();
        this.licensePlate = payment.getCar().getLicensePlate();
        this.amount = payment.getAmount();
        this.cashReceived = payment.getCashReceived();
        this.changeGiven = payment.getChangeGiven();
        this.successful = payment.isSuccessful();
        this.paymentTimestamp = payment.getPaymentTimestamp();
    }
}

