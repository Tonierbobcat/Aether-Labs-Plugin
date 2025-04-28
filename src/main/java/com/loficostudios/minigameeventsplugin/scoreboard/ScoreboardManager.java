package com.loficostudios.minigameeventsplugin.scoreboard;

import com.loficostudios.minigameeventsplugin.AetherLabsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ScoreboardManager {
    private final Map<UUID, FloralScoreboard> activeScoreboards = new HashMap<>();

    public ScoreboardManager() {
         Bukkit.getScheduler().runTaskTimer(AetherLabsPlugin.getInstance(), () -> {
            var entries = new ArrayList<>(activeScoreboards.entrySet());
            for (Map.Entry<UUID, FloralScoreboard> entry : entries) {
                var uuid = entry.getKey();
                var player = Bukkit.getPlayer(uuid);
                var sb = entry.getValue();
                if (player == null || !player.isOnline() || sb == null) {
                    activeScoreboards.remove(uuid);
                    continue;
                }
                if (player.getScoreboard().getObjective("floral_sb:" + sb.getId()) == null) {
                    sb.create(player);
                }
                else {
                    sb.update(player);
                }
            }
        }, 0, 1);
    }

    public void setScoreboard(Player player, FloralScoreboard scoreboard) {
        if (player == null || scoreboard == null)
            return;
        updateScoreboard(player, scoreboard);
    }

    private void updateScoreboard(Player player, @Nullable FloralScoreboard sb) {
        player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
        if (sb != null) {
            activeScoreboards.put(player.getUniqueId(), sb);
            player.setScoreboard(sb.bukkit(player));
        }
    }

    public @Nullable FloralScoreboard removeScoreboard(Player player) {
        if (player == null)
            return null;
        var last = activeScoreboards.remove(player.getUniqueId());
        updateScoreboard(player, null);
        return last;
    }
}
