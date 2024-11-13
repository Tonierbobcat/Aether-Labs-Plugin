package com.loficostudios.minigameeventsplugin.gameEvents.PlateEvents;

import com.loficostudios.minigameeventsplugin.arena.SpawnPlatform;
import com.loficostudios.minigameeventsplugin.api.RandomPlatformSelectorEvent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PlateDirtEvent extends RandomPlatformSelectorEvent {

    @Override
    public @NotNull String getName() {
        return "Random Dirt Platform";
    }

    @Override
    public @NotNull String getWarningMessage() {
        return "plate(s) will get a bit dirty";
    }

    @Override
    public @NotNull Material getDisplayMaterial() {
        return Material.DIRT;
    }

    @Override
    public Integer getMin() {
        return 2;
    }

    @Override
    public Integer getMax() {
        return 3;
    }


    @Override
    protected boolean onSelect(SpawnPlatform selectedObject) {

        Player player = selectedObject.getPlayer();

        if (player != null) {
            selectedObject.setPlatform(Material.DIRT);
            return true;
        }

        return true;
    }
}
