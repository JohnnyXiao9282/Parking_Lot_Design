package exceptions;

public class IllegalTransactionException extends Exception {
    public IllegalTransactionException() {
        System.out.println("Suspicious trsanction! Call an employee to help.");
    }
}
