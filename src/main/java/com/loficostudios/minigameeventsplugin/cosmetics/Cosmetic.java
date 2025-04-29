package com.loficostudios.minigameeventsplugin.cosmetics;

import org.bukkit.Material;
import org.jetbrains.annotations.Nullable;

public interface Cosmetic {
    String getName();
    Type getType();
    Material getIcon();

    Quality getQuality();

    @Nullable UnlockCondition getCondition();

    void equip(CosmeticProfile profile);
    void unequip(CosmeticProfile profile);

    String getIdentifier();
    enum Type {
        PET,
        ARROW_TRAIL
    }
}
