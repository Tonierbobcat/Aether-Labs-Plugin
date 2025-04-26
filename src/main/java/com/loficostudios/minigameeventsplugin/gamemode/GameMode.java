package com.loficostudios.minigameeventsplugin.gamemode;

import org.bukkit.Material;

public interface GameMode extends IInitializationReceiver {
    Material getFillMaterial();

    int getFillSpeed();

    String getName();

    Material getIcon();

    void start();

    void end();

    void reset();
}
