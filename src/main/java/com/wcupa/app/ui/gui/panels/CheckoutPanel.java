package com.wcupa.app.ui.gui.panels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

import com.wcupa.app.domain.core.Customer;
import com.wcupa.app.domain.core.Order;
import com.wcupa.app.service.CheckoutService;



public class CheckoutPanel extends JPanel {
    private MainPanel mainPanel;
    private CheckoutService checkoutService;
    
    public CheckoutPanel(MainPanel mainPanel, CheckoutService checkoutService) {
        this.mainPanel = mainPanel;
        this.checkoutService = checkoutService;
        setLayout(new BorderLayout());
    }

    public void refresh() {
        removeAll();

        JLabel label = new JLabel("Checkout");
        label.setFont(new Font("Arial", Font.BOLD, 24));

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JTextField street1 = new JTextField(36);
        JTextField street2 = new JTextField(36);
        JTextField city = new JTextField(36);
        JTextField state = new JTextField(36);
        JTextField zip = new JTextField(36);

        JTextField cardName = new JTextField(36);
        JTextField cardNumber = new JTextField(36);
        JTextField cardExp = new JTextField(36);
        JTextField cardSec = new JTextField(36);

        JButton submit = new JButton("Submit");
        submit.addActionListener((ActionEvent event) -> {
            int result = JOptionPane.showConfirmDialog(mainPanel, "Submit your order?", "Checkout", JOptionPane.YES_NO_OPTION);
            if (result == 0) {
                String name = String.format("%s %s", mainPanel.getUser().getFirstName(), mainPanel.getUser().getLastName()); 
                String street = street2.getText().isEmpty() ? street1.getText() : String.format("%s\n%s", street1.getText(), street2.getText());
                String address = String.format("%s\n%s\n%s, %s, %s", name, street, city.getText(), state.getText(), zip.getText());
                String processed = checkoutService.submitOrder((Customer) mainPanel.getUser(), mainPanel.getCart(), address);
                if (processed != null) {
                    JOptionPane.showMessageDialog(mainPanel, "Order placed successfully! Confirmation number: " + processed);
                    mainPanel.clearCart();
                    mainPanel.showHomePanel();
                } else {
                    JOptionPane.showMessageDialog(mainPanel, "Failed to submit order.", "Checkout Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JButton back = new JButton("Back");
        submit.addActionListener((ActionEvent event) -> {
            this.mainPanel.showPrevious();
        });

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Shipping/Billing Address:"), gbc);

        gbc.gridy = 1;
        formPanel.add(new JLabel("Street (1):"), gbc);

        gbc.gridx = 1;
        formPanel.add(street1, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Street (2):"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        formPanel.add(street2, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("City:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        formPanel.add(city, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(new JLabel("State:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        formPanel.add(state, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(new JLabel("Zip Code:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 5;
        formPanel.add(zip, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        formPanel.add(new JLabel("Payment Info"), gbc);

        gbc.gridy = 8;
        formPanel.add(new JLabel("Name on Card:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 8;
        formPanel.add(cardName, gbc);

        gbc.gridx = 0;
        gbc.gridy = 9;
        formPanel.add(new JLabel("Card Number:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 9;
        formPanel.add(cardNumber, gbc);

        gbc.gridx = 0;
        gbc.gridy = 10;
        formPanel.add(new JLabel("CVV:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 10;
        formPanel.add(cardSec, gbc);

        gbc.gridx = 0;
        gbc.gridy = 11;
        formPanel.add(new JLabel("Expiration Date:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 11;
        formPanel.add(cardExp, gbc);

        gbc.gridy = 12;
        formPanel.add(submit, gbc);

        gbc.gridy = 13;
        formPanel.add(back, gbc);

        add(label, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);

        revalidate();
        repaint();
    }
}
