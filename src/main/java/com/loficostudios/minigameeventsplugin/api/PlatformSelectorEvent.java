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

import java.util.Collection;

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
        var bar = game.getProgressBar();
        var message = new StringBuilder();
        selectObjects(game, (platform) -> {
            if (onSelect(game, platform)) {
                if (getDisplayedEnabled()) {
                    var player = platform.getPlayer();
                    message.append(" ").append(player != null ? player.getName() : "NaN");
                    bar.setTitle(message.toString());
                }

                game.getPlayers().notify(
                        NotificationType.GLOBAL,
                        Sound.BLOCK_NOTE_BLOCK_PLING,
                        1, 2);
            }
        },platforms -> onComplete(game, platforms));
    }

    @Override
    public void end(Game game) {
        game.getProgressBar().removeAll();
    }
}
