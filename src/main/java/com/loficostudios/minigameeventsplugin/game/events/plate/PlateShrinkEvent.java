package com.loficostudios.minigameeventsplugin.game.events.plate;

import com.loficostudios.minigameeventsplugin.api.event.EventType;
import com.loficostudios.minigameeventsplugin.api.event.impl.AbstractGameEvent;
import com.loficostudios.minigameeventsplugin.game.Game;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import static com.loficostudios.minigameeventsplugin.utils.Debug.log;

public class PlateShrinkEvent extends AbstractGameEvent {
    public PlateShrinkEvent() {
        super("Platform Shrink Event", EventType.PLATE, Material.CRACKED_STONE_BRICKS);
    }

    @Override
    public @NotNull String getWarningMessage() {
        return "All plates will shrink";
    }

    @Override
    public void start(Game game) {
        game.getArena().getSpawnPlatforms().forEach(platform -> {
            if (!platform.setRadius(platform.getRadius() - 1)) {
                log("removing platform");
                game.getArena().removeSpawnPlatform(platform, false);
            }
        });
    }
}
