package com.loficostudios.minigameeventsplugin.api.event.impl;

import com.loficostudios.minigameeventsplugin.gamemode.GameMode;
import org.bukkit.Material;

public abstract class AbstractGameMode implements GameMode {

    protected static final int DEFAULT_FILL_SPEED = 1;
    protected static final Material DEFAULT_FILL_MATERIAL = Material.LAVA;

    @Override
    public Material getFillMaterial() {
        return DEFAULT_FILL_MATERIAL;
    }

    @Override
    public int getFillSpeed() {
        return DEFAULT_FILL_SPEED;
    }
}
