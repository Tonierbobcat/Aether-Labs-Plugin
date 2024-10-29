package com.loficostudios.minigameeventsplugin.Utils;

import org.bukkit.Bukkit;

import java.util.logging.Level;
import java.util.logging.Logger;

import static com.loficostudios.minigameeventsplugin.RandomEventsPlugin.DEBUG_ENABLED;

public class DebugUtil {




    private static void send(Level level, String msg) {
        if (!DEBUG_ENABLED)
            return;
        //MiniGameEventsPlugin plugin = MiniGameEventsPlugin.getInstance();

        //String pluginName = "[" + plugin.getName() + "]";
        String pluginName = "[MiniGameEventsPlugin]";

        Logger logger = Bukkit.getLogger();
        logger.log(level, pluginName + " " +  msg);

    }

    public static void debug(String msg) {


        send(Level.INFO, msg);
    }

    public static void debugWarning(String msg) {

        send(Level.WARNING, msg);
    }

    public static void debugError(String msg) {

        send(Level.SEVERE, msg);
    }
}
