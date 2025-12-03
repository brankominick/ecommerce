package com.wcupa.app.ui.gui.panels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

import com.wcupa.app.domain.core.User;
import com.wcupa.app.service.AuthService;
import com.wcupa.app.ui.gui.frames.MainFrame;

public class LoginPanel extends JPanel {

    public LoginPanel(MainFrame frame, AuthService authService) {

        setLayout(new BorderLayout());

        JLabel label = new JLabel("Login");
        label.setFont(new Font("Arial", Font.BOLD, 24));

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        JTextField emailField = new JTextField(24);
        JPasswordField passwordField = new JPasswordField(24);
        JButton loginButton = new JButton("Login");
        JButton createButton = new JButton("Create Account");

        constraints.gridx = 0;
        constraints.gridy = 0;
        formPanel.add(new JLabel("Email:"), constraints);

        constraints.gridx = 1;
        constraints.gridy = 0;
        formPanel.add(emailField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        formPanel.add(new JLabel("Password:"), constraints);

        constraints.gridx = 1;
        constraints.gridy = 1;
        formPanel.add(passwordField, constraints);

        //JPanel buttonPanel = new JPanel();
        //buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        //buttonPanel.add(loginButton);
        //buttonPanel.add(createButton);
        constraints.gridx = 1;
        constraints.gridy = 2;
        formPanel.add(loginButton, constraints);

        constraints.gridx = 0;
        constraints.gridy = 3;
        formPanel.add(new JLabel("Don't have an account yet?"), constraints);

        constraints.gridx = 1;
        constraints.gridy = 3;
        formPanel.add(createButton, constraints);

        add(label, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);

        loginButton.addActionListener((ActionEvent event) -> {
            String username = emailField.getText();
            String password = new String(passwordField.getPassword());
            User user = authService.logIn(username, password);

            if (user != null) {
                JOptionPane.showMessageDialog(this, "Welcome, " + username + "!");
                frame.getSession().signIn(user);
                frame.loadMainPage();
                frame.showMainPage();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password");
            }
        });

        createButton.addActionListener((ActionEvent event) -> {
            frame.showRegistrationPage();
        });
    }
}
