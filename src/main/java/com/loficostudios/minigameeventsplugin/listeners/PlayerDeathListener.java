package com.loficostudios.minigameeventsplugin.listeners;

import com.loficostudios.minigameeventsplugin.AetherLabsPlugin;
import com.loficostudios.minigameeventsplugin.eggwars.Egg;
import com.loficostudios.minigameeventsplugin.eggwars.EggWarsMode;
import com.loficostudios.minigameeventsplugin.game.GameManager;
import com.loficostudios.minigameeventsplugin.gamemode.GameModes;
import com.loficostudios.minigameeventsplugin.player.profile.PlayerProfile;
import com.loficostudios.minigameeventsplugin.utils.Economy;
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

    private final GameManager gameManager;

    private final AetherLabsPlugin plugin;
    public PlayerDeathListener(GameManager gameManager) {
        this.gameManager = gameManager;
        this.plugin = AetherLabsPlugin.inst();
    }

    @EventHandler
    private void onDeath(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player player))
            return;

        var game = gameManager.getGame(player.getWorld());
        if (game == null)
            return;

        var isDeath = player.getHealth() - e.getFinalDamage() <= 0;
        if (!isDeath)
            return;
        if (!game.inProgress() || !game.getPlayerManager().getPlayersInGame().contains(player))
            return;
        e.setCancelled(true);

        if (e instanceof EntityDamageByEntityEvent event) {
            if (event.getDamager() instanceof Player killer) {
                handleKill(killer);
            } else if (event.getDamager() instanceof Projectile projectile && projectile.getShooter() instanceof Player killer) {
                handleKill(killer);
            }
        }

        if (game.getMode().equals(GameModes.EGG_WARS)) {
            handleEggWars(player);
        } else {
            game.getPlayerManager().handlePlayerDeath(player);
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
        plugin.getProfileManager().getProfile(killer.getUniqueId()).ifPresent(PlayerProfile::addKill);
        Economy.deposit(killer, PLAYER_KILL_MONEY_AMOUNT);
    }

    private void respawn(Player player, Runnable onSpawn, double health, long delay) {
        onSpawn.run();
        player.clearActivePotionEffects();
        player.setFireTicks(0);
        player.setHealth(health);
        player.setFoodLevel(20);
    }

    private void handleEggWars(Player player) {
        var game = gameManager.getGame(player.getWorld());
        if (game == null)
            return;
        EggWarsMode mode = (EggWarsMode) game.getMode();

        Egg egg = mode.getEgg(game, player);

        if (egg == null) {
            game.getPlayerManager().handlePlayerDeath(player);
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
