package com.loficostudios.minigameeventsplugin.config;

import com.loficostudios.melodyapi.file.impl.YamlFile;
import com.loficostudios.minigameeventsplugin.AetherLabsPlugin;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.util.NumberConversions;

import static com.loficostudios.minigameeventsplugin.utils.Debug.log;

@SuppressWarnings("SameParameterValue")
public class ArenaConfig {

//    private final YamlDocument config;

    private final YamlFile file;

    private final AetherLabsPlugin plugin;

    public ArenaConfig(AetherLabsPlugin plugin) {
        this.plugin = plugin;
        this.file = new YamlFile("arena-config.yml", this.plugin);

        var conf = file.getConfig();

        POS_1 = getLocationFromConfig(conf, "pos1", true);
        POS_2 = getLocationFromConfig(conf, "pos2", true);
        WORLD_NAME = conf.getString("world");
    }

    public static Location POS_1 = null;
    public static Location POS_2 = null;
    public static String WORLD_NAME = "";

    private static Location getLocationFromConfig(ConfigurationSection config, String path, boolean round) {
        ConfigurationSection loc = config.getConfigurationSection(path);
        if (loc == null) {
            log("section is null");
            return null;
        }
        if (loc.get("x") == null || loc.get("x") == null) {

            log("location values are null.");
            return null;
        }

        double x = NumberConversions.floor((double) loc.get("x"));
        double y = NumberConversions.floor((double) loc.get("y"));
        double z = NumberConversions.floor((double) loc.get("z"));

        Location location = new Location(null, x, y, z);
        log("config. " + path + ": " + location);
        return location;
    }

    public void update(Location pos1, Location pos2) {
        var conf = file.getConfig();

        World defaultWorld = plugin.getServer().getWorlds().get(0);

        conf.set("world", pos2.getWorld() == null ? defaultWorld : pos2.getWorld().getName());

        conf.set("pos1.x", pos1.getX());
        conf.set("pos1.y", pos1.getY());
        conf.set("pos1.z", pos1.getZ());
        conf.set("pos2.x", pos2.getX());
        conf.set("pos2.y", pos2.getY());
        conf.set("pos2.z", pos2.getZ());

        //File configFile = YamlDocument.load

        file.save();
    }


}
