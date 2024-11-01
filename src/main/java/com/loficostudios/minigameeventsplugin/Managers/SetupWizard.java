package com.loficostudios.minigameeventsplugin.Managers;

import com.loficostudios.melodyapi.utils.SimpleColor;
import com.loficostudios.minigameeventsplugin.Countdown.Countdown;
import com.loficostudios.minigameeventsplugin.GameArena.GameArena;
import com.loficostudios.minigameeventsplugin.GameTypes.BaseGameMode;
import com.loficostudios.minigameeventsplugin.Managers.GameManager.GameManager;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import java.util.Collection;

public class SetupWizard {

    private final GameManager gameManager;
    private final GameArena arena;

    public SetupWizard(GameManager gameManager) {
        this.gameManager = gameManager;
        this.arena = gameManager.getArena();
    }

    public void setup(BaseGameMode mode, Collection<Player> participatingPlayers) {
        gameManager.getPlayerManager()
                .initializePlayers(participatingPlayers);

        BossBar progressBar = gameManager.getProgressBar();
        progressBar.setTitle(SimpleColor.deserialize(
                "-0"
        ));

        new Countdown("setup (" + mode.getId() + ")", countDown -> {

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
            gameManager.getArena().startLevelFillTask(
                    mode.getFillMaterial(),
                    mode.getFillSpeed());
        }).start(5);
    }
}
