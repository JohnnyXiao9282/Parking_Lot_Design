package com.parking.service;

import com.parking.entity.Car;
import com.parking.entity.CardPayment;
import com.parking.exception.InvalidDateRangeException;
import com.parking.exception.NotParkedException;
import com.parking.exception.ResourceNotFoundException;
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
    private final ILeaveService leaveService;

    public CardPaymentServiceImpl(CardPaymentRepository cardPaymentRepository,
                                  CarRepository carRepository,
                                  ILeaveService leaveService) {
        this.cardPaymentRepository = cardPaymentRepository;
        this.carRepository = carRepository;
        this.leaveService = leaveService;
    }

    @Transactional
    @Override
    public CardPayment processCardPayment(Long carId, double amount, String cardNumber) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new ResourceNotFoundException("Car not found: " + carId));

        if (!car.isParked()) {
            throw new NotParkedException("Car is not currently parked, cannot process payment: " + carId);
        }

        CardPayment payment = new CardPayment();
        payment.setCar(car);
        payment.setAmount(amount);
        payment.setCardNumber(cardNumber);
        payment.setPaymentTimestamp(LocalDateTime.now());
        payment.setTransactionId("TXN" + System.currentTimeMillis());

        boolean success = processPayment(amount);
        payment.setSuccessful(success);

        CardPayment saved = cardPaymentRepository.save(payment);

        if (success) {
            leaveService.leave(car.getLicensePlate());
        }

        return saved;
    }

    @Override
    public boolean processPayment(double amount) {
        // Simulate card authorization
        return amount > 0;
    }

    @Override
    public double getAmount() {
        throw new UnsupportedOperationException("Use processCardPayment() to get payment details");
    }

    @Override
    public String getPaymentMethod() {
        return "CARD";
    }

    @Override
    public boolean isSuccessful() {
        throw new UnsupportedOperationException("Use processCardPayment() to get payment details");
    }

    @Override
    public CardPayment getPaymentById(Long id) {
        return cardPaymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Card payment not found: " + id));
    }

    @Override
    public CardPayment getPaymentByTransactionId(String transactionId) {
        return cardPaymentRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found: " + transactionId));
    }

    @Override
    public List<CardPayment> getPaymentsByCarId(Long carId) {
        return cardPaymentRepository.findByCarId(carId);
    }

    @Override
    public List<CardPayment> getPaymentsByDateRange(LocalDateTime start, LocalDateTime end) {
        if (start.isAfter(end)) {
            throw new InvalidDateRangeException("Start time must be before end time");
        }
        return cardPaymentRepository.findByPaymentTimestampBetween(start, end);
    }
}

