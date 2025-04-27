package com.loficostudios.minigameeventsplugin.game.events.PlayerEvents;

import com.loficostudios.minigameeventsplugin.api.PlayerSelectorEvent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

import static com.loficostudios.minigameeventsplugin.game.events.Items.FISHERMANS_ROD;

public class PlayerFishingRodEvent extends PlayerSelectorEvent {



    @Override
    public boolean onSelect(Player selectedPlayer) {

        selectedPlayer.getInventory().addItem(FISHERMANS_ROD);

        return true;
    }

    @Override
    public void onComplete(Collection<Player> selectedPlayers) {

    }

    @Override
    public @NotNull String getName() {
        return "Fishing rod event";
    }

    @Override
    public @NotNull String getWarningMessage() {
        return "player(s) will be a fisherman";
    }

    @Override
    public @NotNull Material getIcon() {
        return Material.FISHING_ROD;
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
