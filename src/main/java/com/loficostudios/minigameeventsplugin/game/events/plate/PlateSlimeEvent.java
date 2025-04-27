package com.loficostudios.minigameeventsplugin.game.events.plate;

import com.loficostudios.minigameeventsplugin.api.event.EventType;
import com.loficostudios.minigameeventsplugin.api.event.impl.AbstractGameEvent;
import com.loficostudios.minigameeventsplugin.game.Game;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

public class PlateSlimeEvent extends AbstractGameEvent {

    public PlateSlimeEvent() {
        super("Trampoline Event", EventType.PLATE, Material.SLIME_BLOCK);
    }

    @Override
    public @NotNull String getWarningMessage() {
        return "All plates will become a trampoline";
    }

    @Override
    public void start(Game game) {
        game.getArena().getSpawnPlatforms().forEach(platform -> {
            platform.setPlatform(Material.SLIME_BLOCK);
        });
    }
}
