package com.wcupa.app.ui.gui.components;

import javax.swing.*;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;

import com.wcupa.app.ui.gui.panels.MainPanel;

import java.awt.*;
import java.awt.event.ActionEvent;

public class SearchPanel extends JPanel {

    public interface SearchListener {
    void onSearchChanged(String query);
}

    public SearchPanel(MainPanel panel, SearchListener listener) {
        setLayout(new FlowLayout(FlowLayout.CENTER));

        JTextField searchField = new JTextField(20);
        searchField.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        searchField.setHorizontalAlignment(JTextField.LEFT);
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { listener.onSearchChanged(searchField.getText()); }
            @Override public void changedUpdate(DocumentEvent e) { listener.onSearchChanged(searchField.getText()); }
            @Override public void removeUpdate(DocumentEvent e) { listener.onSearchChanged(searchField.getText()); }
        });

        JButton searchButton = new JButton("Search");

        searchButton.addActionListener((ActionEvent event) -> {
            if (!panel.getUser().isAdmin()) {
                panel.showProductsPanel(searchField.getText());
            } else {
                panel.showInventoryPanel(searchField.getText());
            }
        });

        add(searchField);
        add(searchButton);
    }
}

