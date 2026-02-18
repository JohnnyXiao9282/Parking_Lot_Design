package model;

public abstract class Car implements Payment {
    protected String make;
    protected String model;
    protected boolean isParked;
    protected int hourlyRate;
    protected int hours;

    public Car() {
    }

    public Car(String make, String model, int hourlyRate) {
        this.make = make;
        this.model = model;
        this.hourlyRate = hourlyRate;
        this.isParked = false;
    }

    public String getMake() {
        return make;
    }
    
    public String getModel() {
        return model;
    }

    @Override
    public int calculateAmount(int duration) {
        return this.hourlyRate * duration;
    }


    public abstract boolean park(Level level);
    public abstract boolean leave(Level level);
}