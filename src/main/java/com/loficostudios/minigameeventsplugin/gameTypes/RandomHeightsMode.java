package com.loficostudios.minigameeventsplugin.gameTypes;

import com.loficostudios.minigameeventsplugin.api.BaseGameMode;
import com.loficostudios.minigameeventsplugin.arena.SpawnPlatform;
import com.loficostudios.minigameeventsplugin.managers.GameManager.GameManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Collection;

public class RandomHeightsMode extends BaseGameMode {


    GameManager gameManager;
    public RandomHeightsMode(GameManager gameManager) {
        this.gameManager = gameManager;
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
    public void prepareResources(Collection<Player> participatingPlayers) {

    }

    @Override
    public void initializeCore(Collection<Player> participatingPlayers) {
//        gameManager.getArena()
//                .InitializeSpawnPlatforms(participatingPlayers, SpawnPlatform.SpawnAlgorithm.RANDOM_HEIGHT);

    }

    @Override
    public void finalizeSetup(Collection<Player> participatingPlayers) {
        for (Player player : participatingPlayers) {
            SpawnPlatform spawnPlatform = gameManager.getArena().addSpawnPlatform(
                    player,
                    null,
                    null,
                    null,
                    SpawnPlatform::teleportCenter,
                    SpawnPlatform.SpawnAlgorithm.RANDOM_HEIGHT);
        }


//
//        for (int i = 0; i < 20; i++) {
//
//            gameManager.getArena().addSpawnPlatform(
//                    null,
//                    null,
//                    null,
//                    null,
//                    SpawnPlatform.SpawnAlgorithm.RANDOM_HEIGHT);
//
//        }


    }
}
