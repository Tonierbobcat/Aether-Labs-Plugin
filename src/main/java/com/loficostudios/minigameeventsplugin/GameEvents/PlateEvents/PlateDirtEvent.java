package com.loficostudios.minigameeventsplugin.GameEvents.PlateEvents;

import com.loficostudios.minigameeventsplugin.GameArena.SpawnPlatform;
import com.loficostudios.minigameeventsplugin.GameEvents.EventType;
import com.loficostudios.minigameeventsplugin.GameEvents.RandomPlatformSelectorEvent;
import com.loficostudios.minigameeventsplugin.GameEvents.RandomPlayerSelectorEvent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

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
