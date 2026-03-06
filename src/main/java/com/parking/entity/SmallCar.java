package com.parking.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@DiscriminatorValue("SMALL")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class SmallCar extends Car {

    public SmallCar(String make, String model, String licensePlate, int hourlyRate) {
        super(null, make, model, licensePlate, hourlyRate, false, null, null, null);
    }

    @Override
    public boolean park(ParkingSpot spot) {
        if (spot != null && spot.isAvailable() && spot.isSmallCarSpot()) {
            this.setParkingSpot(spot);
            this.setParked(true);
            this.setParkedSince(LocalDateTime.now());
            spot.setOccupied(true);
            return true;
        }
        return false;
    }

    @Override
    public boolean leave() {
        if (this.getParkingSpot() != null) {
            this.getParkingSpot().setOccupied(false);
            this.setParkingSpot(null);
            this.setParked(false);
            this.setParkedSince(null);
            return true;
        }
        return false;
    }
}
