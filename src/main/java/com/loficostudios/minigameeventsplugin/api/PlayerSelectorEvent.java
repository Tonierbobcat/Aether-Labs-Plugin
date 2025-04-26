package com.loficostudios.minigameeventsplugin.api;

import com.loficostudios.minigameeventsplugin.api.event.EventType;
import com.loficostudios.minigameeventsplugin.api.event.impl.AbstractSelectorEvent;
import com.loficostudios.minigameeventsplugin.game.Game;
import com.loficostudios.minigameeventsplugin.managers.NotificationType;
import com.loficostudios.minigameeventsplugin.utils.PlayerState;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static com.loficostudios.minigameeventsplugin.utils.Debug.logWarning;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicReference;

import static com.loficostudios.minigameeventsplugin.utils.Debug.log;

public abstract class PlayerSelectorEvent extends AbstractSelectorEvent<Player> {
    protected PlayerSelectorEvent(String id, String name, Material icon, double cost, int min, int max) {
        super(id, name, EventType.PLAYER, icon, cost, min, max);
    }

    protected PlayerSelectorEvent(String id, String name, Material icon, int min, int max) {
        super(id, name, EventType.PLAYER, icon, min, max);
    }

    protected PlayerSelectorEvent(String name, Material icon, int min, int max) {
        super(name, EventType.PLAYER, icon, min, max);
    }

    @Override
    public Collection<Player> getObjects(Game game) {
        return game.getPlayers().getPlayersInGame(PlayerState.ALIVE);
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
