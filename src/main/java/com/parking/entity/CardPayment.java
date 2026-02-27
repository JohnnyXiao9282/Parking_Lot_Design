package com.parking.entity;

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
public class CardPayment {

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
}
