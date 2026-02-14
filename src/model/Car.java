package model;

public abstract class Car implements Payment {
    protected String make;
    protected String model;

    public Car() {
    }

    public Car(String make, String model) {
        this.make = make;
        this.model = model;
    }

    public abstract boolean Park(Level level);

    public abstract boolean leave(double amount, double actual, Level level);

}