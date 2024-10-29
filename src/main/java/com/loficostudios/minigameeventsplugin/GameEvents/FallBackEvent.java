package com.loficostudios.minigameeventsplugin.GameEvents;

import com.loficostudios.melodyapi.utils.SimpleColor;
import com.loficostudios.minigameeventsplugin.Managers.GameManager.GameManager;
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
        return SimpleColor.deserialize("&cError loading next round. Ending game");
    }

    @Override
    public @NotNull Material getDisplayMaterial() {
        return Material.BARRIER;
    }

    @Override
    public void start() {
        gameManager.forceEnd();
    }


}
