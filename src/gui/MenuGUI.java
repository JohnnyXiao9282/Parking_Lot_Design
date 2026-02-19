package gui;

import exceptions.AmountNotEnoughException;
import exceptions.IllegalTransactionException;
import exceptions.OverPaymentException;
import java.awt.*;
import javax.swing.*;

public class MenuGUI extends JFrame {
    private JComboBox<String> carTypeCombo;
    private JTextField hoursField;
    private JButton submitButton;
    private int selectedCarType = 1;
    private int enteredHours = 0;

    public MenuGUI() {
        setTitle("Parking Lot Menu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(350, 200);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(4, 2, 10, 10));

        add(new JLabel("Select Car Type:"));
        carTypeCombo = new JComboBox<>(new String[]{"Small Car", "Large Car"});
        add(carTypeCombo);

        add(new JLabel("Enter Hours:"));
        hoursField = new JTextField();
        add(hoursField);

        submitButton = new JButton("Submit");
        add(submitButton);

        submitButton.addActionListener(e -> {
            selectedCarType = carTypeCombo.getSelectedIndex() + 1;
            try {
                enteredHours = Integer.parseInt(hoursField.getText());
                String carTypeStr = (selectedCarType == 1 ? "Small Car" : "Large Car");
                double price = selectedCarType == 1 ? 5 * enteredHours : 10 * enteredHours;
                // Show receipt and payment dialog
                showReceiptDialog(carTypeStr, enteredHours, price);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(MenuGUI.this,
                    "Please enter a valid number for hours.",
                    "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void showReceiptDialog(String carType, int hours, double price) {
        boolean paid = false;
        while (!paid) {
            JPanel panel = new JPanel(new GridLayout(0, 1));
            panel.add(new JLabel("-------------Receipt:-------------"));
            panel.add(new JLabel("Car Type: " + carType));
            panel.add(new JLabel("Duration: " + hours + " hours"));
            panel.add(new JLabel("Total Amount Due: $" + price));
            panel.add(new JLabel("Please choose your payment option:"));
            String[] payOptions = {"Cash", "Card"};
            JComboBox<String> payTypeCombo = new JComboBox<>(payOptions);
            panel.add(payTypeCombo);
            panel.add(new JLabel("To pay, please enter the amount:"));
            JTextField amountField = new JTextField();
            panel.add(amountField);

            int result = JOptionPane.showConfirmDialog(this, panel, "Receipt & Payment", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                String paymentMethod = (String) payTypeCombo.getSelectedItem();
                String amountPaidStr = amountField.getText();
                try {
                    double amountPaid = Double.parseDouble(amountPaidStr);
                    // Exception logic
                    if (amountPaid < price) {
                        throw new AmountNotEnoughException();
                    } else if (amountPaid > price) {
                        throw new OverPaymentException();
                    } else if (amountPaid == 0) {
                        throw new IllegalTransactionException();
                    }
                    StringBuilder sb = new StringBuilder();
                    sb.append("\n------Payment Summary------\n");
                    sb.append("Car Type: ").append(carType).append("\n");
                    sb.append("Duration: ").append(hours).append(" hours\n");
                    sb.append("Total Due: $").append(price).append("\n");
                    sb.append("Amount Paid: $").append(amountPaid).append("\n");
                    sb.append("Payment Method: ").append(paymentMethod).append("\n");
                    sb.append("------Thank you for parking!------");
                    JOptionPane.showMessageDialog(this, sb.toString(), "Thank You", JOptionPane.INFORMATION_MESSAGE);
                    paid = true;
                    System.exit(0);
                } catch (AmountNotEnoughException ex) {
                    JOptionPane.showMessageDialog(this, "Amount is not enough to pay for the parking fee. Please try again.", "Payment Error", JOptionPane.ERROR_MESSAGE);
                } catch (OverPaymentException ex) {
                    JOptionPane.showMessageDialog(this, "You overpaid, would you like a change? Please try again.", "Payment Error", JOptionPane.ERROR_MESSAGE);
                } catch (IllegalTransactionException ex) {
                    JOptionPane.showMessageDialog(this, "Suspicious transaction! Call an employee to help. Please try again.", "Payment Error", JOptionPane.ERROR_MESSAGE);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Please enter a valid number for amount paid.", "Input Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                // User cancelled
                paid = true;
                System.exit(0);
            }
        }
    }

    public int getSelectedCarType() {
        return selectedCarType;
    }

    public int getEnteredHours() {
        return enteredHours;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MenuGUI().setVisible(true);
        });
    }
}
