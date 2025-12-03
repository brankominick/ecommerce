package com.wcupa.app.ui.gui.panels;

import javax.swing.*;

import java.util.List;

import com.wcupa.app.domain.core.Order;
import com.wcupa.app.service.OrderService;
import com.wcupa.app.service.ProductService;
import com.wcupa.app.session.Session;
import com.wcupa.app.ui.gui.frames.MainFrame;
import com.wcupa.app.ui.gui.components.*;

import java.awt.*;
import java.awt.event.ActionEvent;

public class OrdersPanel extends JPanel {
    protected MainPanel mainPanel;
    private OrderService orderService;
    
    public OrdersPanel(MainPanel mainPanel, OrderService orderService) {
        this.mainPanel = mainPanel;
        this.orderService = orderService;
        setLayout(new BorderLayout());
        refresh();
    }

    public void refresh() {
        List<Order> orders = orderService.getOrdersForEmail(mainPanel.getUser().getEmail());
        System.out.println(String.format("Orders: %s", orders.toString()));

        JPanel listContainer = new JPanel();
        listContainer.setLayout(new BoxLayout(listContainer, BoxLayout.Y_AXIS));
        listContainer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        if (orders.isEmpty()) {
            listContainer.add(new JLabel("No orders found for your account"));
        }
        else {
            for (Order order : orders) {
            listContainer.add(new OrderCard(mainPanel, order));
            listContainer.add(Box.createVerticalStrut(10));
            }
        }

        JScrollPane scrollPane = new JScrollPane(listContainer);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        add(scrollPane, BorderLayout.CENTER);
    }
}
