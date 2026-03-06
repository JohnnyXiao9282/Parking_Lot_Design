package com.parking.service;

import com.parking.entity.Car;
import com.parking.entity.Level;
import com.parking.entity.LargeCar;
import com.parking.entity.ParkingSpot;
import com.parking.entity.SmallCar;
import com.parking.exception.AlreadyParkedException;
import com.parking.exception.NoAvailableSpotException;
import com.parking.repository.CarRepository;
import com.parking.repository.LevelRepository;
import com.parking.repository.ParkingSpotRepository;
import com.parking.web.dto.ParkRequest;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParkServiceImpl implements IParkService {

    private final CarRepository carRepository;
    private final ParkingSpotRepository parkingSpotRepository;
    private final LevelRepository levelRepository;

    public ParkServiceImpl(CarRepository carRepository,
                           ParkingSpotRepository parkingSpotRepository,
                           LevelRepository levelRepository) {
        this.carRepository = carRepository;
        this.parkingSpotRepository = parkingSpotRepository;
        this.levelRepository = levelRepository;
    }

    @Transactional
    @Override
    public ParkingSpot park(ParkRequest request) {
        Car car = carRepository.findByLicensePlate(request.getLicensePlate())
                .orElseGet(() -> createCar(request));

        if (car.isParked()) {
            throw new AlreadyParkedException("Car is already parked: " + request.getLicensePlate());
        }

        boolean isSmall = car instanceof SmallCar;
        List<ParkingSpot> available = parkingSpotRepository.findAvailableSpotsByTypeWithLevel(isSmall);

        if (available.isEmpty()) {
            throw new NoAvailableSpotException("No available spot for " + (isSmall ? "small" : "large") + " car");
        }

        ParkingSpot spot = available.get(0);
        boolean success = car.park(spot);
        if (!success) {
            throw new NoAvailableSpotException("Car refused to park into spot: " + spot.getId());
        }

        parkingSpotRepository.save(spot);
        carRepository.save(car);

        // Keep Level.availableSpots in sync
        Level level = spot.getLevel();
        if (level != null) {
            level.setAvailableSpots(Math.max(0, level.getAvailableSpots() - 1));
            levelRepository.save(level);
        }

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
