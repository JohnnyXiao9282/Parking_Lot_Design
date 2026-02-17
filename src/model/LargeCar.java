package model;

import exceptions.AmountNotEnoughException;
import exceptions.IllegalTransactionException;
import ui.Menu;
import ui.Receipt;

public class LargeCar extends Car {

    public LargeCar() {
        super();
    }

    public LargeCar(String make, String model) {
        super(make, model, 10);
    }
    
    @Override
    public boolean park(Level level) {
        if (isParked) {
            return false;
        }
        if (level.getIsFirst()) {
            return false;
        }
        if (!level.hasSpot()){
            return false;
        }
        Menu m = new Menu();
        this.hours = m.getHours();

        int currentAvail = level.getNumberOfAvail();
        level.setNumberOfAvail(currentAvail - 1);
        this.isParked = true;
        return true;
    }

    @Override
    public boolean payWithCard(double amount, double actual) throws AmountNotEnoughException, IllegalTransactionException {
        try {
            if (actual >= 10000) {
                throw new IllegalTransactionException();
            }

            if (actual < amount) {
                throw new AmountNotEnoughException();
            }

        } catch (IllegalTransactionException ie) {
            return false;
        } catch (AmountNotEnoughException ae) {
            return false;
        } 
        return true;
    }

    @Override
    public boolean payWithCash(double amount, double actual) throws AmountNotEnoughException, IllegalTransactionException {
        try {
            if (actual >= 10000) {
                throw new IllegalTransactionException();
            }

            if (actual < amount) {
                throw new AmountNotEnoughException();
            }

        } catch (IllegalTransactionException ie) {
            return false;
        } catch (AmountNotEnoughException ae) {
            return false;
        } 
        return true;
    }
 
    @Override
    public boolean leave(Level level) {
        if (!isParked) {
            return false;
        }
        double amount = calculateAmount(this.hours);
        boolean paid = false;
        Receipt r = new Receipt(amount);
        boolean iscard = r.getIsCard(); // payment method chosen by user
        double actual = r.getAmount(); // amount paid by user
        // if user chose card payment
        if (iscard) { 
            try {
                paid = payWithCard(amount, actual);
                iscard = true;
            } catch (AmountNotEnoughException | IllegalTransactionException e) {
                paid = false;
            }
        } else {
            // if card payment failed and user chose cash paymen
            try {
                paid = payWithCash(amount, actual);
            } catch (AmountNotEnoughException | IllegalTransactionException e) {
                paid = false;
            }
        }
        if (paid) {
            // print receipt after successful payment
            r.printReceipt(this.hours, amount, this.make, this.model); 
            int spots = level.getNumberOfAvail();
            level.setNumberOfAvail(spots + 1);
            this.hours = 0;
            isParked = false;
            return true;
        } else {
            return false;
        }
        
    }
}