package com.loficostudios.minigameeventsplugin.listeners;

import com.loficostudios.minigameeventsplugin.AetherLabsPlugin;
import com.loficostudios.minigameeventsplugin.api.bukkit.RoundSurvivedEvent;
import com.loficostudios.minigameeventsplugin.arena.GameArena;
import com.loficostudios.minigameeventsplugin.arena.SpawnPlatform;
import com.loficostudios.minigameeventsplugin.game.Game;
import com.loficostudios.minigameeventsplugin.game.GameState;
import com.loficostudios.minigameeventsplugin.managers.VoteManager;
import com.loficostudios.minigameeventsplugin.utils.Debug;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
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
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

import static com.loficostudios.minigameeventsplugin.game.Game.MIN_PLAYERS_TO_START;

public class MiniGameListener implements Listener {
    private static final int ROUND_SURVIVED_MONEY_AMOUNT = 10;

    private final AetherLabsPlugin plugin;

    public MiniGameListener(AetherLabsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    private void onFlammableBlockPlaced(BlockPlaceEvent e) {
        Block block = e.getBlock();

        int time = 20;

        var game = plugin.getActiveGame(e.getPlayer().getWorld());

        if (game.inProgress() && block.getType().equals(Material.WHITE_WOOL)) {

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
        var game = plugin.getActiveGame(player.getWorld());

        GameArena arena = game.getArena();
        if (arena == null)
            return;

        if (player.getWorld().equals(arena.getWorld())) {
            if (game.inProgress()) {
                game.getStatusBar().addPlayer(player);
            }
            else if (game.getPlayers().getPlayersInGameWorld().size() >= MIN_PLAYERS_TO_START){
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        game.startCountdown(Game.GAME_COUNTDOWN);
                    }
                }.runTaskLater(plugin, 5);
            }
        }
    }

    @EventHandler
    private void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        plugin.getOnlinePlayers().remove(player);
        var game = plugin.getActiveGame(player.getWorld());

        VoteManager voteManager = AetherLabsPlugin.getInstance().getActiveGame(player.getWorld()).getVoting();
        if (voteManager != null) {
            voteManager.validateVotes();
        }

        game.getPlayers().handleQuit(player);
    }

    @EventHandler
    private void onWorldChanged(PlayerChangedWorldEvent e) {
        Player player = e.getPlayer();
        var game = plugin.getActiveGame(player.getWorld());

        VoteManager voteManager = AetherLabsPlugin.getInstance().getActiveGame(player.getWorld()).getVoting();
        if (voteManager != null) {
            voteManager.validateVotes();
        }

        game.getPlayers().handleQuit(player);
    }

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
            var game = plugin.getActiveGame(player.getWorld());

            var arena = game.getArena();
            if (arena == null)
                return;
            if (player.getWorld() != arena.getWorld())
                return;

            handleEventDuringSetupGame(e, game);
        }
    }

    @EventHandler
    private void onMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();

        if (e.isCancelled())
            return;
        if (e.getFrom().distance(e.getTo()) < 0.01)
            return;
        var game = plugin.getActiveGame(player.getWorld());

        handleEventDuringSetupGame(e, game);
    }

    @EventHandler
    private void onBlockBreak(BlockBreakEvent e) {
        Player player = e.getPlayer();
        var game = plugin.getActiveGame(player.getWorld());
        if (handleEventDuringSetupGame(e, game))
            return;

        Block block = e.getBlock();

        if (game.inProgress()) {
            SpawnPlatform spawnPlatform =  game.getArena().getSpawnPlatform(player);
            if (spawnPlatform != null) {
                spawnPlatform.handleBlockBreak(block);
            }
        }
    }

    private boolean handleEventDuringSetupGame(Event e, Game game) {
        if (game.getCurrentState() == GameState.SETUP && e instanceof Cancellable cancellable) {
            cancellable.setCancelled(true);
            Bukkit.getLogger().info("cancelled " + e.getEventName());
            return true;
        }

        return false;
    }
}
