package com.loficostudios.minigameeventsplugin.GameEvents.PlayerEvents;

import com.loficostudios.minigameeventsplugin.GameEvents.RandomPlayerSelectorEvent;
import com.loficostudios.minigameeventsplugin.Interfaces.IPlayerEvent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

import static com.loficostudios.minigameeventsplugin.Utils.EventUtils.effectPlayer;

public class PlayerSickEvent extends RandomPlayerSelectorEvent implements IPlayerEvent {
    @Override
    public boolean onSelect(Player selectedPlayer) {

        effectPlayer(selectedPlayer, PotionEffectType.POISON, 10, 1);
        effectPlayer(selectedPlayer, PotionEffectType.HUNGER, 10, 1);

        return true;
    }

    @Override
    public void onComplete(Collection<Player> selectedPlayers) {

    }

    @Override
    public @NotNull String getName() {
        return "Player sick event";
    }

    @Override
    public @NotNull String getWarningMessage() {
        return "player(s) ate something bad and got food poisoning";
    }

    @Override
    public @NotNull Material getDisplayMaterial() {
        return Material.SPIDER_EYE;
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
