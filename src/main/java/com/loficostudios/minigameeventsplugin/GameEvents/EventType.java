package com.loficostudios.minigameeventsplugin.GameEvents;

import lombok.Getter;
import org.bukkit.Material;

public enum EventType {

    DEATH(Material.OMINOUS_TRIAL_KEY),
    PLAYER(Material.PLAYER_HEAD),
    PLATE(Material.STONE_PRESSURE_PLATE),
    GLOBAL(Material.COMMAND_BLOCK);

    private final Material icon;

    EventType(Material icon) {
        this.icon = icon;
    }

    public Material getIcon() {
        return icon;
    }
}
