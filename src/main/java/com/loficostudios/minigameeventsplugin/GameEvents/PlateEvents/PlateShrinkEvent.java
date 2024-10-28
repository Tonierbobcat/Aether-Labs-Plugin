package com.loficostudios.minigameeventsplugin.GameEvents.PlateEvents;

import com.loficostudios.minigameeventsplugin.GameEvents.BaseEvent;
import org.jetbrains.annotations.NotNull;

public class PlateShrinkEvent extends BaseEvent {


    @Override
    public @NotNull String getName() {
        return "Platform Shrink Event";
    }

    @Override
    public @NotNull String warningMessage() {
        return "All plates will shrink";
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
