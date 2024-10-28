package com.loficostudios.minigameeventsplugin.GameEvents.PlayerEvents;

import com.loficostudios.minigameeventsplugin.GameEvents.BaseEvent;
import com.loficostudios.minigameeventsplugin.Utils.PlayerState;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class PlayerCakeEvent extends BaseEvent {

    @Override
    public void start() {
        Collection<Player> playersAlive = getPlayerManager().getPlayers(PlayerState.ALIVE);

        playersAlive.forEach(player -> {
            player.getInventory().addItem(new ItemStack(Material.CAKE));
        });
    }

    @Override
    public @NotNull String getName() {
        return "Cake Event";
    }

    @Override
    public @NotNull String warningMessage() {
        return "All players will get cake!";
    }

    @Override
    public void run() {
        getPlayerManager().getPlayers(PlayerState.ALIVE).forEach(player ->
                player.getWorld().spawnParticle(
                        Particle.HEART,
                        player.getLocation().add(0, 1, 0), 10));
    }


}
