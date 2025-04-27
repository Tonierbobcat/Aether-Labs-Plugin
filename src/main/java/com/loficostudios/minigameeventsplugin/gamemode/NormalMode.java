package com.loficostudios.minigameeventsplugin.gamemode;

import com.loficostudios.minigameeventsplugin.api.event.impl.AbstractGameMode;
import com.loficostudios.minigameeventsplugin.arena.SpawnPlatform;
import com.loficostudios.minigameeventsplugin.arena.SpawnPlatformGenerator;
import com.loficostudios.minigameeventsplugin.game.Game;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Collection;

public class NormalMode extends AbstractGameMode {


    public NormalMode() {
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
    public void prepareResources(Game game, Collection<Player> participatingPlayers) {

    }

    @Override
    public void initializeCore(Game game, Collection<Player> participatingPlayers) {
        for (Player player : participatingPlayers) {
            SpawnPlatform spawnPlatform = game.getArena().addSpawnPlatform(
                    player,
                    null,
                    null,
                    null,
                    null,
                    SpawnPlatformGenerator.SpawnAlgorithm.EQUAL_HEIGHT);
        }
    }

    @Override
    public void finalizeSetup(Game game, Collection<Player> participatingPlayers) {
        for (Player player : participatingPlayers) {
            SpawnPlatform spawnPlatform = game.getArena().getSpawnPlatform(player);

            spawnPlatform.teleport(player);
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
