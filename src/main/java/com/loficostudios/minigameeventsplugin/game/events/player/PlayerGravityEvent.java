package com.loficostudios.minigameeventsplugin.game.events.player;


import com.loficostudios.minigameeventsplugin.api.PlayerSelectorEvent;
import com.loficostudios.minigameeventsplugin.game.Game;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import static com.loficostudios.minigameeventsplugin.utils.EventUtils.effectPlayer;

public class PlayerGravityEvent extends PlayerSelectorEvent {

    public PlayerGravityEvent() {
        super("Gravity Event", Material.FEATHER, 1, 3);
    }

    @Override
    public @NotNull String getWarningMessage() {
        return "player(s) will feel lighter than usual";
    }


    @Override
    public boolean onSelect(Game game, Player player) {
        effectPlayer(player, PotionEffectType.JUMP_BOOST, 5940, 1);
        effectPlayer(player, PotionEffectType.SLOW_FALLING, 5940, 1);

        return true;
    }
}

