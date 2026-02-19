
package model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import model.Car;
import model.Level;

class CarTest {
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
    void testCalculateAmount() {
        Car car = new TestCar("Toyota", "Corolla", 10);
        assertEquals(50, car.calculateAmount(5));
    }

    @Test
    void testGetMakeAndModel() {
        Car car = new TestCar("Honda", "Civic", 8);
        assertEquals("Honda", car.getMake());
        assertEquals("Civic", car.getModel());
    }
}
