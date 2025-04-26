package com.loficostudios.minigameeventsplugin.api.event.impl;


import com.loficostudios.minigameeventsplugin.AetherLabsPlugin;
import com.loficostudios.minigameeventsplugin.api.Warning;
import com.loficostudios.minigameeventsplugin.api.event.EventType;
import com.loficostudios.minigameeventsplugin.api.event.GameEvent;
import com.loficostudios.minigameeventsplugin.game.Game;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;

public abstract class AbstractGameEvent implements GameEvent {

    public static final int DEFAULT_WARNING_TIME = 3;
    public static final int DEFAULT_EVENT_DURATION = 3;

    @Getter
    protected final Collection<BukkitTask> tasks = new ArrayList<>();

    private AetherLabsPlugin plugin;
    private final String id;
    private final String name;
    private final EventType type;
    private final Material icon;

    private final double cost;

    protected AbstractGameEvent(String id, String name, EventType type, Material icon, double cost) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.icon = icon;
        this.cost = cost;
    }

    protected AbstractGameEvent(String id, String name, EventType type, Material icon) {
        this(id, name, type, icon, 100);
    }

    protected AbstractGameEvent(String name, EventType type, Material icon) {
        this(name.toLowerCase().replace(" ", "_"), name, type, icon, 100);
    }

    @Override
    public double getCost() {
        return cost;
    }

    @Override
    public @NotNull String getName() {
       return name;
    }

    @Override
    public @NotNull String getId() {
        return id;
    }

    public @NotNull Material getIcon() {
        return icon;
    }

    public abstract @NotNull String getWarningMessage();

    protected Integer getWarningTime() {
        return DEFAULT_WARNING_TIME;
    }

    @Override
    public @NotNull Warning getWarning() {
        return new Warning(getWarningMessage(), getWarningTime());
    }

    @Override
    public @NotNull EventType getType() {
        return type;
    }

    @Override
    public int getDuration() {
        return DEFAULT_EVENT_DURATION;
    }

    @Override
    public void load(Game game) {
    }
    @Override
    public void start(Game game) {
    }
    @Override
    public void end(Game game) {
    }
    @Override
    public void run(Game game) {
    }

    @Override
    public void cancel(Game game) {
        tasks.forEach(BukkitTask::cancel);
        tasks.clear();
    }

    @Override
    public void register() {
        plugin = AetherLabsPlugin.getInstance();
        plugin.getEvents().subscribe(this);
    }
}

