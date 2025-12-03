package com.wcupa.app.ui.gui.panels;

import javax.swing.*;

import com.wcupa.app.domain.core.Product;
import com.wcupa.app.service.ProductService;
import com.wcupa.app.ui.gui.components.ProductGrid;
import com.wcupa.app.ui.gui.panels.*;

import java.awt.*;
import java.awt.event.ActionEvent;

import java.util.List;

public class HomePanel extends JPanel {
    private MainPanel mainPanel;
    private ProductService productService;

    public HomePanel(MainPanel panel, ProductService productService) {
        this.mainPanel = panel;
        this.productService = productService;
        setLayout(new BorderLayout());
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
        List<Product> recent = productService.getProductsSortedByIdDesc();
        ProductGrid grid = new ProductGrid(mainPanel, recent.subList(0, 5));
        centerBox.add(grid);

        JScrollPane scrollPane = new JScrollPane(centerBox);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        add(scrollPane, BorderLayout.CENTER);

        revalidate();
        repaint();
    }
    
}
