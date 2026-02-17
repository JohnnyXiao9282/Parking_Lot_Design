package ui;
import java.util.*;

public class Menu {
    public Menu() {
        showOptions();
    }

    public void showOptions() {
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

    }
 }
