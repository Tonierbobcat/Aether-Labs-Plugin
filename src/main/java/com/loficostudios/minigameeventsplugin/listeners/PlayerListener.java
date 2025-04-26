package com.loficostudios.minigameeventsplugin.listeners;

import com.loficostudios.minigameeventsplugin.AetherLabsPlugin;
import com.loficostudios.minigameeventsplugin.player.profile.ProfileAlreadyLoadedException;
import com.loficostudios.minigameeventsplugin.player.profile.ProfileManager;
import com.loficostudios.minigameeventsplugin.player.profile.ProfileNotLoadedException;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import static com.loficostudios.minigameeventsplugin.utils.Debug.logWarning;

public class PlayerListener implements Listener {

    final AetherLabsPlugin plugin;
    final ProfileManager profileManager;

    public PlayerListener(AetherLabsPlugin plugin, ProfileManager profileManager) {
        this.profileManager = profileManager;
        this.plugin = plugin;
    }

    @EventHandler
    private void onJoin(final PlayerJoinEvent e) {
        final Player player = e.getPlayer();
        plugin.getOnlinePlayers().add(player);

        try {
            profileManager.loadProfile(player.getUniqueId());
        } catch (ProfileAlreadyLoadedException exception) {
            logWarning("Profile for " + player.getName() + "is already loaded");
        }
    }

    @EventHandler
    private void onQuit(final PlayerQuitEvent e) {
        final Player player = e.getPlayer();
        plugin.getOnlinePlayers().remove(player);

        try {
            profileManager.unloadProfile(player.getUniqueId());
        } catch (ProfileNotLoadedException exception) {
            logWarning("Player " + player.getName() + " tried to unload his profile but it was not loaded.");
        }
    }
}
