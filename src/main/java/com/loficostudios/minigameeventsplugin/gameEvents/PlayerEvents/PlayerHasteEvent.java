package com.loficostudios.minigameeventsplugin.gameEvents.PlayerEvents;

import com.loficostudios.minigameeventsplugin.api.RandomPlayerSelectorEvent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

import static com.loficostudios.minigameeventsplugin.utils.EventUtils.effectPlayer;

public class PlayerHasteEvent extends RandomPlayerSelectorEvent {
    @Override
    public boolean onSelect(Player selectedPlayer) {

        effectPlayer(selectedPlayer, PotionEffectType.HASTE, 30, 2);
        return true;
    }

    @Override
    public void onComplete(Collection<Player> selectedPlayers) {

    }

    @Override
    public @NotNull String getName() {
        return "Haste Event";
    }

    @Override
    public @NotNull String getWarningMessage() {
        return "player(s) will have quicker arms.";
    }

    @Override
    public @NotNull Material getDisplayMaterial() {
        return Material.GOLDEN_PICKAXE;
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
