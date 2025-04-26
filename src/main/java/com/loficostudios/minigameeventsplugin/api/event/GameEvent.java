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

    @NotNull String getId();

    void load(Game game);

    void start(Game game);

    void end(Game game);

    void run(Game game);

    void cancel(Game game);

    void register();
}
