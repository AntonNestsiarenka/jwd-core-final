package com.epam.jwd.core_final.context.impl;

import com.epam.jwd.core_final.context.ApplicationContext;
import com.epam.jwd.core_final.context.ApplicationMenu;

public class ApplicationMenuImpl implements ApplicationMenu {

    private static ApplicationMenuImpl instance;

    private ApplicationMenuImpl() {

    }

    public static ApplicationMenuImpl createInstance() {
        if (instance == null) {
            instance = new ApplicationMenuImpl();
        }
        return instance;
    }

    @Override
    public ApplicationContext getApplicationContext() {
        return NassaContext.createInstance();
    }


}
