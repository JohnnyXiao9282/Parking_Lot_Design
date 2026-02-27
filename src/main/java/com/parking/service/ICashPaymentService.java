package com.parking.service;

import com.parking.entity.CashPayment;

import java.time.LocalDateTime;
import java.util.List;

public interface ICashPaymentService extends Payment {
    CashPayment processCashPayment(Long carId, double amount, double cashReceived);
    CashPayment getPaymentById(Long id);
    List<CashPayment> getPaymentsByCarId(Long carId);
    List<CashPayment> getPaymentsByDateRange(LocalDateTime start, LocalDateTime end);
}

