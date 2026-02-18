package ui;

import model.*;

public class Main {
	public static void main(String[] args) {
		ParkingLot lot = new ParkingLot();
		Level first = lot.getFirstLevel();
		Level second = lot.getSecdonLevel();
		Car c1 = new LargeCar("Toyota", "Camry");
		Car c2 = new SmallCar("Toyota", "Camry");
		// c1.park(second);
		// c1.leave(second);
		c2.park(first);
		c2.leave(first);
	}
}
