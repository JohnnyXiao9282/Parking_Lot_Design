package com.parking.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("LARGE")
@Data
@NoArgsConstructor
public class LargeCar extends Car {

    public LargeCar(String make, String model, String licensePlate, int hourlyRate) {
        super(null, make, model, licensePlate, hourlyRate, false, null, null);
    }

    @Override
    public boolean park(ParkingSpot spot) {
        if (spot != null && spot.isAvailable() && !spot.isSmallCarSpot()) {
            this.setParkingSpot(spot);
            this.setParked(true);
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
            return true;
        }
        return false;
    }
}

