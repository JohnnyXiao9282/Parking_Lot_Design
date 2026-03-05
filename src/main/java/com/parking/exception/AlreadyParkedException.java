package com.parking.exception;

public class AlreadyParkedException extends RuntimeException {
    public AlreadyParkedException(String message) {
        super(message);
    }
}

