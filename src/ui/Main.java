package ui;

import model.*;

public class Main {
	public static void main(String[] args) {
		ParkingLot lot = new ParkingLot();
		Level first = lot.getFirstLevel();
		Level second = lot.getSecdonLevel();
		Car c1 = new LargeCar();
		Car c2 = new SmallCar();
		c1.park(second);
		// c2.park(first);
		c2.leave()

	}
}
