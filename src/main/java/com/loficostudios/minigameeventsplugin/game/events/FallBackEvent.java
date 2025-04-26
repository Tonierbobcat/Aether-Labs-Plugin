package com.loficostudios.minigameeventsplugin.game.events;

import com.loficostudios.minigameeventsplugin.api.event.EventType;
import com.loficostudios.minigameeventsplugin.api.event.impl.AbstractGameEvent;
import com.loficostudios.minigameeventsplugin.game.Game;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

public class FallBackEvent extends AbstractGameEvent {
    public FallBackEvent() {
        super("Fall Back Event", EventType.GLOBAL, Material.BARRIER);
    }

    @Override
    public @NotNull String getWarningMessage() {
        return "&cError loading next round. Ending game";
    }


    @Override
    public void start(Game game) {
        game.forceEnd();
    }
}
