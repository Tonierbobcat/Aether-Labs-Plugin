package com.loficostudios.minigameeventsplugin.listeners;

import com.loficostudios.minigameeventsplugin.AetherLabsPlugin;
import com.loficostudios.minigameeventsplugin.api.bukkit.LavaLevelUpdatedEvent;
import com.loficostudios.minigameeventsplugin.eggwars.Egg;
import com.loficostudios.minigameeventsplugin.eggwars.EggWarsMode;
import com.loficostudios.minigameeventsplugin.game.GameManager;
import com.loficostudios.minigameeventsplugin.game.arena.SpawnPlatform;
import com.loficostudios.minigameeventsplugin.gamemode.GameModes;
import com.loficostudios.minigameeventsplugin.utils.Debug;
import com.loficostudios.minigameeventsplugin.utils.Selection;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ArenaListener implements Listener {

    private final GameManager gameManager;

    public ArenaListener(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @EventHandler
    private void onLavaChange(LavaLevelUpdatedEvent e) {
        var game = gameManager.getGame(e.getArena().getWorld());
        if (game == null)
            return;

        Set<SpawnPlatform> spawnPlatform = new HashSet<>(game.getArena().getSpawnPlatforms());

        Debug.log("platforms count: " + spawnPlatform.size() + " Lava Level: " + e.getY());
        int levelLevel = e.getY();

        for (SpawnPlatform platform : spawnPlatform) {
            int platformY = platform.getLocation().getBlockY();


            //Debug.log("platform y: " + platformY + " Lava Level: " + levelLevel);

            if (levelLevel > platformY + 1) {
                game.getArena().removeSpawnPlatform(platform, false);
            }
        }

        if (!game.getMode().equals(GameModes.EGG_WARS)) {
            return;
        }
        EggWarsMode mode = GameModes.EGG_WARS;

        List<Egg> eggs = new ArrayList<>(mode.getEggs(game));

        for (Egg egg : eggs) {

            int eggLevel = egg.getLocation().getBlockY();

            if (levelLevel == eggLevel) {
                mode.removeEgg(game, egg);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    private void onBlockBreak(BlockBreakEvent e) {
        Block block = e.getBlock();
        if (isArenaBorderBlock(block))
            e.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    private void onBlockInteract(PlayerInteractEvent e) {
        var block = e.getClickedBlock();
        if (block != null && isArenaBorderBlock(block))
            e.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    private void onBlockBreak(EntityChangeBlockEvent e) {
        Block block = e.getBlock();
        if (isArenaBorderBlock(block))
            e.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    private void onEntityExplode(EntityExplodeEvent e) {
        e.blockList().removeIf(this::isArenaBorderBlock);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    private void onBlockExplode(BlockExplodeEvent e) {
        Block block = e.getBlock();
        if (isArenaBorderBlock(block))
            e.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    private void onPiston(BlockPistonExtendEvent e) {
        for (Block block : e.getBlocks()) {
            if (isArenaBorderBlock(block))
                e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    private void onPiston(BlockPistonRetractEvent e) {
        for (Block block : e.getBlocks()) {
            if (isArenaBorderBlock(block))
                e.setCancelled(true);
        }
    }

    private boolean isArenaBorderBlock(Block block) {
        var conf = AetherLabsPlugin.inst().getArenaManager().getConfig(block.getWorld());
        if (conf == null)
            return false;

        Selection selection = conf.getBounds();
        Selection perimeter = selection.getPerimeter(1);

        Block arenaBlock = perimeter.getBlock(block.getX(), block.getY(), block.getZ());
        return arenaBlock != null;
    }
}
