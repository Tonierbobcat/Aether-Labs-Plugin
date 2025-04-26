package com.loficostudios.minigameeventsplugin.placeholders;

import com.loficostudios.minigameeventsplugin.AetherLabsPlugin;
import com.loficostudios.minigameeventsplugin.game.Game;
import com.loficostudios.minigameeventsplugin.player.profile.Profile;
import com.loficostudios.minigameeventsplugin.player.profile.ProfileManager;
import com.loficostudios.minigameeventsplugin.utils.PlayerState;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class MiniGamePlaceholder extends PlaceholderExpansion {

    final ProfileManager profileManager;

    final Game gameManager;

    public MiniGamePlaceholder(ProfileManager profileManager) {
        this.profileManager = profileManager;

        AetherLabsPlugin plugin = AetherLabsPlugin.getInstance();

        gameManager = plugin.getActiveGame();
    }

    @Override
    public @NotNull String getIdentifier() {
        return "minigameplugin";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Tonierbobcat";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String params) {
        if (player == null)
            return "";

        if (params.equals("alive")) {
            return String.valueOf(gameManager.getPlayers().getPlayersInGame(PlayerState.ALIVE).size());
        }

        if (params.equals("rounds")) {
            return String.valueOf(gameManager.getRoundManager().getRoundsElapsed());
        }

        Optional<Profile> optionalProfile = profileManager.getProfile(player.getUniqueId());
        if (optionalProfile.isPresent()) {

            Profile profile = optionalProfile.get();

            return switch (params) {
                case "player_wins" -> String.valueOf(profile.getWins());
                case "player_kills" -> String.valueOf(profile.getKills());
                case "player_deaths" -> String.valueOf(profile.getDeaths());
                default -> "";
            };
        }

        return "";
    }
}
