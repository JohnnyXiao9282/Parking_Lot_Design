package com.parking.service;

import com.parking.entity.CardPayment;

import java.time.LocalDateTime;
import java.util.List;

public interface ICardPaymentService extends Payment {
    CardPayment processCardPayment(Long carId, double amount, String cardNumber);
    CardPayment getPaymentById(Long id);
    CardPayment getPaymentByTransactionId(String transactionId);
    List<CardPayment> getPaymentsByCarId(Long carId);
    List<CardPayment> getPaymentsByDateRange(LocalDateTime start, LocalDateTime end);
}

