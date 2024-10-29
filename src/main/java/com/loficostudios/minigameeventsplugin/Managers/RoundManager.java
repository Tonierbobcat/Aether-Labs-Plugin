package com.loficostudios.minigameeventsplugin.Managers;

import com.loficostudios.minigameeventsplugin.GameEvents.BaseEvent;
import com.loficostudios.minigameeventsplugin.GameEvents.RandomPlayerSelectorEvent;
import com.loficostudios.minigameeventsplugin.GameEvents.FallBackEvent;
import com.loficostudios.minigameeventsplugin.Countdown.Countdown;
import com.loficostudios.minigameeventsplugin.Managers.GameManager.GameManager;
import com.loficostudios.minigameeventsplugin.Managers.PlayerManager.NotificationType;
import com.loficostudios.minigameeventsplugin.RandomEventsPlugin;
import com.loficostudios.minigameeventsplugin.BukkitEvents.RoundSurvivedEvent;
import com.loficostudios.minigameeventsplugin.Utils.PlayerState;
import lombok.Getter;
import org.bukkit.Sound;
import org.bukkit.boss.BossBar;
import org.bukkit.plugin.PluginManager;

import static com.loficostudios.minigameeventsplugin.Utils.DebugUtil.debug;

public class RoundManager {

    private final GameManager gameManager;
    private final EventManager eventManager;

    private static final int NEXT_ROUND_RETRY_ATTEMPTS = 1;
    private static final int MAX_ROUNDS = 100;

    private BossBar progressBar = null;

    @Getter int roundsElapsed;

    private Countdown timer;

    public RoundManager(GameManager gameManager, EventManager eventManager) {
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
            BaseEvent nextEvent = eventManager.getNextEvent();

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

    private void nextRound(BaseEvent nextEvent) {

        roundsElapsed++;

        String warningMessage;

        if (nextEvent instanceof RandomPlayerSelectorEvent randomPlayerSelectorEvent) {
            warningMessage = randomPlayerSelectorEvent.getAmount() + " " + nextEvent.getWarningMessage();
        }
        else {
            warningMessage = nextEvent.getWarningMessage();
        }

        gameManager.getStatusBar().setTitle(warningMessage);

        progressBar = gameManager.getProgressBar();

        gameManager.getPlayerManager().notify(NotificationType.GLOBAL, Sound.BLOCK_STONE_BUTTON_CLICK_OFF, 1, 2);
        timer = new Countdown(
                countdown -> {
                    progressBar.setTitle(countdown + " seconds");
                },
                () -> {
                    progressBar.setTitle("0 seconds");
                    progressBar.removeAll();
                    startRound(nextEvent);
                });

        timer.start(nextEvent.getWarningTime());
    }

    private void startRound(BaseEvent e) {
        if (timer != null)
            timer.stop();

        eventManager.handleStart(e);
        if (!gameManager.inProgress()) {
            return;
        }

        if (e instanceof RandomPlayerSelectorEvent) {
        }
        else {
            gameManager.getPlayerManager().notify(NotificationType.GLOBAL, Sound.BLOCK_NOTE_BLOCK_CHIME, 1, 1);
        }

        timer = new Countdown(
                (Double) -> {
                },
                this::endRound);

        timer.start(e.getDuration());
    }

    public void resetRounds() {
        eventManager.handleCancel(eventManager.getCurrentEvent());

        roundsElapsed = 0;
    }

    public void endRound() {
        if (timer != null)
            timer.stop();

        cancelRound();

        if (eventManager.handleEnd(eventManager.getCurrentEvent())) {
            eventManager.handleCancel(eventManager.getCurrentEvent());
        }

        PluginManager pluginManager = RandomEventsPlugin.getInstance().getServer().getPluginManager();

        gameManager.getPlayerManager()
                .getPlayers(PlayerState.ALIVE)
                .forEach(player -> pluginManager.callEvent(new RoundSurvivedEvent(player)));

        debug("current round: " + roundsElapsed + " maxRounds: " + MAX_ROUNDS);

        handleNextRound();
    }



    public void cancelRound() {

        if (timer != null)
            timer.stop();

        if (this.progressBar != null)
            progressBar.removeAll();
    }


}
