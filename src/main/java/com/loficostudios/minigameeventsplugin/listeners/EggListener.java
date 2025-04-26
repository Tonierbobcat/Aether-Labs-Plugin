package com.loficostudios.minigameeventsplugin.listeners;


import com.loficostudios.minigameeventsplugin.eggwars.Egg;
import com.loficostudios.minigameeventsplugin.eggwars.EggWarsMode;
import com.loficostudios.minigameeventsplugin.game.Game;
import com.loficostudios.minigameeventsplugin.gamemode.GameModes;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;

public class EggListener implements Listener {


    private final Game game;

    public EggListener(Game gameManager) {
        this.game = gameManager;
    }

    @EventHandler
    private void onEggGravity(EntityChangeBlockEvent e) {
        if (isCurrentGameOther())
            return;
        EggWarsMode mode = (EggWarsMode) game.getCurrentMode();

        if (mode.getEgg(game, e.getBlock()) != null) {
            e.setCancelled(true);
        }

    }

    @EventHandler
    private void onEggTeleport(BlockFromToEvent e) {
        if (isCurrentGameOther())
            return;
        EggWarsMode mode = (EggWarsMode) game.getCurrentMode();

        if (mode.getEgg(game, e.getBlock()) != null) {
            e.setCancelled(true);
        }
    }




    @EventHandler
    private void onEggBreak(BlockBreakEvent e) {
        if (isCurrentGameOther())
            return;
        EggWarsMode mode = (EggWarsMode) game.getCurrentMode();

        Block block = e.getBlock();

        Egg egg = mode.getEgg(game, block);

        if (egg != null) {
            if (egg.isBlockBelowEgg(block)) {
                e.setCancelled(true);
                return;
            }
            //e.setCancelled(true);

            mode.handleEggBreak(game, egg);
        }
    }

    private boolean isCurrentGameOther() {
        return !game.getCurrentMode().equals(GameModes.EGG_WARS);
    }

}
