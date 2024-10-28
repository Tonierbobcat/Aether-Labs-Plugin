package com.loficostudios.minigameeventsplugin.Listeners;

import com.loficostudios.minigameeventsplugin.MiniGameEventsPlugin;
import com.loficostudios.minigameeventsplugin.Profile.ProfileAlreadyLoadedException;
import com.loficostudios.minigameeventsplugin.Managers.ProfileManager;
import com.loficostudios.minigameeventsplugin.Profile.ProfileNotLoadedException;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import static com.loficostudios.minigameeventsplugin.Utils.DebugUtil.debugWarning;

public class PlayerListener implements Listener {

    final MiniGameEventsPlugin plugin;
    final ProfileManager profileManager;

    public PlayerListener(ProfileManager profileManager) {
        this.profileManager = profileManager;
        this.plugin = MiniGameEventsPlugin.getInstance();
    }

    @EventHandler
    private void onJoin(final PlayerJoinEvent e) {
        final Player player = e.getPlayer();
        plugin.getOnlinePlayers().add(player);

        try {
            profileManager.loadProfile(player.getUniqueId());
        } catch (ProfileAlreadyLoadedException exception) {
            debugWarning("Profile for " + player.getName() + "is already loaded");
        }
    }

    @EventHandler
    private void onQuit(final PlayerQuitEvent e) {
        final Player player = e.getPlayer();
        plugin.getOnlinePlayers().remove(player);

        try {
            profileManager.unloadProfile(player.getUniqueId());
        } catch (ProfileNotLoadedException exception) {
            debugWarning("Player " + player.getName() + " tried to unload his profile but it was not loaded.");
        }
    }
}
