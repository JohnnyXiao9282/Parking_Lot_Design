package com.parking.exception;

public class NotParkedException extends RuntimeException {
    public NotParkedException(String message) {
        super(message);
    }
}

