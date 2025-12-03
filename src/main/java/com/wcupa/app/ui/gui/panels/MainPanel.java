package com.wcupa.app.ui.gui.panels;

import javax.swing.*;

import java.util.Stack;

import com.wcupa.app.domain.core.Cart;
import com.wcupa.app.domain.core.Product;
import com.wcupa.app.domain.core.Role;
import com.wcupa.app.domain.core.User;
import com.wcupa.app.service.AdminService;
import com.wcupa.app.service.CheckoutService;
import com.wcupa.app.service.OrderService;
import com.wcupa.app.service.ProductService;
import com.wcupa.app.service.ReportingService;
import com.wcupa.app.service.ReviewService;
import com.wcupa.app.service.DiscountService;
import com.wcupa.app.session.Session;
import com.wcupa.app.ui.gui.frames.MainFrame;
import com.wcupa.app.ui.gui.components.*;

import java.awt.*;
import java.awt.event.ActionEvent;

public class MainPanel extends JPanel {
    private CardLayout cardLayout;
    private JPanel controlPanel;
    private MainFrame parentFrame;
    private OrderService orderService;
    private ProductService productService;
    private CheckoutService checkoutService;
    private DiscountService discountService;
    private ReviewService reviewService;
    private AdminService adminService;
    private ReportingService reportingService;
    private CartPanel cartPanel;
    private OrdersPanel ordersPanel;
    private CheckoutPanel checkoutPanel;
    private ProductsPanel productsPanel;
    private HomePanel homePanel;
    private AdminHomePanel adminPanel;
    private InventoryPanel inventoryPanel;
    private OrderManagementPanel orderManagementPanel;
    private ReportsPanel reportsPanel;
    private Stack<String> navigationStack = new Stack<>();

    public MainPanel(MainFrame parentFrame, ProductService productService, OrderService orderService, CheckoutService checkoutService, DiscountService discountService, ReviewService reviewService, AdminService adminService, ReportingService reportingService) {
        this.parentFrame = parentFrame;
        this.orderService = orderService;
        this.productService = productService;
        this.checkoutService = checkoutService;
        this.discountService = discountService;
        this.reviewService = reviewService;
        this.adminService = adminService;
        this.reportingService = reportingService;

        // Top section
        JPanel topSection = new JPanel();
        topSection.setLayout(new BoxLayout(topSection, BoxLayout.Y_AXIS));
        SearchPanel searchPanel = new SearchPanel(this, query -> {
            productsPanel.setSearchQuery(query);
        });
        topSection.add(searchPanel);
        NavBar navBar = new NavBar(this, parentFrame.getUser());
        topSection.add(navBar);
        topSection.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        // Bottom section
        JButton logoutButton = new JButton("Log Out");
        logoutButton.addActionListener((ActionEvent event) -> parentFrame.showLoginPage());

        // Center Section 
        setLayout(new BorderLayout());
        cardLayout = new CardLayout();
        controlPanel = new JPanel(cardLayout);

        // Add components to panel
        add(topSection, BorderLayout.NORTH);
        add(controlPanel, BorderLayout.CENTER);
        add(logoutButton, BorderLayout.SOUTH);

        // Role-Specific - short on time to implement
        if (parentFrame.getUser().isAdmin()) {
            showAdminPanel();
        } else {
            showHomePanel();
        }

    }

    public User getUser() {
        return parentFrame.getUser();
    }

    public Cart getCart() {
        return parentFrame.getCart();
    }

    public void clearCart() {
        parentFrame.clearCart();
    }

    private void navigateTo(String panel) {
        if (navigationStack.isEmpty() || !navigationStack.peek().equals(panel)) {
            navigationStack.push(panel);
        }
        cardLayout.show(controlPanel, panel);
    }

    public void showProductsPanel() {
        if (productsPanel == null) {
            productsPanel = new ProductsPanel(this, productService);
            controlPanel.add(productsPanel, "products");
            productsPanel.refresh();
        } else {
            productsPanel.refresh();
        }
        navigateTo("products");
    }

    public void showProductsPanel(String query) {
        if (productsPanel == null) {
            productsPanel = new ProductsPanel(this, productService);
            controlPanel.add(productsPanel, "products");
            productsPanel.setSearchQuery(query);
            productsPanel.refresh();
        } else {
            productsPanel.setSearchQuery(query);
            productsPanel.refresh();
        }
        navigateTo("products");
    }

    public void showInventoryPanel() {
        if (inventoryPanel == null) {
            inventoryPanel = new InventoryPanel(this, adminService);
            controlPanel.add(inventoryPanel, "inventory");
        }
        inventoryPanel.refresh();
        navigateTo("inventory");
    }

    public void showInventoryPanel(String query) {
        if (inventoryPanel == null) {
            inventoryPanel = new InventoryPanel(this, adminService);
            controlPanel.add(inventoryPanel, "inventory");
            inventoryPanel.setSearchQuery(query);
            inventoryPanel.refresh();
        } else {
            inventoryPanel.setSearchQuery(query);
            inventoryPanel.refresh();
        }
        navigateTo("inventory");
    }

    public void showOrderManagementPanel() {
        if (orderManagementPanel == null) {
            orderManagementPanel = new OrderManagementPanel(this, adminService);
            controlPanel.add(orderManagementPanel, "orderManagement");
            orderManagementPanel.refresh();
        } else {
            orderManagementPanel.refresh();
        }
        navigateTo("orderManagement");
    }

    public void showReportsPanel() {
        if (reportsPanel == null) {
            reportsPanel = new ReportsPanel(this, adminService);
            controlPanel.add(reportsPanel, "reports");
            reportsPanel.refresh();
        } else {
            reportsPanel.refresh();
        }
        navigateTo("reports");
    }

    public void showAdminPanel() {
        if (adminPanel == null) {
            adminService.setOrderService(orderService);
            adminService.setProductService(productService);
            adminService.setReportingService(reportingService);
            adminService.setSession(parentFrame.getSession());
            adminPanel = new AdminHomePanel(this, adminService);
            controlPanel.add(adminPanel, "admin");
        } else {
            adminPanel.refresh();
        }
        navigateTo("admin");
    }

    public void showHomePanel() {
        if (homePanel == null) {
            homePanel = new HomePanel(this, productService);
            controlPanel.add(homePanel, "home");
            homePanel.refresh();
        } else {
            homePanel.refresh();
        }
        navigateTo("home");
    }

    public void showProductDetailsPanel(Product product) {
        ProductDetailsPanel details = new ProductDetailsPanel(this, product, getCart(), reviewService, parentFrame.getUser().getEmail(), adminService);
        String panelName = "productDetails_" + product.getId();
        controlPanel.add(details, panelName);
        navigateTo(panelName);
    }

    public void showCartPanel() {
        if (cartPanel == null) {
        cartPanel = new CartPanel(this, discountService); 
        controlPanel.add(cartPanel, "cart");
        cartPanel.refresh(this);
    } else {
        cartPanel.refresh(this);  
    }
        navigateTo("cart");
    }

    public void showCheckoutPanel() {
        if (checkoutPanel == null) {
            checkoutPanel = new CheckoutPanel(this, checkoutService);
            controlPanel.add(checkoutPanel, "checkout");
            checkoutPanel.refresh();
        } else {
            checkoutPanel.refresh();
        }
        navigateTo("checkout");
    }

    public void showOrdersPanel() {
        if (ordersPanel == null) {
            ordersPanel = new OrdersPanel(this, orderService);
            controlPanel.add(ordersPanel, "orders");
            ordersPanel.refresh();
        } else {
            ordersPanel.refresh();
        }
        navigateTo("orders");
    }

    public void showPrevious() {
        if (navigationStack.size() > 1) {
            navigationStack.pop();
            String previousPanel = navigationStack.peek();
            cardLayout.show(controlPanel, previousPanel);
        }
    }
}
