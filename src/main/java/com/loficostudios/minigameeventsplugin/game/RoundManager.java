package com.loficostudios.minigameeventsplugin.game;

import com.loficostudios.minigameeventsplugin.AetherLabsPlugin;
import com.loficostudios.minigameeventsplugin.api.bukkit.RoundSurvivedEvent;
import com.loficostudios.minigameeventsplugin.api.event.GameEvent;
import com.loficostudios.minigameeventsplugin.api.event.SelectorEvent;
import com.loficostudios.minigameeventsplugin.game.events.FallBackEvent;
import com.loficostudios.minigameeventsplugin.game.player.NotificationType;
import com.loficostudios.minigameeventsplugin.game.player.PlayerState;
import com.loficostudios.minigameeventsplugin.managers.EventController;
import com.loficostudios.minigameeventsplugin.utils.Countdown;
import org.bukkit.Sound;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

import static com.loficostudios.minigameeventsplugin.utils.Debug.log;

public class RoundManager {

    private static final int NEXT_ROUND_RETRY_ATTEMPTS = 1;
    private static final int MAX_ROUNDS = 100;

    private final Game game;
    private final EventController controller;

    private final Set<BukkitTask> tasks = new HashSet<>();

    private final List<Round> rounds = new ArrayList<>();

    public List<Round> getRoundsElapsed() {
        return Collections.unmodifiableList(rounds);
    }

    public RoundManager(Game game) {
        this.game = game;
        this.controller = new EventController(game);
    }

    public EventController getEventController() {
        return controller;
    }

    public void handleNextRound() {
        if (!game.inProgress()) {
            return;
        }

        int attempts = 0;
        boolean valid = false;

        while (!valid && attempts < NEXT_ROUND_RETRY_ATTEMPTS) {
            GameEvent nextEvent = controller.getNextEvent();

            if (nextEvent != null) {
                valid = true;
                setupRound(nextEvent);
            }

            attempts++;
        }

        if (!valid) {
            setupRound(new FallBackEvent());
        }
    }

    private void setupRound(GameEvent next) {
        var round = new Round(next, System.currentTimeMillis(), rounds.size() + 1);
        rounds.add(round);

        String warningMessage;

        if (next instanceof SelectorEvent<?> selector) {
            warningMessage = selector.getAmount(game) + " " + next.getWarning().message();
        }
        else {
            warningMessage = next.getWarning().message();
        }

        game.getIndicator().status(warningMessage);

        game.getPlayerManager().notify(NotificationType.GLOBAL, Sound.BLOCK_STONE_BUTTON_CLICK_OFF, 1, 2);

        var indicator = game.getIndicator();
        indicator.show(GameIndicator.IndicatorType.PROGRESS);
        tasks.add(new Countdown(countdown -> indicator.progress(countdown + " seconds"),
                () -> {
                    indicator.progress("0 seconds");
                    startNextRound(next);
                }).start((int) next.getWarning().time()));
    }

    private void startNextRound(GameEvent e) {
        controller.handleStart(e);
        if (!game.inProgress()) {
            return;
        }

        game.getPlayerManager().notify(NotificationType.GLOBAL, Sound.BLOCK_NOTE_BLOCK_CHIME, 1, 1);

        tasks.add(new Countdown((i) -> {},
                this::endCurrentRound).start(e.getDuration()));
    }

    public void resetRounds() {
        controller.handleCancel(controller.getCurrentEvent());
        rounds.clear();
    }

    public void endCurrentRound() {
        cancelRound();

        if (controller.handleEnd(controller.getCurrentEvent())) {
            controller.handleCancel(controller.getCurrentEvent());
        }

        PluginManager pluginManager = AetherLabsPlugin.inst()
                .getServer()
                .getPluginManager();

        game.getPlayerManager()
                .getPlayersInGame(PlayerState.ALIVE)
                .forEach(player -> pluginManager.callEvent(new RoundSurvivedEvent(player)));

        log("current round: " + getRoundsElapsed().size() + " maxRounds: " + MAX_ROUNDS);

        handleNextRound();
    }

    public void cancelRound() {
        tasks.forEach(BukkitTask::cancel);
        game.getIndicator().hide(GameIndicator.IndicatorType.PROGRESS);
    }
}
