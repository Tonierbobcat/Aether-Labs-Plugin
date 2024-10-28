package com.loficostudios.minigameeventsplugin.Utils;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class Selection {

    @Getter
    Location pos1;
    @Getter
    Location pos2;

    World world;

    Map<Vector, Block> blocks = new HashMap<>();

    public static double randomDouble(double min, double max) {
        return min + ThreadLocalRandom.current().nextDouble(Math.abs(max - min + 1));
    }

    public Selection(Location pos1, Location pos2) {
        this.pos1 = pos1;
        this.pos2 = pos2;

        this.world = pos1.getWorld();

        int minX = Math.min(pos1.getBlockX(), pos2.getBlockX());
        int maxX = Math.max(pos1.getBlockX(), pos2.getBlockX());
        int minY = Math.min(pos1.getBlockY(), pos2.getBlockY());
        int maxY = Math.max(pos1.getBlockY(), pos2.getBlockY());
        int minZ = Math.min(pos1.getBlockZ(), pos2.getBlockZ());
        int maxZ = Math.max(pos1.getBlockZ(), pos2.getBlockZ());

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    Block block = world.getBlockAt(x, y, z);
                    blocks.put(new Vector(x, y, z), block);
                }
            }
        }

    }

    public Location getRandomLocation() {


        double minX = Math.min(pos1.getX(), pos2.getX());
        double minY = Math.min(pos1.getY(), pos2.getY());
        double minZ = Math.min(pos1.getZ(), pos2.getZ());

        double maxX = Math.max(pos1.getX(), pos2.getX());
        double maxY = Math.max(pos1.getY(), pos2.getY());
        double maxZ = Math.max(pos1.getZ(), pos2.getZ());

        long x = Math.round(randomDouble(minX, maxX));
        long y = Math.round(randomDouble(minY, maxY));
        long z = Math.round(randomDouble(minZ, maxZ));

        return new Location(world, x, y, z);
    }

    /*public Selection(Set<Block> blocks) {
        this.blocks.addAll(blocks);
    }*/

    public Selection shrinkTowardsCenter(double shrinkFactor) {
        // Calculate the center coordinates
        int centerX = (pos1.getBlockX() + pos2.getBlockX()) / 2;
        int centerZ = (pos1.getBlockZ() + pos2.getBlockZ()) / 2;

        // Calculate the new corners based on the shrink factor
        int newMinX = (int) Math.round(centerX - ((centerX - Math.min(pos1.getBlockX(), pos2.getBlockX())) * shrinkFactor));
        int newMaxX = (int) Math.round(centerX + ((Math.max(pos1.getBlockX(), pos2.getBlockX()) - centerX) * shrinkFactor));
        int newMinZ = (int) Math.round(centerZ - ((centerZ - Math.min(pos1.getBlockZ(), pos2.getBlockZ())) * shrinkFactor));
        int newMaxZ = (int) Math.round(centerZ + ((Math.max(pos1.getBlockZ(), pos2.getBlockZ()) - centerZ) * shrinkFactor));

        // Create new positions for the shrunk selection
        Location newPos1 = new Location(world, newMinX, getMiddleY(), newMinZ);
        Location newPos2 = new Location(world, newMaxX, getMiddleY(), newMaxZ);

        return new Selection(newPos1, newPos2);
    }

    public int getMiddleY() {
        int minY = Math.min(pos1.getBlockY(), pos2.getBlockY());
        int maxY = Math.max(pos1.getBlockY(), pos2.getBlockY());
        return (minY + maxY) / 2; // Floors the value if there's no exact middle
    }

    public Block getBlock(int x, int y, int z) {
        return blocks.get(new Vector(x, y, z));
    }
}
