package com.loficostudios.minigameeventsplugin.BukkitEvents;

import com.loficostudios.minigameeventsplugin.GameArena.GameArena;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class ArenaLavaLevelChangedEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    @Getter
    private final GameArena arena;
    @Getter
    private final int level;

    @Getter
    private final int y;

    public ArenaLavaLevelChangedEvent(GameArena arena, int y, int level) {
        this.arena =arena;
        this.level = level;
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