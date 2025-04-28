package com.loficostudios.minigameeventsplugin.game;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

public class GameIndicator {
    private final BossBar statusBar;

    private final BossBar progressBar;

    private final Game game;

    public GameIndicator(Game game) {
        this.game = game;
        this.progressBar = Bukkit.createBossBar("", BarColor.WHITE, BarStyle.SOLID);
        this.statusBar = Bukkit.createBossBar("", BarColor.YELLOW, BarStyle.SOLID);
    }

    public void show(IndicatorType type) {
        var players = game.getArena().getWorld().getPlayers();
        switch (type) {
            case STATUS -> players.forEach(statusBar::addPlayer);
            case PROGRESS -> players.forEach(progressBar::addPlayer);
        }
    }

    public void hide(IndicatorType type) {
        switch (type) {
            case PROGRESS -> progressBar.removeAll();
            case STATUS -> statusBar.removeAll();
        }
    }

    public void status(String s) {
        statusBar.setTitle(s);
    }

    public void status(String s, BarColor color) {
        statusBar.setColor(color);
        statusBar.setTitle(s);
    }

    public void progress(String s) {
        progressBar.setTitle(s);
    }

    public void progress(String s, BarColor color) {
        progressBar.setColor(color);
        progressBar.setTitle(s);
    }

    public void update(Player player, IndicatorType indicatorType) {
        switch (indicatorType) {
            case STATUS -> statusBar.addPlayer(player);
            case PROGRESS -> progressBar.addPlayer(player);
        }
    }

    public enum IndicatorType {
        PROGRESS,
        STATUS
    }

    public void reset() {
        statusBar.setColor(BarColor.YELLOW);
        statusBar.removeAll();
        progressBar.removeAll();
    }
}
