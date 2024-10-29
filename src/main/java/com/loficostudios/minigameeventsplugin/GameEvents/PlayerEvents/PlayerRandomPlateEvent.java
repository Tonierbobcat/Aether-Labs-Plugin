package com.loficostudios.minigameeventsplugin.GameEvents.PlayerEvents;

import com.loficostudios.minigameeventsplugin.GameArena.SpawnPlatform;
import com.loficostudios.minigameeventsplugin.GameEvents.RandomPlayerSelectorEvent;
import com.loficostudios.minigameeventsplugin.Interfaces.IPlayerEvent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class PlayerRandomPlateEvent extends RandomPlayerSelectorEvent implements IPlayerEvent {

    Random random = new Random();

    List<SpawnPlatform> spawnPlatforms = null;

    @Override
    public void load() {
        super.load();

        spawnPlatforms = new ArrayList<>(getArena().getSpawnPlatforms());
    }

    @Override
    public boolean onSelect(Player selectedPlayer) {
        if (spawnPlatforms == null || spawnPlatforms.isEmpty())
            return false;
        SpawnPlatform platform = spawnPlatforms.get(random.nextInt(spawnPlatforms.size()));

        if (platform == null)
            return false;

        selectedPlayer.teleport(platform.getTeleportLocation());

        return true;
    }

    @Override
    public void onComplete(Collection<Player> selectedPlayers) {

    }

    @Override
    public @NotNull String getName() {
        return "Random Platform";
    }

    @Override
    public @NotNull String getWarningMessage() {
        return "player(s) will teleport to a random plate.";
    }

    @Override
    public @NotNull Material getDisplayMaterial() {
        return Material.ENDER_EYE;
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
