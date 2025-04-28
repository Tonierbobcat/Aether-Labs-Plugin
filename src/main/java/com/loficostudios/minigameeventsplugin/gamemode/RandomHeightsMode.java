package com.loficostudios.minigameeventsplugin.gamemode;

import com.loficostudios.minigameeventsplugin.api.event.impl.AbstractGameMode;
import com.loficostudios.minigameeventsplugin.game.Game;
import com.loficostudios.minigameeventsplugin.game.arena.SpawnPlatform;
import com.loficostudios.minigameeventsplugin.game.arena.SpawnPlatformGenerator;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Collection;

public class RandomHeightsMode extends AbstractGameMode {
    public RandomHeightsMode() {
    }

    @Override
    public String getName() {
        return "Random Heights Mode";
    }

    @Override
    public Material getIcon() {
        return Material.STONE_BRICKS;
    }

    @Override
    public void start() {

    }

    @Override
    public void reset() {

    }

    @Override
    public void end() {

    }

    @Override
    public void prepareResources(Game game, Collection<Player> participatingPlayers) {

    }

    @Override
    public void initializeCore(Game game, Collection<Player> participatingPlayers) {
    }

    @Override
    public void finalizeSetup(Game game, Collection<Player> participatingPlayers) {
        for (Player player : participatingPlayers) {
            SpawnPlatform platform = game.getArena().addSpawnPlatform(
                    player,
                    null,
                    null,
                    null,
                    plat -> plat.teleport(player),
                    SpawnPlatformGenerator.SpawnAlgorithm.RANDOM_HEIGHT);
        }
    }
}
