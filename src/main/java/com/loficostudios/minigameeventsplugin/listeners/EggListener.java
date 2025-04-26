package com.loficostudios.minigameeventsplugin.listeners;


import com.loficostudios.minigameeventsplugin.eggwars.Egg;
import com.loficostudios.minigameeventsplugin.eggwars.EggWarsMode;
import com.loficostudios.minigameeventsplugin.game.Game;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;

public class EggListener implements Listener {


    private final Game gameManager;

    public EggListener(Game gameManager) {
        this.gameManager = gameManager;
    }

    @EventHandler
    private void onEggGravity(EntityChangeBlockEvent e) {
        if (isCurrentGameOther())
            return;
        EggWarsMode mode = (EggWarsMode) gameManager.getCurrentMode();

        if (mode.getEgg(e.getBlock()) != null) {
            e.setCancelled(true);
        }

    }

    @EventHandler
    private void onEggTeleport(BlockFromToEvent e) {
        if (isCurrentGameOther())
            return;
        EggWarsMode mode = (EggWarsMode) gameManager.getCurrentMode();

        if (mode.getEgg(e.getBlock()) != null) {
            e.setCancelled(true);
        }
    }




    @EventHandler
    private void onEggBreak(BlockBreakEvent e) {
        if (isCurrentGameOther())
            return;
        EggWarsMode mode = (EggWarsMode) gameManager.getCurrentMode();

        Block block = e.getBlock();

        Egg egg = mode.getEgg(block);

        if (egg != null) {
            if (egg.isBlockBelowEgg(block)) {
                e.setCancelled(true);
                return;
            }
            //e.setCancelled(true);

            mode.handleEggBreak(e, egg);
        }
    }

    private boolean isCurrentGameOther() {
        return !gameManager.getCurrentMode().equals(gameManager.EGG_WARS);
    }

}
