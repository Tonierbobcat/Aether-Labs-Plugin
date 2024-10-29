package com.loficostudios.minigameeventsplugin.GameEvents.PlayerEvents;

import com.loficostudios.minigameeventsplugin.GameEvents.RandomPlayerSelectorEvent;
import com.loficostudios.minigameeventsplugin.Interfaces.IPlayerEvent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class PlayerBowEvent extends RandomPlayerSelectorEvent implements IPlayerEvent {

    @Override
    public boolean onSelect(Player selectedPlayer) {

        selectedPlayer.getInventory().addItem(new ItemStack(Material.BOW));
        selectedPlayer.getInventory().addItem(new ItemStack(Material.ARROW, 3));

        return true;
    }

    @Override
    public void onComplete(Collection<Player> selectedPlayers) {

    }


    @Override
    public @NotNull String getName() {
        return "Player Bow Event";
    }

    @Override
    public @NotNull String getWarningMessage() {
        return "player(s) will find a bow on the ground";
    }

    @Override
    public @NotNull Material getDisplayMaterial() {
        return Material.BOW;
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
