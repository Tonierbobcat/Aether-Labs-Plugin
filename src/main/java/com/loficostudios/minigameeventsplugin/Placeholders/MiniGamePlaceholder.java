package com.loficostudios.minigameeventsplugin.Placeholders;

import com.loficostudios.minigameeventsplugin.Managers.GameManager.GameManager;
import com.loficostudios.minigameeventsplugin.Managers.ProfileManager;
import com.loficostudios.minigameeventsplugin.AetherLabsPlugin;
import com.loficostudios.minigameeventsplugin.Profile.Profile;
import com.loficostudios.minigameeventsplugin.Utils.PlayerState;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class MiniGamePlaceholder extends PlaceholderExpansion {

    final ProfileManager profileManager;

    final GameManager gameManager;

    public MiniGamePlaceholder(ProfileManager profileManager) {
        this.profileManager = profileManager;

        AetherLabsPlugin plugin = AetherLabsPlugin.getInstance();

        gameManager = plugin.getGameManager();
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
            return String.valueOf(gameManager.getPlayerManager().getPlayersInGame(PlayerState.ALIVE).size());
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
