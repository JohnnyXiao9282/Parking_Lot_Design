package exceptions;

public class amountNotEnoughException extends Exception {
    
    public amountNotEnoughException() {
        System.out.println("Your remaining amount is not enough");
    }
}
