package com.parking.service;

public interface Payment {
    boolean processPayment(double amount);
    double getAmount();
    String getPaymentMethod();
    boolean isSuccessful();
}

