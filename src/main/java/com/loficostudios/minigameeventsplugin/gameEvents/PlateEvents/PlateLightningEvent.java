package com.loficostudios.minigameeventsplugin.gameEvents.PlateEvents;

import com.loficostudios.minigameeventsplugin.arena.SpawnPlatform;
import com.loficostudios.minigameeventsplugin.api.RandomPlatformSelectorEvent;
import com.loficostudios.minigameeventsplugin.managers.PlayerManager.NotificationType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PlateLightningEvent extends RandomPlatformSelectorEvent {

    @Override
    protected boolean onSelect(SpawnPlatform selectedObject) {

        Player player = selectedObject.getPlayer();

        if (player != null) {
            Location location = selectedObject.getTeleportLocation();

            for (int i = 0; i < 3; i++) {
                getArena().getWorld().strikeLightning(location);
                getPlayerManager().notify(
                        NotificationType.GLOBAL,
                        Sound.ENTITY_LIGHTNING_BOLT_IMPACT,
                        0.5f, 1);
            }
            return true;
        }

        return true;
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

    @Override
    public @NotNull Integer getWarningTime() {
        return 10;
    }
}
