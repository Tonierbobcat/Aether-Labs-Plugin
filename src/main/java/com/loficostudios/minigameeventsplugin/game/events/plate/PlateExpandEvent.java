package com.loficostudios.minigameeventsplugin.game.events.plate;

import com.loficostudios.minigameeventsplugin.api.event.EventType;
import com.loficostudios.minigameeventsplugin.api.event.impl.AbstractGameEvent;
import com.loficostudios.minigameeventsplugin.game.Game;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

public class PlateExpandEvent extends AbstractGameEvent {

    public PlateExpandEvent() {
        super("Expand Event", EventType.PLATE, Material.STONE_BRICKS);
    }

    @Override
    public @NotNull String getWarningMessage() {
        return "All plates will expand";
    }


    @Override
    public void start(Game game) {
        game.getArena().getSpawnPlatforms().forEach(platform -> platform.setRadius(platform.getRadius() + 1));
    }

    @Override
    public double getCost() { //todo debug
        return 1;
    }
}
