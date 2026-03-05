package com.parking.web.dto;

import com.parking.entity.CardPayment;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CardPaymentResponse {

    private final Long id;
    private final Long carId;
    private final String licensePlate;
    private final double amount;
    private final String cardNumber;
    private final String transactionId;
    private final boolean successful;
    private final LocalDateTime paymentTimestamp;

    public CardPaymentResponse(CardPayment payment) {
        this.id = payment.getId();
        this.carId = payment.getCar().getId();
        this.licensePlate = payment.getCar().getLicensePlate();
        this.amount = payment.getAmount();
        this.cardNumber = payment.getCardNumber();
        this.transactionId = payment.getTransactionId();
        this.successful = payment.isSuccessful();
        this.paymentTimestamp = payment.getPaymentTimestamp();
    }
}

