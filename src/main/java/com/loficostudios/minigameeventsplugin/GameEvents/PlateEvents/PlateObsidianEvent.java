package com.loficostudios.minigameeventsplugin.GameEvents.PlateEvents;

import com.loficostudios.minigameeventsplugin.GameEvents.RandomPlayerSelectorEvent;
import com.loficostudios.minigameeventsplugin.GameArena.SpawnPlatform;
import com.loficostudios.minigameeventsplugin.Interfaces.IPlateEvent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class PlateObsidianEvent extends RandomPlayerSelectorEvent implements IPlateEvent {

    @Override
    public boolean onSelect(Player selectedPlayer) {
        SpawnPlatform platform = getArena().getSpawnPlatform(selectedPlayer);

        platform.recreate(Material.OBSIDIAN);

        return true;
    }

    @Override
    public void onComplete(Collection<Player> selectedPlayers) {



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
