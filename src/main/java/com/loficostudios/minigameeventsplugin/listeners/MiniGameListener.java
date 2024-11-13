package com.loficostudios.minigameeventsplugin.listeners;

import com.loficostudios.melodyapi.utils.Common;
import com.loficostudios.minigameeventsplugin.arena.GameArena;
import com.loficostudios.minigameeventsplugin.managers.GameManager.GameManager;
import com.loficostudios.minigameeventsplugin.managers.GameManager.GameState;
import com.loficostudios.minigameeventsplugin.managers.PlayerManager.PlayerManager;
import com.loficostudios.minigameeventsplugin.managers.VoteManager;
import com.loficostudios.minigameeventsplugin.AetherLabsPlugin;
import com.loficostudios.minigameeventsplugin.arena.SpawnPlatform;
import com.loficostudios.minigameeventsplugin.api.events.RoundSurvivedEvent;
import com.loficostudios.minigameeventsplugin.utils.Debug;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

import static com.loficostudios.minigameeventsplugin.managers.GameManager.GameManager.MIN_PLAYERS_TO_START;

public class MiniGameListener implements Listener {

    private final AetherLabsPlugin plugin;
    private final GameManager gameManager;
    private final PlayerManager playerManager;
    public MiniGameListener(AetherLabsPlugin plugin, GameManager gameManager) {
        this.gameManager = gameManager;
        this.plugin = plugin;
        this.playerManager = gameManager.getPlayerManager();
    }




    @EventHandler
    private void onFlammableBlockPlaced(BlockPlaceEvent e) {
        Block block = e.getBlock();

        int time = 20;

        if (gameManager.inProgress() && block.getType().equals(Material.WHITE_WOOL)) {

            new BukkitRunnable() {

                int timer = time;

                List<Material> allowed = new ArrayList<>(List.of(

                        Material.BLACK_WOOL,
                        Material.GRAY_WOOL,
                        Material.LIGHT_GRAY_WOOL,
                        Material.WHITE_WOOL

                ));

                @Override
                public void run() {

                    if (!allowed.contains(block.getType())) {
                        Debug.log("canceled wool task");
                        this.cancel();
                    }


                    if (timer > 0) {
                        timer--;

                        switch (timer) {
                            case 6 -> block.setType(Material.LIGHT_GRAY_WOOL);
                            case 4 -> block.setType(Material.GRAY_WOOL);
                            case 2 -> block.setType(Material.BLACK_WOOL);
                        }
                    }
                    else {
                        block.setType(Material.AIR);
                        this.cancel();
                    }

                }
            }.runTaskTimer(plugin, 0, 20);
        }
    }

    @EventHandler
    private void onJoin(final PlayerJoinEvent e) {
        final Player player = e.getPlayer();
        plugin.getOnlinePlayers().add(player);

        GameArena arena = gameManager.getArena();
        if (arena == null)
            return;

        if (player.getWorld().equals(arena.getWorld())) {
            if (gameManager.inProgress()) {
                gameManager.getStatusBar().addPlayer(player);
            }
            else if (gameManager.getPlayerManager().getPlayersInGameWorld().size() >= MIN_PLAYERS_TO_START){
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        gameManager.startCountdown(GameManager.GAME_COUNTDOWN);
                    }
                }.runTaskLater(plugin, 5);
            }
        }
    }

    @EventHandler
    private void onQuit(final PlayerQuitEvent e) {
        final Player player = e.getPlayer();
        plugin.getOnlinePlayers().remove(player);

        //if (gameManager.getCurrentState().equals(GameState.COUNTDOWN)) {
        //
        //}

        VoteManager voteManager = VoteManager.getInstance();
        if (voteManager != null) {
            voteManager.validateVotes();
        }


        playerManager.handlePlayerQuit(player);
    }

    @EventHandler
    private void onWorldChanged(PlayerChangedWorldEvent e) {
        final Player player = e.getPlayer();

        VoteManager voteManager = VoteManager.getInstance();
        if (voteManager != null) {
            voteManager.validateVotes();
        }

        playerManager.handlePlayerQuit(player);
    }


    private static final int ROUND_SURVIVED_MONEY_AMOUNT = 10;

    @EventHandler
    private void onRoundSurvived(RoundSurvivedEvent e) {
        AetherLabsPlugin plugin = AetherLabsPlugin.getInstance();

        if (plugin.vaultHook) {
            Economy economy = plugin.getEconomy();

            economy.depositPlayer(e.getPlayer(), ROUND_SURVIVED_MONEY_AMOUNT);

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
        if (gameManager.getCurrentState() == GameState.SETUP && e instanceof Cancellable cancellable) {
            cancellable.setCancelled(true);
            Common.broadcast("cancelled " + e.getEventName());
            return true;
        }

        return false;
    }

}
