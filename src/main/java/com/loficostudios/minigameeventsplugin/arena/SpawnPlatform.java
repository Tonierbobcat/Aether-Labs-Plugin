package com.loficostudios.minigameeventsplugin.arena;

import com.loficostudios.minigameeventsplugin.utils.Countdown;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

import static com.loficostudios.minigameeventsplugin.utils.Debug.log;


public class SpawnPlatform {

    public enum Type {
        FLAT,
        PILLAR
    }

    private static final boolean ENABLE_TELEPORT_AFTER_PLATFORM_GENERATE = true;

    private static final int REMOVAL_TIME = 5;

    @Getter
    private final Player player;

    @Getter
    private Location location;


    private Set<Block> blocks = new HashSet<>();

    @Getter
    private Material material;

    private final Type type;

    private static final int DEFAULT_PLATFORM_RADIUS = 2;

    private Hologram hologram;

    @Getter
    private int radius;

    public SpawnPlatform(@NotNull SpawnPlatform.Type type, @NotNull Material material) {
        this.player = null;

        this.radius = DEFAULT_PLATFORM_RADIUS;

        this.type = type;
        this.material = material;
    }

    public SpawnPlatform(@NotNull Player player, @NotNull SpawnPlatform.Type type, @NotNull  Material material) {
        this.player = player;

        this.radius = DEFAULT_PLATFORM_RADIUS;

        this.type = type;
        this.material = material;
    }

    private void setHologram(Hologram holo) {
        if (this.hologram != null) {
            hologram.remove();
        }
        this.hologram = holo;
    }

    public void setLocation(Location center) {
        this.location = center;
        createHologram();
    }

    public void createHologram() {
        Location hologramLoc = new Location(location.getWorld(),
                this.location.getX() + 0.5,
                this.location.getY() - 1,
                this.location.getZ() + 0.5);

        String HOLOGRAM_NAME = "{name}'s plate";

        if (getPlayer() != null) {
            setHologram(new Hologram(
                    HOLOGRAM_NAME.replace("{name}", getPlayer().getName()),
                    hologramLoc));
        }
        else {
            setHologram(new Hologram(
                    HOLOGRAM_NAME.replace("{name}", "NaN"),
                    hologramLoc));
        }
    }
    private SpawnPlatformGenerator generator;
    public void setBlocks(Set<Block> blocks) {
        this.blocks = blocks;
    }

    public void setGenerator(SpawnPlatformGenerator generator) {
        this.generator = generator;
    }

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

    public void handleBlockBreak(Block block) {
        blocks.remove(block);
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
        setPlatform(material, false);
    }

    public void setPlatform(Material material, boolean recreate) {
        if (recreate) {
            blocks.forEach(block -> block.setType(Material.AIR));
            generator.calculateBlocks(location, this::setBlocks);
        }
        for (Block block : blocks) {
            block.setType(material);
        }
        this.material = material;
    }

    private BukkitTask removalTask;

    public void remove() {
        if (removalTask != null) {
            removalTask.cancel();
            removalTask = null;
        }

        for (Block block : blocks) {
            block.setType(Material.AIR);
        }
        blocks.clear();

        if (hologram != null)
            hologram.remove();
    }

    public boolean shrink(int amount) {
        if (radius == 0)
            return false;

        log("current radius " + radius);
        radius -= amount;

        setPlatform(material, true);

        log("new radius " + radius);

        log("shrunk to radius " + radius);

        return true;
    }

    public boolean expand(int amount) {
        radius += amount;

        setPlatform(material, true);

        log("expanded to radius " + radius);

        return true;
    }
}
