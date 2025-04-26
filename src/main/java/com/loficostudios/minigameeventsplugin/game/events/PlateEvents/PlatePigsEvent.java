package com.loficostudios.minigameeventsplugin.game.events.PlateEvents;

import com.loficostudios.minigameeventsplugin.arena.SpawnPlatform;
import com.loficostudios.minigameeventsplugin.api.PlatformSelectorEvent;

import com.loficostudios.minigameeventsplugin.game.Game;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PlatePigsEvent extends PlatformSelectorEvent {

    public PlatePigsEvent() {
        super("Pig event", Material.PIG_SPAWN_EGG, 1, 3);
    }

    @Override
    public boolean onSelect(Game game, SpawnPlatform platform) {
        Player player = platform.getPlayer();

        if (player != null) {
            for (int i = 0; i < 4; i++) {
                game.getArena().spawnMob(EntityType.PIG, platform.getTeleportLocation());
            }

            return true;
        }

        return true;
    }

    @Override
    public @NotNull String getWarningMessage() {
        return "plate(s) will get raided by pigs!";
    }
}
