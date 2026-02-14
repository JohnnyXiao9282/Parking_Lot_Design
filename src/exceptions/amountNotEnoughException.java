package exceptions;

public class AmountNotEnoughException extends Exception {
    
    public AmountNotEnoughException() {
        System.out.println("Your remaining amount is not enough");
    }
}
