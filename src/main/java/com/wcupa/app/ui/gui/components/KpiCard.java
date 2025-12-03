package com.wcupa.app.ui.gui.components;

import javax.swing.*;
import java.awt.*;

public class KpiCard extends JPanel {

    public KpiCard(String title, String value) {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY, 1),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 14f));

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(valueLabel.getFont().deriveFont(Font.BOLD, 18f));
        valueLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        add(titleLabel, BorderLayout.NORTH);
        add(valueLabel, BorderLayout.SOUTH);
    }
}
