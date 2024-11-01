package com.loficostudios.minigameeventsplugin.Listeners;


import com.loficostudios.melodyapi.utils.Common;
import com.loficostudios.minigameeventsplugin.Utils.Debug;
import com.loficostudios.minigameeventsplugin.eggwars.Egg;
import com.loficostudios.minigameeventsplugin.eggwars.EggWarsMode;
import com.loficostudios.minigameeventsplugin.Managers.GameManager.GameManager;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.util.Vector;

public class EggListener implements Listener {


    private final GameManager gameManager;

    public EggListener(GameManager gameManager) {
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
