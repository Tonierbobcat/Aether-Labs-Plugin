package com.loficostudios.minigameeventsplugin.game.events.PlateEvents;

import com.loficostudios.minigameeventsplugin.arena.SpawnPlatform;
import com.loficostudios.minigameeventsplugin.api.PlatformSelectorEvent;
import com.loficostudios.minigameeventsplugin.game.Game;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class PlateDirtEvent extends PlatformSelectorEvent {

    public PlateDirtEvent() {
        super("Random Dirt Platform", Material.DIRT, 2, 3);
    }

    @Override
    public @NotNull String getWarningMessage() {
        return "plate(s) will get a bit dirty";
    }

    @Override
    public boolean onSelect(Game game, SpawnPlatform platform) {
        Player player = platform.getPlayer();

        if (player != null) {
            platform.setPlatform(Material.DIRT);
            return true;
        }

        return true;
    }
}
