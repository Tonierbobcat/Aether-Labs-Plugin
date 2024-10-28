package com.loficostudios.minigameeventsplugin.Utils;

import com.google.common.base.Preconditions;
import com.loficostudios.minigameeventsplugin.MiniGameEventsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;

public class CountdownUtil {
    public static BukkitTask countdownSeconds(int seconds, Consumer<Integer> onTick, Runnable onEnd) {

        return new BukkitRunnable() {
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
    }
}
