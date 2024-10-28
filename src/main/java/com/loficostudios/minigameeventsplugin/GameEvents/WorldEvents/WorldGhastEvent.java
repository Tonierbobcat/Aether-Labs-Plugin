package com.loficostudios.minigameeventsplugin.GameEvents.WorldEvents;

import com.loficostudios.minigameeventsplugin.GameEvents.BaseEvent;
import com.loficostudios.minigameeventsplugin.GameArena.GameArena;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;

public class WorldGhastEvent extends BaseEvent {


    @Override
    public @NotNull String getName() {
        return "Ghast event";
    }

    @Override
    public @NotNull String warningMessage() {
        return "A ghast has been spotted in the arena!";
    }


    @Override
    public void start() {
        //gameManager.getArena().getWorld().spawnEntity(gameManager.getArena().getRandomLocation(), EntityType.GHAST);

        GameArena arena = getGameManager().getArena();

        arena.spawnMob(EntityType.GHAST, arena.getRandomLocation());
    }
}
