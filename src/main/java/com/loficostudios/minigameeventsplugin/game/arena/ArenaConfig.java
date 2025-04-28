package com.loficostudios.minigameeventsplugin.game.arena;

import com.loficostudios.minigameeventsplugin.utils.Selection;
import org.bukkit.World;
import org.bukkit.util.Vector;

@SuppressWarnings("SameParameterValue")
public class ArenaConfig {

    private final Vector min;
    private final Vector max;
    private final World world;

    public final Selection bounds;

    public ArenaConfig(Vector min, Vector max, World world) {
        this.min = min;
        this.max = max;
        this.world = world;
        this.bounds = new Selection(min.toLocation(world), max.toLocation(world));
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

    public Selection getBounds() {
        return bounds;
    }
}
