package com.loficostudios.minigameeventsplugin.Managers;

import com.loficostudios.minigameeventsplugin.Config.ArenaConfig;
import com.loficostudios.minigameeventsplugin.Countdown.Countdown;
import com.loficostudios.minigameeventsplugin.GameArena.GameArena;
import com.loficostudios.minigameeventsplugin.MiniGameEventsPlugin;
import com.loficostudios.minigameeventsplugin.Profile.Profile;
import com.loficostudios.minigameeventsplugin.Utils.Selection;
import com.loficostudios.minigameeventsplugin.GameArena.SpawnPlatform;
import com.loficostudios.minigameeventsplugin.Utils.*;

import lombok.Getter;
import org.bukkit.*;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import java.util.*;

import static com.loficostudios.minigameeventsplugin.Utils.DebugUtil.debug;

@SuppressWarnings("ALL")
public class GameManager {

    //region Variables
    private final MiniGameEventsPlugin plugin;
    private final EventManager eventManager;
    private final ProfileManager profileManager;
    public static final int GAME_COUNTDOWN = 15;

    private static final int RESET_TIMER_AFTER_END_GAME = 3;
    private static final int GAME_COUNTDOWN_SOUND_START_TIME = 3;

    @Getter final RoundManager roundManager;
    @Getter final PlayerManager playerManager;

    @Getter final BossBar statusBar = Bukkit.createBossBar(null, BarColor.YELLOW, BarStyle.SOLID);



    @Getter GameState currentState;
    @Getter
    GameArena arena;

    private Countdown timer;
    //endregion

    public enum GameState { NONE, COUNTDOWN, RUNNING, SETUP }

    public GameManager(MiniGameEventsPlugin plugin, EventManager eventManager, ProfileManager profileManager) {
        this.eventManager = eventManager;
        roundManager = new RoundManager(this, eventManager);
        playerManager = new PlayerManager(plugin);
        this.plugin = plugin;
        this.profileManager = profileManager;
    }

    public Collection<Player> getPlayersInGameWorld() {
        return arena.getWorld().getPlayers();
    }


    public boolean inProgress() {

        if (currentState == null || currentState == GameState.NONE) {
            return false;
        }

        return true;
    }

    //region Start & End Game Functions
    public void startGameCountdown(Integer time) {
        roundManager.cancelRound();

        setState(GameState.COUNTDOWN);

        if (timer != null)
            timer.stop();


        Collection<Player> playersInGameWorld = getPlayersInGameWorld();

        playersInGameWorld.forEach(statusBar::addPlayer);

        statusBar.setTitle("Resetting arena and starting a new game soon...");

        BossBar progressBar = getProgressBar();

        timer = new Countdown(
                (Integer countdown) -> {
                    progressBar.setTitle("In... " + countdown);

                    if (countdown == 3) {
                        //notify(NotificationType.GLOBAL, Sound.BLOCK_STONE_BUTTON_CLICK_OFF, 1, 1);
                        notify(NotificationType.GLOBAL, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);

                    } else if (countdown == 2) {
                        //notify(NotificationType.GLOBAL, Sound.BLOCK_STONE_BUTTON_CLICK_OFF, 1, 1.5f);
                        notify(NotificationType.GLOBAL, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1.5f);
                    } else if (countdown == 1) {
                        //notify(NotificationType.GLOBAL, Sound.BLOCK_STONE_BUTTON_CLICK_OFF, 1, 2);
                        notify(NotificationType.GLOBAL, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 2);
                    }
                },
                () -> {
                    Collection<Player> participatingPlayers = calculatePlayers();
                    progressBar.removeAll();
                    setupGame(participatingPlayers);
                });

        timer.start(time);
    }


    private void setupGame(Collection<Player> participatingPlayers) {
        if (timer != null)
            timer.stop();

        setState(GameState.SETUP);
        statusBar.setTitle("setting up game please wait...");

        participatingPlayers.forEach(player -> {
            playerManager.addPlayer(player, PlayerState.ALIVE);
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
        if (timer != null)
            timer.stop();

        setState(GameState.RUNNING);
        debug("started game");

        arena.startLevelFillTask();

        notify(NotificationType.INGAME,Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);

        roundManager.handleNextRound();
    }

    private void end(Player winner) {
        isEnded = true;

        roundManager.cancelRound();

        arena.cancelLavaTask();
        arena.removeMobs();

        GameMode previousGamemode;

        statusBar.setTitle(winner == null ? "Nobody" : winner.getName() + " won the game");
        if (winner != null) {
            previousGamemode = winner.getGameMode();
            winner.setGameMode(GameMode.SPECTATOR);
        } else {
            previousGamemode = null;
        }

        timer = new Countdown((Integer countdown) -> notify(NotificationType.GLOBAL, Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 1, 1), () -> {
            if (winner != null) {
                winner.setGameMode(previousGamemode);
                profileManager.getProfile(winner.getUniqueId())
                        .ifPresent(profile -> profile.addWin());
            }
            playerManager.getPlayers(PlayerState.ALIVE).forEach(player -> player.teleport(arena.getWorld().getSpawnLocation()));

            isEnded = false;

            reset();
        });

        timer.start(RESET_TIMER_AFTER_END_GAME);
    }

    public void forceEnd() {
        roundManager.cancelRound();
        //setState(GameState.CANCELLED);

        arena.cancelLavaTask();
        arena.removeMobs();

        playerManager.getPlayers(PlayerState.ALIVE).forEach(player -> player.teleport(arena.getWorld().getSpawnLocation()));

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

        arena.clear();

        playerManager.resetPlayers();



        eventManager.currentEvent = null;

        roundManager.resetRounds();

        debug("reset game");
    }
    //endregion

    public enum NotificationType { GLOBAL, INGAME }

    public void notify(NotificationType type, Sound sound, float volume, float pitch) {
        switch (type) {
            case GLOBAL -> getPlayersInGameWorld().forEach(player -> player.playSound(player.getLocation(), sound, volume, pitch));
            case INGAME -> playerManager.getPlayers().forEach(player -> player.playSound(player.getLocation(), sound, volume, pitch));
        }
    }


    public void handlePlayerQuit(final Player player) {
        playerManager.tagPlayer(player, PlayerState.DEAD);
        profileManager.getProfile(player.getUniqueId())
                .ifPresent(Profile::addDeath);

        debug(player.getName() + " quit game");
        verifyPlayers();
    }

    public void handlePlayerDeath(final Player player) {
        playerManager.tagPlayer(player, PlayerState.DEAD);
        profileManager.getProfile(player.getUniqueId())
                .ifPresent(Profile::addDeath);

        verifyPlayers();

        SpawnPlatform platform = arena.getSpawnPlatform(player);

        if (platform != null && !isEnded) {
            arena.removeSpawnPlatform(platform, true);
        }

        debug(player.getName() + " died");

    }

    private Collection<Player> calculatePlayers() {
        return getArena().getWorld()
                .getPlayers()
                .stream()
                .filter(player -> {

                    if (!player.isValid()) {
                        return false;
                    }

                    if (plugin.essentialsHook) {
                        return !plugin.getEssentials().getUser(player).isAfk();
                    }

                    return true;
                }).toList();
    }

    private boolean isEnded = false;

    private void verifyPlayers() {
        if (isEnded)
            return;

        List<Player> alivePlayers = (List<Player>) playerManager.getPlayers(PlayerState.ALIVE);
        if (alivePlayers.isEmpty()) {
            end(null);
        }

        Player winner = playerManager.getLastPlayerAlive();

        if (winner != null) {
            end(winner);
        }
    }

    private final BossBar progressBar = Bukkit.createBossBar("", BarColor.WHITE, BarStyle.SOLID);
    public BossBar getProgressBar() {
        getPlayersInGameWorld().forEach(progressBar::addPlayer);
        return this.progressBar;
    }


    private void setState(GameState state) {
        currentState = state;
    }

    public void createGameArena() {
        arena = new GameArena(ArenaConfig.POS1, ArenaConfig.POS2);
    }



    public void setGameArena(Location pos1, Location pos2) {
        arena = new GameArena(pos1, pos2);
        ArenaConfig.update(pos1, pos2);
    }
}