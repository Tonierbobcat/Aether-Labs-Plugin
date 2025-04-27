package com.loficostudios.minigameeventsplugin.game.events.PlayerEvents;

import com.loficostudios.minigameeventsplugin.api.PlayerSelectorEvent;
import com.loficostudios.minigameeventsplugin.game.Game;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

import static com.loficostudios.minigameeventsplugin.utils.EventUtils.effectPlayer;

public class PlayerHasteEvent extends PlayerSelectorEvent {
    public PlayerHasteEvent() {
        super("Haste Event", Material.GOLDEN_PICKAXE, 1, 3);
    }

    @Override
    public boolean onSelect(Game game, Player player) {
        effectPlayer(player, PotionEffectType.HASTE, 30, 2);
        return true;
    }

    @Override
    public @NotNull String getWarningMessage() {
        return "player(s) will have quicker arms.";
    }
}
