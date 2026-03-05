package com.parking.service;

import com.parking.entity.Car;
import com.parking.entity.CashPayment;
import com.parking.repository.CarRepository;
import com.parking.repository.CashPaymentRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CashPaymentServiceImpl implements ICashPaymentService {

    private final CashPaymentRepository cashPaymentRepository;
    private final CarRepository carRepository;

    public CashPaymentServiceImpl(CashPaymentRepository cashPaymentRepository, CarRepository carRepository) {
        this.cashPaymentRepository = cashPaymentRepository;
        this.carRepository = carRepository;
    }

    @Transactional
    @Override
    public CashPayment processCashPayment(Long carId, double amount, double cashReceived) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new RuntimeException("Car not found: " + carId));

        if (cashReceived < amount) {
            throw new RuntimeException(
                    "Insufficient cash: received " + cashReceived + " but amount due is " + amount);
        }

        CashPayment payment = new CashPayment();
        payment.setCar(car);
        payment.setAmount(amount);
        payment.setCashReceived(cashReceived);
        payment.setPaymentTimestamp(LocalDateTime.now());

        boolean success = processPayment(amount);
        payment.setChangeGiven(success ? cashReceived - amount : 0);
        payment.setSuccessful(success);

        return cashPaymentRepository.save(payment);
    }

    @Override
    public boolean processPayment(double amount) {
        return amount > 0;
    }

    @Override
    public double getAmount() {
        throw new UnsupportedOperationException("Use processCashPayment() to get payment details");
    }

    @Override
    public String getPaymentMethod() {
        return "CASH";
    }

    @Override
    public boolean isSuccessful() {
        throw new UnsupportedOperationException("Use processCashPayment() to get payment details");
    }

    @Override
    public CashPayment getPaymentById(Long id) {
        return cashPaymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cash payment not found: " + id));
    }

    @Override
    public List<CashPayment> getPaymentsByCarId(Long carId) {
        return cashPaymentRepository.findByCarId(carId);
    }

    @Override
    public List<CashPayment> getPaymentsByDateRange(LocalDateTime start, LocalDateTime end) {
        if (start.isAfter(end)) {
            throw new RuntimeException("Start time must be before end time");
        }
        return cashPaymentRepository.findByPaymentTimestampBetween(start, end);
    }
}

