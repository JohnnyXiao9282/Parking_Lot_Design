package model;

import org.junit.Test;
import static org.junit.Assert.*;

public class ParkingLotTest {
    @Test
    public void testParkingLotInitialization() {
        ParkingLot parkingLot = new ParkingLot();
        assertNotNull(parkingLot.getFirstLevel());
        assertNotNull(parkingLot.getSecondLevel());
    }
    @Test
    public void testGetFirstLevel() {
        ParkingLot parkingLot = new ParkingLot();
        Level firstLevel = parkingLot.getFirstLevel();
        assertTrue(firstLevel.getIsFirst());
    }
    @Test
    public void testGetSecondLevel() {
        ParkingLot parkingLot = new ParkingLot();
        Level secondLevel = parkingLot.getSecondLevel();
        assertFalse(secondLevel.getIsFirst());
    }
}
