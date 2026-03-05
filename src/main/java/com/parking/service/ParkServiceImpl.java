package com.parking.service;

import com.parking.entity.Car;
import com.parking.entity.LargeCar;
import com.parking.entity.ParkingSpot;
import com.parking.entity.SmallCar;
import com.parking.repository.CarRepository;
import com.parking.repository.ParkingSpotRepository;
import com.parking.web.dto.ParkRequest;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParkServiceImpl implements IParkService {

    private final CarRepository carRepository;
    private final ParkingSpotRepository parkingSpotRepository;

    public ParkServiceImpl(CarRepository carRepository, ParkingSpotRepository parkingSpotRepository) {
        this.carRepository = carRepository;
        this.parkingSpotRepository = parkingSpotRepository;
    }

    @Transactional
    @Override
    public ParkingSpot park(ParkRequest request) {
        // Look up by license plate — reuse if returning car, register if new
        Car car = carRepository.findByLicensePlate(request.getLicensePlate())
                .orElseGet(() -> createCar(request));

        if (car.isParked()) {
            throw new RuntimeException("Car is already parked: " + request.getLicensePlate());
        }

        boolean isSmall = car instanceof SmallCar;
        List<ParkingSpot> available = parkingSpotRepository.findAvailableSpotsByTypeWithLevel(isSmall);

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

    private Car createCar(ParkRequest request) {
        return switch (request.getCarType()) {
            case SMALL -> new SmallCar(
                    request.getMake(),
                    request.getModel(),
                    request.getLicensePlate(),
                    request.getHourlyRate()
            );
            case LARGE -> new LargeCar(
                    request.getMake(),
                    request.getModel(),
                    request.getLicensePlate(),
                    request.getHourlyRate()
            );
        };
    }
}

