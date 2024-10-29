package com.loficostudios.minigameeventsplugin.GameEvents.PlateEvents;

import com.loficostudios.minigameeventsplugin.GameArena.SpawnPlatform;
import com.loficostudios.minigameeventsplugin.GameEvents.RandomPlayerSelectorEvent;
import com.loficostudios.minigameeventsplugin.Interfaces.IPlateEvent;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class PlateZombieEvent extends RandomPlayerSelectorEvent implements IPlateEvent {
    @Override
    public boolean onSelect(Player selectedPlayer) {

        SpawnPlatform spawnPlatform = getArena().getSpawnPlatform(selectedPlayer);

        if (spawnPlatform == null) {
            return false;
        }
        getArena().spawnMob(EntityType.ZOMBIE, spawnPlatform.getTeleportLocation());

        return true;
    }

    @Override
    public void onComplete(Collection<Player> selectedPlayers) {

    }

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
}
