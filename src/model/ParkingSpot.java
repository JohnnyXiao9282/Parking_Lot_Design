package model; 
import java.util.*;

public class ParkingSpot {
    boolean forSmall;
    boolean isOccupied;

    public ParkingSpot(boolean forSmall) {
        this.forSmall = forSmall;
        this.isOccupied = false;
    }
}