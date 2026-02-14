package model;

public class SmallCar extends Car {

    public SmallCar() {
        super();
    }

    public SmallCar(String make, String model) {
        super(make, model);
    }
    
    @Override
    public boolean Park(Level level) {
        if (!level.hasSpot()){
            return false;
        }
        int currentAvail = level.getNumberOfAvail();
        level.setNumberOfAvail(currentAvail - 1);
        return true;
    }
}