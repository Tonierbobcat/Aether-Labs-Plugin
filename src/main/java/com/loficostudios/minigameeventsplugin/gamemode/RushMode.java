package com.loficostudios.minigameeventsplugin.gamemode;

import org.bukkit.Material;

public class RushMode extends NormalMode{
    @Override
    public String getName() {
        return "Rush Mode";
    }

    @Override
    public Material getIcon() {
        return Material.FEATHER;
    }

    @Override
    public int getFillSpeed() {
        return Math.round(DEFAULT_FILL_SPEED * 7.5f);
    }
}
