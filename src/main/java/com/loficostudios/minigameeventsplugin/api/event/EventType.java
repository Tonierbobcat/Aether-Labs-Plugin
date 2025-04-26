package com.loficostudios.minigameeventsplugin.api.event;

import org.bukkit.Material;

public enum EventType {

    DEATH(Material.OMINOUS_TRIAL_KEY),
    PLAYER(Material.TOTEM_OF_UNDYING),
    PLATE(Material.STONE_BRICKS),
    GLOBAL(Material.RECOVERY_COMPASS);

    private final Material icon;

    EventType(Material icon) {
        this.icon = icon;
    }

    public Material getIcon() {
        return icon;
    }
}
