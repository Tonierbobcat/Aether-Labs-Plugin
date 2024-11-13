package com.loficostudios.minigameeventsplugin.gameEvents.PlayerEvents;

import com.loficostudios.minigameeventsplugin.api.RandomPlayerSelectorEvent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

import static com.loficostudios.minigameeventsplugin.gameEvents.Items.BROKEN_SHEARS;

public class PlayerShearsEvent extends RandomPlayerSelectorEvent {
    @Override
    public boolean onSelect(Player selectedPlayer) {

        selectedPlayer.getInventory().addItem(BROKEN_SHEARS);
        return true;
    }

    @Override
    public void onComplete(Collection<Player> selectedPlayers) {

    }

    @Override
    public @NotNull String getName() {
        return "Player shears event";
    }

    @Override
    public @NotNull String getWarningMessage() {
        return "player(s) will be given a broken pair of shears.";
    }

    @Override
    public @NotNull Material getDisplayMaterial() {
        return Material.SHEARS;
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
