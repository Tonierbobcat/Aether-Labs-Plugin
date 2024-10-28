package com.loficostudios.minigameeventsplugin.Interfaces;

import java.util.Collection;
import java.util.Random;

import static com.loficostudios.minigameeventsplugin.Utils.DebugUtil.*;

public interface IPlayerSelector {
    
    Integer getMin();
    Integer getMax();

    default Integer calculateObjects(Collection<?> objects) {
        Random random = new Random();

        int min = getMin();
        int max = objects != null ? Math.min(getMax(), objects.size()) : getMax();

        if (min >= max) {
            debugError("Invalid range: min (" + min + ") must be less than max (" + max + ")");
            return max;
        }

        if (objects == null) {
            debugError("objects param is null");

            return random.nextInt(min, max);
        }
        else {
            int currentObjectCount = objects.size();
            debug("current object count: " + currentObjectCount);

            if (currentObjectCount > 1) {
                return random.nextInt(min, max);
            } else {
                return 1;
            }
        }
    }
}
