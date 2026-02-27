package com.parking.service;

import com.parking.entity.ParkingSpot;

public interface IParkService {
    ParkingSpot park(Long carId);
}

