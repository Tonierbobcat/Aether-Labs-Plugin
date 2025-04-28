package com.loficostudios.minigameeventsplugin.listeners;

import com.loficostudios.minigameeventsplugin.AetherLabsPlugin;
import com.loficostudios.minigameeventsplugin.api.bukkit.RoundSurvivedEvent;
import com.loficostudios.minigameeventsplugin.game.Game;
import com.loficostudios.minigameeventsplugin.game.GameIndicator;
import com.loficostudios.minigameeventsplugin.game.GameManager;
import com.loficostudios.minigameeventsplugin.game.GameState;
import com.loficostudios.minigameeventsplugin.game.arena.GameArena;
import com.loficostudios.minigameeventsplugin.game.arena.SpawnPlatform;
import com.loficostudios.minigameeventsplugin.managers.VoteManager;
import com.loficostudios.minigameeventsplugin.utils.Debug;
import com.loficostudios.minigameeventsplugin.utils.Economy;
import org.bukkit.Bukkit;
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
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MiniGameListener implements Listener {
    private static final int ROUND_SURVIVED_MONEY_AMOUNT = 10;

    private final AetherLabsPlugin plugin;

    private final GameManager gameManager;

    public MiniGameListener(AetherLabsPlugin plugin, GameManager gameManager) {
        this.plugin = plugin;
        this.gameManager = gameManager;
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
        var player = e.getPlayer();

        var existing = plugin.getActiveGame(player.getWorld());

        var game = existing != null ? existing : createGame(player.getWorld()).orElseThrow();

        var arena = game.getArena();

        if (arena == null || !inArena(player, arena))
            return;
        if (game.inProgress()) {
            game.getIndicator().update(player, GameIndicator.IndicatorType.STATUS);
            return;
        }
        Bukkit.getScheduler().runTaskLater(plugin, () -> gameManager.startCountdown(game, Game.GAME_COUNTDOWN), 5);
    }

    private Optional<Game> createGame(World world) {
        Debug.log("Creating new game");
        var config = plugin.getArenaManager().getConfig(world);
        if (config == null)
            return Optional.empty();
        var arena = new GameArena(plugin, config);
        if (arena.getWorld().getPlayerCount() < Game.MIN_PLAYERS_TO_START)
            return Optional.empty();
        return Optional.of(new Game(arena));
    }

    private boolean inArena(Player player, GameArena arena) {
        return player.getWorld().getName().equals(arena.getWorld().getName());
    }

    @EventHandler
    private void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        var game = plugin.getActiveGame(player.getWorld());

        VoteManager voteManager = AetherLabsPlugin.inst().getActiveGame(player.getWorld()).getVoting();
        if (voteManager != null) {
            voteManager.validateVotes();
        }

        game.getPlayerManager().handleQuit(player);
    }

    @EventHandler
    private void onWorldChanged(PlayerChangedWorldEvent e) {
        Player player = e.getPlayer();
        var game = plugin.getActiveGame(player.getWorld());

        VoteManager voteManager = AetherLabsPlugin.inst().getActiveGame(player.getWorld()).getVoting();
        if (voteManager != null) {
            voteManager.validateVotes();
        }

        game.getPlayerManager().handleQuit(player);
    }

    @EventHandler
    private void onRoundSurvived(RoundSurvivedEvent e) {
        Economy.deposit(e.getPlayer(), ROUND_SURVIVED_MONEY_AMOUNT);
    }

    @EventHandler
    private void onAttack(EntityDamageEvent e) {
        if ((e.getEntity() instanceof Player player)) {
            var game = plugin.getActiveGame(player.getWorld());
            if (game == null)
                return;
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
        if (game == null)
            return true;
        if (game.getCurrentState() == GameState.SETUP && e instanceof Cancellable cancellable) {
            cancellable.setCancelled(true);
            Bukkit.getLogger().info("cancelled " + e.getEventName());
            return true;
        }

        return false;
    }
}
