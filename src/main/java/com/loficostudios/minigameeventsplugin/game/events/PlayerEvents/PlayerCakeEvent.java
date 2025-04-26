package com.loficostudios.minigameeventsplugin.game.events.PlayerEvents;

import com.loficostudios.minigameeventsplugin.api.event.impl.AbstractGameEvent;
import com.loficostudios.minigameeventsplugin.api.event.EventType;
import com.loficostudios.minigameeventsplugin.utils.PlayerState;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class PlayerCakeEvent extends AbstractGameEvent {

    public PlayerCakeEvent() {
        super();
    }

    @Override
    public @NotNull EventType getType() {
        return EventType.PLAYER;
    }

    @Override
    public void start() {
        Collection<Player> playersAlive = getPlayerManager().getPlayersInGame(PlayerState.ALIVE);

        playersAlive.forEach(player -> {
            player.getInventory().addItem(new ItemStack(Material.CAKE));
        });
    }

    @Override
    public @NotNull String getName() {
        return "Cake Event";
    }

    @Override
    public @NotNull String getWarningMessage() {
        return "All players will get cake!";
    }

    @Override
    public @NotNull Material getIcon() {
        return Material.CAKE;
    }

    @Override
    public void run() {
        getPlayerManager().getPlayersInGame(PlayerState.ALIVE).forEach(player ->
                player.getWorld().spawnParticle(
                        Particle.HEART,
                        player.getLocation().add(0, 1, 0), 10));
    }


}
