package com.olehbondaruk;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class App {
    static Logger logger = LogManager.getLogger(App.class);
    public static void main(String[] args) {
        System.out.println("Ahh... shi... here we go again.");
        logger.debug("Hello from logger!");
    }
}
