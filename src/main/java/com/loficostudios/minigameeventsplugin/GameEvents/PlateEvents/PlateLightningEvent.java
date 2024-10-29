package com.loficostudios.minigameeventsplugin.GameEvents.PlateEvents;

import com.loficostudios.minigameeventsplugin.GameArena.SpawnPlatform;
import com.loficostudios.minigameeventsplugin.GameEvents.RandomPlayerSelectorEvent;
import com.loficostudios.minigameeventsplugin.Interfaces.IPlateEvent;
import com.loficostudios.minigameeventsplugin.Managers.GameManager.GameManager;
import com.loficostudios.minigameeventsplugin.Managers.PlayerManager.NotificationType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class PlateLightningEvent extends RandomPlayerSelectorEvent implements IPlateEvent {
    @Override
    public boolean onSelect(Player selectedPlayer) {

        SpawnPlatform spawnPlatform = getArena().getSpawnPlatform(selectedPlayer);

        if (spawnPlatform == null) {
            return false;
        }

        Location location = spawnPlatform.getTeleportLocation();

        for (int i = 0; i < 3; i++) {
            getArena().getWorld().strikeLightning(location);
            getPlayerManager().notify(
                    NotificationType.GLOBAL,
                    Sound.ENTITY_LIGHTNING_BOLT_IMPACT,
                    0.5f, 1);
        }

        return true;
    }

    @Override
    public void onComplete(Collection<Player> selectedPlayers) {

    }

    @Override
    public @NotNull String getName() {
        return "Lightning event";
    }

    @Override
    public @NotNull String getWarningMessage() {
        return "plate(s) will get a burst of lightning.";
    }

    @Override
    public @NotNull Material getDisplayMaterial() {
        return Material.LIGHTNING_ROD;
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
