package ui;
import java.util.*;

// Later change to singleton
public class Menu {
    private boolean isCalled = false;
    private int hours;

    public Menu() {
        showOptions();
        isCalled = true;
    }

    public int showOptions() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("-------------Welcome to Parking Lot-------------");
        System.out.println("Please choose your option:");
        System.out.println("1.Small Car   2.Large Car");
        int carType = scanner.nextInt();
        if (carType != 1 && carType != 2) {
            System.out.println("Wrong car type");
            System.exit(0);
        }
        System.out.println("Enter time:");
        int hours = scanner.nextInt();
        this.hours = hours;
        return hours;

    }

    public int getHours() {
        if (!isCalled) {
            return 0;
        } else {
            return this.hours;
        }
    }
 }
