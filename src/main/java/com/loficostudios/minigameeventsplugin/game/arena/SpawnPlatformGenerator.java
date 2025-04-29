package com.loficostudios.minigameeventsplugin.game.arena;

import com.loficostudios.minigameeventsplugin.utils.Debug;
import com.loficostudios.minigameeventsplugin.utils.Selection;
import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

import static com.loficostudios.minigameeventsplugin.utils.Debug.logWarning;

public class SpawnPlatformGenerator {
//    private final SpawnAlgorithm spawnAlgorithm;
    public enum SpawnAlgorithm { EQUAL_HEIGHT, RANDOM_HEIGHT }
    private final SpawnPlatform plate;
    private final GameArena arena;
    public SpawnPlatformGenerator(SpawnPlatform plate, GameArena arena) {
        plate.setGenerator(this);

        this.plate = plate;

        this.arena = arena;
    }
    private static final int MIN_SPACING = 7;

    public boolean create(Location center, Consumer<Collection<Block>> onGenerate) {
        var calculatedBlocks = calculateBlocks(center);

        for (Block block : calculatedBlocks) {
            block.setType(plate.getMaterial());
        }
        onGenerate.accept(calculatedBlocks);
        plate.setBlocks(calculatedBlocks);

        return true;
    }

    public boolean generate(SpawnAlgorithm algorithm, Consumer<Collection<Block>> onGenerate) {
        Selection bounds = arena.getBounds().adjustSelection(-10);

        Location center = null;

        boolean isCreated = false;
        int attempts = 0;
        Set<Block> blocks = null;
        while (!isCreated && attempts < 50) {
            center = bounds.getRandomLocation();
            center.setY(bounds.getMiddleY());

            if (!isLocationValid(center, plate.getRadius())) {
                attempts++;
                continue;
            }

            if (algorithm.equals(SpawnAlgorithm.RANDOM_HEIGHT)) {
                int y = bounds.getRandomLocation().getBlockY();
                center = new Location(center.getWorld(), center.getBlockX(), y, center.getBlockZ());
            }

            blocks = calculateBlocks(center);
            blocks.forEach(block -> block.setType(plate.getMaterial()));
            plate.setBlocks(blocks);

            isCreated = true;
        }

        if (isCreated) {
            plate.setLocation(center);
            onGenerate.accept(blocks);

            logWarning("attempts to create " + attempts);
            return true;
        }
        else {
            return false;
        }
    }

    public boolean isLocationValid(Location center, int radius) {
        for (SpawnPlatform platform : arena.getSpawnPlatforms()) {

            double dx = center.getX() - platform.getLocation().getX();
            double dz = center.getZ() - platform.getLocation().getZ();
            double distanceXZ = Math.sqrt(dx * dx + dz * dz); // 2D distance ignoring y

            if (distanceXZ < MIN_SPACING + radius) {
                return false;
            }
        }
        return true;
    }

    public Set<Block> calculateBlocks(Location center) {
        var radius = plate.getRadius();
        Debug.log("Calculating blocks... radius: " + radius);

        var blocks = new HashSet<Block>();

        var bounds = arena.getBounds();

        int startX = center.getBlockX() - radius;
        int startZ = center.getBlockZ() - radius;
        int y = center.getBlockY();

        for (int x = startX; x < startX + 2 * radius + 1; x++) {
            for (int z = startZ; z < startZ + 2 * radius + 1; z++) {
                Block block = bounds.getBlock(x, y, z);

                if (block != null)
                    blocks.add(block);
            }
        }

        return blocks;
    }
}
