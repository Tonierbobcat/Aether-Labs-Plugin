package com.loficostudios.minigameeventsplugin.gameEvents.PlayerEvents;

import com.loficostudios.minigameeventsplugin.api.RandomPlayerSelectorEvent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import java.util.Collection;

import static com.loficostudios.minigameeventsplugin.utils.EventUtils.*;


public class PlayerSlowEvent extends RandomPlayerSelectorEvent {

    @Override
    public boolean onSelect(Player selectedPlayer) {
        effectPlayer(selectedPlayer, PotionEffectType.SLOWNESS, 15, 2);
        return true;
    }

    @Override
    public void onComplete(Collection<Player> selectedPlayers) {

    }





    @Override
    public @NotNull String getName() {
        return "Slowness";
    }

    @Override
    public @NotNull String getWarningMessage() {
        return "players(s) will be a bit slower than normal.";
    }

    @Override
    public @NotNull Material getDisplayMaterial() {
        return Material.SOUL_SAND;
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
