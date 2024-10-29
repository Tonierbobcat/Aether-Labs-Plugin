package com.loficostudios.minigameeventsplugin.GameEvents.PlateEvents;

import com.loficostudios.minigameeventsplugin.GameEvents.BaseEvent;
import com.loficostudios.minigameeventsplugin.Interfaces.IPlateEvent;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

public class PlateExpandEvent extends BaseEvent implements IPlateEvent {

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
