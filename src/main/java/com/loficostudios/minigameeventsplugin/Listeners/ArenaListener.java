package com.loficostudios.minigameeventsplugin.Listeners;

import com.loficostudios.melodyapi.utils.Common;
import com.loficostudios.minigameeventsplugin.BukkitEvents.ArenaLavaLevelChangedEvent;
import com.loficostudios.minigameeventsplugin.GameArena.GameArena;
import com.loficostudios.minigameeventsplugin.GameArena.SpawnPlatform;
import com.loficostudios.minigameeventsplugin.Managers.GameManager.GameManager;
import com.loficostudios.minigameeventsplugin.Utils.Debug;
import com.loficostudios.minigameeventsplugin.Utils.Selection;
import com.loficostudios.minigameeventsplugin.eggwars.Egg;
import com.loficostudios.minigameeventsplugin.eggwars.EggWarsMode;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;

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
    private void onLavaChange(ArenaLavaLevelChangedEvent e) {

        Set<SpawnPlatform> spawnPlatform = new HashSet<>(gameManager.getArena().getSpawnPlatforms());

        Debug.log("platforms count: " + spawnPlatform.size() + " Lava Level: " + e.getY());
        int levelLevel = e.getY();

        for (SpawnPlatform platform : spawnPlatform) {
            int platformY = platform.getLocation().getBlockY();


            //Debug.log("platform y: " + platformY + " Lava Level: " + levelLevel);

            if (levelLevel > platformY + 1) {
                gameManager.getArena().removeSpawnPlatform(platform, false);
            }
        }

        if (!gameManager.getCurrentMode().equals(gameManager.EGG_WARS)) {
            return;
        }
        EggWarsMode mode = gameManager.EGG_WARS;

        List<Egg> eggs = new ArrayList<>(mode.getEggs());

        for (Egg egg : eggs) {

            int eggLevel = egg.getLocation().getBlockY();

            if (levelLevel == eggLevel) {
                mode.removeEgg(egg);
            }
        }
    }

    @EventHandler
    private void onArenaBlockBreak(BlockBreakEvent e) {
        Block block = e.getBlock();

        if (e.getPlayer().isOp())
            return;

        if (isArenaBorderBlock(block)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    private void onArenaBlockExplode(BlockExplodeEvent e) {
        Common.broadcast("exploaded");
        Block block = e.getBlock();
        e.setCancelled(true);


        if (isArenaBorderBlock(block)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    private void onArenaBlockChanged(EntityChangeBlockEvent e) {
        Block block = e.getBlock();

        if (isArenaBorderBlock(block)) {
            e.setCancelled(true);
        }
    }

    private boolean isArenaBorderBlock(Block block) {
        GameArena arena = gameManager.getArena();

        if (arena == null)
            return false;

        Selection selection = new Selection(arena.getPos1(), arena.getPos2());
        Selection perimeter = selection.getPerimeter(1);

        Block arenaBlock = perimeter.getBlock(block.getX(), block.getY(), block.getZ());
        return arenaBlock != null;
    }
}
