package com.parking.repository;

import com.parking.service.CashPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CashPaymentRepository extends JpaRepository<CashPayment, Long> {

    List<CashPayment> findByCarId(Long carId);

    List<CashPayment> findBySuccessful(boolean successful);

    List<CashPayment> findByPaymentTimestampBetween(LocalDateTime start, LocalDateTime end);
}

