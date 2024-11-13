package com.loficostudios.minigameeventsplugin.gameEvents.PlayerEvents;

import com.loficostudios.minigameeventsplugin.api.RandomPlayerSelectorEvent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

import static com.loficostudios.minigameeventsplugin.gameEvents.Items.ROSE;

public class PlayerFlowerEvent extends RandomPlayerSelectorEvent {
    @Override
    public boolean onSelect(Player selectedPlayer) {
        selectedPlayer.getInventory().addItem(ROSE);
        return true;
    }

    @Override
    public void onComplete(Collection<Player> selectedPlayers) {

    }

    @Override
    public @NotNull String getName() {
        return "Player Rose Event";
    }

    @Override
    public @NotNull String getWarningMessage() {
        return "player(s) will  be given a rose! <3";
    }

    @Override
    public @NotNull Material getDisplayMaterial() {
        return Material.POPPY;
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
