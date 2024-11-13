package com.loficostudios.minigameeventsplugin.gameEvents.PlayerEvents;

import com.loficostudios.minigameeventsplugin.api.RandomPlayerSelectorEvent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

import static com.loficostudios.minigameeventsplugin.utils.EventUtils.effectPlayer;

public class PlayerGhostEvent extends RandomPlayerSelectorEvent {
    @Override
    public boolean onSelect(Player selectedPlayer) {
        effectPlayer(selectedPlayer, PotionEffectType.INVISIBILITY, 30, 1);
        return true;
    }

    @Override
    public void onComplete(Collection<Player> selectedPlayers) {

    }

    @Override
    public @NotNull String getName() {
        return "Ghost Event";
    }

    @Override
    public @NotNull String getWarningMessage() {
        return "player(s) will become Casper the Ghost.";
    }

    @Override
    public @NotNull Material getDisplayMaterial() {
        return Material.GLASS;
    }

    @Override
    public Integer getMin() {
        return 1;
    }

    @Override
    public Integer getMax() {
        return 3;
    }
}
