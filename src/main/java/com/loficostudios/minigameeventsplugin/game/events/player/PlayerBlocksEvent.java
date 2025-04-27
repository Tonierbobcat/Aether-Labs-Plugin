package com.loficostudios.minigameeventsplugin.game.events.player;

import com.loficostudios.minigameeventsplugin.api.PlayerSelectorEvent;

import com.loficostudios.minigameeventsplugin.game.Game;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class PlayerBlocksEvent extends PlayerSelectorEvent {

    public PlayerBlocksEvent() {
        super("Flammable Block Event", Material.WHITE_WOOL, 1, 3);
    }

    @Override
    public @NotNull String getWarningMessage() {
        return "player(s) will gain some flammable building blocks.";
    }

    @Override
    public boolean onSelect(Game game, Player selectedPlayer) {
        selectedPlayer.getInventory()
                .addItem(new ItemStack(Material.WHITE_WOOL, 32));

        return true;
    }
}
