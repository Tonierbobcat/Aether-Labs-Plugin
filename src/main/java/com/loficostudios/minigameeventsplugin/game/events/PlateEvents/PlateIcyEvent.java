package com.loficostudios.minigameeventsplugin.game.events.PlateEvents;

import com.loficostudios.minigameeventsplugin.api.PlatformSelectorEvent;
import com.loficostudios.minigameeventsplugin.arena.SpawnPlatform;
import com.loficostudios.minigameeventsplugin.game.Game;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PlateIcyEvent extends PlatformSelectorEvent {
    public PlateIcyEvent() {
        super("Plate Icy Event", Material.ICE, 1, 3);
    }

    @Override
    public @NotNull String getWarningMessage() {
        return "plate(s) will be a bit slippery.";
    }

    @Override
    public boolean onSelect(Game game, SpawnPlatform platform) {
        Player player = platform.getPlayer();

        if (player != null) {
            platform.setPlatform(Material.ICE);
            return true;
        }

        return true;
    }
}
