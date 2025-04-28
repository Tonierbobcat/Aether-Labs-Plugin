package com.loficostudios.minigameeventsplugin.managers;

import com.loficostudios.minigameeventsplugin.game.Game;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SpawnManager {


    Game gameManager;

    Map<UUID, Vector> spawns = new HashMap<>();

    public SpawnManager(Game gameManager) {
        this.gameManager = gameManager;
    }

    public boolean addSpawn(@NotNull Player player, @NotNull Location location) {
        spawns.put(player.getUniqueId(),
                new Vector(
                        location.getBlockX(),
                        location.getBlockY(),
                        location.getBlockZ()));
        return true;
    }

    public boolean hasSpawn(@NotNull Player player) {
        UUID uuid = player.getUniqueId();

        if (spawns.containsKey(uuid)) {
            return true;
        }

        return false;
    }

    public boolean removeSpawn(@NotNull Player player) {

        spawns.remove(player.getUniqueId());
        return true;
    }

    public boolean spawnPlayer(@NotNull Player player) {
        UUID uuid = player.getUniqueId();

        if (spawns.containsKey(uuid)) {

            Vector loc = spawns.get(uuid);

            gameManager.getPlayerManager().teleportPlayer(player, loc);
            return true;
        }
        return false;
    }
}
