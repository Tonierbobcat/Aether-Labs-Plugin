package com.loficostudios.minigameeventsplugin.gameEvents;

import com.loficostudios.minigameeventsplugin.managers.GameManager.GameManager;
import com.loficostudios.minigameeventsplugin.api.BaseEvent;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

public class FallBackEvent extends BaseEvent {

    GameManager gameManager;

    public FallBackEvent(GameManager gameManager) {
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
    public @NotNull Material getDisplayMaterial() {
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
