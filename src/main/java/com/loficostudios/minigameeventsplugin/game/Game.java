package com.loficostudios.minigameeventsplugin.game;

import com.loficostudios.minigameeventsplugin.config.ArenaConfig;
import com.loficostudios.minigameeventsplugin.gamemode.RushMode;
import com.loficostudios.minigameeventsplugin.managers.*;
import com.loficostudios.minigameeventsplugin.utils.Countdown;
import com.loficostudios.minigameeventsplugin.arena.GameArena;
import com.loficostudios.minigameeventsplugin.config.Messages;
import com.loficostudios.minigameeventsplugin.eggwars.EggWarsMode;
import com.loficostudios.minigameeventsplugin.gamemode.NormalMode;
import com.loficostudios.minigameeventsplugin.gamemode.RandomHeightsMode;
import com.loficostudios.minigameeventsplugin.player.PlayerManager;
import com.loficostudios.minigameeventsplugin.AetherLabsPlugin;
import com.loficostudios.minigameeventsplugin.player.profile.Profile;
import com.loficostudios.minigameeventsplugin.utils.*;

import lombok.Getter;
import org.bukkit.*;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static com.loficostudios.minigameeventsplugin.utils.Debug.log;

public class Game {

    public final RushMode RUSH = new RushMode();
    private final AetherLabsPlugin plugin;

    //region Variables
    public final EggWarsMode EGG_WARS = new EggWarsMode(this);
    public final NormalMode NORMAL = new NormalMode(this);
    public final RandomHeightsMode DIFFERENT_HEIGHTS = new RandomHeightsMode(this);

    public static int MIN_PLAYERS_TO_START = 1;

    public static final int GAME_COUNTDOWN = 10;
    private static final int RESET_TIMER_AFTER_END_GAME = 3;
    public static final int PLAYER_KILL_MONEY_AMOUNT = 100;

    @Getter
    private final RoundManager roundManager;
    @Getter
    private final PlayerManager players;
    @Getter
    private GameArena arena;
    @Getter
    private @NotNull GameState currentState = GameState.NONE;


    private com.loficostudios.minigameeventsplugin.gamemode.GameMode currentMode;

    @Getter
    private BossBar statusBar = Bukkit.createBossBar(null, BarColor.YELLOW, BarStyle.SOLID);


    //endregion

    private final BossBar progressBar = Bukkit.createBossBar("", BarColor.WHITE, BarStyle.SOLID);

    private final EventManager events = new EventManager(this);

    public Game(AetherLabsPlugin plugin) {
        this.roundManager = new RoundManager(this, events);
        this.players = new PlayerManager(this, plugin.getProfileManager());
        this.plugin = plugin;

        Location pos1 = ArenaConfig.POS_1;
        Location pos2 = ArenaConfig.POS_2;

        if (pos1 != null || pos2 != null) {
            this.arena = new GameArena(plugin, pos1, pos2);
        }
    }

    public boolean inProgress() {
        return currentState != GameState.NONE;
    }

    public BossBar getProgressBar() {
        players.getPlayersInGameWorld().forEach(progressBar::addPlayer);
        return this.progressBar;
    }

    public @NotNull com.loficostudios.minigameeventsplugin.gamemode.GameMode getCurrentMode() {

        if (currentMode == null)
            return NORMAL;

        return currentMode;

    }

    //region Start & End Game Functions
    public boolean cancelCountdown() {
        if (!currentState.equals(GameState.COUNTDOWN))
            return false;


        if (countdown.cancel()) {
            Debug.log("Cancelled countdown");

            setState(GameState.NONE);
            statusBar.removeAll();
            progressBar.removeAll();

            return true;
        }

        return false;
    }

    private Countdown countdown;

    public Boolean startCountdown(Integer time) {
        if (arena == null) {
            return false;
        }

        roundManager.cancelRound();

        setState(GameState.COUNTDOWN);

        VoteManager voteManager = new VoteManager(this);

        Collection<Player> playersInGameWorld = players.getPlayersInGameWorld();

        playersInGameWorld.forEach(statusBar::addPlayer);

        statusBar.setTitle(Messages.STATUS_COUNTDOWN);

        BossBar progressBar = getProgressBar();

        Collection<Player> participatingPlayers = getPlayers().getAvailablePlayers();

        Debug.log("" + participatingPlayers);


        countdown = new Countdown("countdown",
                (Integer countdown) -> {
                    progressBar.setTitle("In... " + countdown);

                    if (countdown == 3) {
                        players.notify(NotificationType.GLOBAL, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
                    } else if (countdown == 2) {
                        players.notify(NotificationType.GLOBAL, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1.5f);
                    } else if (countdown == 1) {
                        players.notify(NotificationType.GLOBAL, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 2);
                    }
                },
                () -> {
                    progressBar.removeAll();


                    if (!participatingPlayers.isEmpty()) {
                        if (participatingPlayers.size() >= MIN_PLAYERS_TO_START) {
                            statusBar.setTitle(Messages.STATUS_STARTING);

                            com.loficostudios.minigameeventsplugin.gamemode.GameMode selected = voteManager.getMode();
                            if (setCurrentMode(selected)) {
                                new SetupWizard(this).setup(selected, participatingPlayers);
                                setState(GameState.SETUP);
                            } else {
                                Debug.logError("Game mode selection failed");
                            }

                            return;
                        }
                        Debug.logWarning("Canceled game! Not enough players.");
                    } else {
                        Debug.logWarning("Canceled game! Player list is empty.");
                    }

                    setState(GameState.NONE);

                    players.notify(NotificationType.GLOBAL, Messages.NOT_ENOUGH_PLAYERS);

                    statusBar.removeAll();
                });

        tasks.add(countdown.start(time));

        return true;
    }

    private boolean setCurrentMode(com.loficostudios.minigameeventsplugin.gamemode.GameMode mode) {

        if (mode != null) {
            currentMode = mode;
            return true;
        }
        else {
            return false;
        }
    }

    public void startGame() {
        setState(GameState.RUNNING);
        currentMode.start();
        log("started game");

        players.notify(NotificationType.INGAME,Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);

        roundManager.handleNextRound();
    }

    public void endGame(Player winner) {
        setState(GameState.ENDED);
        currentMode.end();
        Debug.log("ended game");


        roundManager.cancelRound();

        arena.cancelLavaFillTask();
        arena.removeEntities();



        GameMode previousGamemode;

        players.restorePlayers();

        statusBar.setColor(BarColor.PURPLE);

        statusBar.setTitle(Messages.STATUS_PLAYER_WIN
                .replace("{winner}", winner == null ? "NaN" : winner.getName()));


        if (winner != null) {
            previousGamemode = winner.getGameMode();
            winner.setGameMode(GameMode.SPECTATOR);
        } else {
            previousGamemode = null;
        }

        tasks.add(new Countdown("end", (Integer countdown) ->
                players.notify(
                        NotificationType.GLOBAL,
                        Sound.ENTITY_FIREWORK_ROCKET_LAUNCH,
                        1, 1),
                () -> {
                    tasks.forEach(BukkitTask::cancel);

                    if (winner != null) {
                        winner.setGameMode(previousGamemode);
                        plugin.getProfileManager().getProfile(winner.getUniqueId())
                                .ifPresent(Profile::addWin);
                    }
                    players.getPlayersInGame(PlayerState.ALIVE).forEach(player -> player.teleport(arena.getWorld().getSpawnLocation()));

                    reset();
                    startCountdown(GAME_COUNTDOWN);
                }).start(RESET_TIMER_AFTER_END_GAME));
    }

    public void forceEnd() {
        setState(GameState.ENDED);

        roundManager.cancelRound();

        tasks.forEach(BukkitTask::cancel);

        arena.cancelLavaFillTask();
        arena.removeEntities();

        players.restorePlayers();

        players.getPlayersInGame(PlayerState.ALIVE).forEach(player -> {
            player.teleport(arena.getWorld().getSpawnLocation());
        });

        reset();
    }

    private final Set<BukkitTask> tasks = new HashSet<>();

    private void reset(){
        setState(GameState.NONE);

        statusBar.setColor(BarColor.YELLOW);
        statusBar.removeAll();
        progressBar.removeAll();

        currentMode.reset();

        resetArena();
        resetPlayers();

        roundManager.resetRounds();

        log("reset game");
    }
    //endregion

    private void resetArena() {
        arena.clear();
        arena.removeAllPlatforms();
        arena.removeEntities();
    }

    private void resetPlayers() {
        players.resetPlayers();
    }

    private void setState(GameState state) {
        currentState = state;
    }

    public void setGameArena(Location pos1, Location pos2) {
        plugin.getArenaConfig().update(pos1, pos2);
        this.arena = new GameArena(plugin, pos1, pos2);
    }

    public EventManager getEvents() {
        return events;
    }
}