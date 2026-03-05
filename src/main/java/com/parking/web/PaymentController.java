package com.parking.web;

import com.parking.service.ICardPaymentService;
import com.parking.service.ICashPaymentService;
import com.parking.web.dto.*;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final ICardPaymentService cardPaymentService;
    private final ICashPaymentService cashPaymentService;

    public PaymentController(ICardPaymentService cardPaymentService, ICashPaymentService cashPaymentService) {
        this.cardPaymentService = cardPaymentService;
        this.cashPaymentService = cashPaymentService;
    }

    // -------------------------------------------------------
    // Card Payment
    // -------------------------------------------------------

    /**
     * Process a card payment for a car.
     * POST /api/payments/card
     */
    @PostMapping("/card")
    public ResponseEntity<CardPaymentResponse> payByCard(@Valid @RequestBody CardPaymentRequest request) {
        CardPaymentResponse response = new CardPaymentResponse(
                cardPaymentService.processCardPayment(
                        request.getCarId(),
                        request.getAmount(),
                        request.getCardNumber()
                )
        );
        return ResponseEntity.ok(response);
    }

    /**
     * Get a card payment by its ID.
     * GET /api/payments/card/{id}
     */
    @GetMapping("/card/{id}")
    public ResponseEntity<CardPaymentResponse> getCardPaymentById(@PathVariable Long id) {
        return ResponseEntity.ok(new CardPaymentResponse(cardPaymentService.getPaymentById(id)));
    }

    /**
     * Get a card payment by its transaction ID.
     * GET /api/payments/card/transaction/{transactionId}
     */
    @GetMapping("/card/transaction/{transactionId}")
    public ResponseEntity<CardPaymentResponse> getCardPaymentByTransactionId(@PathVariable String transactionId) {
        return ResponseEntity.ok(new CardPaymentResponse(cardPaymentService.getPaymentByTransactionId(transactionId)));
    }

    /**
     * Get all card payments for a car.
     * GET /api/payments/card/car/{carId}
     */
    @GetMapping("/card/car/{carId}")
    public ResponseEntity<List<CardPaymentResponse>> getCardPaymentsByCarId(@PathVariable Long carId) {
        List<CardPaymentResponse> responses = cardPaymentService.getPaymentsByCarId(carId)
                .stream()
                .map(CardPaymentResponse::new)
                .toList();
        return ResponseEntity.ok(responses);
    }

    /**
     * Get all card payments within a date range.
     * GET /api/payments/card/range?start=...&end=...
     */
    @GetMapping("/card/range")
    public ResponseEntity<List<CardPaymentResponse>> getCardPaymentsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        List<CardPaymentResponse> responses = cardPaymentService.getPaymentsByDateRange(start, end)
                .stream()
                .map(CardPaymentResponse::new)
                .toList();
        return ResponseEntity.ok(responses);
    }

    // -------------------------------------------------------
    // Cash Payment
    // -------------------------------------------------------

    /**
     * Process a cash payment for a car.
     * POST /api/payments/cash
     */
    @PostMapping("/cash")
    public ResponseEntity<CashPaymentResponse> payByCash(@Valid @RequestBody CashPaymentRequest request) {
        CashPaymentResponse response = new CashPaymentResponse(
                cashPaymentService.processCashPayment(
                        request.getCarId(),
                        request.getAmount(),
                        request.getCashReceived()
                )
        );
        return ResponseEntity.ok(response);
    }

    /**
     * Get a cash payment by its ID.
     * GET /api/payments/cash/{id}
     */
    @GetMapping("/cash/{id}")
    public ResponseEntity<CashPaymentResponse> getCashPaymentById(@PathVariable Long id) {
        return ResponseEntity.ok(new CashPaymentResponse(cashPaymentService.getPaymentById(id)));
    }

    /**
     * Get all cash payments for a car.
     * GET /api/payments/cash/car/{carId}
     */
    @GetMapping("/cash/car/{carId}")
    public ResponseEntity<List<CashPaymentResponse>> getCashPaymentsByCarId(@PathVariable Long carId) {
        List<CashPaymentResponse> responses = cashPaymentService.getPaymentsByCarId(carId)
                .stream()
                .map(CashPaymentResponse::new)
                .toList();
        return ResponseEntity.ok(responses);
    }

    /**
     * Get all cash payments within a date range.
     * GET /api/payments/cash/range?start=...&end=...
     */
    @GetMapping("/cash/range")
    public ResponseEntity<List<CashPaymentResponse>> getCashPaymentsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        List<CashPaymentResponse> responses = cashPaymentService.getPaymentsByDateRange(start, end)
                .stream()
                .map(CashPaymentResponse::new)
                .toList();
        return ResponseEntity.ok(responses);
    }
}

