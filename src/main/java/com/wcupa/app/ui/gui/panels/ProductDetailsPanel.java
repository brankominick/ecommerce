package com.wcupa.app.ui.gui.panels;

import javax.swing.*;
import javax.swing.border.Border;

import com.wcupa.app.domain.core.Cart;
import com.wcupa.app.domain.core.Product;
import com.wcupa.app.domain.core.Review;
import com.wcupa.app.service.AdminService;
import com.wcupa.app.service.OrderService;
import com.wcupa.app.service.ProductService;
import com.wcupa.app.service.ReviewService;
import com.wcupa.app.ui.gui.frames.MainFrame;
import com.wcupa.app.utils.validation.InputValidator;
import com.wcupa.app.ui.gui.components.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.security.InvalidParameterException;
import java.util.List;
import java.util.ArrayList;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ProductDetailsPanel extends JPanel {
    private final ReviewService reviewService;
    private final AdminService adminService;
    private final int productId;
    private final String customerEmail;
    private final MainPanel mainPanel;
    
    public ProductDetailsPanel(MainPanel mainPanel, Product product, Cart cart, ReviewService reviewService, String customerEmail, AdminService adminService) {
        this.reviewService = reviewService;
        this.productId = product.getId();
        this.customerEmail = customerEmail;
        this.mainPanel = mainPanel;
        this.adminService = adminService;

        setLayout(new BorderLayout());

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        // Product Image 
        JLabel imageLabel;
        if (product.getImagePath() != null) {
            ImageIcon icon = new ImageIcon(ProductDetailsPanel.class.getResource(product.getImagePath()));
            imageLabel = new JLabel(icon);
        } else {
            imageLabel = new JLabel("No Image");
            imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        }

        leftPanel.add(imageLabel);

        // Product Info
        JLabel productName = new JLabel(product.getName());
        add(productName, BorderLayout.NORTH);

        JLabel productPrice = new JLabel("$" + String.valueOf(product.getPrice()));
        leftPanel.add(productPrice);
        JLabel averageRating = new JLabel(product.getAverageRating()!= 0.0 ? "No reviews yet." : "Rating: " + product.getAverageRating() + "/5");
        leftPanel.add(averageRating);

        int quantity = product.getQuantity();
        if (quantity < 10) {
            JLabel quantityFlag = new JLabel(String.format("Only %s left in stock!", String.valueOf(quantity)));
            leftPanel.add(quantityFlag);
        }
        
        JTextArea productDescription = new JTextArea(product.getDescription());
        productDescription.setLineWrap(true);
        productDescription.setEditable(false);
        centerPanel.add(productDescription);

        if (mainPanel.getUser().isAdmin()) {
            JButton editButton = new JButton("Edit this item");
            editButton.addActionListener(e -> openEditDialog(product));
            centerPanel.add(editButton);

            JButton deleteButton = new JButton("Delete this item");
            deleteButton.addActionListener(e -> {
                int confirmed = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this item from the inventory?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
                if (confirmed == JOptionPane.YES_OPTION) {
                    adminService.deleteProduct(productId);
                    JOptionPane.showConfirmDialog(this, "Product deleted!");
                    adminService.refreshProductService();
                    mainPanel.showInventoryPanel("");
                }
            });
            centerPanel.add(deleteButton);
        }

        JPanel buttonPanel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        SpinnerModel spinnerModel = new SpinnerNumberModel(1, 1, product.getQuantity(), 1);
        JSpinner quantitySpinner = new JSpinner(spinnerModel);
        JButton addToCart = new JButton("Add to Cart");
        JButton backButton = new JButton("Back");

        constraints.gridx = 0;
        constraints.gridy = 0;
        buttonPanel.add(quantitySpinner, constraints);

        constraints.gridx = 1;
        buttonPanel.add(addToCart, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        buttonPanel.add(backButton, constraints);

        centerPanel.add(buttonPanel);

        JPanel reviewsPanel = buildReviewsPanel(reviewService.getReviewsForProduct(product.getId()));
        centerPanel.add(new JScrollPane(reviewsPanel));



        backButton.addActionListener((ActionEvent event) -> {
            mainPanel.showPrevious();
        });

        addToCart.addActionListener((ActionEvent event) -> {
            cart.addProduct(product, (int) quantitySpinner.getValue());
            JOptionPane.showMessageDialog(this, String.format("Added %s %s to the cart!", quantitySpinner.getValue().toString(), product.getName()));
        });

        add(leftPanel, BorderLayout.WEST);
        add(centerPanel, BorderLayout.CENTER);

    }

    private JPanel buildReviewsPanel(List<Review> reviews) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder("Customer Reviews"));

        if (reviews.isEmpty()) {
            JLabel noReviews = new JLabel("No reviews yet.");
            noReviews.setAlignmentX(Component.LEFT_ALIGNMENT);
            panel.add(noReviews);
        }

        for (Review r : reviews) {
            panel.add(buildReview(r));
        }

        JButton addReviewButton = new JButton("Write a review");
        addReviewButton.addActionListener(e -> writeReviewDialog(productId));
        panel.add(addReviewButton);

        return panel;
    }

    private JPanel buildReview(Review review) {
        JPanel reviewPanel = new JPanel();
        reviewPanel.setLayout(new BoxLayout(reviewPanel, BoxLayout.Y_AXIS));
        reviewPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JLabel ratingLabel = new JLabel("Rating: " + review.getRating() + "/5");
        JLabel dateLabel = new JLabel("Date: " + review.getCreateTime());
        JTextArea text = new JTextArea(review.getComment());
        text.setLineWrap(true);
        text.setWrapStyleWord(true);
        text.setEditable(false);

        reviewPanel.add(ratingLabel);
        reviewPanel.add(dateLabel);
        reviewPanel.add(text);

        return reviewPanel;
    }

    private void writeReviewDialog(int productId) {
        JDialog dialog = new JDialog((Frame) null, "Write a review", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        form.add(new JLabel("Rating (1–5):"));
        JSpinner ratingSpinner = new JSpinner(new SpinnerNumberModel(5, 1, 5, 1));
        form.add(ratingSpinner);

        form.add(Box.createVerticalStrut(10));
        form.add(new JLabel("Comment:"));
        JTextArea commentArea = new JTextArea(5, 20);
        commentArea.setLineWrap(true);
        form.add(new JScrollPane(commentArea));

        JPanel buttonPanel = new JPanel();
        JButton submit = new JButton("Submit");
        JButton cancel = new JButton("Cancel");
        buttonPanel.add(submit);
        buttonPanel.add(cancel);

        dialog.add(form, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        cancel.addActionListener(e -> dialog.dispose());

        submit.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(mainPanel, "Publish your review?", "Review", JOptionPane.YES_NO_OPTION);
            if (result == 0) {
                int rating = (int) ratingSpinner.getValue();
                String comment = commentArea.getText().trim();

                if (comment.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Comment cannot be empty.");
                    return;
                }

                try {
                    Review newReview = new Review(productId, customerEmail, rating, comment);
                    reviewService.addReview(newReview);
                    JOptionPane.showMessageDialog(dialog, "Review submitted!");

                    refreshReviews(productId);

                    dialog.dispose();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Failed to submit review.", "Review Submission Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        dialog.setVisible(true);
    }

    private void refreshReviews(int productId) {
        List<Review> updatedReviews = reviewService.getReviewsForProduct(productId);
        
        JPanel newPanel = buildReviewsPanel(updatedReviews);

        for (Component c : getComponents()) {
            if (c instanceof JPanel && 
                ((JPanel) c).getBorder() instanceof javax.swing.border.TitledBorder) {
                remove(c);
                break;
            }
        }

        add(newPanel, BorderLayout.SOUTH);
        revalidate();
        repaint();
    }

    private void openEditDialog(Product product) {
        JDialog dialog = new JDialog((Frame) null, "Edit Product", true);
        dialog.setSize(400, 450);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        form.add(new JLabel("Product Name:"));
        JTextField nameField = new JTextField(product.getName());
        JLabel nameErrorLabel = new JLabel();
        InputValidator.attachValidator(
            nameField,
            InputValidator.notEmpty(),
            nameErrorLabel,
            "Name field cannot be blank."
        );
        form.add(nameField);

        form.add(new JLabel("Price:"));
        JTextField priceField = new JTextField(String.valueOf(product.getPrice()));
        JLabel priceErrorLabel = new JLabel();
        InputValidator.attachValidator(
            priceField,
            InputValidator.isPrice(),
            priceErrorLabel,
            "Must be number with optional decimal values (e.g. 0.01, 5, etc.)"
        );
        form.add(priceField);

        form.add(new JLabel("Quantity:"));
        JTextField quantityField = new JTextField(String.valueOf(product.getQuantity()));
        JLabel quantityErrorLabel = new JLabel();
        InputValidator.attachValidator(
            quantityField,
            InputValidator.isInteger(),
            quantityErrorLabel,
            "Must be whole number (0+)."
        );
        form.add(quantityField);

        form.add(new JLabel("Description:"));
        JTextArea descriptionArea = new JTextArea(product.getDescription(), 5, 20);
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
        JFileChooser imageChooser = new JFileChooser(product.getImagePath());
        form.add(imageChooser);
        //imageChooser.showSaveDialog(null);

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
                    adminService.updateExistingProduct(product.getId(), name, price, quantity, description, imagePath);
                    dialog.setVisible(false);
                    adminService.refreshProductService();
                    Product updatedProduct = adminService.getProductById(productId);
                    mainPanel.showProductDetailsPanel(updatedProduct);
                }
            } catch (Exception exc) {
                JOptionPane.showMessageDialog(this, "Could not update product with given values. Please ensure all fields are valid. ERROR:\n" + exc.toString(), "Product Update Failed", JOptionPane.ERROR_MESSAGE);
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
