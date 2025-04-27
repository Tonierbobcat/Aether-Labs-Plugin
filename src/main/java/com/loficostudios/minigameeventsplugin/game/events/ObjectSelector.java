package com.loficostudios.minigameeventsplugin.game.events;

import java.util.Collection;
import java.util.concurrent.ThreadLocalRandom;

public interface ObjectSelector {
    
    int getMin();
    int getMax();

    static int calculate(Collection<?> objects, int min, int max) throws IllegalArgumentException {
        int size = objects.size();
        int effective = Math.min(size, max);

        if (min > effective)
            throw new IllegalArgumentException(String.format("min is greater than max (%d, %d)", min, effective));

        return ThreadLocalRandom.current().nextInt(min, effective + 1);
    }
}
