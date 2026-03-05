package com.parking.service;

import com.parking.entity.ParkingSpot;
import com.parking.web.dto.ParkRequest;

public interface IParkService {
    ParkingSpot park(ParkRequest request);
}

