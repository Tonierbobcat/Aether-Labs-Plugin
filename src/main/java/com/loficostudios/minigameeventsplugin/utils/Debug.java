package com.loficostudios.minigameeventsplugin.utils;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;


import java.util.logging.Level;
import java.util.logging.Logger;

import static com.loficostudios.minigameeventsplugin.AetherLabsPlugin.DEBUG_ENABLED;

@UtilityClass
public class Debug {

    private static void send(Level level, String msg) {
        if (!DEBUG_ENABLED)
            return;
        //MiniGameEventsPlugin plugin = MiniGameEventsPlugin.getInstance();

        //String pluginName = "[" + plugin.getName() + "]";
        String pluginName = "[MiniGameEventsPlugin]";

        Logger logger = Bukkit.getLogger();
        logger.log(level, pluginName + " " +  msg);

    }

    public static void log(String msg) {


        send(Level.INFO, msg);
    }

    public static void logWarning(String msg) {

        send(Level.WARNING, msg);
    }

    public static void logError(String msg) {

        send(Level.SEVERE, msg);
    }
}
