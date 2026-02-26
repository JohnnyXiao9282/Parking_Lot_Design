package com.parking.service;

import com.parking.entity.Car;
import com.parking.entity.ParkingSpot;
import com.parking.repository.CarRepository;
import com.parking.repository.ParkingSpotRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class LeaveService {

    private final CarRepository carRepository;
    private final ParkingSpotRepository parkingSpotRepository;

    public LeaveService(CarRepository carRepository, ParkingSpotRepository parkingSpotRepository) {
        this.carRepository = carRepository;
        this.parkingSpotRepository = parkingSpotRepository;
    }

    /**
     * Unparks a car by its id. Frees the occupied spot and marks the car as not parked.
     * Returns true on success.
     */
    @Transactional
    public boolean leave(Long carId) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new RuntimeException("Car not found: " + carId));

        if (!car.isParked()) {
            throw new RuntimeException("Car is not currently parked: " + carId);
        }

        ParkingSpot spot = car.getParkingSpot();

        boolean success = car.leave();
        if (!success) {
            throw new RuntimeException("Car failed to leave its spot: " + carId);
        }

        if (spot != null) {
            parkingSpotRepository.save(spot);
        }
        carRepository.save(car);
        return true;
    }
}

