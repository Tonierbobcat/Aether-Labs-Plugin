package com.loficostudios.minigameeventsplugin.Managers.PlayerManager;

import com.loficostudios.minigameeventsplugin.GameArena.SpawnPlatform;
import com.loficostudios.minigameeventsplugin.Managers.GameManager.GameManager;
import com.loficostudios.minigameeventsplugin.Managers.GameManager.GameState;
import com.loficostudios.minigameeventsplugin.Managers.ProfileManager;
import com.loficostudios.minigameeventsplugin.RandomEventsPlugin;
import com.loficostudios.minigameeventsplugin.Profile.Profile;
import com.loficostudios.minigameeventsplugin.Utils.PlayerState;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

import java.util.*;

import static com.loficostudios.minigameeventsplugin.Utils.DebugUtil.debug;

public class PlayerManager {

    private final GameManager gameManager;
    private final ProfileManager profileManager;

    private final Map<Player, PlayerState> players = new HashMap<>();

    final Map<UUID, ItemStack[]> savedPlayerInventory = new HashMap<>();
    final Map<UUID, ItemStack[]> savedPlayerEquipment = new HashMap<>();

    public PlayerManager(GameManager gameManager, ProfileManager profileManager) {
        this.gameManager = gameManager;
        this.profileManager = profileManager;
    }

    public PlayerState getPlayerState(Player player) {
        return players.get(player);
    }

    public Collection<Player> getPlayers() {
        return players.keySet().stream().toList();
    }

    public Collection<Player> getPlayers(PlayerState state) {
        return players.entrySet()
                .stream()
                .filter(entry -> entry.getValue() == state)
                .map(Map.Entry::getKey)
                .toList();
    }

    public Collection<Player> getAvailablePlayers() {

        RandomEventsPlugin plugin = RandomEventsPlugin.getInstance();

        return gameManager.getArena().getWorld()
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

    public Collection<Player> getPlayersInGameWorld() {
        return gameManager.getArena().getWorld().getPlayers();
    }

    //region INVENTORY SHIT
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

        debug("restored players inventory " + player.getName());
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
    //endregion


    public void handlePlayerQuit(final Player player) {
        restorePlayer(player);

        setPlayerState(player, PlayerState.DEAD);
        profileManager.getProfile(player.getUniqueId())
                .ifPresent(Profile::addDeath);

        debug(player.getName() + " quit game");
        validatePlayers();
    }

    public void handlePlayerDeath(final Player player) {
        restorePlayer(player);

        setPlayerState(player, PlayerState.DEAD);
        profileManager.getProfile(player.getUniqueId())
                .ifPresent(Profile::addDeath);

        validatePlayers();

        SpawnPlatform platform = gameManager.getArena().getSpawnPlatform(player);

        if (platform != null && !gameManager.getCurrentState().equals(GameState.ENDED)) {
            gameManager.getArena().removeSpawnPlatform(platform, true);
        }

        debug(player.getName() + " died");

    }

    public void validatePlayers() {
        if (gameManager.getCurrentState().equals(GameState.ENDED))
            return;
        List<Player> alivePlayers = getPlayers(PlayerState.ALIVE).stream().toList();

        if (!alivePlayers.isEmpty()) {
            Player winner = alivePlayers.get(0);
            if (alivePlayers.size() == 1 && winner != null) {
                gameManager.end(winner);
            }
        }
        else {
            gameManager.end(null);
        }
    }

    public void resetPlayers() {
        players.clear();
    }

    //region Player Functions

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

    public void notify(NotificationType type, Sound sound, float volume, float pitch) {
        switch (type) {
            case GLOBAL -> getPlayersInGameWorld().forEach(player -> player.playSound(player.getLocation(), sound, volume, pitch));
            case INGAME -> getPlayers().forEach(player -> player.playSound(player.getLocation(), sound, volume, pitch));
        }
    }
}
