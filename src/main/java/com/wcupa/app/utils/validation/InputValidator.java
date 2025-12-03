package com.wcupa.app.utils.validation;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;

import java.util.function.Predicate;

public class InputValidator {

    public static void attachValidator(
        JTextComponent field,
        Predicate<String> rule,
        JLabel errorLabel,
        String errorMessage
    ) {
        errorLabel.setText("");
        errorLabel.setForeground(java.awt.Color.RED);

        field.putClientProperty("isValid", false);

        DocumentListener listener = new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { validate(); }
            public void removeUpdate(DocumentEvent e) { validate(); }
            public void changedUpdate(DocumentEvent e) { validate(); }

            private void validate() {
                String text = field.getText();
                boolean isValid = rule.test(text);

                if (isValid) {
                    errorLabel.setText("");
                } else {
                    errorLabel.setText(errorMessage);
                }
                field.putClientProperty("isValid", isValid);
            }
        };
        field.getDocument().addDocumentListener(listener);
    }

    public static boolean isFieldValid(JTextField field) {
        Object v = field.getClientProperty("isValid");
        return v instanceof Boolean && (Boolean) v;
    }

    public static Predicate<String> notEmpty() {
        return text -> text != null && !text.trim().isEmpty();
    }

    public static Predicate<String> isEmail() {
        return text -> text.matches("^[^@]+@[^@]+\\.[^@]+$");
    }

    public static Predicate<String> isInteger() {
        return text -> text.matches("\\d+");
    }

    public static Predicate<String> isPrice() {
        return text -> text.matches("\\d+(\\.\\d{1,2})?");
    }
}