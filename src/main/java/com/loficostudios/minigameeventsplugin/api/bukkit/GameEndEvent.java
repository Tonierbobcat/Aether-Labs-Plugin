package com.loficostudios.minigameeventsplugin.api.bukkit;

import com.loficostudios.minigameeventsplugin.game.Game;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class GameEndEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private final Game game;

    public GameEndEvent(Game game) {
        this.game = game;
    }

    public Game getGame() {
        return game;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
