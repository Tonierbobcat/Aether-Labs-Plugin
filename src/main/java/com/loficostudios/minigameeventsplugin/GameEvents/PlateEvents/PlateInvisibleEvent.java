package com.loficostudios.minigameeventsplugin.GameEvents.PlateEvents;

import com.loficostudios.minigameeventsplugin.GameArena.SpawnPlatform;
import com.loficostudios.minigameeventsplugin.GameEvents.RandomPlayerSelectorEvent;
import com.loficostudios.minigameeventsplugin.Interfaces.IPlateEvent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class PlateInvisibleEvent extends RandomPlayerSelectorEvent implements IPlateEvent {
    @Override
    public boolean onSelect(Player selectedPlayer) {
        SpawnPlatform spawnPlatform = getArena().getSpawnPlatform(selectedPlayer);

        spawnPlatform.setPlatform(Material.BARRIER);

        return true;
    }

    @Override
    public void onComplete(Collection<Player> selectedPlayers) {

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
