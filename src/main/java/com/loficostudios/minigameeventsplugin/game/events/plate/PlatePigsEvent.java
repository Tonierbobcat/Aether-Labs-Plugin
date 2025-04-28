package com.loficostudios.minigameeventsplugin.game.events.plate;

import com.loficostudios.minigameeventsplugin.api.PlatformSelectorEvent;
import com.loficostudios.minigameeventsplugin.game.Game;
import com.loficostudios.minigameeventsplugin.game.arena.SpawnPlatform;
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
                game.getArena().spawnEntity(EntityType.PIG, platform.getTeleportLocation());
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
