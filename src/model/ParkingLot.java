package model;

public class ParkingLot {
    private Level smallCarLevel;
    private Level largeCarLevel;

    public ParkingLot() {
        this.smallCarLevel = new Level(true, 200);
        this.largeCarLevel = new Level(false, 100);
    }

    public Level getFirstLevel() {
        return this.smallCarLevel;
    }

    public Level getSecondLevel() {
        return this.largeCarLevel;
    }
    
}