package com.loficostudios.minigameeventsplugin.Config;


import com.loficostudios.melodyapi.libs.boostedyaml.YamlDocument;
import com.loficostudios.melodyapi.libs.boostedyaml.block.implementation.Section;
import com.loficostudios.melodyapi.utils.SimpleDocument;
import com.loficostudios.minigameeventsplugin.RandomEventsPlugin;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.NumberConversions;

import java.io.IOException;

import static com.loficostudios.minigameeventsplugin.Utils.DebugUtil.debug;
import static com.loficostudios.minigameeventsplugin.Utils.DebugUtil.debugWarning;

@SuppressWarnings("SameParameterValue")
public class ArenaConfig {

    private final YamlDocument config;

    private final RandomEventsPlugin plugin;

    public ArenaConfig(RandomEventsPlugin plugin) {
        this.plugin = plugin;

        this.config = SimpleDocument.create(this.plugin, "arena-config.yml");

        POS_1 = getLocationFromConfig(config, "pos1", true);
        POS_2 = getLocationFromConfig(config, "pos2", true);
        WORLD_NAME = (String) config.get("world");
    }

    public static Location POS_1 = null;
    public static Location POS_2 = null;
    public static String WORLD_NAME = "";

    private static Location getLocationFromConfig(YamlDocument config, String path, boolean round) {
        Section loc = config.getSection(path);

        double x = NumberConversions.floor((double) loc.get("x"));
        double y = NumberConversions.floor((double) loc.get("y"));
        double z = NumberConversions.floor((double) loc.get("z"));

        Location location = new Location(null, x, y, z);
        debug("config. " + path + ": " + location);
        return location;
    }

    public void update(Location pos1, Location pos2) {

        Section loc1 = config.getSection("pos1");
        Section loc2 = config.getSection("pos2");

        World defaultWorld = plugin.getServer().getWorlds().get(0);

        config.set("world", pos2.getWorld() == null ? defaultWorld : pos2.getWorld().getName());

        loc1.set("x", pos1.getX());
        loc1.set("y", pos1.getY());
        loc1.set("z", pos1.getZ());
        loc2.set("x", pos2.getX());
        loc2.set("y", pos2.getY());
        loc2.set("z", pos2.getZ());

        //File configFile = YamlDocument.load

        try {
            config.save();
        }
        catch (IOException e) {
            debugWarning(e.getMessage());
        }
    }


}
