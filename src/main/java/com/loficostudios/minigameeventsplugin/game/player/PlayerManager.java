package com.loficostudios.minigameeventsplugin.game.player;

import com.loficostudios.minigameeventsplugin.game.Game;
import com.loficostudios.minigameeventsplugin.game.GameState;
import com.loficostudios.minigameeventsplugin.game.arena.SpawnPlatform;
import com.loficostudios.minigameeventsplugin.player.profile.PlayerProfile;
import com.loficostudios.minigameeventsplugin.player.profile.ProfileManager;
import com.loficostudios.minigameeventsplugin.utils.Debug;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static com.loficostudios.minigameeventsplugin.utils.Debug.log;

public class PlayerManager {

    private final Game game;
    private final ProfileManager profileManager;

    private final Map<Player, PlayerState> players = new HashMap<>();

    private final RestoreController restoreController;

    public PlayerManager(Game game, ProfileManager profileManager) {
        this.game = game;
        this.restoreController = new RestoreController();
        this.profileManager = profileManager;
    }

    public List<Player> getAvailablePlayers() {
        return game.getArena().getWorld()
                .getPlayers()
                .stream()
                .filter(this::isAvailable).toList();
    }

    private boolean isAvailable(Player player) {
        var opt = profileManager.getProfile(player.getUniqueId());
        if (opt.isEmpty() || opt.get().isOptedOut())
            return false;
        return player.isValid() && !isAFK(player);
    }

    boolean isAFK(Player player) {
        return false;
    }

    public Collection<Player> getPlayersInGameWorld() {
        return game.getArena().getWorld().getPlayers();
    }

    public Collection<Player> getPlayersInGame() {
        return players.keySet().stream().toList();
    }

    public Collection<Player> getPlayersInGame(PlayerState state) {
        return players.entrySet()
                .stream()
                .filter(entry -> entry.getValue() == state)
                .map(Map.Entry::getKey)
                .toList();
    }

    public PlayerState getPlayerState(Player player) {
        return players.get(player);
    }

    public void initializePlayers(@NotNull Collection<Player> players) {
        if (players.isEmpty()) {
            game.forceEnd();
            return;
        }

        players.forEach(this::initializePlayer);
    }

    private void initializePlayer(Player player) {
        restoreController.save(player);
        this.addPlayer(player, PlayerState.ALIVE);
    }


    public void handleQuit(final Player player) {
        GameState currentState = game.getCurrentState();

        switch (currentState) {
            case COUNTDOWN:
                List<Player> validPlayers = new ArrayList<>(getAvailablePlayers());

                int size = validPlayers.size() - 1;

                Debug.log("valid players: " + size);
                if (size < Game.MIN_PLAYERS_TO_START) {
                    //todo cancel countdown
                }
                break;
            case RUNNING:
                if (getPlayersInGame().contains(player)) {
                    restoreController.restore(player);

                    setPlayerState(player, PlayerState.DEAD);
                    profileManager.getProfile(player.getUniqueId())
                            .ifPresent(PlayerProfile::addDeath);

                    Debug.log(player.getName() + " quit game");

                    validatePlayersAlive();
                }
                break;
        }
    }

    public void handlePlayerDeath(final Player player) {
        restoreController.restore(player);

        setPlayerState(player, PlayerState.DEAD);
        profileManager.getProfile(player.getUniqueId())
                .ifPresent(PlayerProfile::addDeath);

        validatePlayersAlive();

        SpawnPlatform platform = game.getArena().getSpawnPlatform(player);

        if (platform != null && !game.getCurrentState().equals(GameState.ENDED)) {
            game.getArena().removeSpawnPlatform(platform, true);
        }

        log(player.getName() + " died");

    }


    public void validatePlayersAlive() {
        GameState currentState = game.getCurrentState();

        if (currentState.equals(GameState.ENDED))
            return;

        List<Player> alivePlayers = getPlayersInGame(PlayerState.ALIVE).stream().toList();
        if (!alivePlayers.isEmpty()) {
            Player winner = alivePlayers.get(0);
            if (alivePlayers.size() == 1 && winner != null) {
                game.endGame(winner);
            }
        }
        else {
            game.endGame(null);
        }
    }

    public void resetPlayers() {
        players.clear();
    }

    public void setPlayerState(Player player, PlayerState state) {
        PlayerState oldState = getPlayerState(player);

        if (oldState.equals(state))
            return;

        if (state != null)
            players.put(player, state);
    }

    public void addPlayer(Player player, PlayerState state) {
        players.put(player, state);
    }

    //region HELPER METHODS
    public void notify(NotificationType type, Sound sound, float volume, float pitch) {
        switch (type) {
            case GLOBAL -> getPlayersInGameWorld().forEach(player -> player.playSound(player.getLocation(), sound, volume, pitch));
            case INGAME -> getPlayersInGame().forEach(player -> player.playSound(player.getLocation(), sound, volume, pitch));
        }
    }

    public void notify(NotificationType type, String msg) {

        var message = Component.text(msg);

        switch (type) {
            case GLOBAL -> getPlayersInGameWorld().forEach(player -> player.sendMessage(message));
            case INGAME -> getPlayersInGame().forEach(player -> player.sendMessage(message));
        }
    }

    public void teleportPlayer(Player player, Vector loc) {
        player.teleport(new Location(
                game.getArena().getWorld(),
                loc.getX(),
                loc.getY(),
                loc.getZ()));
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_TELEPORT, 1, 1);
    }

    public void restorePlayers() {
        for (Player player : players.keySet()) {
            if (!player.isOnline())
                continue;
            restoreController.restore(player);
        }
    }

    //endregion
}