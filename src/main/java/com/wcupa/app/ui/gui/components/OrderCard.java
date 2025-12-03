package com.wcupa.app.ui.gui.components;


import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.*;

import com.wcupa.app.domain.core.Order;
import com.wcupa.app.service.AdminService;
import com.wcupa.app.ui.gui.panels.*;;

public class OrderCard extends JPanel {
    private final AdminService adminService;

    public OrderCard(MainPanel mainPanel, Order order, AdminService adminService) {
        this.adminService = adminService;
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        setBackground(Color.WHITE);

        setMaximumSize(new Dimension(Integer.MAX_VALUE, 250));

        JLabel conf = new JLabel("Order #" + order.getConfirmation());
        JLabel date = new JLabel("Order placed on " + order.getOrderPlaced().toString());
        JLabel total = new JLabel("$" + String.valueOf(order.getTotal()));
        JLabel orderStatus = new JLabel(order.getOrderStatus().toString());

        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.add(date);
        info.add(total);
        info.add(orderStatus);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        JButton viewItems = new JButton("View Items");
        viewItems.addActionListener((ActionEvent event) -> {
            // TODO: replace with order details page?
            JOptionPane.showMessageDialog(mainPanel, "Function not yet implemented. Please contact support for help with an existing order.", "Cannot Complete Action", JOptionPane.ERROR_MESSAGE);
        });
        buttonPanel.add(viewItems);
        JButton updateOrder = new JButton("Update Order Status");
        updateOrder.addActionListener(e -> {
            showStatusChangeDialog(mainPanel, order, adminService, orderStatus);
        });
        buttonPanel.add(updateOrder);

        add(conf, BorderLayout.WEST);
        add(info, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.EAST);
    }

    public OrderCard(MainPanel mainPanel, Order order) {
        this(mainPanel, order, null);
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        setBackground(Color.WHITE);

        setMaximumSize(new Dimension(Integer.MAX_VALUE, 250));

        JLabel conf = new JLabel("Order #" + order.getConfirmation());
        JLabel date = new JLabel("Order placed on " + order.getOrderPlaced().toString());
        JLabel total = new JLabel("$" + String.valueOf(order.getTotal()));
        JLabel orderStatus = new JLabel(order.getOrderStatus().toString());

        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.add(date);
        info.add(total);
        info.add(orderStatus);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        JButton viewItems = new JButton("View Items");
        viewItems.addActionListener((ActionEvent event) -> {
            // TODO: replace with order details page?
            JOptionPane.showMessageDialog(mainPanel, "Function not yet implemented. Please contact support for help with an existing order.", "Cannot Complete Action", JOptionPane.ERROR_MESSAGE);
        });
        buttonPanel.add(viewItems);

        add(conf, BorderLayout.WEST);
        add(info, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.EAST);
    }

    public void showStatusChangeDialog(MainPanel mainPanel, Order order, AdminService adminService, JLabel statusLabel) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        ButtonGroup group = new ButtonGroup();
        JRadioButton[] buttons = new JRadioButton[Order.OrderStatus.values().length];

        Order.OrderStatus currentStatus = order.getOrderStatus();

        int index = 0;
        for (Order.OrderStatus status : Order.OrderStatus.values()) {
            JRadioButton btn = new JRadioButton(status.toString());
            buttons[index] = btn;
            group.add(btn);
            panel.add(btn);

            // Pre-select the existing status
            if (status == currentStatus) {
                btn.setSelected(true);
            }
            index++;
        }

        int result = JOptionPane.showConfirmDialog(
                mainPanel,
                panel,
                "Update Order Status",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.YES_OPTION) {
            // Find selected status
            for (int i = 0; i < buttons.length; i++) {
                if (buttons[i].isSelected()) {
                    Order.OrderStatus newStatus = Order.OrderStatus.values()[i];
                    order.setOrderStatus(newStatus);
                    statusLabel.setText(newStatus.toString());
                    statusLabel.revalidate();
                    statusLabel.repaint();
                    break;
                }
            }
        }
    }
    
}
