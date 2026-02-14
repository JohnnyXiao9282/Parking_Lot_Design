package model;

public interface Payment {
    public boolean payWithCard(double amount);
    public boolean payWithCash(double amount);
}