package com.loficostudios.minigameeventsplugin.listeners;

import com.loficostudios.minigameeventsplugin.AetherLabsPlugin;
import com.loficostudios.minigameeventsplugin.eggwars.Egg;
import com.loficostudios.minigameeventsplugin.eggwars.EggWarsMode;
import com.loficostudios.minigameeventsplugin.game.Game;
import com.loficostudios.minigameeventsplugin.gamemode.GameModes;
import com.loficostudios.minigameeventsplugin.player.PlayerManager;
import com.loficostudios.minigameeventsplugin.player.profile.Profile;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.scheduler.BukkitRunnable;

import static com.loficostudios.minigameeventsplugin.game.Game.PLAYER_KILL_MONEY_AMOUNT;

public class PlayerDeathListener implements Listener {

    private final Game game;
    private final AetherLabsPlugin plugin;
    private final PlayerManager players;
    public PlayerDeathListener(Game gameManager) {
        this.game = gameManager;
        this.plugin = AetherLabsPlugin.getInstance();
        players = gameManager.getPlayers();
    }


    @EventHandler
    private void onDeath(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player player))
            return;

        var isDeath = player.getHealth() - e.getFinalDamage() <= 0;
        if (!isDeath)
            return;
        if (!game.inProgress() || !players.getPlayersInGame().contains(player))
            return;
        e.setCancelled(true);

        if (e instanceof EntityDamageByEntityEvent event) {
            if (event.getDamager() instanceof Player killer) {
                handleKill(killer);
            } else if (event.getDamager() instanceof Projectile projectile && projectile.getShooter() instanceof Player killer) {
                handleKill(killer);
            }
        }

        if (game.getCurrentMode().equals(GameModes.EGG_WARS)) {
            handleEggWars(player);
        } else {
            players.handlePlayerDeath(player);
            respawn(player, () -> player.teleport(player.getWorld().getSpawnLocation()), getMaxHealth(player), 5);
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                player.playSound(player, Sound.ENTITY_PLAYER_DEATH, 1,1);
            }
        }.runTaskLater(plugin, 1);
    }

    private void handleKill(Player killer) {
        plugin.getProfileManager().getProfile(killer.getUniqueId()).ifPresent(Profile::addKill);

        if (plugin.vaultHook) {
            plugin.getEconomy().depositPlayer(
                    killer,
                    PLAYER_KILL_MONEY_AMOUNT);
        }
    }

    private void respawn(Player player, Runnable onSpawn, double health, long delay) {
        onSpawn.run();
        player.clearActivePotionEffects();
        player.setFireTicks(0);
        player.setHealth(health);
        player.setFoodLevel(20);
    }

    private void handleEggWars(Player player) {
        EggWarsMode mode = (EggWarsMode) game.getCurrentMode();

        Egg egg = mode.getEgg(game, player);

        if (egg == null) {
            players.handlePlayerDeath(player);
        }
        respawn(player, () -> {
            if (egg != null)
                egg.spawn();
            else player.teleport(player.getWorld().getSpawnLocation());
        }, getMaxHealth(player), 5);
    }

    private double getMaxHealth(Player player) {
        var attribute = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);

        double max = 20;
        if (attribute != null) {
            max = attribute.getValue();
        }
        return max;
    }
}
