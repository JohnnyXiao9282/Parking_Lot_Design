package model;

import exceptions.AmountNotEnoughException;
import exceptions.IllegalTransactionException;

public class SmallCar extends Car {

    public SmallCar() {
        super();
    }

    public SmallCar(String make, String model) {
        super(make, model);
        this.hourlyRate = 5;
    }
    
    @Override
    public boolean Park(Level level) {
        if (isParked) {
            return false;
        }
        if (!level.getIsFirst()) {
            return false;
        }
        if (!level.hasSpot()){
            return false;
        }
        int currentAvail = level.getNumberOfAvail();
        level.setNumberOfAvail(currentAvail - 1);
        isParked = true;
        return true;
    }

    @Override
    public boolean payWithCard(double amount, double actual) throws AmountNotEnoughException, IllegalTransactionException{
        try {
            if (actual >= 10000){
                throw new IllegalTransactionException();
            }
            if (actual < amount){
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
    public boolean payWithCash(double amount, double actual) throws AmountNotEnoughException, IllegalTransactionException{
        try {
            if (actual >= 10000){
                throw new IllegalTransactionException();
            }
            if (actual < amount){
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
    public boolean leave(int duration, double actual, Level level) {
        if (!isParked) {
            return false;
        }
        double amount = calculateAmount(duration);
        boolean paid = false;
        try {
            paid = payWithCard(amount, actual);
        } catch (IllegalTransactionException ie) {
            paid = false;
        } catch (AmountNotEnoughException ae) {
            paid = false;
        }
        if (!paid){
            try {
                paid = payWithCash(amount, actual);
            } catch (IllegalTransactionException ie) {
                paid = false;
            } catch (AmountNotEnoughException ae) {
                paid = false;
            }
        }
        if (!paid){
            return false;
        } else{
            int currentAvail = level.getNumberOfAvail();
            level.setNumberOfAvail(currentAvail + 1);
            isParked = false;
            return true;
        }
        
    }
}