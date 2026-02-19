
package model;

import org.junit.Test;

// ...existing code...

import static org.junit.Assert.*;
// ...existing code...

public class CarTest {
    // Public no-arg constructor for JUnit 4
    public CarTest() {}
    // Example concrete Car subclass for testing
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
        Car car = new TestCar("Toyota", "Corolla", 10);
        assertEquals(50, car.calculateAmount(5));
    }

    @Test
    public void testGetMakeAndModel() {
        Car car = new TestCar("Honda", "Civic", 8);
        assertEquals("Honda", car.getMake());
        assertEquals("Civic", car.getModel());
    }
}
