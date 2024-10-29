package com.loficostudios.minigameeventsplugin.GameEvents.WorldEvents;

import com.loficostudios.minigameeventsplugin.GameEvents.BaseEvent;
import com.loficostudios.minigameeventsplugin.Interfaces.IWorldEvent;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

public class WorldPlateRepairEvent extends BaseEvent implements IWorldEvent {



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
    public @NotNull String getWarningMessage() {
        return "All plates will be repaired.";
    }

    @Override
    public @NotNull Material getDisplayMaterial() {
        return Material.STONE_BRICKS;
    }

}
