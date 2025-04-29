package com.loficostudios.minigameeventsplugin.listeners;

import com.loficostudios.melodyapi.gui.events.GuiCloseEvent;
import com.loficostudios.minigameeventsplugin.AetherLabsPlugin;
import com.loficostudios.minigameeventsplugin.game.player.PlayerState;
import com.loficostudios.minigameeventsplugin.gui.VoteGui;
import com.loficostudios.minigameeventsplugin.player.profile.ProfileAlreadyLoadedException;
import com.loficostudios.minigameeventsplugin.player.profile.ProfileManager;
import com.loficostudios.minigameeventsplugin.player.profile.ProfileNotLoadedException;
import com.loficostudios.minigameeventsplugin.scoreboard.FloralScoreboard;
import com.loficostudios.minigameeventsplugin.scoreboard.ScoreboardManager;
import com.loficostudios.minigameeventsplugin.utils.Economy;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.List;

import static com.loficostudios.minigameeventsplugin.utils.Debug.logWarning;

public class PlayerListener implements Listener {

    final AetherLabsPlugin plugin;
    final ProfileManager profileManager;
    private final ScoreboardManager scoreboards;
    private final FloralScoreboard scoreboard;

    public PlayerListener(AetherLabsPlugin plugin, ProfileManager profileManager) {
        this.profileManager = profileManager;
        this.plugin = plugin;

        this.scoreboard = new FloralScoreboard("game", Component.text("§b§LAether Labs"), player -> {
            var game = plugin.getActiveGame(player.getWorld());
            var opt = plugin.getProfileManager().getProfile(player.getUniqueId());
            if (opt.isEmpty())
                return new ArrayList<>(8);
            var profile = opt.get();

            if (game != null && game.inProgress()) {
                return List.of(
                        "Money: " + Economy.getBalance(player),
                        " ",
                        "Alive: " + game.getPlayerManager().getPlayersInGame(PlayerState.ALIVE).size(),
                        "Rounds: " + game.getRounds().getRoundsElapsed().size(),
                        " ",
                        "Wins: " + profile.getWins(),
                        "Kills: " + profile.getKills(),
                        "Deaths: " + profile.getDeaths()
                );
            } else {
                return List.of(
                        "Money: " + Economy.getBalance(player),
                        " ",
                        "Wins: " + profile.getWins(),
                        "Kills: " + profile.getKills(),
                        "Deaths: " + profile.getDeaths()
                );
            }
        });
        this.scoreboards = new ScoreboardManager();
    }

    @EventHandler
    private void onJoin(final PlayerJoinEvent e) {
        final Player player = e.getPlayer();

        try {
            profileManager.loadProfile(player.getUniqueId());
            scoreboards.setScoreboard(player, scoreboard);
        } catch (ProfileAlreadyLoadedException exception) {
            logWarning("Profile for " + player.getName() + "is already loaded");
        }
    }

    @EventHandler
    private void onClose(GuiCloseEvent e) {
        if (e.getGui() instanceof VoteGui gui) {
            VoteGui.instances.remove(gui);
        }
    }

    @EventHandler
    private void onQuit(final PlayerQuitEvent e) {
        final Player player = e.getPlayer();

        try {
            profileManager.unloadProfile(player.getUniqueId());
        } catch (ProfileNotLoadedException exception) {
            logWarning("Player " + player.getName() + " tried to unload his profile but it was not loaded.");
        }
    }
}
