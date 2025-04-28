package com.loficostudios.minigameeventsplugin.config;

import org.bukkit.World;
import org.bukkit.util.Vector;

@SuppressWarnings("SameParameterValue")
public class ArenaConfig {

    private final Vector min;
    private final Vector max;
    private final World world;

    public ArenaConfig(Vector min, Vector max, World world) {
        this.min = min;
        this.max = max;
        this.world = world;
    }

    public Vector getMin() {
        return min;
    }

    public Vector getMax() {
        return max;
    }

    public World getWorld() {
        return world;
    }
}
