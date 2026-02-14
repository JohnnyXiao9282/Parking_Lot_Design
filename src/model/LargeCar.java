package model;

public class LargeCar extends Car {

    public LargeCar() {
        super();
    }

    public LargeCar(String make, String model) {
        super(make, model);
    }
    
    @Override
    public boolean Park(Level level) {
        if (level.getIsFirst()) {
            return false;
        }
        if (!level.hasSpot()){
            return false;
        }
        int currentAvail = level.getNumberOfAvail();
        level.setNumberOfAvail(currentAvail - 1);
        return true;
    }
}