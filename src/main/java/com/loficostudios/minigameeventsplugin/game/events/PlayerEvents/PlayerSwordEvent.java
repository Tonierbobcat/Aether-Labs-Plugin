package com.loficostudios.minigameeventsplugin.game.events.PlayerEvents;


import com.loficostudios.minigameeventsplugin.api.PlayerSelectorEvent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Random;

public class PlayerSwordEvent extends PlayerSelectorEvent {

    private final Random random = new Random();
    private final ItemStack[] swords = new ItemStack[] {
            new ItemStack(Material.NETHERITE_SWORD),
            new ItemStack(Material.DIAMOND_SWORD),
            new ItemStack(Material.IRON_SWORD),
            new ItemStack(Material.GOLDEN_SWORD),
            new ItemStack(Material.STONE_SWORD),
            new ItemStack(Material.WOODEN_SWORD)
    };


    @Override
    public @NotNull String getName() {
        return "Random Sword Event";
    }

    @Override
    public @NotNull String getWarningMessage() {
        return "player(s) will be granted a random sword.";
    }

    @Override
    public @NotNull Material getIcon() {
        return Material.DIAMOND_SWORD;
    }

    @Override
    public boolean onSelect(Player selectedPlayer) {
        selectedPlayer.getInventory()
                .addItem(swords[random.nextInt(swords.length)]);
        return true;
    }

    @Override
    public void onComplete(Collection<Player> selectedPlayers) {
    }

    @Override
    public Integer getMin() {
        return 1;
    }

    @Override
    public Integer getMax() {
        return 3;
    }
}
