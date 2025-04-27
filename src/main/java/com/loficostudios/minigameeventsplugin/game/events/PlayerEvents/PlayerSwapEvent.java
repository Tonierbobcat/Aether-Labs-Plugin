package com.loficostudios.minigameeventsplugin.game.events.PlayerEvents;

import com.loficostudios.minigameeventsplugin.api.PlayerSelectorEvent;
import com.loficostudios.minigameeventsplugin.api.event.EventType;
import com.loficostudios.minigameeventsplugin.game.Game;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class PlayerSwapEvent extends PlayerSelectorEvent {

    public PlayerSwapEvent() {
        super("Swap Event", Material.ENDER_PEARL, 1, 1);
    }

    @Override
    public boolean onSelect(Game game, Player selectedPlayer) {
        List<Player> players = new ArrayList<>(getObjects(game)
                .stream()
                .filter(player -> !player.getUniqueId().equals(selectedPlayer.getUniqueId()))
                .toList());
        if (players.isEmpty())
            return false;

        Random random = new Random();
        Player target = players.get(random.nextInt(players.size()));

        if (target == null)
            return false;

        Location cache = target.getLocation();

        target.teleport(selectedPlayer.getLocation());

        selectedPlayer.teleport(cache);

        return true;
    }

    @Override
    public void onComplete(Game game, Collection<Player> selected) {
    }

    @Override
    public @NotNull String getWarningMessage() {
        return "player(s) will swap with someone random.";
    }
}
