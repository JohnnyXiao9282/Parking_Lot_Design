package model;

import java.util.*;

public class Level {
    private List<ParkingSpot> spots;
    private boolean isFirst;
    private int numberOfAvail;


    public Level() {
    }

    public Level(boolean isFirst, int numberOfSpots) {
        spots = new ArrayList<>(numberOfSpots);
        this.numberOfAvail = spots.size();
        boolean forSmall = (isFirst)? true : false;

        for (ParkingSpot spot : this.spots) {
            spot.forSmall = forSmall;
        }
    }

    public boolean hasSpot() {
        return numberOfAvail > 0;
    }
    
    public int getNumberOfAvail() {
        return numberOfAvail;
    }

    public void setNumberOfAvail(int numberOfAvail) {
        this.numberOfAvail = numberOfAvail;
    }

    public boolean getIsFirst() {
        return isFirst;
    }
}