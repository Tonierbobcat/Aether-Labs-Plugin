package com.loficostudios.minigameeventsplugin.game.events;

import com.loficostudios.minigameeventsplugin.api.event.EventType;
import com.loficostudios.minigameeventsplugin.game.Game;
import com.loficostudios.minigameeventsplugin.api.event.impl.AbstractGameEvent;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

public class FallBackEvent extends AbstractGameEvent {

    Game gameManager;

    public FallBackEvent(Game gameManager) {
        super();
        this.gameManager = gameManager;
    }



    @Override
    public @NotNull String getName() {
        return "Fall Back Event";
    }

    @Override
    public @NotNull String getWarningMessage() {
        return "&cError loading next round. Ending game";
    }

    @Override
    public @NotNull Material getIcon() {
        return Material.BARRIER;
    }

    @Override
    public @NotNull EventType getType() {
        return EventType.GLOBAL;
    }

    @Override
    public void start() {
        gameManager.forceEnd();
    }


}
