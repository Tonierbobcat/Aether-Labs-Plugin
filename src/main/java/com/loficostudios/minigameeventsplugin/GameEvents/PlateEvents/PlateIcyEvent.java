package com.loficostudios.minigameeventsplugin.GameEvents.PlateEvents;

import com.loficostudios.minigameeventsplugin.GameArena.SpawnPlatform;
import com.loficostudios.minigameeventsplugin.GameEvents.EventType;
import com.loficostudios.minigameeventsplugin.GameEvents.RandomPlatformSelectorEvent;
import com.loficostudios.minigameeventsplugin.GameEvents.RandomPlayerSelectorEvent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class PlateIcyEvent extends RandomPlatformSelectorEvent {
    @Override
    public @NotNull EventType getType() {
        return EventType.PLATE;
    }




    @Override
    public @NotNull String getName() {
        return "Plate Icy Event";
    }

    @Override
    public @NotNull String getWarningMessage() {
        return "plate(s) will be a bit slippery.";
    }

    @Override
    public @NotNull Material getDisplayMaterial() {
        return Material.ICE;
    }

    @Override
    public Integer getMin() {
        return 1;
    }

    @Override
    public Integer getMax() {
        return 3;
    }

    @Override
    protected boolean onSelect(SpawnPlatform selectedObject) {

        Player player = selectedObject.getPlayer();

        if (player != null) {
            selectedObject.setPlatform(Material.ICE);
            return true;
        }

        return true;
    }
}
