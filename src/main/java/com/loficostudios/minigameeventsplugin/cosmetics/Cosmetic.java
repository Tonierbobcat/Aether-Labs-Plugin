package com.loficostudios.minigameeventsplugin.cosmetics;

import org.bukkit.Material;
import org.jetbrains.annotations.Nullable;

public interface Cosmetic {
    String getName();
    Type getType();
    Material getIcon();

    Quality getQuality();

    @Nullable UnlockCondition getCondition();

    String getIdentifier();
    enum Type {
        ARROW_TRAIL
    }
}
