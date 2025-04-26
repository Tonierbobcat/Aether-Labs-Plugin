package com.loficostudios.minigameeventsplugin.managers;

import com.loficostudios.minigameeventsplugin.api.event.GameEvent;
import com.loficostudios.minigameeventsplugin.api.event.SelectorEvent;
import com.loficostudios.minigameeventsplugin.game.events.FallBackEvent;
import com.loficostudios.minigameeventsplugin.utils.Countdown;
import com.loficostudios.minigameeventsplugin.game.Game;
import com.loficostudios.minigameeventsplugin.AetherLabsPlugin;
import com.loficostudios.minigameeventsplugin.api.bukkit.RoundSurvivedEvent;
import com.loficostudios.minigameeventsplugin.utils.PlayerState;
import lombok.Getter;
import org.bukkit.Sound;
import org.bukkit.boss.BossBar;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashSet;
import java.util.Set;

import static com.loficostudios.minigameeventsplugin.utils.Debug.log;

public class RoundManager {

    private final Game gameManager;
    private final EventManager eventManager;

    private static final int NEXT_ROUND_RETRY_ATTEMPTS = 1;
    private static final int MAX_ROUNDS = 100;

    private BossBar progressBar = null;

    @Getter int roundsElapsed;

    //private Countdown timer;

    private Set<BukkitTask> tasks = new HashSet<>();

    public RoundManager(Game gameManager, EventManager eventManager) {
        this.gameManager = gameManager;
        this.eventManager = eventManager;
    }

    public void handleNextRound() {
        if (!gameManager.inProgress()) {
            return;
        }

        int attempts = 0;
        boolean valid = false;

        while (!valid && attempts < NEXT_ROUND_RETRY_ATTEMPTS) {
            GameEvent nextEvent = eventManager.getNextEvent();

            if (nextEvent != null) {
                valid = true;
                nextRound(nextEvent);
            }

            attempts++;
        }

        if (!valid) {
            nextRound(new FallBackEvent(gameManager));
        }
    }

    private void nextRound(GameEvent nextEvent) {

        roundsElapsed++;

        String warningMessage;

        if (nextEvent instanceof SelectorEvent<?> selectorEvent) {
            warningMessage = selectorEvent.getAmount(gameManager) + " " + nextEvent.getWarning().message();
        }
        else {
            warningMessage = nextEvent.getWarning().message();
        }

        gameManager.getStatusBar().setTitle(warningMessage);

        progressBar = gameManager.getProgressBar();

        gameManager.getPlayers().notify(NotificationType.GLOBAL, Sound.BLOCK_STONE_BUTTON_CLICK_OFF, 1, 2);

        tasks.add(new Countdown("next round",
                countdown -> {
                    progressBar.setTitle(countdown + " seconds");
                },
                () -> {
                    progressBar.setTitle("0 seconds");
                    progressBar.removeAll();
                    startRound(nextEvent);
                }).start((int) nextEvent.getWarning().time()));
    }

    private void startRound(GameEvent e) {
        eventManager.handleStart(e);
        if (!gameManager.inProgress()) {
            return;
        }

        gameManager.getPlayers().notify(NotificationType.GLOBAL, Sound.BLOCK_NOTE_BLOCK_CHIME, 1, 1);

        tasks.add(new Countdown("end round",
                (Double) -> {
                },
                this::endRound).start(e.getDuration()));
    }

    public void resetRounds() {
        eventManager.handleCancel(eventManager.getCurrentEvent());

        roundsElapsed = 0;
    }

    public void endRound() {
        cancelRound();

        if (eventManager.handleEnd(eventManager.getCurrentEvent())) {
            eventManager.handleCancel(eventManager.getCurrentEvent());
        }

        PluginManager pluginManager = AetherLabsPlugin.getInstance()
                .getServer()
                .getPluginManager();

        gameManager.getPlayers()
                .getPlayersInGame(PlayerState.ALIVE)
                .forEach(player -> pluginManager.callEvent(new RoundSurvivedEvent(player)));

        log("current round: " + roundsElapsed + " maxRounds: " + MAX_ROUNDS);

        handleNextRound();
    }

    public void cancelRound() {
        tasks.forEach(BukkitTask::cancel);

        if (this.progressBar != null)
            progressBar.removeAll();
    }
}
