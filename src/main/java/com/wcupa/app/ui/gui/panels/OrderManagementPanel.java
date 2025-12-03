package com.wcupa.app.ui.gui.panels;

import javax.swing.*;
import java.awt.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidParameterException;

import com.wcupa.app.domain.core.Product;
import com.wcupa.app.service.AdminService;
import com.wcupa.app.service.ProductService;
import com.wcupa.app.utils.validation.InputValidator;

public class OrderManagementPanel extends OrdersPanel {
    private final AdminService adminService;
    
    public OrderManagementPanel(MainPanel mainPanel, AdminService adminService) {
        super(mainPanel, adminService.getOrderService());
        this.adminService = adminService;
    }
}
