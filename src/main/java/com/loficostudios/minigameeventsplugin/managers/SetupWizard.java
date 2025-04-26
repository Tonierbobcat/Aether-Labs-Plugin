package com.loficostudios.minigameeventsplugin.managers;

import com.loficostudios.minigameeventsplugin.arena.GameArena;
import com.loficostudios.minigameeventsplugin.game.Game;
import com.loficostudios.minigameeventsplugin.utils.Countdown;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import java.util.Collection;

public class SetupWizard {

    private final Game game;
    private final GameArena arena;

    public SetupWizard(Game gameManager) {
        this.game = gameManager;
        this.arena = gameManager.getArena();
    }

    public void setup(com.loficostudios.minigameeventsplugin.gamemode.GameMode mode, Collection<Player> participatingPlayers) {
        game.getPlayers()
                .initializePlayers(participatingPlayers);

        BossBar progressBar = game.getProgressBar();
        progressBar.setTitle("-1");

        new Countdown("setup (" + mode.getName().toLowerCase().replace(" ", "_") + ")", countDown -> {
                //First Round Of Setup; //for expensiveTasks
                if (countDown == 5) {
                    arena.clear();
                    mode.prepareResources(game, participatingPlayers);
                }
                //Second Round Of Setup; //for general tasks
                else if (countDown == 3) {
                    mode.initializeCore(game, participatingPlayers);
                }
                //Final Round Of Setup; //cleanUp / right before start game
                if (countDown == 2) {
                    mode.finalizeSetup(game, participatingPlayers);
                }
        }, () -> {
            game.startGame();
            game.getArena().startFillTask(mode.getFillMaterial(), mode.getFillSpeed());
        }).start(5);
    }
}
