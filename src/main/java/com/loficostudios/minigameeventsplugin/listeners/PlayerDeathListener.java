package com.loficostudios.minigameeventsplugin.listeners;

import com.loficostudios.minigameeventsplugin.game.Game;
import com.loficostudios.minigameeventsplugin.player.PlayerManager;
import com.loficostudios.minigameeventsplugin.player.profile.Profile;
import com.loficostudios.minigameeventsplugin.AetherLabsPlugin;
import com.loficostudios.minigameeventsplugin.eggwars.Egg;
import com.loficostudios.minigameeventsplugin.eggwars.EggWarsMode;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;

import static com.loficostudios.minigameeventsplugin.game.Game.PLAYER_KILL_MONEY_AMOUNT;

public class PlayerDeathListener implements Listener {

    private final Game gameManager;
    private final AetherLabsPlugin plugin;
    private final PlayerManager playerManager;
    public PlayerDeathListener(Game gameManager) {
        this.gameManager = gameManager;
        this.plugin = AetherLabsPlugin.getInstance();
        playerManager = gameManager.getPlayers();
    }

    @EventHandler
    private void onDeath(PlayerDeathEvent e) {
        final Player player = e.getEntity();

        if (gameManager.inProgress() && playerManager.getPlayersInGame().contains(player)) {

            if (gameManager.getCurrentMode().equals(gameManager.EGG_WARS)) {
                EggWarsMode mode = (EggWarsMode) gameManager.getCurrentMode();
                Bukkit.getLogger().info("current mode is eggwars");
                if (mode.hasSpawn(player)) {
                    Bukkit.getLogger().info("player has spawn");
                    Egg egg = mode.getEgg(player);

                    if (egg != null) {
                        Bukkit.getLogger().info("egg is not null");
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                player.sendMessage("Spawning back...");


                                if (player.isValid()) {
                                    egg.Spawn();
                                }
                            }
                        }.runTaskLater(plugin, 5);
                    }
                    e.setDeathMessage(null);
                }
                else {
                    e.setKeepInventory(true);
                    playerManager.handlePlayerDeath(player);
                }
            }
            else {
                e.setKeepInventory(true);
                playerManager.handlePlayerDeath(player);
            }














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

    //    @EventHandler
//    private void onDeath(EntityDamageEvent e) {
//        if (isCurrentGameOther())
//            return;
//        EggWarsMode mode = (EggWarsMode) gameManager.getCurrentMode();
//
//
//        if (e.getEntity() instanceof Player player) {
//            if (mode.hasSpawn(player)) {
//                Egg egg = mode.getEgg(player);
//
//                if (egg != null) {
//                    player.setHealth(20.0);
//                    egg.Spawn();
//                    e.setCancelled(true);
//                }
//            }
//        }
//    }

}
