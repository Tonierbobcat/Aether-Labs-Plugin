package com.loficostudios.minigameeventsplugin.api.event;

import com.loficostudios.minigameeventsplugin.api.Warning;
import com.loficostudios.minigameeventsplugin.game.Game;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

public interface GameEvent {
    double getCost();

    @NotNull String getName();

    @NotNull Material getIcon();

    @NotNull EventType getType();

    @NotNull Warning getWarning();

    int getDuration();

    @NotNull String getIdentifier();

    default void load(Game game) {
    }

    default void start(Game game) {
    }

    default void end(Game game) {
    }

    default void run(Game game) {
    }

    default void cancel(Game game) {
    }

    void register();
}
