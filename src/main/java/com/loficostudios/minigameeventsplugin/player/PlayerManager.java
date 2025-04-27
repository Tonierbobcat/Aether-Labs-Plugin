package com.loficostudios.minigameeventsplugin.player;

import com.loficostudios.minigameeventsplugin.AetherLabsPlugin;
import com.loficostudios.minigameeventsplugin.arena.SpawnPlatform;
import com.loficostudios.minigameeventsplugin.game.Game;
import com.loficostudios.minigameeventsplugin.game.GameState;
import com.loficostudios.minigameeventsplugin.managers.NotificationType;
import com.loficostudios.minigameeventsplugin.player.profile.Profile;
import com.loficostudios.minigameeventsplugin.player.profile.ProfileManager;
import com.loficostudios.minigameeventsplugin.utils.Debug;
import com.loficostudios.minigameeventsplugin.utils.PlayerState;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static com.loficostudios.minigameeventsplugin.utils.Debug.log;

public class PlayerManager {

    private final Game gameManager;
    private final ProfileManager profileManager;

    private final Map<Player, PlayerState> players = new HashMap<>();

    final Map<UUID, ItemStack[]> savedPlayerInventory = new HashMap<>();
    final Map<UUID, ItemStack[]> savedPlayerEquipment = new HashMap<>();

    public PlayerManager(Game gameManager, ProfileManager profileManager) {
        this.gameManager = gameManager;
        this.profileManager = profileManager;
    }

    public Collection<Player> getAvailablePlayers() {

        AetherLabsPlugin plugin = AetherLabsPlugin.getInstance();

        return gameManager.getArena().getWorld()
                .getPlayers()
                .stream()
                .filter(player -> {
                    Profile profile = null;
                    
                    Optional<Profile> optional = profileManager.getProfile(player.getUniqueId());
                    if (optional.isPresent()) {
                        profile = optional.get();
                    }
                    
                    if (profile != null && profile.isOptedOut()) {
                        Debug.log("Player " + player.getName() + "is opted out from game");
                        return false;
                    }
                    
                    if (!player.isValid()) {
                        return false;
                    }

                    if (plugin.essentialsHook) {
                        return !plugin.getEssentials().getUser(player).isAfk();
                    }

                    return true;
                }).toList();
    }

    public Collection<Player> getPlayersInGameWorld() {
        return gameManager.getArena().getWorld().getPlayers();
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

    public void initializePlayers(@NotNull Collection<Player> participatingPlayers) {
        if (participatingPlayers.isEmpty()) {

            gameManager.forceEnd();
            throw new IllegalArgumentException("participatingPlayers cannot be empty");
        }

        participatingPlayers.forEach(player -> {
            this.savePlayer(player);
            this.addPlayer(player, PlayerState.ALIVE);

            clearPotionEffects(player);

            clearInventory(player);
        });
    }

    public void savePlayer(final Player player) {

        UUID uuid = player.getUniqueId();

        EntityEquipment equipment = player.getEquipment();

        if (equipment != null)
            savedPlayerEquipment.put(uuid, equipment.getArmorContents().clone());

        savedPlayerInventory.put(player.getUniqueId(), player.getInventory().getContents().clone());
    }

    public void restorePlayer(final Player player) {
        UUID uuid = player.getUniqueId();

        ItemStack[] contents = savedPlayerInventory.get(uuid);
        player.getInventory().setContents(contents);

        ItemStack[] equipmentItems = savedPlayerEquipment.get(uuid);

        EntityEquipment equipment = player.getEquipment();

        if (equipment != null)
            equipment.setArmorContents(equipmentItems);

        savedPlayerInventory.remove(uuid);
        savedPlayerEquipment.remove(uuid);

        log("restored players inventory " + player.getName());
    }

    public void restorePlayers() {

        savedPlayerInventory.forEach((uuid, inventory) -> {
            Player player = Bukkit.getPlayer(uuid);

            if (player != null) {
                player.getInventory().setContents(inventory);
            }
        });

        savedPlayerEquipment.forEach((uuid, equipmentItems) -> {
            Player player = Bukkit.getPlayer(uuid);

            if (player != null) {
                EntityEquipment equipment = player.getEquipment();
                if (equipment != null) {
                    player.getEquipment().setArmorContents(equipmentItems);
                }
            }
        });

        savedPlayerInventory.clear();
        savedPlayerEquipment.clear();
    }

    public void handleQuit(final Player player) {
        GameState currentState = gameManager.getCurrentState();

        switch (currentState) {
            case COUNTDOWN:
                List<Player> validPlayers = new ArrayList<>(getAvailablePlayers());

                int size = validPlayers.size() - 1;

                Debug.log("valid players: " + size);
                if (size < Game.MIN_PLAYERS_TO_START) {

                    if (gameManager.cancelCountdown())
                        return;
                    else {
                        Debug.logError("Could not cancel game countdown");
                    }
                }
                break;
            case RUNNING:
                if (getPlayersInGame().contains(player)) {
                    restorePlayer(player);

                    setPlayerState(player, PlayerState.DEAD);
                    profileManager.getProfile(player.getUniqueId())
                            .ifPresent(Profile::addDeath);

                    Debug.log(player.getName() + " quit game");

                    validatePlayersAlive();
                }
                break;
        }
    }

    public void handlePlayerDeath(final Player player) {
        restorePlayer(player);

        setPlayerState(player, PlayerState.DEAD);
        profileManager.getProfile(player.getUniqueId())
                .ifPresent(Profile::addDeath);

        validatePlayersAlive();

        SpawnPlatform platform = gameManager.getArena().getSpawnPlatform(player);

        if (platform != null && !gameManager.getCurrentState().equals(GameState.ENDED)) {
            gameManager.getArena().removeSpawnPlatform(platform, true);
        }

        log(player.getName() + " died");

    }


    public void validatePlayersAlive() {
        GameState currentState = gameManager.getCurrentState();

        if (currentState.equals(GameState.ENDED))
            return;

//        if (validPlayers.size() < GameManager.MIN_PLAYERS_TO_START) {
//            gameManager.cancelGameCountdown();
//            players.clear();
//
//            Debug.log("cancelled countdown");
//
//            return;
//        }

        List<Player> alivePlayers = getPlayersInGame(PlayerState.ALIVE).stream().toList();
        if (!alivePlayers.isEmpty()) {
            Player winner = alivePlayers.get(0);
            if (alivePlayers.size() == 1 && winner != null) {
                gameManager.endGame(winner);
            }
        }
        else {
            gameManager.endGame(null);
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

    //region INVENTORY FUNCTIONS
    private void clearInventory(@NotNull final Player player) {
        EntityEquipment equipment = player.getEquipment();
        if (equipment != null) {
            player.getEquipment().clear();
        }

        player.getInventory().clear();
    }
    //endregion

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

    public void clearPotionEffects(@NotNull Player player) {
        player.getActivePotionEffects().forEach(effect ->
                player.removePotionEffect(effect.getType()));
    }

    public void teleportPlayer(Player player, Vector loc) {

        player.teleport(new Location(
                gameManager.getArena().getWorld(),
                loc.getX(),
                loc.getY(),
                loc.getZ()));
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_TELEPORT, 1, 1);
    }
    //endregion
}