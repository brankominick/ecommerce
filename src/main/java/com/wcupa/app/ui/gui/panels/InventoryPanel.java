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

public class InventoryPanel extends ProductsPanel {
    private final AdminService adminService;

    public InventoryPanel(MainPanel panel, AdminService adminService) {
        super(panel, adminService.getProductService());
        this.adminService = adminService;

        JButton addProductButton = new JButton("Add New Product");

        addProductButton.addActionListener(e -> {
            openAddDialog();
        });

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(addProductButton);

        this.add(topPanel, BorderLayout.NORTH);
    }

    private void openAddDialog() {
        JDialog dialog = new JDialog((Frame) null, "Edit Product", true);
        dialog.setSize(400, 450);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        form.add(new JLabel("Product Name:"));
        JTextField nameField = new JTextField();
        JLabel nameErrorLabel = new JLabel();
        InputValidator.attachValidator(
            nameField,
            InputValidator.notEmpty(),
            nameErrorLabel,
            "Name field cannot be blank."
        );
        form.add(nameField);

        form.add(new JLabel("Price:"));
        JTextField priceField = new JTextField();
        JLabel priceErrorLabel = new JLabel();
        InputValidator.attachValidator(
            priceField,
            InputValidator.isPrice(),
            priceErrorLabel,
            "Must be number with optional decimal values (e.g. 0.01, 5, etc.)"
        );
        form.add(priceField);

        form.add(new JLabel("Quantity:"));
        JTextField quantityField = new JTextField();
        JLabel quantityErrorLabel = new JLabel();
        InputValidator.attachValidator(
            quantityField,
            InputValidator.isInteger(),
            quantityErrorLabel,
            "Must be whole number (0+)."
        );
        form.add(quantityField);

        form.add(new JLabel("Description:"));
        JTextArea descriptionArea = new JTextArea(5, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JLabel descriptionErrorLabel = new JLabel();
        InputValidator.attachValidator(
            descriptionArea,
            InputValidator.notEmpty(),
            descriptionErrorLabel,
            "Description cannot be blank."
        );
        form.add(new JScrollPane(descriptionArea));

        form.add(new JLabel("Image Path:"));
        JFileChooser imageChooser = new JFileChooser();
        form.add(imageChooser);

        JPanel buttonPanel = new JPanel();
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            try {
                if (!(nameField.isValid() && priceField.isValid() && quantityField.isValid() && descriptionArea.isValid())) {
                    throw new InvalidParameterException("All product field values must be valid.");
                }
                String name = nameField.getText().strip();
                double price = Double.parseDouble(priceField.getText().strip());
                int quantity = Integer.parseInt(quantityField.getText().strip());
                String description = descriptionArea.getText().strip();
                Path selected = imageChooser.getSelectedFile().toPath().toAbsolutePath().normalize();
                Path resourcesRoot = Paths.get("src/main/resources").toAbsolutePath().normalize();
                Path relative = resourcesRoot.relativize(selected);
                String imagePath = "/" + relative.toString().replace("\\", "/");
                int proceed = JOptionPane.showConfirmDialog(dialog, "Update this product?", "Confirm Update", JOptionPane.YES_NO_OPTION);
                if (proceed == JOptionPane.YES_OPTION) {
                    Product newProduct = new Product(name, price, description, null, quantity, imagePath);
                    adminService.addNewProduct(newProduct);
                    dialog.setVisible(false);
                    adminService.refreshProductService();
                    refresh();
                }
            } catch (Exception exc) {
                JOptionPane.showMessageDialog(this, "Could not add new product with given values. Please ensure all fields are valid. ERROR:\n" + exc.toString(), "Product Update Failed", JOptionPane.ERROR_MESSAGE);
            }
        });
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dialog.dispose());
        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);

        dialog.add(form, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }
}
