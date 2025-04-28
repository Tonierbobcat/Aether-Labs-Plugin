package com.loficostudios.minigameeventsplugin.game.events.plate;

import com.loficostudios.minigameeventsplugin.api.PlatformSelectorEvent;
import com.loficostudios.minigameeventsplugin.game.Game;
import com.loficostudios.minigameeventsplugin.game.arena.SpawnPlatform;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PlateZombieEvent extends PlatformSelectorEvent {

    public PlateZombieEvent() {
        super("Plate zombie event", Material.ZOMBIE_SPAWN_EGG, 1, 3);
    }

    @Override
    public @NotNull String getWarningMessage() {
        return "plate(s) will get a pet zombie! :D";
    }

    @Override
    public boolean onSelect(Game game, SpawnPlatform platform) {

        Player player = platform.getPlayer();

        if (player != null) {
            game.getArena().spawnEntity(EntityType.ZOMBIE, platform.getTeleportLocation());
        }

        return true;
    }
}
