package com.wcupa.app.ui.gui.components;

import javax.swing.*;

import java.awt.event.MouseListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.*;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.wcupa.app.domain.core.CartItem;
import com.wcupa.app.domain.core.Product;
import com.wcupa.app.ui.gui.panels.*;;

public class CartItemCard extends JPanel {
    private final CartPanel cartPanel;

    public CartItemCard(MainPanel mainPanel, CartItem item, CartPanel cartPanel) {
        this.cartPanel = cartPanel;
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 250));

        ProductCard prod = new ProductCard(mainPanel, item.getProduct());
        add(prod, BorderLayout.WEST);

        SpinnerModel spinnerModel = new SpinnerNumberModel(item.getQuantity(), 0, item.getProduct().getQuantity(), 1);
        JSpinner quantitySpinner = new JSpinner(spinnerModel);
        quantitySpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSpinner sourceSpinner = (JSpinner) e.getSource();
                int currentValue = (int) sourceSpinner.getValue();
                item.setQuantity(currentValue);
                JOptionPane.showMessageDialog(mainPanel, currentValue == 0 ? String.format("%s removed from cart!", item.getProduct().getName()) : String.format("Cart updated!"));
                cartPanel.refresh(mainPanel);
            }
        });
        add(quantitySpinner, BorderLayout.EAST);
    }
    
}
