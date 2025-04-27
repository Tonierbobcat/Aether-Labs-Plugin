package com.loficostudios.minigameeventsplugin.game.events.player;

import com.loficostudios.minigameeventsplugin.api.PlayerSelectorEvent;
import com.loficostudios.minigameeventsplugin.game.Game;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import static com.loficostudios.minigameeventsplugin.utils.EventUtils.effectPlayer;

public class PlayerSpeedEvent extends PlayerSelectorEvent {
    public PlayerSpeedEvent() {
        super("Player Sonic Event", Material.POTION, 1, 3);
    }

    @Override
    public boolean onSelect(Game game, Player selectedPlayer) {
        effectPlayer(selectedPlayer, PotionEffectType.SPEED, 15, 2);
        return true;
    }

    @Override
    public @NotNull String getWarningMessage() {
        return "player(s) wanna be just like sonic!";
    }
}
