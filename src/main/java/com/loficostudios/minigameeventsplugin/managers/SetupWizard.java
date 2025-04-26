package com.loficostudios.minigameeventsplugin.managers;

import com.loficostudios.minigameeventsplugin.utils.Countdown;
import com.loficostudios.minigameeventsplugin.arena.GameArena;
import com.loficostudios.minigameeventsplugin.game.Game;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import java.util.Collection;

public class SetupWizard {

    private final Game gameManager;
    private final GameArena arena;

    public SetupWizard(Game gameManager) {
        this.gameManager = gameManager;
        this.arena = gameManager.getArena();
    }

    public void setup(com.loficostudios.minigameeventsplugin.gamemode.GameMode mode, Collection<Player> participatingPlayers) {
        gameManager.getPlayers()
                .initializePlayers(participatingPlayers);

        BossBar progressBar = gameManager.getProgressBar();
        progressBar.setTitle("-0");

        new Countdown("setup (" + mode.getName().toLowerCase().replace(" ", "_") + ")", countDown -> {
                //First Round Of Setup; //for expensiveTasks
                if (countDown == 5) {
                    arena.clear();
                    mode.prepareResources(participatingPlayers);
                }
                //Second Round Of Setup; //for general tasks
                else if (countDown == 3) {
                    mode.initializeCore(participatingPlayers);
                }
                //Final Round Of Setup; //cleanUp / right before start game
                if (countDown == 2) {
                    mode.finalizeSetup(participatingPlayers);
                }
        }, () -> {
            gameManager.startGame();
            gameManager.getArena().startLevelFillTask(mode.getFillMaterial(), mode.getFillSpeed());
        }).start(5);
    }
}
