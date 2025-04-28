package com.loficostudios.minigameeventsplugin.utils;

import com.loficostudios.minigameeventsplugin.AetherLabsPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.function.Consumer;

public class Countdown {

    private BukkitTask task;

    private final Consumer<Integer> onTick;
    private final Runnable onEnd;
    boolean cancelled = false;

    public Countdown(Consumer<Integer> onTick, Runnable onEnd) {
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
            }
        }.runTaskTimer(AetherLabsPlugin.inst(), 0, 20);

        return task;
    }
}