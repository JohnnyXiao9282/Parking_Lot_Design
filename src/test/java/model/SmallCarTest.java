package model;
import org.junit.Test;

import exceptions.AmountNotEnoughException;

import static org.junit.Assert.*;

public class SmallCarTest {
    @Test 
    public void testCalculateAmount() {
        SmallCar car = new SmallCar("Toyota", "Corolla");
        assertEquals(25, car.calculateAmount(5));
    }
    @Test
    public void testConstructor() {
        SmallCar car = new SmallCar("Toyota", "Corolla");
        assertEquals("Toyota", car.getMake());
        assertEquals("Corolla", car.getModel());
        assertEquals(5, car.getHourlyRate());
    }

    @Test
    public void testPark() {
        SmallCar car = new SmallCar("Toyota", "Corolla");
        ParkingLot lot = new ParkingLot();
        Level firstLevel = lot.getFirstLevel();
        int before = firstLevel.getNumberOfAvail();
        assertTrue(car.park(firstLevel));
        assertEquals(before - 1, firstLevel.getNumberOfAvail());
        assertTrue(car.getIsParked());
    }

    @Test
    public void testPayWithCardSuccess() throws Exception {
        SmallCar car = new SmallCar("Toyota", "Corolla");
        try {
            assertTrue(car.payWithCard(10, 10));
        } catch (AmountNotEnoughException e) {
            fail("Should not throw exception");
        }
    }

    @Test
    public void testPayWithCardFail() throws Exception {
        SmallCar car = new SmallCar("Toyota", "Corolla");
        assertFalse(car.payWithCard(10, 5));
        
    }

    @Test
    public void testPayWithCashSuccess() throws Exception {
        SmallCar car = new SmallCar("Toyota", "Corolla");
        try {
            assertTrue(car.payWithCash(10, 10));
        } catch (AmountNotEnoughException e) {
            fail("Should not throw exception");
        }
    }

    @Test
    public void testPayWithCashFail() throws Exception {
        SmallCar car = new SmallCar("Toyota", "Corolla");
        assertFalse(car.payWithCash(10, 5));
    }
}
