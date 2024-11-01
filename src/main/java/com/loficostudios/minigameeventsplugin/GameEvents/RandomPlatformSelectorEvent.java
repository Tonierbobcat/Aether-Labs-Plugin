package com.loficostudios.minigameeventsplugin.GameEvents;

import com.loficostudios.minigameeventsplugin.GameArena.SpawnPlatform;
import com.loficostudios.minigameeventsplugin.Managers.PlayerManager.NotificationType;
import com.loficostudios.minigameeventsplugin.Utils.Debug;
import com.loficostudios.minigameeventsplugin.Utils.PlayerState;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicReference;

import static com.loficostudios.minigameeventsplugin.Utils.Debug.log;
import static com.loficostudios.minigameeventsplugin.Utils.Debug.logWarning;

public abstract class RandomPlatformSelectorEvent extends SelectorEvent<SpawnPlatform> {
    @Override
    public Boolean getDisplayedEnabled() {
        return true;
    }

    @Override
    protected Collection<SpawnPlatform> getObjects() {

        Debug.log("Platforms size " + getArena().getSpawnPlatforms().size());



        return getArena().getSpawnPlatforms();
    }


    @Override
    public void start() {

        AtomicReference<String> message = new AtomicReference<>("");

        progressBar = getGameManager().getProgressBar();

        selectObjects((SpawnPlatform platform) -> {
            boolean selected = false;

            if (onSelect(platform)) {
                selected = true;
                log("selected " + platform);
            }
            else {
                logWarning("could not select " + platform);
            }

            if (getDisplayedEnabled()) {
                Player player = platform.getPlayer();

                message.set(message + " " + (selected && player != null ? player.getName() : "NaN"));
                progressBar.setTitle(message.get());
            }

            getPlayerManager().notify(
                    NotificationType.GLOBAL,
                    Sound.BLOCK_NOTE_BLOCK_PLING,
                    1, 2);

        }, this::onComplete);
    }

    @Override
    public @NotNull EventType getType() {

        return EventType.PLATE;
    }

    @Override
    public void end() {
        progressBar.removeAll();
    }
}
