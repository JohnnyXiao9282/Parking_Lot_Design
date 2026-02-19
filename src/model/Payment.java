package model;
import exceptions.*;

public interface Payment {
    public int calculateAmount(int duration);
    public boolean payWithCard(double amount, double actual) throws AmountNotEnoughException, IllegalTransactionException, OverPaymentException;
    public boolean payWithCash(double amount, double actual) throws AmountNotEnoughException, IllegalTransactionException, OverPaymentException;
}