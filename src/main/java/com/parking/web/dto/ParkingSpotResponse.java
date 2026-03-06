package com.parking.web.dto;

import com.parking.entity.ParkingSpot;
import lombok.Getter;

@Getter
public class ParkingSpotResponse {

    private final Long spotId;
    private final int spotNumber;
    private final boolean isSmallCarSpot;
    private final Long levelId;
    private final int levelNumber;

    public ParkingSpotResponse(ParkingSpot spot) {
        this.spotId = spot.getId();
        this.spotNumber = spot.getSpotNumber();
        this.isSmallCarSpot = spot.isSmallCarSpot();
        if (spot.getLevel() != null) {
            this.levelId = spot.getLevel().getId();
            this.levelNumber = spot.getLevel().getLevelNumber();
        } else {
            this.levelId = null;
            this.levelNumber = 0;
        }
    }
}
