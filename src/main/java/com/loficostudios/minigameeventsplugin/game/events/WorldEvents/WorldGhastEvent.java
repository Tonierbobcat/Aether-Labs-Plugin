package com.loficostudios.minigameeventsplugin.game.events.WorldEvents;

import com.loficostudios.minigameeventsplugin.api.event.impl.AbstractGameEvent;
import com.loficostudios.minigameeventsplugin.arena.GameArena;
import com.loficostudios.minigameeventsplugin.api.event.EventType;
import com.loficostudios.minigameeventsplugin.game.Game;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;


@SuppressWarnings("SpellCheckingInspection")
public class WorldGhastEvent extends AbstractGameEvent {

    public WorldGhastEvent() {
        super("Ghast event", EventType.GLOBAL, Material.GHAST_TEAR);
    }

    @Override
    public @NotNull String getWarningMessage() {
        return "A ghast has been spotted in the arena!";
    }

    @Override
    public void start(Game game) {
        GameArena arena = game.getArena();
        arena.spawnMob(EntityType.GHAST, arena.getRandomLocation());
    }
}
