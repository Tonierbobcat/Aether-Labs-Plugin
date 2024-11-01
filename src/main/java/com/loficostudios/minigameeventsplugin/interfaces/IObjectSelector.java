package com.loficostudios.minigameeventsplugin.interfaces;

import com.loficostudios.minigameeventsplugin.AetherLabsPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

import static com.loficostudios.minigameeventsplugin.Utils.Debug.*;

public interface IObjectSelector<Impl> {
    
    Integer getMin();
    Integer getMax();

    default Integer calculateObjects(Collection<Impl> objects) {
        Random random = new Random();

        int min = getMin();
        int max = objects != null ? Math.min(getMax(), objects.size()) : getMax();

        if (min >= max) {
            logError("Invalid range: min (" + min + ") must be less than max (" + max + ")");
            return max;
        }

        if (objects == null) {
            logError("objects param is null");

            return random.nextInt(min, max);
        }
        else {
            int currentObjectCount = objects.size();
            log("current object count: " + currentObjectCount);

            if (currentObjectCount > 1) {
                return random.nextInt(min, max);
            } else {
                return 1;
            }
        }
    }
}
