package com.parking.service;

import com.parking.entity.Car;
import com.parking.entity.CardPayment;
import com.parking.repository.CardPaymentRepository;
import com.parking.repository.CarRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CardPaymentServiceImpl implements ICardPaymentService {

    private final CardPaymentRepository cardPaymentRepository;
    private final CarRepository carRepository;

    private double currentAmount;
    private boolean currentSuccessful;

    public CardPaymentServiceImpl(CardPaymentRepository cardPaymentRepository, CarRepository carRepository) {
        this.cardPaymentRepository = cardPaymentRepository;
        this.carRepository = carRepository;
    }

    @Transactional
    @Override
    public CardPayment processCardPayment(Long carId, double amount, String cardNumber) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new RuntimeException("Car not found: " + carId));

        CardPayment payment = new CardPayment();
        payment.setCar(car);
        payment.setAmount(amount);
        payment.setCardNumber(cardNumber);
        payment.setPaymentTimestamp(LocalDateTime.now());

        boolean success = processPayment(amount);
        payment.setTransactionId("TXN" + System.currentTimeMillis());
        payment.setSuccessful(success);

        this.currentAmount = amount;
        this.currentSuccessful = success;

        return cardPaymentRepository.save(payment);
    }

    @Override
    public boolean processPayment(double amount) {
        // Simulate card authorization
        return amount > 0;
    }

    @Override
    public double getAmount() {
        return currentAmount;
    }

    @Override
    public String getPaymentMethod() {
        return "CARD";
    }

    @Override
    public boolean isSuccessful() {
        return currentSuccessful;
    }

    @Override
    public CardPayment getPaymentById(Long id) {
        return cardPaymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Card payment not found: " + id));
    }

    @Override
    public CardPayment getPaymentByTransactionId(String transactionId) {
        return cardPaymentRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found: " + transactionId));
    }

    @Override
    public List<CardPayment> getPaymentsByCarId(Long carId) {
        return cardPaymentRepository.findByCarId(carId);
    }

    @Override
    public List<CardPayment> getPaymentsByDateRange(LocalDateTime start, LocalDateTime end) {
        if (start.isAfter(end)) {
            throw new RuntimeException("Start time must be before end time");
        }
        return cardPaymentRepository.findByPaymentTimestampBetween(start, end);
    }
}

