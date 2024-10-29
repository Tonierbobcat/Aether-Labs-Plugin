package com.loficostudios.minigameeventsplugin.GameEvents.PlayerEvents;

import com.loficostudios.minigameeventsplugin.GameEvents.RandomPlayerSelectorEvent;
import com.loficostudios.minigameeventsplugin.Interfaces.IPlayerEvent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

import static com.loficostudios.minigameeventsplugin.Utils.EventUtils.effectPlayer;

public class PlayerFloatEvent extends RandomPlayerSelectorEvent implements IPlayerEvent {
    @Override
    public boolean onSelect(Player selectedPlayer) {
        effectPlayer(selectedPlayer, PotionEffectType.LEVITATION, 10, 1);
        return true;
    }

    @Override
    public void onComplete(Collection<Player> selectedPlayers) {

    }

    @Override
    public @NotNull String getName() {
        return "Player float event";
    }

    @Override
    public @NotNull String getWarningMessage() {
        return "player(s) will start floating to the sky!";
    }

    @Override
    public @NotNull Material getDisplayMaterial() {
        return Material.ELYTRA;

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
