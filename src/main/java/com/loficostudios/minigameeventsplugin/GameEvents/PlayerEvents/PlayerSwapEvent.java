package com.loficostudios.minigameeventsplugin.GameEvents.PlayerEvents;

import com.loficostudios.minigameeventsplugin.GameEvents.RandomPlayerSelectorEvent;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class PlayerSwapEvent extends RandomPlayerSelectorEvent {

    @Override
    public boolean onSelect(Player selectedPlayer) {
        List<Player> players = new ArrayList<>(getPlayers()
                .stream()
                .filter(player -> !player.getUniqueId().equals(selectedPlayer.getUniqueId()))
                .toList());
        if (players.isEmpty())
            return false;

        Random random = new Random();
        Player target = players.get(random.nextInt(players.size()));

        if (target != null) {
            Location cache = target.getLocation();

            target.teleport(selectedPlayer.getLocation());

            selectedPlayer.teleport(cache);
            return true;
        }

        return false;
    }

    @Override
    public void onComplete(Collection<Player> selectedPlayers) {

    }



    @Override
    public @NotNull String getName() {
        return "Swap Event";
    }

    @Override
    public @NotNull String warningMessage() {
        return "1 player(s) will swap with someone random.";
    }

    @Override
    public Integer getMin() {
        return 1;
    }

    @Override
    public Integer getMax() {
        return 1;
    }
}