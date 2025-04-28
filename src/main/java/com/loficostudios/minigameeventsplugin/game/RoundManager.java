package com.loficostudios.minigameeventsplugin.game;

import com.loficostudios.minigameeventsplugin.AetherLabsPlugin;
import com.loficostudios.minigameeventsplugin.api.bukkit.RoundSurvivedEvent;
import com.loficostudios.minigameeventsplugin.api.event.GameEvent;
import com.loficostudios.minigameeventsplugin.api.event.SelectorEvent;
import com.loficostudios.minigameeventsplugin.game.events.FallBackEvent;
import com.loficostudios.minigameeventsplugin.game.player.NotificationType;
import com.loficostudios.minigameeventsplugin.game.player.PlayerState;
import com.loficostudios.minigameeventsplugin.managers.EventManager;
import com.loficostudios.minigameeventsplugin.utils.Countdown;
import lombok.Getter;
import org.bukkit.Sound;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashSet;
import java.util.Set;

import static com.loficostudios.minigameeventsplugin.utils.Debug.log;

public class RoundManager {

    private final Game game;
    private final EventManager eventManager;

    private static final int NEXT_ROUND_RETRY_ATTEMPTS = 1;
    private static final int MAX_ROUNDS = 100;

    @Getter int roundsElapsed;

    //private Countdown timer;

    private Set<BukkitTask> tasks = new HashSet<>();

    public RoundManager(Game gameManager, EventManager eventManager) {
        this.game = gameManager;
        this.eventManager = eventManager;
    }

    public void handleNextRound() {
        if (!game.inProgress()) {
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
            nextRound(new FallBackEvent());
        }
    }

    private void nextRound(GameEvent nextEvent) {

        roundsElapsed++;

        String warningMessage;

        if (nextEvent instanceof SelectorEvent<?> selector) {
            warningMessage = selector.getAmount(game) + " " + nextEvent.getWarning().message();
        }
        else {
            warningMessage = nextEvent.getWarning().message();
        }

        game.getIndicator().status(warningMessage);

        game.getPlayerManager().notify(NotificationType.GLOBAL, Sound.BLOCK_STONE_BUTTON_CLICK_OFF, 1, 2);

        var indicator = game.getIndicator();
        indicator.show(GameIndicator.IndicatorType.PROGRESS);
        tasks.add(new Countdown(countdown -> indicator.progress(countdown + " seconds"),
                () -> {
                    indicator.progress("0 seconds");
                    startRound(nextEvent);
                }).start((int) nextEvent.getWarning().time()));
    }

    private void startRound(GameEvent e) {
        eventManager.handleStart(e);
        if (!game.inProgress()) {
            return;
        }

        game.getPlayerManager().notify(NotificationType.GLOBAL, Sound.BLOCK_NOTE_BLOCK_CHIME, 1, 1);

        tasks.add(new Countdown((i) -> {},
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

        game.getPlayerManager()
                .getPlayersInGame(PlayerState.ALIVE)
                .forEach(player -> pluginManager.callEvent(new RoundSurvivedEvent(player)));

        log("current round: " + roundsElapsed + " maxRounds: " + MAX_ROUNDS);

        handleNextRound();
    }

    public void cancelRound() {
        tasks.forEach(BukkitTask::cancel);
        game.getIndicator().hide(GameIndicator.IndicatorType.PROGRESS);
    }
}
