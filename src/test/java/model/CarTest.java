package model;

import org.junit.Test;
import static org.junit.Assert.*;
// ...existing code...

public class CarTest {
    static class TestCar extends Car {
        public TestCar(String make, String model, int hourlyRate) {
            super(make, model, hourlyRate);
        }
        @Override
        public boolean park(Level level) { return true; }
        @Override
        public boolean leave(Level level) { return true; }
        @Override
        public boolean payWithCard(double amount, double actual) { return true; }
        @Override
        public boolean payWithCash(double amount, double actual) { return true; }
    }

    @Test
    public void testCalculateAmount() {
        Car car = new TestCar("Toyota", "Corolla", 5);
        assertEquals(25, car.calculateAmount(5));
    }

    @Test
    public void testGetMakeAndModel() {
        Car car = new TestCar("Honda", "Civic", 8);
        assertEquals("Honda", car.getMake());
        assertEquals("Civic", car.getModel());
    }
}
