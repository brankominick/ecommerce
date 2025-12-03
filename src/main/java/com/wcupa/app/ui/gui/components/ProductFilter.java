package com.wcupa.app.ui.gui.components;

import javax.swing.*;

import com.wcupa.app.domain.util.FilterSettings;
import com.wcupa.app.domain.util.FilterSettings.SortSetting;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class ProductFilter extends JPanel {
    private JSlider priceSlider;
    private JLabel priceLabel;
    private JSlider ratingSlider;
    private JLabel ratingLabel;
    private JCheckBox ratingCheckBox;

    private final Map<String, JCheckBox> categoryBoxes = new HashMap<>();
    private final ButtonGroup sortGroup = new ButtonGroup();

    private Consumer<FilterSettings> onFilterChange;

    public ProductFilter(List<String> categories, Consumer<FilterSettings> onFilterChange) {
        this.onFilterChange = onFilterChange;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createTitledBorder("Filters"));

        addPriceSection();
        addRatingSection();
        addCategorySection(categories);
        addSortSection();

        triggerFilterUpdate();
    }

    // -------- Price Section --------
    private void addPriceSection() {
        JPanel pricePanel = new JPanel();
        pricePanel.setBorder(BorderFactory.createTitledBorder("Max Price"));
        pricePanel.setLayout(new BorderLayout());

        priceSlider = new JSlider(0, 200);
        priceSlider.setMajorTickSpacing(50);
        priceSlider.setMinorTickSpacing(10);
        priceSlider.setPaintTicks(true);
        priceSlider.setPaintLabels(true);

        priceLabel = new JLabel("Max Price: $200");

        priceSlider.addChangeListener(e -> {
            int value = priceSlider.getValue();
            priceLabel.setText("Max Price: $" + value);
            triggerFilterUpdate();
        });

        pricePanel.add(priceLabel, BorderLayout.NORTH);
        pricePanel.add(priceSlider, BorderLayout.CENTER);

        add(pricePanel);
    }

    // -------- Rating Section --------
    private void addRatingSection() {
        JPanel ratingPanel = new JPanel();
        ratingPanel.setBorder(BorderFactory.createTitledBorder("Max Rating"));
        ratingPanel.setLayout(new BorderLayout());

        ratingCheckBox = new JCheckBox("Enable Rating Filter");
        ratingCheckBox.setSelected(false);
        ratingCheckBox.addActionListener(e -> {
            boolean enabled = ratingCheckBox.isSelected();
            ratingSlider.setEnabled(enabled);
            ratingLabel.setEnabled(enabled);
            triggerFilterUpdate();
        });

        ratingSlider = new JSlider(0, 5);
        ratingSlider.setMajorTickSpacing(50);
        ratingSlider.setMinorTickSpacing(10);
        ratingSlider.setPaintTicks(true);
        ratingSlider.setPaintLabels(true);

        ratingLabel = new JLabel("Min Rating: 1-5 stars");

        ratingSlider.addChangeListener(e -> {
            int value = ratingSlider.getValue();
            ratingLabel.setText("Min Rating: " + value + "/5");
            triggerFilterUpdate();
        });

        ratingPanel.add(ratingCheckBox, BorderLayout.NORTH);
        ratingPanel.add(ratingLabel, BorderLayout.CENTER);
        ratingPanel.add(ratingSlider, BorderLayout.SOUTH);

        add(ratingPanel);
    }

    // -------- Category Section --------
    private void addCategorySection(List<String> categories) {
        JPanel catPanel = new JPanel();
        catPanel.setBorder(BorderFactory.createTitledBorder("Categories"));
        catPanel.setLayout(new BoxLayout(catPanel, BoxLayout.Y_AXIS));

        for (String c : categories) {
            JCheckBox box = new JCheckBox(c);
            categoryBoxes.put(c, box);
            catPanel.add(box);

            box.addActionListener(e -> triggerFilterUpdate());
        }

        add(catPanel);
    }

    // -------- Sort Section --------
    private void addSortSection() {
        JPanel sortPanel = new JPanel();
        sortPanel.setBorder(BorderFactory.createTitledBorder("Sort By"));
        sortPanel.setLayout(new BoxLayout(sortPanel, BoxLayout.Y_AXIS));

        addRadio(sortPanel, "Price (Low - High)", SortSetting.PRICE_ASC);
        addRadio(sortPanel, "Price (High - Low)", SortSetting.PRICE_DESC);
        addRadio(sortPanel, "Rating (Low - High)", SortSetting.RATING_ASC);
        addRadio(sortPanel, "Rating (High - Low)", SortSetting.RATING_DESC);
        addRadio(sortPanel, "Name (A - Z)",     SortSetting.NAME_ASC);
        addRadio(sortPanel, "Name (Z - A)",     SortSetting.NAME_DESC);
        addRadio(sortPanel, "Date Added (New - Old)", SortSetting.DATE_DESC);
        addRadio(sortPanel, "Date Added (Old - New)", SortSetting.DATE_ASC);

        add(sortPanel);
    }

    private void addRadio(JPanel panel, String label, SortSetting setting) {
        JRadioButton rb = new JRadioButton(label);
        rb.setActionCommand(setting.name());
        sortGroup.add(rb);
        panel.add(rb);

        rb.addActionListener(e -> triggerFilterUpdate());
    }

    // -------- Main Update Trigger --------
    private void triggerFilterUpdate() {
        if (onFilterChange == null) return;

        List<String> selectedCats = categoryBoxes.entrySet().stream()
                .filter(e -> e.getValue().isSelected())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        SortSetting sortSetting =
                sortGroup.getSelection() != null
                ? SortSetting.valueOf(sortGroup.getSelection().getActionCommand())
                : SortSetting.DATE_DESC;

        FilterSettings fs = new FilterSettings(
                "",
                selectedCats,
                0.0,
                (double) priceSlider.getValue(),
                ratingCheckBox.isSelected() ? (double) ratingSlider.getValue() : 0.0,
                sortSetting
        );

        onFilterChange.accept(fs);
    }

    public double getMinPrice() {
        return 0.0;
    }

    public double getMaxPrice() {
        return priceSlider.getValue();
    }

    public double getMinRating() {
        return ratingSlider.getValue();
    }

    public List<String> getSelectedCategories() {
        return categoryBoxes.entrySet().stream()
                .filter(e -> e.getValue().isSelected())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public SortSetting getSortSetting() {
        if (sortGroup.getSelection() != null) {
            return SortSetting.valueOf(sortGroup.getSelection().getActionCommand());
        }
        return SortSetting.DATE_DESC;
    }

    public void addFilterChangeListener(Runnable callback) {
        priceSlider.addChangeListener(e -> callback.run());
        categoryBoxes.values().forEach(cb -> cb.addActionListener(e -> callback.run()));

        for (Enumeration<AbstractButton> e = sortGroup.getElements(); e.hasMoreElements();) {
            e.nextElement().addActionListener(e2 -> callback.run());
        }
    }
}
