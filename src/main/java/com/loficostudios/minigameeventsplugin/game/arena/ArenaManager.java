package com.loficostudios.minigameeventsplugin.game.arena;

import com.loficostudios.melodyapi.file.impl.YamlFile;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.util.Vector;

import java.util.HashMap;

public class ArenaManager {
    private YamlFile file;
    private final HashMap<String, ArenaConfig> arenas = new HashMap<>();

    public ArenaManager() {
    }

    public void initialize(YamlFile file) {
        this.file = file;
        var conf = file.getConfig();

        var worlds = conf.getKeys(false);

        for (String name : worlds) {
            var section = conf.getConfigurationSection(name);
            if (section == null)
                continue;

            var minSection = section.getConfigurationSection("min");
            var maxSection = section.getConfigurationSection("max");

            if (minSection == null || maxSection == null)
                continue;

            double minX = minSection.getDouble("x");
            double minY = minSection.getDouble("y");
            double minZ = minSection.getDouble("z");

            double maxX = maxSection.getDouble("x");
            double maxY = maxSection.getDouble("y");
            double maxZ = maxSection.getDouble("z");

            var world = Bukkit.getWorld(name);

            if (world == null)
                continue;

            var min = new Vector(minX, minY, minZ);
            var max = new Vector(maxX, maxY, maxZ);

            arenas.put(name, new ArenaConfig(min, max, world));
        }
    }

    public ArenaConfig getConfig(World world) {
        return arenas.get(world.getName());
    }

    public void setConfig(World world, ArenaConfig config) {
        arenas.put(world.getName(), config);
        save();
    }

    public void save() {
        var conf = file.getConfig();
        for (var entry : arenas.entrySet()) {
            String worldName = entry.getKey();
            ArenaConfig config = entry.getValue();

            conf.set(worldName + ".min.x", config.getMin().getX());
            conf.set(worldName + ".min.y", config.getMin().getY());
            conf.set(worldName + ".min.z", config.getMin().getZ());

            conf.set(worldName + ".max.x", config.getMax().getX());
            conf.set(worldName + ".max.y", config.getMax().getY());
            conf.set(worldName + ".max.z", config.getMax().getZ());
        }
        file.save();
    }
}
