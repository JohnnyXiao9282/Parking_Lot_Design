package exceptions;

public class AmountNotEnoughException extends Exception {
    public AmountNotEnoughException() {
        System.out.println("Suspicious trsanction! Call an employee to help.");
    }
}
