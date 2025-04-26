package com.loficostudios.minigameeventsplugin.utils;

import com.loficostudios.minigameeventsplugin.AetherLabsPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.function.Consumer;

import static com.loficostudios.minigameeventsplugin.utils.Debug.log;

public class Countdown {

    private BukkitTask task;

    private final Consumer<Integer> onTick;
    private final Runnable onEnd;
    boolean cancelled = false;
    private final String id;

    public Countdown(String id, Consumer<Integer> onTick, Runnable onEnd) {
        this.id = id;
        this.onTick = onTick;
        this.onEnd = onEnd;
    }

    public boolean cancel() {

        if (task != null) {
            task.cancel();
            task = null;
            return true;
        }

        return false;
    }

    public BukkitTask start(int time) {
        cancelled = false;
        log(id + " timer started");
        task = new BukkitRunnable() {
            int countdown = time;

            @Override
            public void run() {
                if (!cancelled) {
                    if (countdown > 0) {
                        if (onTick != null) {
                            onTick.accept(countdown);
                        }

                        countdown--;
                    } else {
                        this.cancel();

                        if (onEnd != null) {
                            onEnd.run();
                        }
                    }
                }
            }

            @Override
            public synchronized void cancel() throws IllegalStateException {
                super.cancel();
                cancelled = true;
                log(id + " timer cancelled");
            }
        }.runTaskTimer(AetherLabsPlugin.getInstance(), 0, 20);

        return task;
    }
}