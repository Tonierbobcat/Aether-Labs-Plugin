package com.loficostudios.minigameeventsplugin.game.events.player;

import com.loficostudios.minigameeventsplugin.api.PlayerSelectorEvent;
import com.loficostudios.minigameeventsplugin.game.Game;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static com.loficostudios.minigameeventsplugin.game.events.Items.FISHERMANS_ROD;

public class PlayerFishingRodEvent extends PlayerSelectorEvent {

    public PlayerFishingRodEvent() {
        super("Fishing rod event", Material.FISHING_ROD, 1, 3);
    }

    @Override
    public boolean onSelect(Game game, Player player) {
        player.getInventory().addItem(FISHERMANS_ROD);
        return true;
    }

    @Override
    public @NotNull String getWarningMessage() {
        return "player(s) will be a fisherman";
    }
}
