package com.wcupa.app.ui.gui.panels;

import java.util.List;

import java.awt.*;

import javax.swing.*;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;

import com.wcupa.app.domain.core.Product;
import com.wcupa.app.service.AdminService;
import com.wcupa.app.ui.gui.components.ProductGrid;

public class AdminHomePanel extends JPanel {
    private MainPanel mainPanel;
    private final AdminService adminService;

    AdminHomePanel(MainPanel mainPanel, AdminService adminService) {
        this.mainPanel = mainPanel;
        this.adminService = adminService;
    }

    public void refresh() {
        removeAll();

        ImageIcon banner = new ImageIcon(HomePanel.class.getResource("/images/home-banner.png"));

        JLabel bannerLabel = new JLabel(banner);
        bannerLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel bannerContainer = new JPanel(new BorderLayout());
        bannerContainer.add(bannerLabel, BorderLayout.NORTH); 

        add(bannerContainer, BorderLayout.NORTH);

        JPanel centerBox = new JPanel();
        centerBox.setLayout(new BoxLayout(centerBox, BoxLayout.Y_AXIS));

        // Recent items
        JLabel recentLabel = new JLabel("New Items");
        recentLabel.setFont(new Font("Arial", Font.BOLD, 24));
        centerBox.add(recentLabel);
        List<Product> recent = adminService.getProductsSortedByIdDesc();
        ProductGrid grid = new ProductGrid(mainPanel, recent.subList(0, 5));
        centerBox.add(grid);

        // TODO: Add daily reports
        JLabel reportsLabel = new JLabel("Today's Reports");
        reportsLabel.setFont(new Font("Arial", Font.BOLD, 24));
        centerBox.add(reportsLabel);
        centerBox.add(new JLabel("Coming soon!"));

        JScrollPane scrollPane = new JScrollPane(centerBox);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        add(scrollPane, BorderLayout.CENTER);

        revalidate();
        repaint();
    }
    
}
