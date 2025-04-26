package com.loficostudios.minigameeventsplugin.game.events;

import java.util.Collection;
import java.util.Random;

import static com.loficostudios.minigameeventsplugin.utils.Debug.*;

public interface IObjectSelector<Impl> {
    
    int getMin();
    int getMax();

    default int calculateObjects(Collection<Impl> objects) {
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
