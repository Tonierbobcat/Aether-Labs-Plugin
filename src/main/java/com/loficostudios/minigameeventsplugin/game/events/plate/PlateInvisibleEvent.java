package com.loficostudios.minigameeventsplugin.game.events.plate;

import com.loficostudios.minigameeventsplugin.api.PlatformSelectorEvent;
import com.loficostudios.minigameeventsplugin.game.Game;
import com.loficostudios.minigameeventsplugin.game.arena.SpawnPlatform;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PlateInvisibleEvent extends PlatformSelectorEvent {

    public PlateInvisibleEvent() {
        super("Invisible Plate Event", Material.GLASS, 1, 3);
    }

    @Override
    public boolean onSelect(Game game, SpawnPlatform platform) {
        Player player = platform.getPlayer();

        if (player != null) {
            platform.setPlatform(Material.BARRIER);
            return true;
        }

        return true;
    }

    @Override
    public @NotNull String getWarningMessage() {
        return "plate(s) will become completely transparent.";
    }
}
