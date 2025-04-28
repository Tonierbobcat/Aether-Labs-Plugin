package com.loficostudios.minigameeventsplugin.api;

import com.loficostudios.minigameeventsplugin.api.event.EventType;
import com.loficostudios.minigameeventsplugin.api.event.impl.AbstractSelectorEvent;
import com.loficostudios.minigameeventsplugin.game.Game;
import com.loficostudios.minigameeventsplugin.game.GameIndicator;
import com.loficostudios.minigameeventsplugin.game.arena.SpawnPlatform;
import com.loficostudios.minigameeventsplugin.game.player.NotificationType;
import com.loficostudios.minigameeventsplugin.utils.Debug;
import org.bukkit.Material;
import org.bukkit.Sound;

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
        var bar = game.getIndicator();
        bar.show(GameIndicator.IndicatorType.PROGRESS);
        var message = new StringBuilder();
        selectObjects(game, (platform) -> {
            if (onSelect(game, platform)) {
                if (getDisplayedEnabled()) {
                    var player = platform.getPlayer();
                    message.append(" ").append(player != null ? player.getName() : "NaN");
                    bar.progress(message.toString());
                }

                game.getPlayerManager().notify(
                        NotificationType.GLOBAL,
                        Sound.BLOCK_NOTE_BLOCK_PLING,
                        1, 2);
            }
        },platforms -> onComplete(game, platforms));
    }

    @Override
    public void end(Game game) {
        super.end(game);
        game.getIndicator().hide(GameIndicator.IndicatorType.PROGRESS);
    }
}
