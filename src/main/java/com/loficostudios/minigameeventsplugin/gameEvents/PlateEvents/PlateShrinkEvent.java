package com.loficostudios.minigameeventsplugin.gameEvents.PlateEvents;

import com.loficostudios.minigameeventsplugin.api.BaseEvent;

import com.loficostudios.minigameeventsplugin.gameEvents.EventType;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

public class PlateShrinkEvent extends BaseEvent {

    @Override
    public @NotNull EventType getType() {
        return EventType.PLATE;
    }

    @Override
    public @NotNull String getName() {
        return "Platform Shrink Event";
    }

    @Override
    public @NotNull String getWarningMessage() {
        return "All plates will shrink";
    }

    @Override
    public @NotNull Material getDisplayMaterial() {
        return Material.CRACKED_STONE_BRICKS;
    }

    @Override
    public void start() {

        getArena().getSpawnPlatforms().forEach(spawnPlatform -> {
            if (!spawnPlatform.shrink(1)) {
                getArena().removeSpawnPlatform(spawnPlatform, false);
            }
        });
    }
}
