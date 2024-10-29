package com.loficostudios.minigameeventsplugin.Managers.GameManager;

import com.loficostudios.minigameeventsplugin.Countdown.Countdown;
import com.loficostudios.minigameeventsplugin.GameArena.GameArena;
import com.loficostudios.minigameeventsplugin.Managers.PlayerManager.NotificationType;
import com.loficostudios.minigameeventsplugin.Managers.PlayerManager.PlayerManager;
import com.loficostudios.minigameeventsplugin.Managers.RoundManager;
import com.loficostudios.minigameeventsplugin.RandomEventsPlugin;
import com.loficostudios.minigameeventsplugin.Profile.Profile;
import com.loficostudios.minigameeventsplugin.Utils.*;

import lombok.Getter;
import org.bukkit.*;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static com.loficostudios.minigameeventsplugin.Utils.DebugUtil.debug;


public class GameManager {

    private final RandomEventsPlugin plugin;



    //region Variables
    public static final int GAME_COUNTDOWN = 15;
    private static final int RESET_TIMER_AFTER_END_GAME = 3;
    public static final int PLAYER_KILL_MONEY_AMOUNT = 100;

    @Getter
    private final RoundManager roundManager;
    @Getter
    private final PlayerManager playerManager;
    @Getter
    private GameArena arena;
    @Getter
    private @NotNull GameState currentState = GameState.NONE;
    @Getter
    private BossBar statusBar = Bukkit.createBossBar(null, BarColor.YELLOW, BarStyle.SOLID);

    private Countdown timer;
    //endregion

    private final BossBar progressBar = Bukkit.createBossBar("", BarColor.WHITE, BarStyle.SOLID);

    public GameManager(RandomEventsPlugin plugin) {
        this.roundManager = new RoundManager(this, plugin.getEventManager());
        this.playerManager = new PlayerManager(this, plugin.getProfileManager());
        this.plugin = plugin;
        this.arena = new GameArena(plugin.getArenaConfig().POS_1, plugin.getArenaConfig().POS_2);
    }

    public boolean inProgress() {
        return currentState != GameState.NONE;
    }

    public BossBar getProgressBar() {
        playerManager.getPlayersInGameWorld().forEach(progressBar::addPlayer);
        return this.progressBar;
    }

    //region Start & End Game Functions
    public void startGameCountdown(Integer time) {
        roundManager.cancelRound();

        setState(GameState.COUNTDOWN);

        if (timer != null)
            timer.stop();

        Collection<Player> playersInGameWorld = playerManager.getPlayersInGameWorld();

        playersInGameWorld.forEach(statusBar::addPlayer);

        statusBar.setTitle("Resetting arena and starting a new game soon...");

        BossBar progressBar = getProgressBar();

        timer = new Countdown(
                (Integer countdown) -> {
                    progressBar.setTitle("In... " + countdown);

                    if (countdown == 3) {
                        playerManager.notify(NotificationType.GLOBAL, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
                    } else if (countdown == 2) {
                        playerManager.notify(NotificationType.GLOBAL, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1.5f);
                    } else if (countdown == 1) {
                        playerManager.notify(NotificationType.GLOBAL, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 2);
                    }
                },
                () -> {
                    progressBar.removeAll();

                    Collection<Player> participatingPlayers = getPlayerManager().getAvailablePlayers();
                    setupGame(participatingPlayers);
                });

        timer.start(time);
    }

    private void setupGame(Collection<Player> participatingPlayers) {
        setState(GameState.SETUP);

        if (timer != null)
            timer.stop();

        statusBar.setTitle("Setting up game please wait...");

        participatingPlayers.forEach(player -> {

            playerManager.addPlayer(player, PlayerState.ALIVE);

            for (PotionEffect effect : player.getActivePotionEffects())
                player.removePotionEffect(effect.getType());

            playerManager.savePlayer(player);

            player.getEquipment().clear();
            player.getInventory().clear();

        });

        BossBar progressBar = getProgressBar();

        timer = new Countdown(countDown -> {
            progressBar.setTitle(String.valueOf(countDown));

            switch(countDown) {
                case 3:
                    debug("clearing area...");
                    arena.clear();
                    break;
                case 2:
                    debug("spawning platforms...");
                    arena.spawnPlatforms(participatingPlayers);

                    break;
                case 1:

                    break;
            }

        }, this::startGame);
        timer.start(3);
    }

    private void startGame() {
        setState(GameState.RUNNING);

        if (timer != null)
            timer.stop();

        arena.startLevelFillTask(GameArena.DEFAULT_FILL_MATERIAL, GameArena.DEFAULT_FILL_SPEED);

        playerManager.notify(NotificationType.INGAME,Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);

        roundManager.handleNextRound();

        debug("started game");
    }

    public void end(Player winner) {
        currentState = GameState.ENDED;

        roundManager.cancelRound();

        arena.cancelLavaTask();
        arena.removeMobs();

        GameMode previousGamemode;

        playerManager.restorePlayers();

        statusBar.setTitle(winner == null ? "Nobody" : winner.getName() + " won the game");
        if (winner != null) {
            previousGamemode = winner.getGameMode();
            winner.setGameMode(GameMode.SPECTATOR);
        } else {
            previousGamemode = null;
        }

        timer = new Countdown((Integer countdown) ->
                playerManager.notify(
                        NotificationType.GLOBAL,
                        Sound.ENTITY_FIREWORK_ROCKET_LAUNCH,
                        1, 1),
                () -> {
                    if (winner != null) {
                        winner.setGameMode(previousGamemode);
                        plugin.getProfileManager().getProfile(winner.getUniqueId())
                                .ifPresent(Profile::addWin);
                    }
                    playerManager.getPlayers(PlayerState.ALIVE).forEach(player -> player.teleport(arena.getWorld().getSpawnLocation()));

                    reset();
                });
        timer.start(RESET_TIMER_AFTER_END_GAME);
    }

    public void forceEnd() {
        currentState = GameState.ENDED;

        roundManager.cancelRound();

        arena.cancelLavaTask();
        arena.removeMobs();

        playerManager.restorePlayers();

        playerManager.getPlayers(PlayerState.ALIVE).forEach(player -> {
            player.teleport(arena.getWorld().getSpawnLocation());
        });

        reset();
    }

    private void reset() {
        setState(GameState.NONE);

        if (timer != null) {
            timer.stop();
            timer = null;
        }

        statusBar.removeAll();
        progressBar.removeAll();

        resetArena();
        resetPlayers();

        roundManager.resetRounds();

        debug("reset game");
    }
    //endregion

    private void resetArena() {
        arena.clear();
        arena.removeAllPlatforms();
        arena.removeMobs();
    }

    private void resetPlayers() {
        playerManager.resetPlayers();
    }

    private void setState(GameState state) {
        currentState = state;
    }

    public void setGameArena(Location pos1, Location pos2) {
        arena = new GameArena(pos1, pos2);
        plugin.getArenaConfig().update(pos1, pos2);
    }
}