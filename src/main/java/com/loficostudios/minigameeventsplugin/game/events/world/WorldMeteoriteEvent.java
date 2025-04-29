package com.loficostudios.minigameeventsplugin.game.events.world;

import com.loficostudios.minigameeventsplugin.api.event.EventType;
import com.loficostudios.minigameeventsplugin.api.event.impl.AbstractGameEvent;
import com.loficostudios.minigameeventsplugin.game.Game;
import com.loficostudios.minigameeventsplugin.game.arena.SpawnPlatformGenerator;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ThreadLocalRandom;

public class WorldMeteoriteEvent extends AbstractGameEvent {
    public WorldMeteoriteEvent() {
        super("Meteorite Event", EventType.GLOBAL, Material.DEEPSLATE_EMERALD_ORE);
    }

    @Override
    public void start(Game game) {
        int radius = 4;

        var center = game.getArena().getRandomLocation();
        int maxAttempts = 10;
        int attempts = 0;

        var centerY = (game.getArena().getMin().getY() + game.getArena().getMax().getY()) / 2.0;

        while (center.getY() <  centerY ) {
            if (attempts >= maxAttempts)
                break;
            center = game.getArena().getRandomLocation();
            attempts++;
        }

        var minX = center.getBlockX();
        var minY = center.getBlockY();
        var minZ = center.getBlockZ();

        var maxX = minX + radius;
        var maxY = minY  + radius;
        var maxZ = minZ + radius;

        var blocks = new Material[] {
                Material.DEEPSLATE,
                Material.DEEPSLATE_DIAMOND_ORE,
                Material.MAGMA_BLOCK,
                Material.IRON_ORE,
                Material.DEEPSLATE,
                Material.DEEPSLATE,
                Material.DEEPSLATE_GOLD_ORE
        };

        var rand = ThreadLocalRandom.current();

        center.getWorld().playSound(center, Sound.ENTITY_GENERIC_EXPLODE, 1, 1);
        center.getWorld().playSound(center, Sound.BLOCK_AMETHYST_BLOCK_CHIME, 1, 1);

        for (int x = minX; x < maxX; x++)
            for (int y = minY; y < maxY; y++)
                for (int z = minZ; z < maxZ; z++) {
                    center.getWorld().getBlockAt(x,y,z).setType(blocks[rand.nextInt(blocks.length)]);
                }

//        var generator = new SpawnPlatformGenerator(null, game.getArena());
    }

    @Override
    public @NotNull String getWarningMessage() {
        return "A meteorite is about to spawn";
    }
}
