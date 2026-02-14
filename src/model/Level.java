package model;

import java.util.*;

public class Level {
    private List<ParkingSpot> spots;
    private boolean isFirst;

    public Level() {
    }

    public Level(boolean isFirst, int numberOfSpots) {
        spots = new ArrayList<>(numberOfSpots);

        boolean forSmall = (isFirst)? true : false;

        for (ParkingSpot spot : this.spots) {
            spot.forSmall = forSmall;
        }
    }


}