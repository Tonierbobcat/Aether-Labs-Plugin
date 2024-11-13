package com.loficostudios.minigameeventsplugin.api.events;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class RoundSurvivedEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    @Getter
    private final Player player;

    public RoundSurvivedEvent(@NotNull Player player) {
        this.player = player;
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