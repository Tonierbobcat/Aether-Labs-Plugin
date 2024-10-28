package com.loficostudios.minigameeventsplugin.Utils;

import com.loficostudios.melodyapi.utils.SimpleColor;
import com.loficostudios.minigameeventsplugin.MiniGameEventsPlugin;

import java.util.logging.Level;
import java.util.logging.Logger;

public class DebugUtil {

    private static final boolean DEBUG_ENABLED = true;


    private static void send(Level level, String msg) {
        MiniGameEventsPlugin plugin = MiniGameEventsPlugin.getInstance();

        String pluginName = "[" + plugin.getName() + "]";

        Logger logger = plugin.getServer().getLogger();
        logger.log(level, pluginName + " " +  msg);

    }

    public static void debug(String msg) {
        if (!DEBUG_ENABLED)
            return;

        send(Level.INFO, msg);
    }

    public static void debugWarning(String msg) {
        if (!DEBUG_ENABLED)
            return;

        send(Level.WARNING, msg);
    }

    public static void debugError(String msg) {
        if (!DEBUG_ENABLED)
            return;

        send(Level.SEVERE, msg);
    }
}
