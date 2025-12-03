package com.wcupa.app.ui.gui.components;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JToolBar;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.wcupa.app.domain.core.User;
import com.wcupa.app.ui.gui.panels.MainPanel;

public class NavBar extends JToolBar {
    
    public NavBar(MainPanel mainPanel, User user) {
        JButton homeButton = new JButton("Home");
        JButton browseButton = new JButton("Browse");
        JButton cartButton = new JButton("My Cart");
        JButton orderButton = new JButton("My Orders");
        JButton adminHomeButton = new JButton("Home");
        JButton adminInventoryButton = new JButton("Inventory");
        JButton adminOrdersButton = new JButton("Orders");
        JButton adminReportsButton = new JButton("Reports");
        //String[] accountOptions = {"Log Out"};
        //JComboBox<String> settingsBox = new JComboBox<>(accountOptions);

        homeButton.addActionListener((ActionEvent event) -> {
            mainPanel.showHomePanel();
        });

        browseButton.addActionListener((ActionEvent event) -> {
            mainPanel.showProductsPanel();
        });

        cartButton.addActionListener((ActionEvent event) -> {
            mainPanel.showCartPanel();
        });

        orderButton.addActionListener((ActionEvent event) -> {
            mainPanel.showOrdersPanel();
        });

        adminHomeButton.addActionListener((ActionEvent event) -> {
            mainPanel.showAdminPanel();
        });

        adminInventoryButton.addActionListener((ActionEvent event) -> {
            mainPanel.showInventoryPanel("");
        });

        adminOrdersButton.addActionListener((ActionEvent event) -> {
            mainPanel.showOrderManagementPanel();
        });

        adminReportsButton.addActionListener((ActionEvent event) -> {
            mainPanel.showReportsPanel();
        });

        if (!user.isAdmin()) {
            add(homeButton);
            add(browseButton);
            add(cartButton);
            add(orderButton);
        } else {
            add(adminHomeButton);
            add(adminInventoryButton);
            add(adminOrdersButton);
            add(adminReportsButton);
        }
    }
}
