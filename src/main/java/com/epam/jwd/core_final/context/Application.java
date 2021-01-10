package com.epam.jwd.core_final.context;

import com.epam.jwd.core_final.context.impl.ApplicationMenuImpl;
import com.epam.jwd.core_final.context.impl.NassaContext;
import com.epam.jwd.core_final.exception.InvalidStateException;
import com.epam.jwd.core_final.util.PropertyReaderUtil;

import java.util.function.Supplier;

public interface Application {

    static ApplicationMenu start() throws InvalidStateException {
        PropertyReaderUtil.loadProperties();
        ApplicationMenu applicationMenu = ApplicationMenuImpl.createInstance();
        final Supplier<ApplicationContext> applicationContextSupplier = applicationMenu::getApplicationContext; // todo
        final NassaContext nassaContext = NassaContext.createInstance();

        nassaContext.init();
        return applicationMenu;
    }
}
