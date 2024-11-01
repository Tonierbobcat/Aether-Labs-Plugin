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

public class PlateZombieEvent extends RandomPlatformSelectorEvent {

    @Override
    public @NotNull String getName() {
        return "Plate zombie event";
    }

    @Override
    public @NotNull String getWarningMessage() {
        return "plate(s) will get a pet zombie! :D";
    }

    @Override
    public @NotNull Material getDisplayMaterial() {
        return Material.ZOMBIE_SPAWN_EGG;
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
            getArena().spawnMob(EntityType.ZOMBIE, selectedObject.getTeleportLocation());
        }

        return true;
    }
}
