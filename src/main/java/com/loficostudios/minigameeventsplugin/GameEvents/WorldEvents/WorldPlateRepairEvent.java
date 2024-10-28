package com.loficostudios.minigameeventsplugin.GameEvents.WorldEvents;

import com.loficostudios.minigameeventsplugin.GameEvents.BaseEvent;
import org.jetbrains.annotations.NotNull;

public class WorldPlateRepairEvent extends BaseEvent {



    @Override
    public void start() {
        getArena().getSpawnPlatforms().forEach(spawnPlatform -> {
            spawnPlatform.recreate(spawnPlatform.getMaterial());
        });
    }

    @Override
    public @NotNull String getName() {
        return "Plate Repair Event";
    }

    @Override
    public @NotNull String warningMessage() {
        return "All plates will be repaired.";
    }

}
