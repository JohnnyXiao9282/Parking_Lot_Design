package com.parking.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "parking_spots")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParkingSpot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int spotNumber;

    @Column(nullable = false)
    private boolean isSmallCarSpot;

    @Column(nullable = false)
    private boolean isOccupied = false;

    @ManyToOne
    @JoinColumn(name = "level_id", nullable = false)
    private Level level;

    @OneToOne(mappedBy = "parkingSpot")
    private Car car;

    public boolean isAvailable() {
        return !isOccupied;
    }

    public void setOccupied(boolean occupied) {
        this.isOccupied = occupied;
    }
}

