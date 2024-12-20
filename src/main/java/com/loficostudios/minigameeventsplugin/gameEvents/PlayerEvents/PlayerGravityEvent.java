package com.loficostudios.minigameeventsplugin.gameEvents.PlayerEvents;


import com.loficostudios.minigameeventsplugin.api.RandomPlayerSelectorEvent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

import static com.loficostudios.minigameeventsplugin.utils.EventUtils.effectPlayer;

public class PlayerGravityEvent extends RandomPlayerSelectorEvent {



    @Override
    public @NotNull String getName() {
        return "Gravity Event";
    }

    @Override
    public @NotNull String getWarningMessage() {
        return "player(s) will feel lighter than usual";
    }

    @Override
    public @NotNull Material getDisplayMaterial() {
        return Material.FEATHER;
    }


    @Override
    public Integer getMin() {
        return 1;
    }

    @Override
    public Integer getMax() {
        return 3;
    }

    @Override
    public boolean onSelect(Player selectedPlayer) {
        effectPlayer(selectedPlayer, PotionEffectType.JUMP_BOOST, 5940, 1);
        effectPlayer(selectedPlayer, PotionEffectType.SLOW_FALLING, 5940, 1);

        return true;
    }

    @Override
    public void onComplete(Collection<Player> selectedPlayers) {

    }
}

