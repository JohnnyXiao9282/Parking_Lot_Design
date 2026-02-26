package com.parking.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "cash_payments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CashPayment implements Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "car_id", nullable = false)
    private Car car;

    @Column(nullable = false)
    private double amount;

    @Column(nullable = false)
    private double cashReceived;

    @Column(nullable = false)
    private double changeGiven;

    @Column(nullable = false)
    private boolean successful;

    @Column(nullable = false)
    private LocalDateTime paymentTimestamp = LocalDateTime.now();

    @Override
    public boolean processPayment(double amount) {
        if (cashReceived >= amount) {
            this.changeGiven = cashReceived - amount;
            this.successful = true;
            return true;
        }
        this.successful = false;
        return false;
    }

    @Override
    public double getAmount() {
        return amount;
    }

    @Override
    public String getPaymentMethod() {
        return "CASH";
    }

    @Override
    public boolean isSuccessful() {
        return successful;
    }
}

