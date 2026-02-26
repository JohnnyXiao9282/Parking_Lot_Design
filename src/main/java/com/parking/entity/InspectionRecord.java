package com.parking.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "inspection_records")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InspectionRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "parking_lot_id", nullable = false)
    private ParkingLot parkingLot;

    @ManyToOne
    @JoinColumn(name = "admin_id", nullable = false)
    private Admin inspector;

    @Column(nullable = false)
    private LocalDateTime inspectionTime = LocalDateTime.now();

    @Column(nullable = false)
    private int totalSpots;

    @Column(nullable = false)
    private int occupiedSpots;

    @Column(nullable = false)
    private int availableSpots;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InspectionStatus status;

    @Column(length = 1000)
    private String notes;

    public enum InspectionStatus {
        PASSED, FAILED, NEEDS_MAINTENANCE
    }

    public double getOccupancyRate() {
        if (totalSpots == 0) return 0.0;
        return (double) occupiedSpots / totalSpots * 100;
    }
}

