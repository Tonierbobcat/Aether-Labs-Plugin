package com.loficostudios.minigameeventsplugin.cosmetics;

public class CosmeticInstance {
    private Cosmetic cosmetic;
    private boolean unlocked;

    private long dateBought;

    public CosmeticInstance(Cosmetic cosmetic, boolean unlocked) {
        this.cosmetic = cosmetic;
        this.unlocked = unlocked;
    }

    public Cosmetic getCosmetic() {
        return cosmetic;
    }

    public boolean isUnlocked() {
        return unlocked;
    }

    public long getTimeBought() {
        return dateBought;
    }

    public void setUnlocked(boolean b) {
        unlocked = b;
        if (b) dateBought = System.currentTimeMillis();
    }
}
