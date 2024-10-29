package com.loficostudios.minigameeventsplugin.GameEvents.PlayerEvents;

import com.loficostudios.minigameeventsplugin.GameEvents.RandomPlayerSelectorEvent;
import com.loficostudios.minigameeventsplugin.Interfaces.IPlayerEvent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

import static com.loficostudios.minigameeventsplugin.Utils.EventUtils.effectPlayer;

public class PlayerGhostEvent extends RandomPlayerSelectorEvent implements IPlayerEvent {
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
