package com.parking.service;

import com.parking.entity.Car;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "card_payments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardPayment implements Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "car_id", nullable = false)
    private Car car;

    @Column(nullable = false)
    private double amount;

    @Column(nullable = false)
    private String cardNumber; // Last 4 digits only for security

    @Column(nullable = false)
    private String transactionId;

    @Column(nullable = false)
    private boolean successful;

    @Column(nullable = false)
    private LocalDateTime paymentTimestamp = LocalDateTime.now();

    @Override
    public boolean processPayment(double amount) {
        // Simulate card processing
        this.transactionId = "TXN" + System.currentTimeMillis();
        this.successful = true;
        return true;
    }

    @Override
    public double getAmount() {
        return amount;
    }

    @Override
    public String getPaymentMethod() {
        return "CARD";
    }

    @Override
    public boolean isSuccessful() {
        return successful;
    }
}

