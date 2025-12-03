package com.wcupa.app.ui.gui.panels;


import javax.swing.*;

import com.wcupa.app.domain.core.Product;
import com.wcupa.app.domain.util.FilterSettings;
import com.wcupa.app.domain.util.FilterSettings.SortSetting;
import com.wcupa.app.service.ProductService;
import com.wcupa.app.ui.gui.panels.*;
import com.wcupa.app.utils.ProductTree;
import com.wcupa.app.ui.gui.components.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.stream.Collectors;
import java.util.List;
import java.util.ArrayList;

public class ProductsPanel extends JPanel {
    protected MainPanel mainPanel;
    private ProductService productService;
    private List<Product> products;
    private FilterSettings filter;
    private ProductFilter productFilter;
    private ProductGrid productGrid;
    private JScrollPane productScroll;

    
    public ProductsPanel(MainPanel panel, ProductService productService) {
    this.mainPanel = panel;
    this.productService = productService;
    this.products = productService.getProductsSortedByPriceAsc();
    this.filter = new FilterSettings("", new ArrayList<String>(), 0.0, Double.MAX_VALUE, 0.0, SortSetting.DATE_DESC);

    setLayout(new BorderLayout());

    this.productGrid = new ProductGrid(panel, products);
    productScroll = new JScrollPane(productGrid);
    productScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    productScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
    add(productScroll, BorderLayout.CENTER);

    this.productFilter = new ProductFilter(
            productService.getAllCategories(), 
            newFilter -> {
                this.filter = newFilter;
                applyFilters();
            });
    add(new JScrollPane(productFilter), BorderLayout.WEST);
}


    public void refresh() {
        applyFilters();
    }

    public void setSearchQuery(String query) {
        filter.setQueryString(query.strip().toLowerCase());
        applyFilters();
    }

    private void applyFilters() {
        products = switch (filter.getSortSetting()) {
            case PRICE_ASC -> productService.getProductsSortedByPriceAsc();
            case PRICE_DESC -> productService.getProductsSortedByPriceDesc();
            case RATING_ASC -> productService.getProductsSortedByRatingAsc();
            case RATING_DESC -> productService.getProductsSortedByRatingDesc();
            case NAME_ASC -> productService.getProductsSortedByNameAsc();
            case NAME_DESC -> productService.getProductsSortedByNameDesc();
            case DATE_ASC -> productService.getProductsSortedByIdAsc();
            case DATE_DESC -> productService.getProductsSortedByIdDesc();
            default -> productService.getProductsSortedByPriceAsc();
        };

        List<Product> filteredProducts = products.stream()
                                                .filter(filter::isMatch)
                                                .toList();

        productGrid.removeAll();
        for (Product p : filteredProducts) {
            productGrid.add(new ProductCard(mainPanel, p));
        }

        productGrid.revalidate();
        productGrid.repaint();
    }

}
