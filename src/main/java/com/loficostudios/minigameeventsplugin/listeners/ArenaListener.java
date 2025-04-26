package com.loficostudios.minigameeventsplugin.listeners;

import com.loficostudios.minigameeventsplugin.api.bukkit.LavaLevelUpdatedEvent;
import com.loficostudios.minigameeventsplugin.arena.GameArena;
import com.loficostudios.minigameeventsplugin.arena.SpawnPlatform;
import com.loficostudios.minigameeventsplugin.eggwars.Egg;
import com.loficostudios.minigameeventsplugin.eggwars.EggWarsMode;
import com.loficostudios.minigameeventsplugin.game.Game;
import com.loficostudios.minigameeventsplugin.gamemode.GameModes;
import com.loficostudios.minigameeventsplugin.utils.Debug;
import com.loficostudios.minigameeventsplugin.utils.Selection;
import org.bukkit.Bukkit;
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

    private final Game game;

    public ArenaListener(Game gameManager) {
        this.game = gameManager;
    }

    @EventHandler
    private void onLavaChange(LavaLevelUpdatedEvent e) {

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

        if (!game.getCurrentMode().equals(GameModes.EGG_WARS)) {
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
        Bukkit.getLogger().info("exploded");
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
        GameArena arena = game.getArena();

        if (arena == null)
            return false;

        Selection selection = new Selection(arena.getPos1(), arena.getPos2());
        Selection perimeter = selection.getPerimeter(1);

        Block arenaBlock = perimeter.getBlock(block.getX(), block.getY(), block.getZ());
        return arenaBlock != null;
    }
}
