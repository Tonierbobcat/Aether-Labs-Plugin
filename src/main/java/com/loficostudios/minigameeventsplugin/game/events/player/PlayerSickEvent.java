package com.loficostudios.minigameeventsplugin.game.events.player;

import com.loficostudios.minigameeventsplugin.api.PlayerSelectorEvent;
import com.loficostudios.minigameeventsplugin.game.Game;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import static com.loficostudios.minigameeventsplugin.utils.EventUtils.effectPlayer;

public class PlayerSickEvent extends PlayerSelectorEvent {
    public PlayerSickEvent() {
        super("Player sick event", Material.SPIDER_EYE, 1, 3);
    }

    @Override
    public boolean onSelect(Game game, Player selectedPlayer) {

        effectPlayer(selectedPlayer, PotionEffectType.POISON, 10, 1);
        effectPlayer(selectedPlayer, PotionEffectType.HUNGER, 10, 1);

        return true;
    }

    @Override
    public @NotNull String getWarningMessage() {
        return "player(s) ate something bad and got food poisoning";
    }
}
