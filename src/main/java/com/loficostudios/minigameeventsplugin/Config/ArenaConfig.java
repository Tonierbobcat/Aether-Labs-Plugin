package com.loficostudios.minigameeventsplugin.Config;


import com.loficostudios.melodyapi.libs.boostedyaml.YamlDocument;
import com.loficostudios.melodyapi.libs.boostedyaml.block.implementation.Section;
import com.loficostudios.melodyapi.utils.Common;
import com.loficostudios.minigameeventsplugin.MiniGameEventsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.io.IOException;

import static com.loficostudios.minigameeventsplugin.Utils.DebugUtil.debug;

@SuppressWarnings("SameParameterValue")
public class ArenaConfig {

    private static final YamlDocument arenaConfig = MiniGameEventsPlugin.getInstance().getArenaConfig();

    public static final Location POS1 = getLocationFromConfig(arenaConfig, "pos1", true);
    public static final Location POS2 = getLocationFromConfig(arenaConfig, "pos2", true);


    public static void update(Location pos1, Location pos2) {

        Section loc1 = arenaConfig.getSection("pos1");
        Section loc2 = arenaConfig.getSection("pos2");

        World defaultWorld = MiniGameEventsPlugin.getInstance().getServer().getWorlds().get(0);

        loc1.set("world", pos2.getWorld() == null ? defaultWorld : pos2.getWorld().getName());
        loc1.set("x", pos1.getX());
        loc1.set("y", pos1.getY());
        loc1.set("z", pos1.getZ());

        loc2.set("world", pos2.getWorld() == null ? defaultWorld : pos2.getWorld().getName());
        loc2.set("x", pos2.getX());
        loc2.set("y", pos2.getY());
        loc2.set("z", pos2.getZ());

        //File configFile = YamlDocument.load

        try {
            arenaConfig.save();
        }
        catch (IOException e) {
            MiniGameEventsPlugin.getInstance().getServer().getLogger().warning(e.getMessage());
        }
    }

    private static Location getLocationFromConfig(YamlDocument config, String path, boolean round) {
        Section loc = config.getSection(path);

        double x = round ? Math.round((double) loc.get("x")) : (double) loc.get("x");
        double y = round ? Math.round((double) loc.get("y")) : (double) loc.get("y");
        double z = round ? Math.round((double) loc.get("z")) : (double) loc.get("z");

        String worldName = (String) loc.get("world");

        World defaultWorld = MiniGameEventsPlugin.getInstance().getServer().getWorlds().get(0);

        World worldFromFile = Bukkit.getWorld(worldName == null ? "none" : worldName);

        World world = worldFromFile == null ? defaultWorld : worldFromFile;

        debug("config. " + path + ": " + new Location(world, x, y, z));
        return new Location(world, x, y, z);
    }
}
