package com.loficostudios.minigameeventsplugin.arena;

import com.loficostudios.minigameeventsplugin.api.bukkit.LavaLevelUpdatedEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

import static com.loficostudios.minigameeventsplugin.utils.Debug.logError;

public class ArenaFillTask extends BukkitRunnable {

    int lavaLevel = 0;
    int currentYLevel;
    int currentIndex = 0;

    private final Material fill;
    private final List<Block> blocks;
    private final int batchSize;
    private final GameArena arena;
    public ArenaFillTask(GameArena arena, Material fill, List<Block> blocks, int batchSize) {
        this.blocks = blocks;
        this.fill = fill;
        this.arena = arena;
        this.batchSize = batchSize;
        currentYLevel = blocks.get(0).getY();
    }

    @Override
    public void run() {
        if (currentIndex >= blocks.size()) {
            this.cancel();
            return;
        }

        for (int i = 0; i < batchSize && currentIndex < blocks.size(); i++) {
            Block block = blocks.get(currentIndex);

            try {
                block.setType(fill);

                if (block.getY() != currentYLevel) {
                    lavaLevel++;

                    currentYLevel = block.getY();
                    Bukkit.getPluginManager().callEvent(new LavaLevelUpdatedEvent(arena, currentYLevel, lavaLevel));
                }

            } catch (Exception e) {
                logError("Could not fill arena with material: " + fill.name());
                this.cancel();
                return;
            }

            currentIndex++;
        }
    }
}
