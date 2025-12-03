package com.wcupa.app.ui.gui.frames;

import javax.swing.*;
import java.awt.*;
import com.wcupa.app.ui.gui.panels.*;
import com.wcupa.app.service.OrderService;
import com.wcupa.app.service.ProductService;
import com.wcupa.app.service.ReportingService;
import com.wcupa.app.service.ReviewService;
import com.wcupa.app.service.UserService;
import com.wcupa.app.domain.core.Admin;
import com.wcupa.app.domain.core.Cart;
import com.wcupa.app.domain.core.Review;
import com.wcupa.app.domain.core.User;
import com.wcupa.app.service.AdminService;
import com.wcupa.app.service.AuthService;
import com.wcupa.app.service.CheckoutService;
import com.wcupa.app.service.DiscountService;
import com.wcupa.app.session.Session;


public class MainFrame extends JFrame {
    private final AuthService authService = AuthService.getInstance();
    private final UserService userService = UserService.getInstance();
    private final ProductService productService = ProductService.getInstance();
    private final OrderService orderService = OrderService.getInstance();
    private final CheckoutService checkoutService = CheckoutService.getInstance();
    private final DiscountService discountService = DiscountService.getInstance();
    private final ReviewService reviewService = ReviewService.getInstance();
    private final AdminService adminService = AdminService.getInstance();
    private final ReportingService reportingService = ReportingService.getInstance();
    private CardLayout cardLayout;
    private JPanel controlPanel;
    private Session session = new Session();
    

    public MainFrame() {
        
        setTitle("Brian's eCommerce Platform");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 1080);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        controlPanel = new JPanel(cardLayout);

        LoginPanel loginPanel = new LoginPanel(this, authService);
        RegistrationPanel registrationPanel = new RegistrationPanel(this, authService);

        controlPanel.add(loginPanel, "login");
        controlPanel.add(registrationPanel, "registration");

        add(controlPanel);
        cardLayout.show(controlPanel, "login");
    }

    public void showLoginPage() {
        cardLayout.show(controlPanel, "login");
    }

    public void loadMainPage() {
        MainPanel mainPanel = new MainPanel(this, productService, orderService, checkoutService, discountService, reviewService, adminService, reportingService);
        controlPanel.add(mainPanel, "main");
        cardLayout.show(controlPanel, "main");
    }

    public void showMainPage() {
        cardLayout.show(controlPanel, "main");
    }

    public void showRegistrationPage() {
        cardLayout.show(controlPanel, "registration");
    }

    public Session getSession() {
        return this.session;
    }

    public User getUser() {
        return session.getUser();
    }

    public Cart getCart() {
        return session.getCart();
    }

    public void clearCart() {
        session.clearCart();
    }
    
}
