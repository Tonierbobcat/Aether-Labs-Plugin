package com.loficostudios.minigameeventsplugin.GameEvents.PlayerEvents;

import com.loficostudios.minigameeventsplugin.GameEvents.RandomPlayerSelectorEvent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class PlayerBlocksEvent extends RandomPlayerSelectorEvent {

    @Override
    public @NotNull String getName() {
        return "Flammable Block Event";
    }

    @Override
    public @NotNull String warningMessage() {
        return getAmount() + " player(s) will gain some flammable building blocks.";
    }


    @Override
    public boolean onSelect(Player selectedPlayer) {
        selectedPlayer.getInventory()
                .addItem(new ItemStack(Material.WHITE_WOOL, 32));

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
