package com.parking.service;

import com.parking.entity.Car;
import com.parking.entity.ParkingSpot;
import com.parking.repository.CarRepository;
import com.parking.repository.ParkingSpotRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class LeaveServiceImpl implements ILeaveService {

    private final CarRepository carRepository;
    private final ParkingSpotRepository parkingSpotRepository;

    public LeaveServiceImpl(CarRepository carRepository, ParkingSpotRepository parkingSpotRepository) {
        this.carRepository = carRepository;
        this.parkingSpotRepository = parkingSpotRepository;
    }

    @Transactional
    @Override
    public boolean leave(String licensePlate) {
        Car car = carRepository.findByLicensePlate(licensePlate)
                .orElseThrow(() -> new RuntimeException("Car not found: " + licensePlate));

        if (!car.isParked()) {
            throw new RuntimeException("Car is not currently parked: " + licensePlate);
        }

        ParkingSpot spot = car.getParkingSpot();

        boolean success = car.leave();
        if (!success) {
            throw new RuntimeException("Car failed to leave its spot: " + licensePlate);
        }

        if (spot != null) {
            parkingSpotRepository.save(spot);
        }
        carRepository.save(car);
        return true;
    }
}

