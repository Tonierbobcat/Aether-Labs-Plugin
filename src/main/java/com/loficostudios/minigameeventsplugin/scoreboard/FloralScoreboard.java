package com.loficostudios.minigameeventsplugin.scoreboard;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class FloralScoreboard {

    private final String id;

    private final Component title;

    private final Function<@NotNull Player, List<String>> lines;

    public FloralScoreboard(String id, Component title, Function<@Nullable Player, List<String>> lines) {
        this.id = id;
        this.title = title;
        this.lines = lines;
    }

    public void create(Player player) {
        var sb = bukkit(player);
        player.setScoreboard(sb);
    }

    @SuppressWarnings("deprecation")
    public Scoreboard bukkit(Player player) {
        var lines = getLines(player);

        var sb = Bukkit.getScoreboardManager().getNewScoreboard();
        var objective = sb.registerNewObjective("floral_sb:" + id, Criteria.DUMMY, title);

        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        for (int i = 0; i < lines.size(); i++) {
            var line = lines.get(i);

            var team = sb.registerNewTeam("line_" + i);
            team.addEntry(ChatColor.values()[i] + "");
            team.prefix(Component.text(line));

            objective.getScore(ChatColor.values()[i] + "").setScore(lines.size() - i);
        }

        var dummy = sb.registerNewTeam("dummy");
        dummy.prefix(Component.text(""));
        return sb;
    }

    public List<String> getLines(Player player) {
        List<String> lines;
        try {
            lines = this.lines.apply(player);
        } catch (Exception e) {
            e.printStackTrace();
            lines = new ArrayList<>();
        }
        return lines;
    }

    public void update(Player player) {
        var lines = getLines(player);

        var sb = player.getScoreboard();
        var objective = sb.getObjective("floral_sb:" + id);

        if (objective == null)
            return;
        for (int i = 0; i < lines.size(); i++)
            b(sb.getTeam("line_" + i), lines.get(i));
    }

    private void b(Team team, String line) {
        if (team == null)
            return;
        Component parsed;
        try {
            parsed = Component.text(line);
        } catch (Exception ignore) {
            parsed = Component.text(line);
        }

        team.prefix(parsed);
    }


    public String getId() {
        return id;
    }
}
