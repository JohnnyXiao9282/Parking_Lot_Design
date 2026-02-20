package model;

import exceptions.*;

public class Admin {
    private ParkingLot lot;
    private String name;
    private int employeeId;
    private int workHours;


    public Admin() {}//default cons

    public void inspect() {
        System.out.println("Admin inspecting exceptions:");

        // Instantiating each exception will trigger their constructor messages
        new AmountNotEnoughException();
        new IllegalTransactionException();
        new OverPaymentException();

    }
}

