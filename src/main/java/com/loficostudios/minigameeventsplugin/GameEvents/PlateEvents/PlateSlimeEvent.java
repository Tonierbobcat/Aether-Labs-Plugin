package com.loficostudios.minigameeventsplugin.GameEvents.PlateEvents;

import com.loficostudios.minigameeventsplugin.GameEvents.BaseEvent;
import com.loficostudios.minigameeventsplugin.GameEvents.EventType;
import com.loficostudios.minigameeventsplugin.Managers.PlayerManager.PlayerManager;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

public class PlateSlimeEvent extends BaseEvent {

    @Override
    public @NotNull EventType getType() {
        return EventType.PLATE;
    }

    @Override
    public @NotNull String getName() {
        return "Trampoline Event";
    }

    @Override
    public @NotNull String getWarningMessage() {
        return "All plates will become a trampoline";
    }

    @Override
    public @NotNull Material getDisplayMaterial() {
        return Material.SLIME_BLOCK;
    }


    @Override
    public void start() {
        PlayerManager playerManager = getGameManager().getPlayerManager();

        getArena().getSpawnPlatforms().forEach(spawnPlatform -> {
            spawnPlatform.setPlatform(Material.SLIME_BLOCK);
        });
    }


}
