package com.loficostudios.minigameeventsplugin.GameEvents;


import com.loficostudios.minigameeventsplugin.Managers.*;
import com.loficostudios.minigameeventsplugin.MiniGameEventsPlugin;
import com.loficostudios.minigameeventsplugin.GameArena.GameArena;
import lombok.Getter;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;

import static com.loficostudios.minigameeventsplugin.Utils.DebugUtil.debugError;

public abstract class BaseEvent {

    public static final int DEFAULT_WARNING_TIME = 3;
    public static final int DEFAULT_EVENT_DURATION = 3;

    private GameManager gameManager;
    private PlayerManager playerManager;

    @Getter
    protected final Collection<BukkitTask> tasks = new ArrayList<>();

    public abstract @NotNull String getName();
    public abstract @NotNull String warningMessage();


    public void load() {
    }

    public void start() {
    }

    public void end() {
    }

    public void run() {
    }

    public void cancel() {
        tasks.forEach(BukkitTask::cancel);
        tasks.clear();
    }

    public @NotNull Integer getWarningTime() {
        return DEFAULT_WARNING_TIME;
    }

    public @NotNull Integer getDuration() {
        return DEFAULT_EVENT_DURATION;
    }

    public String getId() {
        return getName().toLowerCase().replace(" ", "_");
    }

    protected GameArena getArena() {
        return gameManager.getArena();
    }

    protected GameManager getGameManager() {
        return this.gameManager;
    }

    protected PlayerManager getPlayerManager() {
        return this.playerManager;
    }

    public void register() {
        MiniGameEventsPlugin plugin = MiniGameEventsPlugin.getInstance();
        gameManager = plugin.getGameManager();

        if (gameManager != null) {
            playerManager = gameManager.getPlayerManager();
        } else {
            debugError("gameManager is null in register baseEvent ");
        }

        plugin.getEventManager()
                .subscribe(this);
    }
}

