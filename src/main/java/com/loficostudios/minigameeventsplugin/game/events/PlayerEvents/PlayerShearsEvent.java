package com.loficostudios.minigameeventsplugin.game.events.PlayerEvents;

import com.loficostudios.minigameeventsplugin.api.PlayerSelectorEvent;
import com.loficostudios.minigameeventsplugin.game.Game;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

import static com.loficostudios.minigameeventsplugin.game.events.Items.BROKEN_SHEARS;

public class PlayerShearsEvent extends PlayerSelectorEvent {

    public PlayerShearsEvent() {
        super("Player shears event", Material.SHEARS, 1, 3);
    }

    @Override
    public boolean onSelect(Game game, Player player) {
        player.getInventory().addItem(BROKEN_SHEARS);
        return true;
    }

    @Override
    public @NotNull String getWarningMessage() {
        return "player(s) will be given a broken pair of shears.";
    }
}
