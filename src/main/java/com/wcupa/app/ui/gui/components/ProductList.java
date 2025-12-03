package com.wcupa.app.ui.gui.components;

import com.wcupa.app.domain.core.Product;
import com.wcupa.app.ui.gui.panels.MainPanel;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.*;

import java.awt.*;

import java.util.List;

public class ProductList extends JPanel {

    public ProductList(MainPanel panel, List<Product> products) {
        JPanel listContainer = new JPanel();
        listContainer.setLayout(new BoxLayout(listContainer, BoxLayout.Y_AXIS));
        listContainer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        if (products.isEmpty()) {
            listContainer.add(new JLabel("No products found"));
        }
        else {
            for (Product product : products) {
            listContainer.add(new ProductCard(panel, product));
            listContainer.add(Box.createVerticalStrut(10));
            }
        }

        JScrollPane scrollPane = new JScrollPane(listContainer);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        add(scrollPane, BorderLayout.CENTER);
    }
}
