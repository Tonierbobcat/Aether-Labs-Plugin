package com.loficostudios.minigameeventsplugin.api.bukkit;

import com.loficostudios.minigameeventsplugin.arena.GameArena;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class LavaLevelUpdatedEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    @Getter
    @NotNull
    private final GameArena arena;
    @Getter
    private final int lavaLevel;
    @Getter
    private final int y;

    public LavaLevelUpdatedEvent(@NotNull GameArena arena, int y, int lavaLevel) {
        this.arena = arena;
        this.lavaLevel = lavaLevel;
        this.y = y;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}