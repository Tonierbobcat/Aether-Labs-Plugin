package com.loficostudios.minigameeventsplugin.game.events.player;

import com.loficostudios.minigameeventsplugin.api.PlayerSelectorEvent;
import com.loficostudios.minigameeventsplugin.game.Game;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PlayerColdEvent extends PlayerSelectorEvent {

    public PlayerColdEvent() {
        super("Antarctica Event", Material.SNOW_BLOCK, 1, 3);
    }

    @Override
    public boolean onSelect(Game game, Player player) {
        player.setFreezeTicks(10 * 4 * 20);
        return true;
    }

    @Override
    public @NotNull String getWarningMessage() {
        return "player(s) will be sent to Antarctica without any winter gear.";
    }
}
