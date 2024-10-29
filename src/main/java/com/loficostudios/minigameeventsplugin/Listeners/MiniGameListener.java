package com.loficostudios.minigameeventsplugin.Listeners;

import com.loficostudios.melodyapi.utils.Common;
import com.loficostudios.minigameeventsplugin.Countdown.Countdown;
import com.loficostudios.minigameeventsplugin.Managers.GameManager.GameManager;
import com.loficostudios.minigameeventsplugin.Managers.GameManager.GameState;
import com.loficostudios.minigameeventsplugin.Managers.PlayerManager.PlayerManager;
import com.loficostudios.minigameeventsplugin.RandomEventsPlugin;
import com.loficostudios.minigameeventsplugin.GameArena.SpawnPlatform;
import com.loficostudios.minigameeventsplugin.Profile.Profile;
import com.loficostudios.minigameeventsplugin.BukkitEvents.RoundSurvivedEvent;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import static com.loficostudios.minigameeventsplugin.Managers.GameManager.GameManager.PLAYER_KILL_MONEY_AMOUNT;
import static com.loficostudios.minigameeventsplugin.Utils.DebugUtil.debugError;

public class MiniGameListener implements Listener {

    private final RandomEventsPlugin plugin;
    private final GameManager gameManager;
    private final PlayerManager playerManager;
    public MiniGameListener(GameManager gameManager) {
        this.gameManager = gameManager;
        this.plugin = RandomEventsPlugin.getInstance();
        this.playerManager = gameManager.getPlayerManager();
    }




    @EventHandler
    private void onFlammableBlockPlaced(BlockPlaceEvent e) {
        //Player player = e.getPlayer();
        Block block = e.getBlock();

        int time = 3;




        if (gameManager.inProgress() && block.getType().equals(Material.WHITE_WOOL)) {
            Countdown timer = new Countdown(countdown -> {
                //on tick

                if (countdown == 2) {
                    block.setType(Material.BLACK_WOOL);
                }


            }, () -> {
                block.setType(Material.AIR);
            });


            timer.start(time);
        }

    }

    @EventHandler
    private void onJoin(final PlayerJoinEvent e) {
        final Player player = e.getPlayer();
        plugin.getOnlinePlayers().add(player);

        World arenaWorld = gameManager.getArena().getWorld();

        if (arenaWorld != null) {
            debugError("World is null onJoin");
        }

        if (player.getWorld().equals(arenaWorld)) {
            if (gameManager.inProgress()) {
                gameManager.getStatusBar().addPlayer(player);
            }
            else if (plugin.getOnlinePlayers().size() > 1){
                gameManager.startGameCountdown(GameManager.GAME_COUNTDOWN);
            }
        }
    }

    @EventHandler
    private void onQuit(final PlayerQuitEvent e) {
        final Player player = e.getPlayer();
        plugin.getOnlinePlayers().remove(player);

        if (playerManager.getPlayers().contains(player)) {
            playerManager.handlePlayerQuit(player);
        }
    }

    @EventHandler
    private void onWorldChanged(PlayerChangedWorldEvent e) {
        final Player player = e.getPlayer();

        if (playerManager.getPlayers().contains(player)) {
            playerManager.handlePlayerQuit(player);
        }
    }

    @EventHandler
    private void onDeath(PlayerDeathEvent e) {
        final Player player = e.getEntity();

        if (gameManager.inProgress() && playerManager.getPlayers().contains(player)) {
            e.setKeepInventory(true);
            playerManager.handlePlayerDeath(player);

            final Player killer = player.getKiller();

            if (killer != null) {
                plugin.getProfileManager().getProfile(killer.getUniqueId()).ifPresent(Profile::addKill);

                if (plugin.vaultHook) {
                    plugin.getEconomy().depositPlayer(
                            killer,
                            PLAYER_KILL_MONEY_AMOUNT);
                }
            }
        }
    }

    private static final int ROUND_SURVIVED_MONEY_AMOUNT = 10;

    @EventHandler
    private void onRoundSurvived(RoundSurvivedEvent e) {
        RandomEventsPlugin plugin = RandomEventsPlugin.getInstance();

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
