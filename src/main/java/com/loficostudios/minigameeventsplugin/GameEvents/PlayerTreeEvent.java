package com.loficostudios.minigameeventsplugin.GameEvents;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Random;

public class PlayerTreeEvent extends RandomPlayerSelectorEvent {

    Random random = new Random();

    ItemStack[] saplings = new ItemStack[] {
            new ItemStack(Material.OAK_SAPLING),
            new ItemStack(Material.SPRUCE_SAPLING),
            new ItemStack(Material.BIRCH_SAPLING),
            new ItemStack(Material.JUNGLE_SAPLING),
            new ItemStack(Material.ACACIA_SAPLING),
            new ItemStack(Material.DARK_OAK_SAPLING),
            new ItemStack(Material.CHERRY_SAPLING)
    };

    ItemStack[] items = new ItemStack[] {
            saplings[random.nextInt(saplings.length)],
            new ItemStack(Material.BONE_MEAL, random.nextInt(4, 6)),
            new ItemStack(Material.DIRT)
    };

    @Override
    public boolean onSelect(Player selectedPlayer) {

        for (ItemStack item : items) {
            selectedPlayer.getInventory().addItem(item);
        }
        return true;
    }

    @Override
    public void onComplete(Collection<Player> selectedPlayers) {

    }

    @Override
    public @NotNull String getName() {
        return "Player Tree Event";
    }

    @Override
    public @NotNull String getWarningMessage() {
        return "player(s) will start a small tree farm";
    }

    @Override
    public @NotNull Material getDisplayMaterial() {
        return Material.OAK_SAPLING;
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
