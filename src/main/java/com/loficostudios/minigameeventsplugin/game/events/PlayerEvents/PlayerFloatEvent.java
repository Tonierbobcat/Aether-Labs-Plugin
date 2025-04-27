package com.loficostudios.minigameeventsplugin.game.events.PlayerEvents;

import com.loficostudios.minigameeventsplugin.api.PlayerSelectorEvent;
import com.loficostudios.minigameeventsplugin.game.Game;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

import static com.loficostudios.minigameeventsplugin.utils.EventUtils.effectPlayer;

public class PlayerFloatEvent extends PlayerSelectorEvent {
    public PlayerFloatEvent() {
        super("Player float event", Material.ELYTRA, 1, 3);
    }

    @Override
    public boolean onSelect(Game game, Player player) {
        effectPlayer(player, PotionEffectType.LEVITATION, 10, 1);
        return true;
    }

    @Override
    public @NotNull String getWarningMessage() {
        return "player(s) will start floating to the sky!";
    }
}
