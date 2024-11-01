package com.loficostudios.minigameeventsplugin.GameEvents.PlateEvents;

import com.loficostudios.minigameeventsplugin.GameArena.SpawnPlatform;
import com.loficostudios.minigameeventsplugin.GameEvents.BaseEvent;
import com.loficostudios.minigameeventsplugin.GameEvents.EventType;
import com.loficostudios.minigameeventsplugin.GameEvents.RandomPlatformSelectorEvent;
import com.loficostudios.minigameeventsplugin.GameEvents.RandomPlayerSelectorEvent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class PlateInvisibleEvent extends RandomPlatformSelectorEvent {
    @Override
    protected boolean onSelect(SpawnPlatform selectedObject) {

        Player player = selectedObject.getPlayer();

        if (player != null) {
            selectedObject.setPlatform(Material.BARRIER);
            return true;
        }

        return true;
    }


    @Override
    public @NotNull String getName() {
        return "Invisible Plate Event";
    }

    @Override
    public @NotNull String getWarningMessage() {
        return "plate(s) will become completely transparent.";
    }

    @Override
    public @NotNull Material getDisplayMaterial() {
        return Material.GLASS;
    }

    @Override
    public Integer getMin() {
        return 1;
    }

    @Override
    public Integer getMax() {
        return 3;
    }
}
