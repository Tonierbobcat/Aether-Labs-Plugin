package com.loficostudios.minigameeventsplugin.listeners;


import com.loficostudios.minigameeventsplugin.eggwars.Egg;
import com.loficostudios.minigameeventsplugin.eggwars.EggWarsMode;
import com.loficostudios.minigameeventsplugin.game.Game;
import com.loficostudios.minigameeventsplugin.game.GameManager;
import com.loficostudios.minigameeventsplugin.gamemode.GameModes;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;

public class EggListener implements Listener {

    private final GameManager gameManager;

    public EggListener(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @EventHandler
    private void onEggGravity(EntityChangeBlockEvent e) {
        var block = e.getBlock();
        var game = gameManager.getGame(block.getWorld());
        if (game == null)
            return;
        if (isCurrentGameOther(game))
            return;
        EggWarsMode mode = (EggWarsMode) game.getMode();

        if (mode.getEgg(game, block) != null) {
            e.setCancelled(true);
        }

    }

    @EventHandler
    private void onEggTeleport(BlockFromToEvent e) {
        var block = e.getBlock();
        var game = gameManager.getGame(block.getWorld());
        if (game == null)
            return;
        if (isCurrentGameOther(game))
            return;
        EggWarsMode mode = (EggWarsMode) game.getMode();

        if (mode.getEgg(game, block) != null) {
            e.setCancelled(true);
        }
    }




    @EventHandler
    private void onEggBreak(BlockBreakEvent e) {
        var block = e.getBlock();
        var game = gameManager.getGame(block.getWorld());
        if (game == null)
            return;
        if (isCurrentGameOther(game))
            return;
        EggWarsMode mode = (EggWarsMode) game.getMode();

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

    private boolean isCurrentGameOther(Game game) {
        return !game.getMode().equals(GameModes.EGG_WARS);
    }

}
