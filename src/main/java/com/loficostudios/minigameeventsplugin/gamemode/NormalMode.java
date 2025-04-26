package com.loficostudios.minigameeventsplugin.gamemode;

import com.loficostudios.minigameeventsplugin.api.event.impl.AbstractGameMode;
import com.loficostudios.minigameeventsplugin.arena.SpawnPlatform;
import com.loficostudios.minigameeventsplugin.game.Game;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Collection;

public class NormalMode extends AbstractGameMode {

    private final Game gameManager;

    public NormalMode(Game gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public String getName() {
        return "Normal Mode";
    }

    @Override
    public Material getIcon() {
        return Material.ENDER_EYE;
    }

    @Override
    public void prepareResources(Collection<Player> participatingPlayers) {

    }

    @Override
    public void initializeCore(Collection<Player> participatingPlayers) {
        for (Player player : participatingPlayers) {
            SpawnPlatform spawnPlatform = gameManager.getArena().addSpawnPlatform(
                    player,
                    null,
                    null,
                    null,
                    null,
                    SpawnPlatform.SpawnAlgorithm.EQUAL_HEIGHT);
        }
    }

    @Override
    public void finalizeSetup(Collection<Player> participatingPlayers) {
        for (Player player : participatingPlayers) {
            SpawnPlatform spawnPlatform = gameManager.getArena().getSpawnPlatform(player);

            spawnPlatform.teleportCenter();
        }
    }

    @Override
    public void start() {
        //Common.broadcast("Started Normal");
    }

    @Override
    public void end() {

    }

    @Override
    public void reset() {

    }
}
