package com.loficostudios.minigameeventsplugin.GameEvents.PlateEvents;

import com.loficostudios.minigameeventsplugin.GameEvents.RandomPlayerSelectorEvent;
import com.loficostudios.minigameeventsplugin.Interfaces.IPlateEvent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class PlateDirtEvent extends RandomPlayerSelectorEvent implements IPlateEvent {



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
    public boolean onSelect(Player selectedPlayer) {
        getArena().getSpawnPlatform(selectedPlayer).setPlatform(Material.DIRT);
        return true;
    }

    @Override
    public void onComplete(Collection<Player> selectedPlayers) {
    }
}
