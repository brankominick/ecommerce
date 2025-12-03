package com.wcupa.app.ui.gui.panels;

import javax.swing.*;
import java.awt.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidParameterException;

import java.time.LocalDate;

import com.wcupa.app.domain.analytics.TotalRevenueReport;
import com.wcupa.app.domain.core.Product;
import com.wcupa.app.service.AdminService;
import com.wcupa.app.service.ProductService;
import com.wcupa.app.ui.gui.components.KpiCard;
import com.wcupa.app.utils.validation.InputValidator;

public class ReportsPanel extends JPanel {
    private MainPanel mainPanel;
    private final AdminService adminService;

    private JPanel kpiPanel;
    private JTable bestSellers;
    private JTable lowStock;

    public ReportsPanel(MainPanel mainPanel, AdminService adminService) {
        this.mainPanel = mainPanel;
        this.adminService = adminService;

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel title = new JLabel("Reporting Dashboard");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        add(title, BorderLayout.NORTH);

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        split.setResizeWeight(.40);
        add(split, BorderLayout.CENTER);

        split.setTopComponent(buildKpiSection());
        split.setBottomComponent(buildTablesSection());

        refresh();
    }

    private JPanel buildKpiSection() {
        kpiPanel = new JPanel();
        kpiPanel.setLayout(new GridLayout(1, 4, 10, 10));
        kpiPanel.setBorder(BorderFactory.createEmptyBorder(10, 10,10, 10));
        return kpiPanel;
    }

    private JTabbedPane buildTablesSection() {
        JTabbedPane tabs = new JTabbedPane();

        bestSellers = new JTable();
        JScrollPane bestSellersScroll = new JScrollPane(bestSellers);
        tabs.addTab("Best Sellers", bestSellersScroll);

        lowStock = new JTable();
        JScrollPane lowStockScroll = new JScrollPane(lowStock);
        tabs.addTab("Low Stock Items", lowStockScroll);

        return tabs;
    } 

    public void refresh() {
        refreshKpiCards();
        refreshBestSellers();
        refreshLowStock();
    }

    private void refreshKpiCards() {
        kpiPanel.removeAll();

        TotalRevenueReport revReport = adminService.getTotalRevenueReport(LocalDate.of(2025, 11, 1), LocalDate.of(2025, 12, 12));
        kpiPanel.add(new KpiCard("Total Revenue", String.valueOf(revReport.getData().getTotalRevenue())));
        kpiPanel.add(new KpiCard("Total Tax", String.valueOf(revReport.getData().getTotalTax())));
        kpiPanel.add(new KpiCard("Total Orders", String.valueOf(revReport.getData().getTotalOrders())));

        kpiPanel.revalidate();
        kpiPanel.repaint();
    }

    private void refreshBestSellers() {
        bestSellers.removeAll();
        /*
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Product");
        model.addColumn("Units Sold");
        model.addColumn("Revenue");

        bestSellerTable.setModel(model); */
    }

    private void refreshLowStock() {
        lowStock.removeAll();
        /*
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Product");
        model.addColumn("Current Stock");

        lowStockTable.setModel(model);
    } */
    }
    
}
