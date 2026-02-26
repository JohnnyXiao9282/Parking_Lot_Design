package com.parking.entity;

public interface Payment {
    boolean processPayment(double amount);
    double getAmount();
    String getPaymentMethod();
    boolean isSuccessful();
}

