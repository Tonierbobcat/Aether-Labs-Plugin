package com.loficostudios.minigameeventsplugin.game.arena;

import com.loficostudios.minigameeventsplugin.AetherLabsPlugin;
import com.loficostudios.minigameeventsplugin.utils.Selection;
import org.bukkit.Bukkit;
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
        calculateBlocks(center, calculatedBlocks -> {
            for (Block block : calculatedBlocks) {
                block.setType(plate.getMaterial());
            }
            onGenerate.accept(calculatedBlocks);
            plate.setBlocks(calculatedBlocks);
        });

        return true;
    }

    public boolean create(SpawnAlgorithm algorithm, Consumer<Collection<Block>> onGenerate) {
        Selection bounds = new Selection(arena.getPos1(), arena.getPos2()).adjustSelection(-10);

        Location center = null;

        boolean isCreated = false;
        int attempts = 0;

        while (!isCreated && attempts < 50) {

            center = bounds.getRandomLocation();
            center.setY(bounds.getMiddleY());

            if (isLocationValid(center)) {
                if (algorithm.equals(SpawnAlgorithm.RANDOM_HEIGHT)) {

                    int y = bounds.getRandomLocation().getBlockY();

                    //center.setY(y);

                    center = new Location(center.getWorld(), center.getBlockX(), y, center.getBlockZ());
                }

                calculateBlocks(center, calculatedBlocks -> {
                    for (Block block : calculatedBlocks) {
                        block.setType(plate.getMaterial());
                    }
                    onGenerate.accept(calculatedBlocks);

                    plate.setBlocks(calculatedBlocks);
                });

                isCreated = true;
            }
            attempts++;
        }

        if (isCreated) {
            plate.setLocation(center);
            logWarning("attempts to create " + attempts);
            return true;
        }
        else {
            return false;
        }
    }

    private boolean isLocationValid(Location center) {
        var radius = plate.getRadius();
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

    public void calculateBlocks(Location center, Consumer<Set<Block>> callback) {
        var radius = plate.getRadius();
        Set<Block> blocksToSet = new HashSet<>();
        Bukkit.getScheduler().runTaskAsynchronously(AetherLabsPlugin.getInstance(), () -> {
            Selection selection = new Selection(arena.getPos1(), arena.getPos2());


            // Calculate the start position to center the platform based on the radius
            int startX = center.getBlockX() - radius;
            int startZ = center.getBlockZ() - radius;
            int y = center.getBlockY();

            // Place blocks in a 3x3 area, based on radius
            for (int x = startX; x < startX + 2 * radius + 1; x++) {
                for (int z = startZ; z < startZ + 2 * radius + 1; z++) {
                    Block block = selection.getBlock(x, y, z);

                    if (block != null)
                        blocksToSet.add(block);
                }
            }

            Bukkit.getScheduler().runTask(AetherLabsPlugin.getInstance(), () -> callback.accept(blocksToSet));
        });
    }
}
