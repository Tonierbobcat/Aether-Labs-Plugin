package com.loficostudios.minigameeventsplugin.Utils;

import com.loficostudios.minigameeventsplugin.RandomEventsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

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
                    Bukkit.getScheduler().runTask(RandomEventsPlugin.getInstance(), onEnd);
                    this.cancel();
                }
            }
        }.runTaskTimer(RandomEventsPlugin.getInstance(), 0, 20);
    }
}
