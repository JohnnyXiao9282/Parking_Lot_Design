package com.parking.service;

import com.parking.entity.Car;
import com.parking.entity.CashPayment;
import com.parking.exception.InsufficientPaymentException;
import com.parking.exception.InvalidDateRangeException;
import com.parking.exception.NotParkedException;
import com.parking.exception.ResourceNotFoundException;
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
    private final ILeaveService leaveService;

    public CashPaymentServiceImpl(CashPaymentRepository cashPaymentRepository,
                                  CarRepository carRepository,
                                  ILeaveService leaveService) {
        this.cashPaymentRepository = cashPaymentRepository;
        this.carRepository = carRepository;
        this.leaveService = leaveService;
    }

    @Transactional
    @Override
    public CashPayment processCashPayment(Long carId, double amount, double cashReceived) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new ResourceNotFoundException("Car not found: " + carId));

        if (!car.isParked()) {
            throw new NotParkedException("Car is not currently parked, cannot process payment: " + carId);
        }

        if (cashReceived < amount) {
            throw new InsufficientPaymentException(
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

        CashPayment saved = cashPaymentRepository.save(payment);

        if (success) {
            leaveService.leave(car.getLicensePlate());
        }

        return saved;
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
                .orElseThrow(() -> new ResourceNotFoundException("Cash payment not found: " + id));
    }

    @Override
    public List<CashPayment> getPaymentsByCarId(Long carId) {
        return cashPaymentRepository.findByCarId(carId);
    }

    @Override
    public List<CashPayment> getPaymentsByDateRange(LocalDateTime start, LocalDateTime end) {
        if (start.isAfter(end)) {
            throw new InvalidDateRangeException("Start time must be before end time");
        }
        return cashPaymentRepository.findByPaymentTimestampBetween(start, end);
    }
}

