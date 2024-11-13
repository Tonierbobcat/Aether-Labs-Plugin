package com.loficostudios.minigameeventsplugin.gameEvents.PlateEvents;

import com.loficostudios.minigameeventsplugin.api.BaseEvent;
import com.loficostudios.minigameeventsplugin.gameEvents.EventType;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

public class PlateExpandEvent extends BaseEvent {

    @Override
    public @NotNull EventType getType() {
        return EventType.PLATE;
    }

    @Override
    public @NotNull String getName() {
        return "Expand Event";
    }

    @Override
    public @NotNull String getWarningMessage() {
        return "All plates will expand";
    }

    @Override
    public @NotNull Material getDisplayMaterial() {
        return Material.STONE_BRICKS;
    }


    @Override
    public void start() {
        getArena().getSpawnPlatforms().forEach(spawnPlatform -> spawnPlatform.expand(1));
    }
}
