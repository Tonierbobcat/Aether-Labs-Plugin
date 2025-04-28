package com.loficostudios.minigameeventsplugin.utils;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Selection {

    @Getter
    Location min;
    @Getter
    Location max;

    World world;

    Map<Vector, Block> blocks = new HashMap<>();


    public Collection<Block> getBlocks() {
        return this.blocks.values();
    }

    public static double randomDouble(double min, double max) {
        return min + ThreadLocalRandom.current().nextDouble(Math.abs(max - min + 1));
    }

    public int count() {
        return blocks.size();
    }

    public Selection(Set<Block> blocks) {
        blocks.forEach(block -> {

            int x = block.getX();
            int y = block.getY();
            int z = block.getZ();

            this.blocks.put(new Vector(x, y, z), block);
        });
    }

    public Selection(Location pos1, Location pos2) {
        this.min = pos1;
        this.max = pos2;

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


        double minX = Math.min(min.getX(), max.getX());
        double minY = Math.min(min.getY(), max.getY());
        double minZ = Math.min(min.getZ(), max.getZ());

        double maxX = Math.max(min.getX(), max.getX());
        double maxY = Math.max(min.getY(), max.getY());
        double maxZ = Math.max(min.getZ(), max.getZ());

        long x = Math.round(randomDouble(minX, maxX));
        long y = Math.round(randomDouble(minY, maxY));
        long z = Math.round(randomDouble(minZ, maxZ));

        return new Location(world, x, y, z);
    }

    /*public Selection(Set<Block> blocks) {
        this.blocks.addAll(blocks);
    }*/

    public int getMiddleY() {
        int minY = Math.min(min.getBlockY(), max.getBlockY());
        int maxY = Math.max(min.getBlockY(), max.getBlockY());
        return (minY + maxY) / 2; // Floors the value if there's no exact middle
    }

    public @Nullable Block getBlock(int x, int y, int z) {
        return blocks.get(new Vector(x, y, z));
    }

    public Selection adjustSelection(int distance) {
        int minX = Math.min(min.getBlockX(), max.getBlockX()) - distance;
        int maxX = Math.max(min.getBlockX(), max.getBlockX()) + distance;
        int minY = Math.min(min.getBlockY(), max.getBlockY()) - distance;
        int maxY = Math.max(min.getBlockY(), max.getBlockY()) + distance;
        int minZ = Math.min(min.getBlockZ(), max.getBlockZ()) - distance;
        int maxZ = Math.max(min.getBlockZ(), max.getBlockZ()) + distance;

        if (minX > maxX) minX = maxX;
        if (minY > maxY) minY = maxY;
        if (minZ > maxZ) minZ = maxZ;

        Location newPos1 = new Location(world, minX, minY, minZ);
        Location newPos2 = new Location(world, maxX, maxY, maxZ);

        return new Selection(newPos1, newPos2);
    }

    public Selection getPerimeter(int distance) {
        Set<Block> perimeterBlocks = new HashSet<>();

        int minX = Math.min(min.getBlockX(), max.getBlockX()) - distance;
        int maxX = Math.max(min.getBlockX(), max.getBlockX()) + distance;
        int minY = Math.min(min.getBlockY(), max.getBlockY()) - distance;
        int maxY = Math.max(min.getBlockY(), max.getBlockY()) + distance;
        int minZ = Math.min(min.getBlockZ(), max.getBlockZ()) - distance;
        int maxZ = Math.max(min.getBlockZ(), max.getBlockZ()) + distance;

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                perimeterBlocks.add(world.getBlockAt(x, y, minZ));
                perimeterBlocks.add(world.getBlockAt(x, y, maxZ));
            }
        }

        for (int y = minY; y <= maxY; y++) {
            for (int z = minZ; z <= maxZ; z++) {
                perimeterBlocks.add(world.getBlockAt(minX, y, z));
                perimeterBlocks.add(world.getBlockAt(maxX, y, z));
            }
        }

        for (int x = minX; x <= maxX; x++) {
            for (int z = minZ; z <= maxZ; z++) {
                perimeterBlocks.add(world.getBlockAt(x, minY, z));
                perimeterBlocks.add(world.getBlockAt(x, maxY, z));
            }
        }

        return new Selection(perimeterBlocks);
    }



//    public Set<Block> getPerimeterBlocks() {
//        Set<Block> perimeterBlocks = new HashSet<>();
//
//        int minX = Math.min(pos1.getBlockX(), pos2.getBlockX());
//        int maxX = Math.max(pos1.getBlockX(), pos2.getBlockX());
//        int minY = Math.min(pos1.getBlockY(), pos2.getBlockY());
//        int maxY = Math.max(pos1.getBlockY(), pos2.getBlockY());
//        int minZ = Math.min(pos1.getBlockZ(), pos2.getBlockZ());
//        int maxZ = Math.max(pos1.getBlockZ(), pos2.getBlockZ());
//
//        for (int x = minX; x <= maxX; x++) {
//            for (int y = minY; y <= maxY; y++) {
//                // Add blocks on the Z boundaries
//                perimeterBlocks.add(world.getBlockAt(x, y, minZ));
//                perimeterBlocks.add(world.getBlockAt(x, y, maxZ));
//            }
//        }
//
//        for (int y = minY; y <= maxY; y++) {
//            for (int z = minZ; z <= maxZ; z++) {
//                // Add blocks on the X boundaries
//                perimeterBlocks.add(world.getBlockAt(minX, y, z));
//                perimeterBlocks.add(world.getBlockAt(maxX, y, z));
//            }
//        }
//
//        for (int x = minX; x <= maxX; x++) {
//            for (int z = minZ; z <= maxZ; z++) {
//                // Add blocks on the Y boundaries
//                perimeterBlocks.add(world.getBlockAt(x, minY, z));
//                perimeterBlocks.add(world.getBlockAt(x, maxY, z));
//            }
//        }
//
//        return perimeterBlocks;
//    }
}
