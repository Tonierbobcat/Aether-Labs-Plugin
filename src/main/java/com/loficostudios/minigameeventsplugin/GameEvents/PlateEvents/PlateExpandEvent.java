package com.loficostudios.minigameeventsplugin.GameEvents.PlateEvents;

import com.loficostudios.minigameeventsplugin.GameEvents.BaseEvent;
import org.jetbrains.annotations.NotNull;

public class PlateExpandEvent extends BaseEvent {

    @Override
    public @NotNull String getName() {
        return "Expand Event";
    }

    @Override
    public @NotNull String warningMessage() {
        return "All plates will expand";
    }



    @Override
    public void start() {
        getArena().getSpawnPlatforms().forEach(spawnPlatform -> spawnPlatform.expand(1));
    }
}
