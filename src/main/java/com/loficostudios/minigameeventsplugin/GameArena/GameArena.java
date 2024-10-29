package com.loficostudios.minigameeventsplugin.GameArena;

import com.loficostudios.minigameeventsplugin.Config.ArenaConfig;
import com.loficostudios.minigameeventsplugin.RandomEventsPlugin;
import com.loficostudios.minigameeventsplugin.Utils.Selection;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.loficostudios.minigameeventsplugin.Utils.DebugUtil.*;
import static com.loficostudios.minigameeventsplugin.Utils.Selection.randomDouble;
import static com.loficostudios.minigameeventsplugin.Utils.WorldUtils.fillArea;

public class GameArena {

    public static final Material DEFAULT_FILL_MATERIAL = Material.LAVA;
    public static final int DEFAULT_FILL_SPEED = 5;
    public static final Material DEFAULT_PLATFORM_MATERIAL = Material.STONE;
    public static final SpawnPlatform.PlatformType DEFAULT_PLATFORM_TYPE = SpawnPlatform.PlatformType.FLAT;
    private static final Boolean SPAWN_EXTRA_PLATFORMS = false;
    private static final Integer EXTRA_PLATFORMS_AMOUNT = 5;

    @Getter private Location pos1;
    @Getter private Location pos2;

    private World world;

    private final Collection<Entity> mobs = new ArrayList<>();

    private final Map<Player, SpawnPlatform> playerSpawnPlatforms = new HashMap<>();
    private final Collection<SpawnPlatform> spawnPlatforms = new ArrayList<>();

    private BukkitTask lavaTask;

    public World getWorld() {

        if (this.world == null) {
            World worldFromConfig = RandomEventsPlugin.getInstance()
                    .getServer()
                    .getWorld(ArenaConfig.WORLD_NAME);

            if (worldFromConfig != null) {
                this.world = worldFromConfig;
                this.pos1.setWorld(worldFromConfig);
                this.pos2.setWorld(worldFromConfig);
                return worldFromConfig;
            }
            throw new IllegalArgumentException();
        }
        else {
            return this.world;
        }
    }

    public GameArena(Location pos1, Location pos2) {
        this.pos1 = pos1;
        this.pos2 = pos2;
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

        return new Location(pos1.getWorld(), x, y, z);
    }

    //region Clean Up
    public void cancelLavaTask() {
        if (lavaTask != null) {
            debug("canceled lava task");
            lavaTask.cancel();
            lavaTask = null;
        }
    }

    public void clear() {
        fillArea(pos1, pos2, Material.AIR);
    }

    public void removeMobs() {

        int amountRemoved = 0;

        for (Entity mob : mobs) {
            mob.remove();
            amountRemoved++;
        }

        debug("Removed " + amountRemoved + " mobs");
    }
    //endregion


    //region Platform Functions
    public void spawnPlatforms(Collection<Player> players) {

        Selection bounds = new Selection(getPos1(), getPos2());

        int platformsCreated = 0;

        for (Player player : players) {
            Location randomLocation = getRandomLocation();
            Location location = new Location(getWorld(), randomLocation.getX(), bounds.getMiddleY(), randomLocation.getZ());
            SpawnPlatform spawnPlatform = addSpawnPlatform(player, location, null, null, null);
            if (spawnPlatform != null) {
                platformsCreated++;
            }
        }


        if (SPAWN_EXTRA_PLATFORMS) {
            for(int i = 0; i < EXTRA_PLATFORMS_AMOUNT; i++) {
                Location randomLocation = getRandomLocation();

                Location location = new Location(getWorld(), randomLocation.getX(), bounds.getMiddleY(), randomLocation.getZ());

                SpawnPlatform spawnPlatform = addSpawnPlatform(location, null, null, null);
                if (spawnPlatform != null) {
                    platformsCreated++;
                }
            }
        }

        debug("created " + platformsCreated + " platforms");

    }

    public SpawnPlatform addSpawnPlatform(@NotNull Location location, @Nullable SpawnPlatform.PlatformType type, @Nullable Material material, @Nullable Consumer<Collection<Block>> onGenerate) {

        SpawnPlatform spawnPlatform = new SpawnPlatform(location,
                type != null ? type : DEFAULT_PLATFORM_TYPE,
                material != null ? material : DEFAULT_PLATFORM_MATERIAL);

        spawnPlatforms.add(spawnPlatform);

        if (!spawnPlatform.create(onGenerate)) {
            spawnPlatforms.remove(spawnPlatform);
        }

        return spawnPlatform;
    }

    public SpawnPlatform addSpawnPlatform(@NotNull Player player, @NotNull Location location, @Nullable SpawnPlatform.PlatformType type, @Nullable Material material, @Nullable Consumer<Collection<Block>> onGenerate) {

        SpawnPlatform spawnPlatform = new SpawnPlatform(player, location,
                type != null ? type : DEFAULT_PLATFORM_TYPE,
                material != null ? material : DEFAULT_PLATFORM_MATERIAL);

        playerSpawnPlatforms.put(player, spawnPlatform);

        if (!spawnPlatform.create(onGenerate)) {
            playerSpawnPlatforms.remove(player);
        }


        return spawnPlatform;
    }

    public void removeSpawnPlatform(SpawnPlatform platform, boolean warning) {
        Player player = platform.getPlayer();


        if (warning)
            platform.startRemovalTimer();
        else {
            platform.remove();
        }

        if (player != null) {
            playerSpawnPlatforms.remove(player);
            debug("removed " + player.getName() +  "'s platform from arena");
        }
        else {
            spawnPlatforms.remove(platform);
            debug("removed platform from arena");
        }
    }

    public void removeAllPlatforms() {
        spawnPlatforms.forEach(SpawnPlatform::remove);
        spawnPlatforms.clear();

        List<Player> keys = new ArrayList<>(playerSpawnPlatforms.keySet());
        for (Player key : keys) {
            SpawnPlatform platform = playerSpawnPlatforms.get(key);
            if (platform != null)
                platform.remove();
        }
        playerSpawnPlatforms.clear();
    }



    public Collection<SpawnPlatform> getSpawnPlatforms() {
        return Stream.concat(
                playerSpawnPlatforms.values().stream(),
                spawnPlatforms.stream()
        ).collect(Collectors.toList());
    }

    public SpawnPlatform getSpawnPlatform(Player player) {
        return playerSpawnPlatforms.get(player);
    }
    //endregion

    public void spawnMob(EntityType type, Location location) {
        mobs.add(getWorld().spawnEntity(location, type));
    }

    public void startLevelFillTask(Material fillMaterial, int speed) {
        if (lavaTask != null) {
            debugWarning("Unable to start lava fill task. already started");
            return;
        }

        RandomEventsPlugin plugin = RandomEventsPlugin.getInstance();

        Selection bounds = new Selection(pos1, pos2);
        List<Block> blocks = new ArrayList<>();

        lavaTask = new BukkitRunnable() {
            @Override
            public void run() {
                int minX = Math.min(pos1.getBlockX(), pos2.getBlockX());
                int maxX = Math.max(pos1.getBlockX(), pos2.getBlockX());
                int minY = Math.min(pos1.getBlockY(), pos2.getBlockY());
                int maxY = Math.max(pos1.getBlockY(), pos2.getBlockY());
                int minZ = Math.min(pos1.getBlockZ(), pos2.getBlockZ());
                int maxZ = Math.max(pos1.getBlockZ(), pos2.getBlockZ());


                for (int y = minY; y <= maxY; y++) {
                    for (int z = minZ; z <= maxZ; z++) {
                        for (int x = minX; x <= maxX; x++) {
                            Block block = bounds.getBlock(x, y, z);
                            blocks.add(block);
                        }
                    }
                }

                int batch_size = (maxX - minX + 1);


                lavaTask = new BukkitRunnable() {

                    int currentIndex = 0;

                    @Override
                    public void run() {
                        // Check if all blocks have been filled
                        if (currentIndex >= blocks.size()) {
                            cancel(); // Stop the task when done
                            return;
                        }

                        for (int i = 0; i < batch_size && currentIndex < blocks.size(); i++) {
                            Block block = blocks.get(currentIndex);

                            try {
                                block.setType(fillMaterial);
                            } catch (Exception e) {

                                debugError("could not fill arena with material: " + fillMaterial.name());
                                this.cancel();
                                return;
                            }

                            currentIndex++;
                        }
                    }
                }.runTaskTimer(plugin, 0, speed);
            }
        }.runTaskAsynchronously(plugin);
    }
}