package com.wcupa.app.ui.gui.panels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

import com.wcupa.app.service.AuthService;
import com.wcupa.app.ui.gui.frames.MainFrame;
import com.wcupa.app.utils.validation.InputValidator;

public class RegistrationPanel extends JPanel {

    public RegistrationPanel(MainFrame frame, AuthService authService) {

        setLayout(new BorderLayout());

        JLabel label = new JLabel("Account Creation");
        label.setFont(new Font("Arial", Font.BOLD, 24));

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        JTextField emailField = new JTextField(24);
        JLabel emailErrorLabel = new JLabel();
        InputValidator.attachValidator(
            emailField,
            InputValidator.isEmail(),
            emailErrorLabel,
            "Please enter a valid email."
        );
        JPasswordField passwordField = new JPasswordField(24);
        JLabel passwordErrorLabel = new JLabel();
        InputValidator.attachValidator(
            passwordField,
            InputValidator.notEmpty(),
            passwordErrorLabel,
            "Password cannot be blank."
        );
        JTextField firstNameField = new JTextField(24);
        JTextField lastNameField = new JTextField(24);
        String[] roles = {"Customer", "Admin"};
        JComboBox<String> roleSelection = new JComboBox<String>(roles);
        JButton registerButton = new JButton("Register");
        JButton backButton = new JButton("Back to Login");

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

        constraints.gridx = 0;
        constraints.gridy = 2;
        formPanel.add(new JLabel("First Name:"), constraints);

        constraints.gridx = 1;
        constraints.gridy = 2;
        formPanel.add(firstNameField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 3;
        formPanel.add(new JLabel("Last Name:"), constraints);

        constraints.gridx = 1;
        constraints.gridy = 3;
        formPanel.add(lastNameField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 4;
        formPanel.add(new JLabel("Role:"), constraints);

        constraints.gridx = 1;
        constraints.gridy = 4;
        formPanel.add(roleSelection, constraints);

        constraints.gridx = 0;
        constraints.gridy = 5;
        formPanel.add(backButton, constraints);

        constraints.gridx = 1;
        constraints.gridy = 5;
        formPanel.add(registerButton, constraints);

        add(label, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);

        registerButton.addActionListener((ActionEvent event) -> {
            String email = emailField.getText().strip();
            String password = new String(passwordField.getPassword()).strip();
            String firstName = firstNameField.getText().strip();
            String lastName = lastNameField.getText().strip();
            String role = roleSelection.getSelectedItem().toString().toUpperCase();
            boolean created = authService.createAccount(email, password, firstName, lastName, role);

            if (created && emailField.isValid() && passwordField.isValid()) {
                JOptionPane.showMessageDialog(this, "Account created successfully!");
                frame.showLoginPage();
            } else {
                JOptionPane.showMessageDialog(this, "Account creation failed. Ensure email and password are valid.");
            }
        });

        backButton.addActionListener((ActionEvent event) -> {
            frame.showLoginPage();
        });
    }
}
