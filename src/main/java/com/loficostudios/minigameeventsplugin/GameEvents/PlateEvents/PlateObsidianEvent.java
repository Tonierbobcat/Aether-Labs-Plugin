package com.loficostudios.minigameeventsplugin.GameEvents.PlateEvents;

import com.loficostudios.minigameeventsplugin.GameEvents.EventType;
import com.loficostudios.minigameeventsplugin.GameEvents.RandomPlatformSelectorEvent;
import com.loficostudios.minigameeventsplugin.GameEvents.RandomPlayerSelectorEvent;
import com.loficostudios.minigameeventsplugin.GameArena.SpawnPlatform;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class PlateObsidianEvent extends RandomPlatformSelectorEvent {

    @Override
    public boolean onSelect(SpawnPlatform selectedObject) {
        Player player = selectedObject.getPlayer();

        if (player != null) {
            selectedObject.recreate(Material.OBSIDIAN);
            return true;
        }

        return true;
    }


    @Override
    public @NotNull String getName() {
        return "Obsidian Event";
    }

    @Override
    public @NotNull String getWarningMessage() {
        return "plate(s) will become solid";
    }

    @Override
    public @NotNull Material getDisplayMaterial() {
        return Material.OBSIDIAN;
    }


    @Override
    public Integer getMin() {
        return 2;
    }

    @Override
    public Integer getMax() {
        return 3;
    }
}
