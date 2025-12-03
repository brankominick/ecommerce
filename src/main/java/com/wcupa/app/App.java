// src/main/java/com/wcupa/app/App.java
package com.wcupa.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wcupa.app.session.Session;

import javax.swing.SwingUtilities;
import com.wcupa.app.ui.gui.frames.MainFrame;



public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}