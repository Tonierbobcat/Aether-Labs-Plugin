package com.loficostudios.minigameeventsplugin.game.events.world;

import com.loficostudios.minigameeventsplugin.api.event.EventType;
import com.loficostudios.minigameeventsplugin.api.event.impl.AbstractGameEvent;
import com.loficostudios.minigameeventsplugin.game.Game;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

public class WorldPlateRepairEvent extends AbstractGameEvent {
    public WorldPlateRepairEvent() {
        super("Plate Repair Event", EventType.GLOBAL, Material.STONE_BRICKS);
    }

    @Override
    public void start(Game game) {
        game.getArena().getSpawnPlatforms().forEach(spawnPlatform -> {
            spawnPlatform.recreate(spawnPlatform.getMaterial());
        });
    }

    @Override
    public @NotNull String getWarningMessage() {
        return "All plates will be repaired.";
    }
}
