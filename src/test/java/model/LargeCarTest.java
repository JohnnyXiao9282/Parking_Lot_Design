package model;
import org.junit.Test;

import exceptions.AmountNotEnoughException;

import static org.junit.Assert.*;

public class LargeCarTest {
    @Test 
    public void testCalculateAmount() {
        LargeCar car = new LargeCar("Toyota", "Corolla");
        assertEquals(50, car.calculateAmount(5));
    }
    @Test
    public void testConstructor() {
        LargeCar car = new LargeCar("Toyota", "Corolla");
        assertEquals("Toyota", car.getMake());
        assertEquals("Corolla", car.getModel());
        assertEquals(10, car.getHourlyRate());
    }

    @Test
    public void testParkRightLevel() {
        LargeCar car = new LargeCar("Toyota", "Corolla");
        ParkingLot lot = new ParkingLot();
        Level secondLevel = lot.getSecondLevel();
        int before = secondLevel.getNumberOfAvail();
        assertTrue(car.park(secondLevel));
        assertEquals(before - 1, secondLevel.getNumberOfAvail());
        assertTrue(car.getIsParked());
    }

    @Test
    public void testParkWrongLevel() {
        LargeCar car = new LargeCar("Toyota", "Corolla");
        ParkingLot lot = new ParkingLot();
        Level firstLevel = lot.getFirstLevel();
        int before = firstLevel.getNumberOfAvail();
        assertFalse(car.park(firstLevel));
        assertEquals(before, firstLevel.getNumberOfAvail());
        assertFalse(car.getIsParked());
    }

    @Test
    public void testPayWithCardSuccess() throws Exception {
        LargeCar car = new LargeCar("Toyota", "Corolla");
        try {
            assertTrue(car.payWithCard(10, 10));
        } catch (AmountNotEnoughException e) {
            fail("Should not throw exception");
        }
    }

    @Test
    public void testPayWithCardFail() throws Exception {
        LargeCar car = new LargeCar("Toyota", "Corolla");
        assertFalse(car.payWithCard(10, 5));
        
    }

    @Test
    public void testPayWithCashSuccess() throws Exception {
        LargeCar car = new LargeCar("Toyota", "Corolla");
        try {
            assertTrue(car.payWithCash(10, 10));
        } catch (AmountNotEnoughException e) {
            fail("Should not throw exception");
        }
    }

    @Test
    public void testPayWithCashFail() throws Exception {
        LargeCar car = new LargeCar("Toyota", "Corolla");
        assertFalse(car.payWithCash(10, 5));
    }
}
