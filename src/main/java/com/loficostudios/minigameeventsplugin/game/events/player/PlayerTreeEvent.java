package com.loficostudios.minigameeventsplugin.game.events.player;

import com.loficostudios.minigameeventsplugin.api.PlayerSelectorEvent;
import com.loficostudios.minigameeventsplugin.game.Game;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class PlayerTreeEvent extends PlayerSelectorEvent {

    private final Random random = new Random();

    private final ItemStack[] saplings = new ItemStack[] {
            new ItemStack(Material.OAK_SAPLING),
            new ItemStack(Material.SPRUCE_SAPLING),
            new ItemStack(Material.BIRCH_SAPLING),
            new ItemStack(Material.JUNGLE_SAPLING),
            new ItemStack(Material.ACACIA_SAPLING),
            new ItemStack(Material.DARK_OAK_SAPLING),
            new ItemStack(Material.CHERRY_SAPLING)
    };

    private final ItemStack[] items = new ItemStack[] {
            saplings[random.nextInt(saplings.length)],
            new ItemStack(Material.BONE_MEAL, random.nextInt(4, 6)),
            new ItemStack(Material.DIRT)
    };

    public PlayerTreeEvent() {
        super("Player Tree Event", Material.OAK_SAPLING, 2, 4);
    }

    @Override
    public boolean onSelect(Game game, Player selectedPlayer) {
        for (ItemStack item : items) {
            selectedPlayer.getInventory().addItem(item);
        }
        return true;
    }

    @Override
    public @NotNull String getWarningMessage() {
        return "player(s) will start a small tree farm";
    }
}
