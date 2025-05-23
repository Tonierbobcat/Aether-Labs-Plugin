package com.loficostudios.minigameeventsplugin.game.events.world;

import com.loficostudios.minigameeventsplugin.api.event.EventType;
import com.loficostudios.minigameeventsplugin.api.event.impl.AbstractGameEvent;
import com.loficostudios.minigameeventsplugin.game.Game;
import com.loficostudios.minigameeventsplugin.game.arena.GameArena;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;


@SuppressWarnings("SpellCheckingInspection")
public class WorldGhastEvent extends AbstractGameEvent {

    public WorldGhastEvent() {
        super("Ghast Event", EventType.GLOBAL, Material.GHAST_TEAR);
    }

    @Override
    public @NotNull String getWarningMessage() {
        return "A ghast has been spotted in the arena!";
    }

    @Override
    public void start(Game game) {
        GameArena arena = game.getArena();
        arena.spawnEntity(EntityType.GHAST, arena.getRandomLocation());
    }
}
