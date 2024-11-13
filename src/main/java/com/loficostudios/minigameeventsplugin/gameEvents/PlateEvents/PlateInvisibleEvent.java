package com.loficostudios.minigameeventsplugin.gameEvents.PlateEvents;

import com.loficostudios.minigameeventsplugin.arena.SpawnPlatform;
import com.loficostudios.minigameeventsplugin.api.RandomPlatformSelectorEvent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

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
