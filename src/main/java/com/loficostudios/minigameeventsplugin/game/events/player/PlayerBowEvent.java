package com.loficostudios.minigameeventsplugin.game.events.player;

import com.loficostudios.minigameeventsplugin.api.PlayerSelectorEvent;
import com.loficostudios.minigameeventsplugin.game.Game;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;


public class PlayerBowEvent extends PlayerSelectorEvent {

    public PlayerBowEvent() {
        super("Player Bow Event", Material.BOW, 1, 3);
    }

    @Override
    public boolean onSelect(Game game, Player selectedPlayer) {
        selectedPlayer.getInventory().addItem(new ItemStack(Material.BOW));
        selectedPlayer.getInventory().addItem(new ItemStack(Material.ARROW, 3));

        return true;
    }

    @Override
    public @NotNull String getWarningMessage() {
        return "player(s) will find a bow on the ground";
    }
}
