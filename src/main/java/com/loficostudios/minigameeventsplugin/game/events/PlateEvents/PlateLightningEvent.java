package com.loficostudios.minigameeventsplugin.game.events.PlateEvents;

import com.loficostudios.minigameeventsplugin.api.PlatformSelectorEvent;
import com.loficostudios.minigameeventsplugin.arena.SpawnPlatform;
import com.loficostudios.minigameeventsplugin.game.Game;
import com.loficostudios.minigameeventsplugin.managers.NotificationType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PlateLightningEvent extends PlatformSelectorEvent {

    public PlateLightningEvent() {
        super("Lightning event", Material.LIGHTNING_ROD, 1, 3);
    }

    @Override
    public boolean onSelect(Game game, SpawnPlatform platform) {

        Player player = platform.getPlayer();

        if (player != null) {
            Location location = platform.getTeleportLocation();

            for (int i = 0; i < 3; i++) {
                game.getArena().getWorld().strikeLightning(location);
                game.getPlayers().notify(
                        NotificationType.GLOBAL,
                        Sound.ENTITY_LIGHTNING_BOLT_IMPACT,
                        0.5f, 1);
            }
            return true;
        }

        return true;
    }

    @Override
    public @NotNull String getWarningMessage() {
        return "plate(s) will get a burst of lightning.";
    }

    @Override
    public Integer getWarningTime() {
        return 10;
    }
}
