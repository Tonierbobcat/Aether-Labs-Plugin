package com.loficostudios.minigameeventsplugin.api;

import com.loficostudios.minigameeventsplugin.api.event.EventType;
import com.loficostudios.minigameeventsplugin.api.event.impl.AbstractSelectorEvent;
import com.loficostudios.minigameeventsplugin.arena.SpawnPlatform;
import com.loficostudios.minigameeventsplugin.game.Game;
import com.loficostudios.minigameeventsplugin.managers.NotificationType;
import com.loficostudios.minigameeventsplugin.utils.Debug;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

import static com.loficostudios.minigameeventsplugin.utils.Debug.log;
import static com.loficostudios.minigameeventsplugin.utils.Debug.logWarning;

public abstract class PlatformSelectorEvent extends AbstractSelectorEvent<SpawnPlatform> {
    protected PlatformSelectorEvent(String name, Material icon, int min, int max) {
        super(name, EventType.PLATE, icon, min, max);
    }

    @Override
    public Collection<SpawnPlatform> getObjects(Game game) {
        Debug.log("Platforms size " +  game.getArena().getSpawnPlatforms().size());
        return game.getArena().getSpawnPlatforms();
    }

    @Override
    public void onComplete(Game game, Collection<SpawnPlatform> selected) {
    }

    @Override
    public void start(Game game) {
        selectObjects(game, game.getProgressBar());
    }

    @Override
    public void end(Game game) {
        game.getProgressBar().removeAll();
    }
}
