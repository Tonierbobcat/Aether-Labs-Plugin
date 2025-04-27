package com.loficostudios.minigameeventsplugin.game.events.PlayerEvents;

import com.loficostudios.minigameeventsplugin.api.PlayerSelectorEvent;
import com.loficostudios.minigameeventsplugin.game.Game;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

import static com.loficostudios.minigameeventsplugin.utils.EventUtils.effectPlayer;

public class PlayerGhostEvent extends PlayerSelectorEvent {
    public PlayerGhostEvent() {
        super("Ghost Event", Material.GLASS, 1, 3);
    }

    @Override
    public boolean onSelect(Game game, Player selectedPlayer) {
        effectPlayer(selectedPlayer, PotionEffectType.INVISIBILITY, 30, 1);
        return true;
    }

    @Override
    public @NotNull String getWarningMessage() {
        return "player(s) will become Casper the Ghost.";
    }
}
