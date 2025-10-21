package com.wcupa.app.ui.cli;

import java.util.List;
import java.util.Scanner;

import com.wcupa.app.domain.Product;
import com.wcupa.app.domain.User;
import com.wcupa.app.domain.Role;
import com.wcupa.app.session.Session;
import com.wcupa.app.domain.Permission;
import com.wcupa.app.utils.*;


public class CLI {
    public Session session = new Session();
    public Scanner scanner = new Scanner(System.in);

    public void start() {
        while (session.signedIn() == false) {
            signIn();
        }
        while (session.signedIn() == true) {
            mainMenu();
        }
    }

    private void signIn() {
        System.out.println("Options\n1. Log In\n2. Register\n3. Exit");
        System.out.print("Command: ");
        int command = scanner.nextInt();
        switch (command) {
            case 1:
                login();
            case 2:
                register();
            case 3:
                exit(0);
            default:
                System.out.println("Unknown command");
        }
    }

    private void login() {
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        User user = session.userService.login(email, password);
        System.out.println(user != null ? "Login successful." : "Invalid credentials.");
        session.setUser(user);
    }

    private void register() {
        System.out.print("Role (Customer | Admin): ");
        String role = scanner.nextLine().strip().toUpperCase();
        System.out.print("First Name: ");
        String firstName = scanner.nextLine();
        System.out.print("Last Name: ");
        String lastName = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        boolean registered = session.userService.createAccount(User.createUser(firstName, lastName, email, password, Role.valueOf(role)));
        System.out.println(registered ? "User registered." : "Email already exists.");
    }

    private void mainMenu() {
        if (session.getPermissions().contains(Permission.VIEW)) {
            System.out.println("1. Browse Products");
    }
        if (session.getPermissions().contains(Permission.INVENTORY)) {
            System.out.println("2. Manage Inventory");
        }

        System.out.println("0. Exit");

        System.out.print("Command: ");
        int command = scanner.nextInt();
        switch (command) {
            case 1:
                browseProducts();
            case 2:
                manageInventory();
            case 0:
                exit(0);
            default:
                System.out.println("Unknown command");
        }

    }

    private void browseProducts() {
        if (session.getPermissions().contains(Permission.VIEW)) {
            System.out.println("1. Show All Products");
            System.out.println("2. Search by Keyword");
    }
        if (session.getPermissions().contains(Permission.VIEW)) {
            System.out.println("2. Search by Keyword");
        }
    }

    private void manageInventory() {
        System.out.println("1. View Current Inventory");
        System.out.println("2. Add Product");
        System.out.println("3. Remove Product");
        System.out.println("0. Exit");
        int command = scanner.nextInt();
        switch (command) {
            case 1:
                showAllProducts();
            case 2:
                addProduct();
            case 3:
                removeProduct();
            case 0:
                exit(0);
            default:
                System.out.println("Unknown command");
        }


    }

    private void addProduct() {
        System.out.print("Product name: ");
        String name = scanner.nextLine();
        System.out.print("Price (double): ");
        double price = Double.parseDouble(scanner.nextLine());
        System.out.print("Description: ");
        String description = scanner.nextLine();
        System.out.print("Quantity (int): ");
        int quantity = scanner.nextInt();
        System.out.print("Tags e.g. tag1,tag2,tag3: ");
        List<String> tags = domainUtils.splitStringToList(scanner.nextLine());
        boolean result = session.productService.addProduct(new Product(name, price, description, tags, quantity));
        System.out.println(result ? "Product added successfully" : "Product entry updated");
    }

    private void removeProduct() {
        System.out.print("Product name: ");
        String name = scanner.nextLine();
        boolean result = session.productService.deleteProduct(name);
        System.out.println(result ? "Product deleted successfully" : "Product could not be deleted");
    }

    private void showAllProducts() {
        System.out.println("Current Inventory");
        session.productService.printAllProducts();
    }

    private void exit(int status) {
        System.out.println("Goodbye");
        System.exit(status);
    }
}
