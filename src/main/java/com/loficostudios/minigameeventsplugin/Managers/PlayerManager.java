package com.loficostudios.minigameeventsplugin.Managers;

import com.loficostudios.minigameeventsplugin.MiniGameEventsPlugin;
import com.loficostudios.minigameeventsplugin.Utils.PlayerState;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerManager {

    private final MiniGameEventsPlugin plugin;

    private final Map<Player, PlayerState> players = new HashMap<>();

    public PlayerManager(MiniGameEventsPlugin plugin) {
        this.plugin = plugin;
    }

    public Player getLastPlayerAlive() {

        List<Player> alivePlayers = (List<Player>) getPlayers(PlayerState.ALIVE);
        if (alivePlayers.size() == 1) {
            return alivePlayers.get(0);
        }
        else {
            return null;
        }
    }

    public void resetPlayers() {
        players.clear();
    }

    //region Player Functions
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

    public void tagPlayer(Player player, PlayerState state) {
        PlayerState oldState = getPlayerState(player);

        if (oldState.equals(state))
            return;

        if (state != null)
            setPlayerState(player, PlayerState.DEAD);
    }

    public void setPlayerState(Player player, PlayerState state) {
        players.put(player, state);
    }

    public void addPlayer(Player player, PlayerState state) {
        players.put(player, state);
    }

    public PlayerState getPlayerState(Player player) {
        return players.get(player);
    }
}
