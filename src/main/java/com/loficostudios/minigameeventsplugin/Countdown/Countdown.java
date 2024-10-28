package com.loficostudios.minigameeventsplugin.Countdown;

import com.loficostudios.minigameeventsplugin.Managers.GameManager;
import com.loficostudios.minigameeventsplugin.MiniGameEventsPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.function.Consumer;

import static com.loficostudios.minigameeventsplugin.Utils.DebugUtil.*;

/*public class CountdownTimer {

    private BukkitTask task;

    @Setter
    Runnable onCancel;

    private long startTime;

    //private Long endTime;

    public long start(int seconds, Consumer<Integer> onTick, Runnable onEnd) {
        if (task != null) {
            task.cancel();
            task = null;
        }

        startTime = System.currentTimeMillis();

        task = new BukkitRunnable() {
            int countdown = seconds;

            @Override
            public void run() {
                if (countdown > 0) {
                    onTick.accept(countdown);
                    countdown--;
                }
                else {
                    // I have it like this so that it does not run the onEnd tasks async.
                    Bukkit.getScheduler().runTask(MiniGameEventsPlugin.getInstance(), onEnd);
                    this.cancel();
                }
            }
        }.runTaskTimer(MiniGameEventsPlugin.getInstance(), 0, 20);

        return startTime;
    }

    public long stop() {

        onCancel.run();

        if (task != null) {
            task.cancel();
            task = null;
        }

        return System.currentTimeMillis() - startTime;
    }
}*/
public class Countdown {

    private final GameManager gameManager;

    private BukkitTask task;

    private final Consumer<Integer> onTick;
    private final Runnable onEnd;

    public Countdown(Consumer<Integer> onTick, Runnable onEnd) {

        this.onTick = onTick;
        this.onEnd = onEnd;

        gameManager = MiniGameEventsPlugin.getInstance().getGameManager();
    }

    public BukkitTask start(int time) {
        if (task != null) {
            task.cancel();
            task = null;
        }
        debug("timer started");
        return task = new BukkitRunnable() {
            int countdown = time;

            @Override
            public void run() {
                if (countdown > 0) {
                    if (onTick != null) {
                        onTick.accept(countdown);
                    }

                    countdown--;
                } else {
                    if (onEnd != null) {
                        onEnd.run();
                    }
                    this.cancel();
                }
            }
        }.runTaskTimer(MiniGameEventsPlugin.getInstance(), 0, 20);
    }

    public void stop() {
        if (task != null) {
            task.cancel();
            task = null;
        }
        debug("timer stopped");
    }
}