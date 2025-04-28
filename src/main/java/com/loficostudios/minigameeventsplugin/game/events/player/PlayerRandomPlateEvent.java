package com.loficostudios.minigameeventsplugin.game.events.player;

import com.loficostudios.minigameeventsplugin.api.PlayerSelectorEvent;
import com.loficostudios.minigameeventsplugin.game.Game;
import com.loficostudios.minigameeventsplugin.game.arena.SpawnPlatform;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ThreadLocalRandom;

public class PlayerRandomPlateEvent extends PlayerSelectorEvent {

    public PlayerRandomPlateEvent() {
        super("Random Platform", Material.ENDER_EYE, 2, 3);
    }

    @Override
    public boolean onSelect(Game game, Player selectedPlayer) {
        var plates = game.getArena().getSpawnPlatforms().toArray(SpawnPlatform[]::new);
        if (plates == null || plates.length < 1)
            return false;
        SpawnPlatform platform = plates[ThreadLocalRandom.current().nextInt(plates.length)];

        if (platform == null)
            return false;

        selectedPlayer.teleport(platform.getTeleportLocation());

        return true;
    }

    @Override
    public @NotNull String getWarningMessage() {
        return "player(s) will teleport to a random plate.";
    }
}
