package com.wcupa.app.ui.gui.panels;


import javax.swing.*;

import com.wcupa.app.ui.gui.components.CartItemCard;
import com.wcupa.app.ui.gui.panels.*;

import java.awt.*;
import java.awt.event.ActionEvent;

import com.wcupa.app.domain.core.Cart;
import com.wcupa.app.domain.core.CartItem;
import com.wcupa.app.domain.core.Discount;
import com.wcupa.app.domain.core.User;
import com.wcupa.app.service.DiscountService;



public class CartPanel extends JPanel {
    private MainPanel mainPanel;
    private DiscountService discountService;

    public CartPanel(MainPanel mainPanel, DiscountService discountService) {
        this.mainPanel = mainPanel;
        this.discountService = discountService;
        setLayout(new BorderLayout());
        refresh(this.mainPanel);
    }

    public void refresh(MainPanel panel) {
        removeAll();

        this.mainPanel = panel;
        Cart cart = mainPanel.getCart();
        
        JPanel listContainer = new JPanel();
        listContainer.setLayout(new BoxLayout(listContainer, BoxLayout.Y_AXIS));
        listContainer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        listContainer.add(new JLabel(String.format("Total: $%.2f", cart.getTotalAfterDiscounts())));
        listContainer.add(new JLabel(String.format("Total number of items %d", cart.getNumberItems())));
        if (!cart.getDiscounts().isEmpty()) {
            StringBuilder sb = new StringBuilder("Active Discounts: ");
            for (Discount d : cart.getDiscounts()) {
                sb.append(d.getName()).append(" ");
            }
            listContainer.add(new JLabel(sb.toString()));
        }

        
        if (cart.getItems().isEmpty()) {
            listContainer.add(new JLabel("Your cart is empty"));
        } else {
            for (CartItem item : cart.getItems()) {
                listContainer.add(new CartItemCard(mainPanel, item, this));
            }
        }

        JPanel discountSection = new JPanel();
        discountSection.setLayout(new FlowLayout());
        JTextField discountCode = new JTextField(10);
        discountCode.setMaximumSize(new Dimension(100, discountCode.getPreferredSize().height));
        JButton addDiscount = new JButton("Apply discount code");
        addDiscount.addActionListener((event) -> {
            String code = discountCode.getText().trim();
            Discount discount = discountService.getActiveDiscountByName(code);

            if (discount != null) {
                cart.addDiscount(discount);
                JOptionPane.showMessageDialog(mainPanel, "Discount applied!");
                refresh(panel);
            } else {
                JOptionPane.showMessageDialog(mainPanel, "Invalid or inactive discount code.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        discountSection.add(discountCode);
        discountSection.add(addDiscount);
        listContainer.add(discountCode);
        listContainer.add(addDiscount);
        JButton checkout = new JButton("Checkout");
        checkout.addActionListener((event) -> {
            if (!cart.getItems().isEmpty()) {
                mainPanel.showCheckoutPanel();
            } else {
                JOptionPane.showMessageDialog(mainPanel, "Can't proceed with empty cart!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        listContainer.add(checkout);

        JScrollPane scrollPane = new JScrollPane(listContainer);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollPane);

        revalidate();
        repaint();
    }
    
}
