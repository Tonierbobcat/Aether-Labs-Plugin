package com.loficostudios.minigameeventsplugin.arena;

import com.loficostudios.minigameeventsplugin.AetherLabsPlugin;
import com.loficostudios.minigameeventsplugin.utils.Countdown;
import com.loficostudios.minigameeventsplugin.utils.Generator;
import com.loficostudios.minigameeventsplugin.utils.Selection;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

import static com.loficostudios.minigameeventsplugin.utils.Debug.log;
import static com.loficostudios.minigameeventsplugin.utils.Debug.logWarning;


public class SpawnPlatform {

    public enum PlatformType {
        FLAT,
        PILLAR
    }

    private final AetherLabsPlugin plugin;

    @Getter
    private final Player player;

    @Getter
    private Location location;


    private final Set<Block> blocks = new HashSet<>();

    @Getter
    private Material material;

    private final PlatformType type;

    private static final int DEFAULT_PLATFORM_RADIUS = 2;

    private Hologram hologram;

    @Getter
    private int radius;

    private final GameArena arena;

    private final SpawnAlgorithm spawnAlgorithm;

    public SpawnPlatform(@NotNull PlatformType type, @NotNull Material material, @NotNull SpawnPlatform.SpawnAlgorithm spawnAlgorithm) {
        this.player = null;

        this.plugin = AetherLabsPlugin.getInstance();

        this.radius = DEFAULT_PLATFORM_RADIUS;

        this.arena = this.plugin.getActiveGame().getArena();

        this.spawnAlgorithm = spawnAlgorithm;

        this.type = type;
        this.material = material;



        log("BEFORE CREATE");
    }

    public SpawnPlatform(@NotNull Player player, @NotNull PlatformType type, @NotNull  Material material,  @NotNull SpawnPlatform.SpawnAlgorithm spawnAlgorithm) {
        this.player = player;
        //this.location = loc;
        this.plugin = AetherLabsPlugin.getInstance();
        this.radius = DEFAULT_PLATFORM_RADIUS;

        this.arena = this.plugin.getActiveGame().getArena();
        this.spawnAlgorithm = spawnAlgorithm;
        this.type = type;
        this.material = material;

    }

    private static final boolean ENABLE_TELEPORT_AFTER_PLATFORM_GENERATE = true;

    private static final int REMOVAL_TIME = 5;

    public void startRemovalTimer() {



        removalTask = new Countdown("platform removal", countdown -> {

            if (countdown % 2 == 0) {
                setPlatform(Material.RED_STAINED_GLASS);
            }
            else {
                setPlatform(Material.WHITE_STAINED_GLASS);
            }
        }, this::remove).start(REMOVAL_TIME);
    }

    public enum SpawnAlgorithm { EQUAL_HEIGHT, RANDOM_HEIGHT }

    public boolean create(Location location, Consumer<Collection<Block>> onGenerate) {

        Location center = null;

        Generator generator = new Generator(material, generatedBlocks -> {

            if (onGenerate != null) {
                onGenerate.accept(generatedBlocks);
            }



        });

        calculateBlocks(center, calculatedBlocks -> {
            generator.generate(calculatedBlocks);
            blocks.addAll(calculatedBlocks);
        });

        this.location = center;
        return true;
    }

    public boolean create(Consumer<Collection<Block>> onGenerate) {



        Selection bounds = new Selection(arena.getPos1(), arena.getPos2()).adjustSelection(-10);



        Location center = null;

        boolean isCreated = false;
        int attempts = 0;



        while (!isCreated && attempts < 50) {



            center = bounds.getRandomLocation();
            center.setY(bounds.getMiddleY());


            //OLD DIFFERENT HEIGHTS LOGIC
//            switch (algorithm) {
//                case RANDOM_HEIGHT:
//                    center = bounds.getRandomLocation();
//                    break;
//                case EQUAL_HEIGHT:
//                    center = bounds.getRandomLocation();
//                    center.setY(bounds.getMiddleY());
//                    break;
//            }

            if (isLocationValid(center)) {




                Generator generator = new Generator(material, generatedBlocks -> {

                    if (onGenerate != null) {
                        onGenerate.accept(generatedBlocks);
                    }

                });

                if (this.spawnAlgorithm.equals(SpawnAlgorithm.RANDOM_HEIGHT)) {

                    int y = bounds.getRandomLocation().getBlockY();

                    //center.setY(y);

                    center = new Location(center.getWorld(), center.getBlockX(), y, center.getBlockZ());
                }

                calculateBlocks(center, calculatedBlocks -> {
                    generator.generate(calculatedBlocks);
                    blocks.addAll(calculatedBlocks);
                });

                isCreated = true;
            }
            attempts++;
        }

        if (isCreated) {
            this.location = center;
            logWarning("attempts to create " + attempts);

            Location hologramLoc = new Location(location.getWorld(),
                    this.location.getX() + 0.5,
                    this.location.getY() - 1,
                    this.location.getZ() + 0.5);


            String HOLOGRAM_NAME = "{name}'s plate";

            if (player != null) {
                hologram = new Hologram(
                        HOLOGRAM_NAME.replace("{name}", player.getName()),
                        hologramLoc);
            }
            else {
                hologram = new Hologram(
                        HOLOGRAM_NAME.replace("{name}", "NaN"),
                        hologramLoc);
            }


            //ArmorStand hologram = location.getWorld().spawn

            return true;
        }
        else {
            return false;
        }
    }

    public void handleBlockBreak(Block block) {
        blocks.remove(block);
    }

    private static final int MIN_SPACING = 7;



    private boolean isLocationValid(Location center) {


        /*for (SpawnPlatform platform : arena.getSpawnPlatforms()) {
            if (center.distance(platform.location) < MIN_SPACING + radius) {
                return false;
            }
        }
        return true;*/

        for (SpawnPlatform platform : arena.getSpawnPlatforms()) {
            // Calculate the distance in x and z only
            double dx = center.getX() - platform.location.getX();
            double dz = center.getZ() - platform.location.getZ();
            double distanceXZ = Math.sqrt(dx * dx + dz * dz); // 2D distance ignoring y

            if (distanceXZ < MIN_SPACING + radius) {
                return false;
            }
        }
        return true;
    }

    private void calculateBlocks(Location center, Consumer<Set<Block>> callback) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            Selection selection = new Selection(arena.getPos1(), arena.getPos2());

            Set<Block> blocksToSet = new HashSet<>();

            // Calculate the start position to center the platform based on the radius
            int startX = center.getBlockX() - radius;
            int startZ = center.getBlockZ() - radius;
            int y = center.getBlockY();

            // Place blocks in a 3x3 area, based on radius
            for (int x = startX; x < startX + 2 * radius + 1; x++) {
                for (int z = startZ; z < startZ + 2 * radius + 1; z++) {
                    Block block = selection.getBlock(x, y, z);

                    if (block != null)
                        blocksToSet.add(block);
                }
            }

            Bukkit.getScheduler().runTask(plugin, () -> callback.accept(blocksToSet));
        });
    }

    public @NotNull Location getTeleportLocation() {
        return new Location(this.location.getWorld(), this.location.getX() + 0.5, this.location.getY() + 1, this.location.getZ() + 0.5);
    }

    public void teleportCenter() {
        Location location = getTeleportLocation();

        player.teleport(location);
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_TELEPORT, 1, 1);
    }

    public void setPlatform(Material material) {
        new Generator(material, null)
                .generate(blocks);

        this.material = material;
    }

    private BukkitTask removalTask;

    public void remove() {
        if (removalTask != null) {
            removalTask.cancel();
            removalTask = null;
        }

        Generator generator = new Generator(Material.AIR, null);

        generator.generate(blocks);
        blocks.clear();

        if (hologram != null)
            hologram.remove();
    }

    public boolean shrink(int amount) {


        if (radius == 0) {
            log("removing platform");
            return false;
        }

        log("current radius " + radius);
        radius -= amount;
        log("new radius " + radius);



        updateBlocks(material, true);



        log("shrunk to radius " + radius);

        return true;
    }

    public boolean expand(int amount) {

        radius += amount;

        updateBlocks(material, false);

        log("expanded to radius " + radius);

        return true;
    }

    private void updateBlocks(Material material, boolean setBlocks) {
        Generator generator = new Generator(material, null);
        calculateBlocks(location, calculatedBlocks -> {
            if (setBlocks)
                blocks.forEach(block -> block.setType(Material.AIR));

            blocks.clear();

            generator.generate(calculatedBlocks);
            blocks.addAll(calculatedBlocks);
        });
    }

    public void recreate(Material material) {
        Generator generator = new Generator(material, null);

        blocks.clear();
        calculateBlocks(location, calculatedBlocks -> {
            generator.generate(calculatedBlocks);
            blocks.addAll(calculatedBlocks);
        });
    }



    //Location is randomly selected in constructor;

//    //region OLD OLD OLD
//    private boolean create(Location location, Material material, int radius) {
//
//        GameArena arena = MiniGameEventsPlugin.getInstance().getGameManager().getArena();
//        Selection bounds = new Selection(arena.getPos1(), arena.getPos2());
//
//        Collection<Block> blocksToSet = new ArrayList<>();
//
//        int centerX = location.getBlockX();
//        int centerY = location.getBlockY();
//        int centerZ = location.getBlockZ();
//
//        Location minCorner = new Location(arena.getWorld(), centerX - radius, centerY, centerZ - radius);
//        Location maxCorner = new Location(arena.getWorld(), centerX + radius, centerY, centerZ + radius);
//
//        Selection platform = new Selection(minCorner, maxCorner);
//
//        for (int x = -radius; x <= radius; x++) {
//            for (int z = -radius; z <= radius; z++) {
//
//                Block block = bounds.getBlock(centerX + x, centerY, centerZ + z);
//
//                if (block != null) {
//                    blocksToSet.add(block);
//                }
//            }
//        }
//
//        if (platform.blocks.stream().noneMatch(block -> block.getType().equals(Material.AIR))) {
//            return false;
//        }
//
//        setBlocks(blocksToSet, material);
//
//        return true;
//    }
//
//    //private static final int BATCH_SIZE = 50;
//
//    public void setBlocks(Collection<Block> blocksToSet, Material material) {
//
//        blocksToSet.forEach(block -> {
//            block.setType(material);
//            blocks.add(block);
//        });
//
//        /*Set<Block> trackedBlocks = new HashSet<>(blocks);
//        new BukkitRunnable() {
//
//            final Iterator<Block> iterator = blocksToSet.iterator();
//
//            @Override
//            public void run() {
//
//                debug("HANG TICK");
//
//                int count = 0;
//                while (iterator.hasNext() && count < BATCH_SIZE) {
//                    Block block = iterator.next();
//                    block.setType(material);
//                    trackedBlocks.add(block);
//                    count++;
//                }
//
//                if (!iterator.hasNext()) {
//                    blocks.addAll(trackedBlocks);
//
//                    this.cancel();
//                }
//            }
//        }.runTaskTimer(MiniGameEventsPlugin.getInstance(), 0, 1);*/
//    }
//    //endregion
}
