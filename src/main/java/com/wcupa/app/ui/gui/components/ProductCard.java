package com.wcupa.app.ui.gui.components;

import javax.swing.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.event.MouseListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.*;

import com.wcupa.app.domain.core.Product;
import com.wcupa.app.domain.util.FilterSettings;
import com.wcupa.app.ui.gui.panels.*;;


public class ProductCard extends JPanel {

    public ProductCard(MainPanel mainPanel, Product product) {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        setPreferredSize(new Dimension(200, 250));

        JLabel imageLabel;
        if (product.getImagePath() != null) {
            ImageIcon icon = new ImageIcon(ProductCard.class.getResource(product.getImagePath()));
            Image scaled = icon.getImage().getScaledInstance(180, 120, Image.SCALE_SMOOTH);
            imageLabel = new JLabel(new ImageIcon(scaled));
        } else {
            imageLabel = new JLabel("No Image", SwingConstants.CENTER);
        }

        add(imageLabel, BorderLayout.NORTH);

        JLabel title = new JLabel(product.getName());
        title.setFont(title.getFont().deriveFont(Font.BOLD, 14f));

        String description = product.getDescription();
        JLabel desc = new JLabel("<html><p style='width:170px'>" +
                description.substring(0, Math.min(description.length(), 120)) +
                "</p></html>");

        JLabel price = new JLabel("$" + product.getPrice());
        price.setFont(price.getFont().deriveFont(Font.BOLD, 12f));

        double rating = product.getAverageRating();
        JLabel ratingLabel = new JLabel((rating == 0.0) ? "No reviews yet" : rating + "/5");
        ratingLabel.setFont(ratingLabel.getFont().deriveFont(Font.BOLD, 12f));

        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        desc.setAlignmentX(Component.LEFT_ALIGNMENT);
        price.setAlignmentX(Component.LEFT_ALIGNMENT);
        ratingLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        info.add(title);
        info.add(Box.createVerticalStrut(5));
        info.add(desc);
        info.add(Box.createVerticalStrut(5));
        info.add(ratingLabel);
        info.add(Box.createVerticalGlue());
        info.add(price);

        info.setPreferredSize(new Dimension(200, 130));
        info.setMinimumSize(new Dimension(200, 130));


        add(info, BorderLayout.CENTER);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mainPanel.showProductDetailsPanel(product);
            }
        });
    }
}
