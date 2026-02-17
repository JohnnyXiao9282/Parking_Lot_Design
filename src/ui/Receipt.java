package ui;

import java.util.Scanner;

public class Receipt {
    private boolean isCard;
    private double amount;
    public Receipt(double price) {
        process(price);
    }
    public void process(double price) {
        System.out.println("Please choose your payment option:");
        System.out.println("1.Cash   2.Card");
        Scanner scanner = new Scanner(System.in);
        int payType = scanner.nextInt();
        if (payType != 1 && payType != 2) {
            System.out.println("Wrong payment type");
            System.exit(0);
        } 
        if (payType == 1) { 
            isCard = false;
        } else {
            isCard = true;
        }
        System.out.println("Total amount due: $" + price);
        System.out.println("Please enter the amount paid:");
        this.amount = scanner.nextDouble();
    }

    public void printReceipt(int duration, double price, String make, String model) {
        String paymentMethod = isCard ? "Card" : "Cash";
        System.out.println("-------------Receipt:-------------");
        System.out.println("Car: " + make + " " + model);
        System.out.println("Duration: " + duration + " hours");
        System.out.println("Total Amount: $" + price);
        System.out.println("Payment Method: " + paymentMethod);
        System.out.println("------Thank you for parking!------");
    }

    public boolean getIsCard() {
        return isCard;
    }

    public double getAmount() {
        return amount;
    }
}
