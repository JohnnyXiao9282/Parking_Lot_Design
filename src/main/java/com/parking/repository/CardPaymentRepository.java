package com.parking.repository;

import com.parking.service.CardPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CardPaymentRepository extends JpaRepository<CardPayment, Long> {

    List<CardPayment> findByCarId(Long carId);

    List<CardPayment> findBySuccessful(boolean successful);

    List<CardPayment> findByPaymentTimestampBetween(LocalDateTime start, LocalDateTime end);

    Optional<CardPayment> findByTransactionId(String transactionId);
}

