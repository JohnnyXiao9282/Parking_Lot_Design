package exceptions;

public class OverPaymentException extends Exception {
    public OverPaymentException() {
        System.out.println("You overpaid, would you like a change?");
    }

    
}
