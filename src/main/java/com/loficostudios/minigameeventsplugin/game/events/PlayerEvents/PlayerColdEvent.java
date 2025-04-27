package com.loficostudios.minigameeventsplugin.game.events.PlayerEvents;

import com.loficostudios.minigameeventsplugin.api.PlayerSelectorEvent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class PlayerColdEvent extends PlayerSelectorEvent {

    @Override
    public boolean onSelect(Player selectedPlayer) {
        selectedPlayer.setFreezeTicks(10 * 4 * 20);
        return true;
    }

    @Override
    public void onComplete(Collection<Player> selectedPlayers) {

    }

    @Override
    public @NotNull String getName() {
        return "Antarctica Event";
    }

    @Override
    public @NotNull String getWarningMessage() {
        return "player(s) will be sent to Antarctica without any winter gear.";
    }

    @Override
    public @NotNull Material getIcon() {
        return Material.SNOW_BLOCK;
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
