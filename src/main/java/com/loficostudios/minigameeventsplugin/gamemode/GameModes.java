package com.loficostudios.minigameeventsplugin.gamemode;

import com.loficostudios.minigameeventsplugin.eggwars.EggWarsMode;

public class GameModes {
    private static final com.loficostudios.minigameeventsplugin.gamemode.GameMode[] values = new com.loficostudios.minigameeventsplugin.gamemode.GameMode[] {
            new NormalMode(),
            new RandomHeightsMode(),
            new EggWarsMode(),
            new RushMode()
    };

    public static final EggWarsMode EGG_WARS = ((EggWarsMode) values[2]);
    public static final NormalMode NORMAL = ((NormalMode) values[0]);
    public static final RandomHeightsMode DIFFERENT_HEIGHTS = ((RandomHeightsMode) values[1]);
    public static final RushMode RUSH = ((RushMode) values[3]);

    public static GameMode[] values() {
        return values;
    }
}
