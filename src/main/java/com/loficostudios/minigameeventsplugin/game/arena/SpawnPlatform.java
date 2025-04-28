package com.loficostudios.minigameeventsplugin.game.arena;

import com.loficostudios.minigameeventsplugin.utils.Countdown;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;


@SuppressWarnings({"LombokGetterMayBeUsed", "LombokSetterMayBeUsed"})
public class SpawnPlatform {

    // Static constants
    private static final boolean ENABLE_TELEPORT_AFTER_PLATFORM_GENERATE = true;
    private static final int REMOVAL_TIME = 5;
    private static final int DEFAULT_PLATFORM_RADIUS = 2;

    private final Player player;

    private Location location;

    private Material material;

    private int radius;

    public Player getPlayer() {
        return player;
    }

    public Location getLocation() {
        return location;
    }

    public Material getMaterial() {
        return material;
    }

    public Type getType() {
        return type;
    }

    public int getRadius() {
        return radius;
    }

    public boolean setRadius(int i) {
        if (i <= 0)
            return false;
        this.radius = i;
        setPlatform(material, true);
        return true;
    }

    private final Type type;
    private Set<Block> blocks = new HashSet<>();
    private SpawnPlatformGenerator generator;
    private BukkitTask removalTask;
    private Hologram hologram;

    public SpawnPlatform(@NotNull SpawnPlatform.Type type, @NotNull Material material) {
        this.player = null;
        this.radius = DEFAULT_PLATFORM_RADIUS;
        this.type = type;
        this.material = material;
    }

    public SpawnPlatform(@NotNull Player player, @NotNull SpawnPlatform.Type type, @NotNull Material material) {
        this.player = player;
        this.radius = DEFAULT_PLATFORM_RADIUS;
        this.type = type;
        this.material = material;
    }

    public void setLocation(Location center) {
        this.location = center;
        updateHologram(this.location);
    }

    private void updateHologram(Location location) {
        Location hologramLoc = new Location(location.getWorld(),
                location.getX() + 0.5,
                location.getY() - 1,
                location.getZ() + 0.5);

        if (hologram == null) {
            String text = "{name}'s plate";

            if (getPlayer() != null) {
                this.hologram = new Hologram(
                        text.replace("{name}", getPlayer().getName()),
                        hologramLoc);
            } else {
                this.hologram = new Hologram(
                        text.replace("{name}", "NaN"),
                        hologramLoc);
            }
        } else {
            hologram.teleport(location);
        }
    }

    public void setBlocks(Set<Block> blocks) {
        this.blocks = blocks;
    }

    public void setGenerator(SpawnPlatformGenerator generator) {
        this.generator = generator;
    }

//    public void setHologram(Hologram holo) {
//        if (this.hologram != null) {
//            hologram.remove();
//        }
//        this.hologram = holo;
//        updateHologram(this.location);
//    }

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

    public void startRemovalTimer() {
        removalTask = new Countdown(i -> {
            if (i % 2 == 0) {
                setPlatform(Material.RED_STAINED_GLASS);
            } else {
                setPlatform(Material.WHITE_STAINED_GLASS);
            }
        }, this::remove).start(REMOVAL_TIME);
    }

    public @NotNull Location getTeleportLocation() {
        return new Location(this.location.getWorld(), this.location.getX() + 0.5, this.location.getY() + 1, this.location.getZ() + 0.5);
    }

    public void teleport(Player player) {
        Location location = getTeleportLocation();
        if (player != null) {
            player.teleport(location);
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_TELEPORT, 1, 1);
        }
    }

    public void handleBlockBreak(Block block) {
        blocks.remove(block);
    }

    public void remove() {
        if (removalTask != null) {
            removalTask.cancel();
            removalTask = null;
        }

        for (Block block : blocks) {
            block.setType(Material.AIR);
        }
        blocks.clear();

        if (hologram != null) {
            hologram.remove();
        }
    }

    public enum Type {
        FLAT,
        PILLAR
    }
}