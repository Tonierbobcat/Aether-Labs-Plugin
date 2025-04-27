package com.loficostudios.minigameeventsplugin.game.events.PlayerEvents;

import com.loficostudios.minigameeventsplugin.api.PlayerSelectorEvent;
import com.loficostudios.minigameeventsplugin.game.Game;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

import static com.loficostudios.minigameeventsplugin.game.events.Items.ROSE;

public class PlayerFlowerEvent extends PlayerSelectorEvent {
    public PlayerFlowerEvent() {
        super("Player Rose Event", Material.POPPY, 1, 3);
    }

    @Override
    public boolean onSelect(Game game, Player player) {
        player.getInventory().addItem(ROSE);
        return true;
    }

    @Override
    public @NotNull String getWarningMessage() {
        return "player(s) will  be given a rose! <3";
    }
}
