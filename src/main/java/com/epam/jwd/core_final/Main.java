package com.epam.jwd.core_final;

import com.epam.jwd.core_final.context.Application;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        try {
            Application.start();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}