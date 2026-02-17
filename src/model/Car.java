package model;

public abstract class Car implements Payment {
    protected String make;
    protected String model;
    protected boolean isParked;
    protected int hourlyRate;

    public Car() {
    }

    public Car(String make, String model) {
        this.make = make;
        this.model = model;
        this.isParked = false;
    }

    @Override
    public int calculateAmount(int duration) {
        return this.hourlyRate * duration;
    }


    public abstract boolean Park(Level level);
    public abstract boolean leave(int duration, double actual, Level level);
}