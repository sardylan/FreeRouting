package org.thehellnet.tools.freerouting;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thehellnet.tools.freerouting.gui.MainApplication;

/**
 * Created by sardylan on 19/12/16.
 */
public final class FreeRouting {

    private static final Logger logger = LoggerFactory.getLogger(FreeRouting.class);

    public static void main(String[] args) {
        logger.info("Starting Main Application");
        MainApplication.main(args);
    }
}
