package com.loficostudios.minigameeventsplugin.game.arena;

import com.loficostudios.minigameeventsplugin.AetherLabsPlugin;
import com.loficostudios.minigameeventsplugin.config.ArenaConfig;
import com.loficostudios.minigameeventsplugin.utils.Debug;
import com.loficostudios.minigameeventsplugin.utils.Selection;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;

import static com.loficostudios.minigameeventsplugin.utils.Debug.log;
import static com.loficostudios.minigameeventsplugin.utils.Debug.logWarning;
import static com.loficostudios.minigameeventsplugin.utils.Selection.randomDouble;

@SuppressWarnings("UnusedReturnValue")
public class GameArena {

    public static final int MIN_GAME_ARENA_AREA = 146068;
    public static final Material DEFAULT_PLATFORM_MATERIAL = Material.STONE;
    public static final SpawnPlatform.Type DEFAULT_PLATFORM_TYPE = SpawnPlatform.Type.FLAT;
    private static final Boolean DEFAULT_SPAWN_EXTRA_PLATFORMS = true;
    private static final Integer EXTRA_PLATFORMS_AMOUNT = 25;

    private final AetherLabsPlugin plugin;

    @Getter private Location pos1;
    @Getter private Location pos2;

    @Getter
    private World world;

    @Setter
    @Getter
    private int lavaSpeed;

    private final Collection<Entity> mobs = new ArrayList<>();

    private final Map<Player, SpawnPlatform> playerSpawnPlatforms = new HashMap<>();
    private final Collection<SpawnPlatform> spawnPlatforms = new ArrayList<>();

    private BukkitTask lavaTask;

    public GameArena(AetherLabsPlugin plugin, ArenaConfig config) {
        this.plugin = plugin;
        this.pos1 = config.getMin().toLocation(config.getWorld());
        this.pos2 = config.getMax().toLocation(config.getWorld());
        this.world = config.getWorld();
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

    public void clear() {
        cancelFillTask();

        Selection selection = new Selection(pos1, pos2);

        List<Block> blocks = new ArrayList<>(selection.getBlocks());

        blocks.forEach(block -> {
            block.setType(Material.AIR);
        });
    }

    public void removeEntities() {
        int amountRemoved = 0;

        int minX = Math.min(pos1.getBlockX(), pos2.getBlockX());
        int maxX = Math.max(pos1.getBlockX(), pos2.getBlockX());
        int minY = Math.min(pos1.getBlockY(), pos2.getBlockY());
        int maxY = Math.max(pos1.getBlockY(), pos2.getBlockY());
        int minZ = Math.min(pos1.getBlockZ(), pos2.getBlockZ());
        int maxZ = Math.max(pos1.getBlockZ(), pos2.getBlockZ());


        for (LivingEntity entity : world.getLivingEntities()) {
            Location loc = entity.getLocation();
            if (loc.getBlockX() >= minX && loc.getBlockX() <= maxX &&
                    loc.getBlockY() >= minY && loc.getBlockY() <= maxY &&
                    loc.getBlockZ() >= minZ && loc.getBlockZ() <= maxZ) {


                if (entity.getType() != EntityType.PLAYER) {
                    entity.remove();
                    amountRemoved++;
                }
            }
        }

        log("Removed " + amountRemoved + " mobs");
    }
    //endregion

    public SpawnPlatform addSpawnPlatform(@Nullable Location location, @Nullable SpawnPlatform.Type type, @Nullable Material material, @Nullable Runnable onAdd, @NotNull SpawnPlatformGenerator.SpawnAlgorithm spawnAlgorithm) {

        SpawnPlatform spawnPlatform = new SpawnPlatform(
                type != null ? type : DEFAULT_PLATFORM_TYPE,
                material != null ? material : DEFAULT_PLATFORM_MATERIAL);

        boolean isCreated;

        var gen = new SpawnPlatformGenerator(spawnPlatform, this);

        if (location != null) {
            isCreated = gen.create(location, onGenerate -> {
                if (onAdd != null)
                    onAdd.run();
            });
        }
        else {
            isCreated = gen.create(spawnAlgorithm, onGenerate -> {
                if (onAdd != null)
                    onAdd.run();
            });
        }

        if (!isCreated) {
            return null;
        }
        else {
            spawnPlatforms.add(spawnPlatform);
            return spawnPlatform;
        }
    }

    public SpawnPlatform addSpawnPlatform(@NotNull Player player, @Nullable Location location, @Nullable SpawnPlatform.Type type, @Nullable Material material, @Nullable Consumer<SpawnPlatform> onAdd, @NotNull SpawnPlatformGenerator.SpawnAlgorithm spawnAlgorithm) {

        SpawnPlatform spawnPlatform = new SpawnPlatform(player,
                type != null ? type : DEFAULT_PLATFORM_TYPE,
                material != null ? material : DEFAULT_PLATFORM_MATERIAL);

        boolean isCreated = false;

        var gen = new SpawnPlatformGenerator(spawnPlatform, this);

        if (location != null) {
            isCreated = gen.create(location, onGenerate -> {
                if (onAdd != null)
                    onAdd.accept(spawnPlatform);
            });
        }
        else {
            isCreated = gen.create(spawnAlgorithm, onGenerate -> {
                if (onAdd != null)
                    onAdd.accept(spawnPlatform);
            });
        }

        if (!isCreated) {
            return null;
        }
        else {
            playerSpawnPlatforms.put(player, spawnPlatform);
            Debug.log("add spawn platform for " + player.getName());
            return spawnPlatform;
        }
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
            log("removed " + player.getName() +  "'s platform from arena");
        }
        else {
            spawnPlatforms.remove(platform);
            log("removed platform from arena");
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

        Collection<SpawnPlatform> concat = new ArrayList<>();

        concat.addAll(spawnPlatforms);
        concat.addAll(playerSpawnPlatforms.values());

        return concat;
    }

    public SpawnPlatform getSpawnPlatform(Player player) {
        return playerSpawnPlatforms.get(player);
    }

    public Entity spawnEntity(EntityType type, Location location) {
        return getWorld().spawnEntity(location, type);
    }

    public void startFillTask(Material fill, int speed) {
        if (lavaTask != null) {
            logWarning("Unable to start lava fill task. already started");
            return;
        }
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage("Fill Speed: " + speed);
        }


        int minX = Math.min(pos1.getBlockX(), pos2.getBlockX());
        int maxX = Math.max(pos1.getBlockX(), pos2.getBlockX());
        int minY = Math.min(pos1.getBlockY(), pos2.getBlockY());
        int maxY = Math.max(pos1.getBlockY(), pos2.getBlockY());
        int minZ = Math.min(pos1.getBlockZ(), pos2.getBlockZ());
        int maxZ = Math.max(pos1.getBlockZ(), pos2.getBlockZ());

        int base = (maxX - minX + 1);
        int batchSize = (int) (base * (1.0 + (speed - 1) * (1.0 / 9.0)));

        List<Block> blocks = new ArrayList<>();

        Selection bounds = new Selection(pos1, pos2);

        for (int y = minY; y <= maxY; y++) {
            for (int z = minZ; z <= maxZ; z++) {
                for (int x = minX; x <= maxX; x++) {
                    Block block = bounds.getBlock(x, y, z);
                    blocks.add(block);
                }
            }
        }

        lavaTask = new ArenaFillTask(this, fill, blocks, batchSize)
                .runTaskTimer(plugin, 0, 5 - (int) ((speed - 1) * (4.0 / 9.0)));
    }

    public void cancelFillTask() {
        if (lavaTask == null)
            return;
        lavaTask.cancel();
        lavaTask = null;
    }
}