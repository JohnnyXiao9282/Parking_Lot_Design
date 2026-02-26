package com.parking.service;

import com.parking.entity.Car;
import com.parking.entity.ParkingSpot;
import com.parking.entity.SmallCar;
import com.parking.repository.CarRepository;
import com.parking.repository.ParkingSpotRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParkService {

    private final CarRepository carRepository;
    private final ParkingSpotRepository parkingSpotRepository;

    public ParkService(CarRepository carRepository, ParkingSpotRepository parkingSpotRepository) {
        this.carRepository = carRepository;
        this.parkingSpotRepository = parkingSpotRepository;
    }

    /**
     * Parks a car by its id into the first available spot matching its type.
     * Returns the assigned ParkingSpot on success.
     */
    @Transactional
    public ParkingSpot park(Long carId) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new RuntimeException("Car not found: " + carId));

        if (car.isParked()) {
            throw new RuntimeException("Car is already parked: " + carId);
        }

        boolean isSmall = car instanceof SmallCar;
        List<ParkingSpot> available = parkingSpotRepository.findAvailableSpotsByType(isSmall);

        if (available.isEmpty()) {
            throw new RuntimeException("No available spot for " + (isSmall ? "small" : "large") + " car");
        }

        ParkingSpot spot = available.get(0);
        boolean success = car.park(spot);
        if (!success) {
            throw new RuntimeException("Car refused to park into spot: " + spot.getId());
        }

        parkingSpotRepository.save(spot);
        carRepository.save(car);
        return spot;
    }
}

