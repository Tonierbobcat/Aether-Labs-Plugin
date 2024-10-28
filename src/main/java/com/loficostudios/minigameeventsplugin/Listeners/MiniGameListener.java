package com.loficostudios.minigameeventsplugin.Listeners;

import com.loficostudios.melodyapi.utils.Common;
import com.loficostudios.minigameeventsplugin.Managers.GameManager;
import com.loficostudios.minigameeventsplugin.Managers.PlayerManager;
import com.loficostudios.minigameeventsplugin.MiniGameEventsPlugin;
import com.loficostudios.minigameeventsplugin.GameArena.SpawnPlatform;
import com.loficostudios.minigameeventsplugin.Profile.Profile;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class MiniGameListener implements Listener {

    private final MiniGameEventsPlugin plugin;
    private final GameManager gameManager;
    private final PlayerManager playerManager;
    public MiniGameListener(GameManager gameManager) {
        this.gameManager = gameManager;
        this.plugin = MiniGameEventsPlugin.getInstance();
        this.playerManager = gameManager.getPlayerManager();
    }


    @EventHandler
    void onWorldChanged(PlayerChangedWorldEvent e) {

    }

    @EventHandler
    private void onJoin(final PlayerJoinEvent e) {
        final Player player = e.getPlayer();
        plugin.getOnlinePlayers().add(player);

        if (gameManager.inProgress() && player.getWorld().equals(gameManager.getArena().getWorld())) {
            gameManager.getStatusBar().addPlayer(player);
        }

        /*if (!gameManager.inProgress()) {
            gameManager.startGameCountdown(GameManager.GAME_COUNTDOWN);
        }*/
    }

    @EventHandler
    private void onQuit(final PlayerQuitEvent e) {
        final Player player = e.getPlayer();
        plugin.getOnlinePlayers().remove(player);

        if (playerManager.getPlayers().contains(player)) {
            gameManager.handlePlayerQuit(player);
        }
    }

    @EventHandler
    private void onDeath(PlayerDeathEvent e) {
        Player player = e.getEntity();

        if (gameManager.inProgress() && playerManager.getPlayers().contains(player)) {
            gameManager.handlePlayerDeath(player);

            Player killer = player.getKiller();

            if (killer != null) {
                plugin.getProfileManager().getProfile(killer.getUniqueId()).ifPresent(Profile::addKill);
            }
        }
    }



    @EventHandler
    private void onAttack(EntityDamageEvent e) {
        if ((e.getEntity() instanceof Player player)) {
            if (player.getWorld() != gameManager.getArena().getWorld())
                return;

            handleEventDuringSetupGame(e);
        }
    }

    @EventHandler
    private void onBlockBreak(BlockBreakEvent e) {
        if (handleEventDuringSetupGame(e))
            return;

        Block block = e.getBlock();
        Player player = e.getPlayer();

        if (gameManager.inProgress()) {
            SpawnPlatform spawnPlatform =  gameManager.getArena().getSpawnPlatform(player);
            if (spawnPlatform != null) {
                spawnPlatform.handleBlockBreak(block);
            }
        }
    }

    private boolean handleEventDuringSetupGame(Event e) {
        if (gameManager.getCurrentState() == GameManager.GameState.SETUP && e instanceof Cancellable cancellable) {
            cancellable.setCancelled(true);
            Common.broadcast("cancelled " + e.getEventName());
            return true;
        }

        return false;
    }

}
