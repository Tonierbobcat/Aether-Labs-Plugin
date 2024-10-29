package com.loficostudios.minigameeventsplugin.GameEvents.WorldEvents;

import com.loficostudios.minigameeventsplugin.GameEvents.BaseEvent;
import com.loficostudios.minigameeventsplugin.GameArena.GameArena;
import com.loficostudios.minigameeventsplugin.Interfaces.IWorldEvent;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;


public class WorldGhastEvent extends BaseEvent implements IWorldEvent {


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
