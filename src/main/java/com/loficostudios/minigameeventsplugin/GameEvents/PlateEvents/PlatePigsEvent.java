package com.loficostudios.minigameeventsplugin.GameEvents.PlateEvents;

import com.loficostudios.minigameeventsplugin.GameArena.SpawnPlatform;
import com.loficostudios.minigameeventsplugin.GameEvents.EventType;
import com.loficostudios.minigameeventsplugin.GameEvents.RandomPlatformSelectorEvent;
import com.loficostudios.minigameeventsplugin.GameEvents.RandomPlayerSelectorEvent;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class PlatePigsEvent extends RandomPlatformSelectorEvent {
    @Override
    public boolean onSelect(SpawnPlatform selectedObject) {
        Player player = selectedObject.getPlayer();

        if (player != null) {
            for (int i = 0; i < 4; i++) {
                getArena().spawnMob(EntityType.PIG, selectedObject.getTeleportLocation());
            }

            return true;
        }

        return true;
    }

    @Override
    public @NotNull String getName() {
        return "Pig event";
    }

    @Override
    public @NotNull String getWarningMessage() {
        return "plate(s) will get raided by pigs!";
    }

    @Override
    public @NotNull Material getDisplayMaterial() {
        return Material.PIG_SPAWN_EGG;
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
