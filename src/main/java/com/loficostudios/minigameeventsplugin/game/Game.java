package com.loficostudios.minigameeventsplugin.game;

import com.loficostudios.minigameeventsplugin.AetherLabsPlugin;
import com.loficostudios.minigameeventsplugin.api.bukkit.GameEndEvent;
import com.loficostudios.minigameeventsplugin.config.Messages;
import com.loficostudios.minigameeventsplugin.game.arena.GameArena;
import com.loficostudios.minigameeventsplugin.game.player.NotificationType;
import com.loficostudios.minigameeventsplugin.game.player.PlayerManager;
import com.loficostudios.minigameeventsplugin.game.player.PlayerState;
import com.loficostudios.minigameeventsplugin.gamemode.GameMode;
import com.loficostudios.minigameeventsplugin.gamemode.GameModes;
import com.loficostudios.minigameeventsplugin.managers.EventController;
import com.loficostudios.minigameeventsplugin.managers.VoteManager;
import com.loficostudios.minigameeventsplugin.player.profile.PlayerProfile;
import com.loficostudios.minigameeventsplugin.utils.Countdown;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static com.loficostudios.minigameeventsplugin.utils.Debug.log;

public class Game {

    public static int MIN_PLAYERS_TO_START = 2;
    public static final int GAME_COUNTDOWN = 10;
    private static final int RESET_TIMER_AFTER_END_GAME = 3;
    public static final int PLAYER_KILL_MONEY_AMOUNT = 100;

    private final Set<BukkitTask> tasks = new HashSet<>();
    private final HashMap<String, Object> persistentData = new HashMap<String, Object>();

    @Getter
    private final RoundManager rounds;
    @Getter
    private final PlayerManager playerManager;
    @Getter
    private final GameArena arena;

    private VoteManager voting;
    private GameMode mode;

    @Getter
    private @NotNull GameState currentState = GameState.NONE;

    @Getter
    private final GameIndicator indicator = new GameIndicator(this);

    public Game(GameArena arena) {
        this.rounds = new RoundManager(this);
        this.playerManager = new PlayerManager(this, AetherLabsPlugin.inst().getProfileManager());
        this.arena = arena;
    }

    public boolean inProgress() {
        return currentState != GameState.NONE;
    }

    public @NotNull com.loficostudios.minigameeventsplugin.gamemode.GameMode getMode() {
        if (mode == null)
            return GameModes.NORMAL;

        return mode;
    }

    public void startVote() {
        this.voting = new VoteManager(this);
    }

    public @Nullable VoteManager getVoting() {
        return voting;
    }

    public boolean start() {
        List<Player> participating = playerManager.getAvailablePlayers();
        log("Participating" + participating);

        indicator.hide(GameIndicator.IndicatorType.PROGRESS);

        if (participating.isEmpty() || participating.size() < MIN_PLAYERS_TO_START) {
            setState(GameState.NONE);

            this.playerManager.notify(NotificationType.GLOBAL, Messages.NOT_ENOUGH_PLAYERS);
            indicator.hide(GameIndicator.IndicatorType.STATUS);
            return false;
        }

        indicator.status(Messages.STATUS_STARTING);

        var opt = Optional.ofNullable(voting);
        this.voting = null;

        var selected = opt.isPresent()
                ? opt.get().getMode()
                : GameModes.NORMAL;

        setup(participating, selected);
        return true;
    }

    private void setup(List<Player> participating, GameMode mode) {
        setState(GameState.SETUP);
        this.mode = mode;

        getPlayerManager().initializePlayers(participating);
        indicator.show(GameIndicator.IndicatorType.PROGRESS);
        indicator.progress("-1");

        new Countdown(i -> {
            if (i == 5) {
                arena.clear();
                mode.prepareResources(this, participating);
            }

            else if (i == 3) {
                mode.initializeCore(this, participating);
            }

            if (i == 2) {
                mode.finalizeSetup(this, participating);
            }
        }, () -> {
            setState(GameState.RUNNING);

            this.mode.start();
            log("started game");

            playerManager.notify(NotificationType.INGAME,Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);

            rounds.handleNextRound();
            getArena().startFillTask(mode.getFillMaterial(), mode.getFillSpeed());
        }).start(5);
    }

    public HashMap<String, Object> getPersistentData() {
        return persistentData;
    }

    public void endGame(Player winner) {
        setState(GameState.ENDED);
        mode.end();
        persistentData.clear();
        log("ended game");

        rounds.cancelRound();

        arena.cancelFillTask();
        arena.removeEntities();

        playerManager.restorePlayers();

        indicator.status(Messages.STATUS_PLAYER_WIN.replace("{winner}", winner == null ? "NaN" : winner.getName()), BarColor.PURPLE);

        org.bukkit.GameMode previousGM;
        if (winner != null) {
            previousGM = winner.getGameMode();
            winner.setGameMode(org.bukkit.GameMode.SPECTATOR);
        } else {
            previousGM = null;
        }

        var countdown = new Countdown((i) -> playerManager.notify(NotificationType.GLOBAL, Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 1, 1),
                () -> finalize(winner, previousGM));
        tasks.add(countdown.start(RESET_TIMER_AFTER_END_GAME));
    }

    private void finalize(Player winner, org.bukkit.GameMode previous) {
        tasks.forEach(BukkitTask::cancel);

        if (winner != null) {
            winner.setGameMode(previous);
            AetherLabsPlugin.inst().getProfileManager().getProfile(winner.getUniqueId())
                    .ifPresent(PlayerProfile::addWin);
        }
        playerManager.getPlayersInGame(PlayerState.ALIVE).forEach(player -> player.teleport(arena.getWorld().getSpawnLocation()));

        reset();

        Bukkit.getPluginManager().callEvent(new GameEndEvent(this));
    }

    public void forceEnd() {
        setState(GameState.ENDED);

        rounds.cancelRound();

        tasks.forEach(BukkitTask::cancel);

        arena.cancelFillTask();
        arena.removeEntities();

        playerManager.restorePlayers();

        playerManager.getPlayersInGame(PlayerState.ALIVE).forEach(player -> {
            player.teleport(arena.getWorld().getSpawnLocation());
        });

        reset();
    }

    private void reset(){
        setState(GameState.NONE);

        indicator.reset();

        mode.reset();

        resetArena();
        resetPlayers();

        rounds.resetRounds();

        log("reset game");
    }

    private void resetArena() {
        arena.clear();
        arena.removeAllPlatforms();
        arena.removeEntities();
    }

    private void resetPlayers() {
        playerManager.resetPlayers();
    }

    private void setState(GameState state) {
        currentState = state;
    }
}