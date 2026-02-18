package exceptions;

public class AmountNotEnoughException extends Exception {
    public AmountNotEnoughException() {
        System.out.println("Amount is not enough to pay for the parking fee.");
    }
}
