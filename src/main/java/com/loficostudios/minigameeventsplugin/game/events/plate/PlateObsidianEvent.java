package com.loficostudios.minigameeventsplugin.game.events.plate;

import com.loficostudios.minigameeventsplugin.api.PlatformSelectorEvent;
import com.loficostudios.minigameeventsplugin.arena.SpawnPlatform;
import com.loficostudios.minigameeventsplugin.game.Game;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PlateObsidianEvent extends PlatformSelectorEvent {

    public PlateObsidianEvent() {
        super("Obsidian Event", Material.OBSIDIAN, 2, 3);
    }

    @Override
    public boolean onSelect(Game game, SpawnPlatform platform) {
        Player player = platform.getPlayer();

        if (player != null) {
            platform.recreate(Material.OBSIDIAN);
            return true;
        }

        return true;
    }

    @Override
    public @NotNull String getWarningMessage() {
        return "plate(s) will become solid";
    }
}
