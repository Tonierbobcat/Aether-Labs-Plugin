package com.loficostudios.minigameeventsplugin.gameEvents.WorldEvents;

import com.loficostudios.minigameeventsplugin.api.BaseEvent;
import com.loficostudios.minigameeventsplugin.arena.GameArena;
import com.loficostudios.minigameeventsplugin.gameEvents.EventType;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;


@SuppressWarnings("SpellCheckingInspection")
public class WorldGhastEvent extends BaseEvent {

    @Override
    public @NotNull EventType getType() {
        return EventType.GLOBAL;
    }

    @Override
    public @NotNull String getName() {
        return "Ghast event";
    }

    @Override
    public @NotNull String getWarningMessage() {
        return "A ghast has been spotted in the arena!";
    }

    @Override
    public @NotNull Material getDisplayMaterial() {
        return Material.GHAST_TEAR;
    }


    @Override
    public void start() {
        //gameManager.getArena().getWorld().spawnEntity(gameManager.getArena().getRandomLocation(), EntityType.GHAST);

        GameArena arena = getGameManager().getArena();

        arena.spawnMob(EntityType.GHAST, arena.getRandomLocation());
    }
}
