package com.loficostudios.minigameeventsplugin.GameEvents;

import com.loficostudios.minigameeventsplugin.Managers.PlayerManager.NotificationType;
import com.loficostudios.minigameeventsplugin.Utils.PlayerState;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static com.loficostudios.minigameeventsplugin.Utils.Debug.logWarning;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicReference;

import static com.loficostudios.minigameeventsplugin.Utils.Debug.log;

public abstract class RandomPlayerSelectorEvent extends SelectorEvent<Player> {

    @Override
    public Boolean getDisplayedEnabled() {
        return true;
    }

    @Override
    protected Collection<Player> getObjects() {
        return getPlayerManager().getPlayersInGame(PlayerState.ALIVE);
    }


    @Override
    public void start() {

        AtomicReference<String> message = new AtomicReference<>("");

        progressBar = getGameManager().getProgressBar();

        selectObjects((Player player) -> {
        //selectObjects(amount, (Player player) -> {
            boolean selected = false;

            if (onSelect(player)) {
                selected = true;
                log("selected " + player.getName());
            }
            else {
                logWarning("could not select " + player);
            }

            if (getDisplayedEnabled()) {
                message.set(message + " " + (selected ? player.getName() : "NaN"));
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

        return EventType.PLAYER;
    }

    @Override
    public void end() {
        progressBar.removeAll();
    }
}
