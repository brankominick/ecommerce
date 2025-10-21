// src/main/java/com/wcupa/app/App.java
package com.wcupa.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wcupa.app.session.Session;
import com.wcupa.app.ui.cli.CLI;



public class App {
    public static void main(String[] args) {
        System.out.println("App started!");
        CLI cli = new CLI();

        cli.start();
    }
}