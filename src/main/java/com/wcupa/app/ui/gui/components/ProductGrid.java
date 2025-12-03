package com.wcupa.app.ui.gui.components;

import javax.swing.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.List;

import com.wcupa.app.domain.core.Product;
import com.wcupa.app.domain.util.FilterSettings;
import com.wcupa.app.ui.gui.panels.*;;


public class ProductGrid extends JPanel {

    private static final Logger logger = LoggerFactory.getLogger(ProductGrid.class);

    public ProductGrid(MainPanel mainPanel, List<Product> products) {
        setLayout(new WrapLayout(FlowLayout.LEFT, 15, 15)); 
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        for (Product p : products) {
            if (p.getQuantity() > 0) {
                add(new ProductCard(mainPanel, p));
            }
        }
    }

    @Override
    public Dimension getPreferredSize() {
        Container parent = getParent();
        if (parent instanceof JViewport) {
            JViewport viewport = (JViewport) parent;
            int viewportWidth = viewport.getWidth();

            if (viewportWidth > 0) {
                setSize(viewportWidth, 1); 
                int layoutHeight = getLayout().preferredLayoutSize(this).height;
                Insets borderInsets = getBorder() != null ? getBorder().getBorderInsets(this) : new Insets(0, 0, 0, 0);
                int totalHeight = layoutHeight + borderInsets.top + borderInsets.bottom;
                return new Dimension(viewportWidth, totalHeight);            }
        }
        
        return super.getPreferredSize();
    }
}
