package com.loficostudios.minigameeventsplugin.cosmetics;

import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public class CosmeticContainer {
    private final HashMap<Cosmetic.Type, Cosmetic> slots = new HashMap<>();

    private final CosmeticProfile profile;

    public CosmeticContainer(CosmeticProfile profile) {
        this.profile = profile;
    }

    /**
     *
     * @return the last cosmetic in that slot
     */
    public @Nullable Cosmetic setSlot(Cosmetic.Type slot, @Nullable Cosmetic cosmetic) {
        if (cosmetic == null) {
            var last = slots.put(slot, null);
            if (last != null) {
                last.unequip(profile);
            }
            return last;
        }
        if (!cosmetic.getType().equals(slot))
            throw new IllegalArgumentException("Cosmetic is not of correct slot type");
        var last = slots.put(slot, cosmetic);
        cosmetic.equip(profile);
        return last;
    }

    public @Nullable Cosmetic getCosmetic(Cosmetic.Type slot) {
        return slots.get(slot);
    }

    public boolean isEmpty(Cosmetic.Type slot) {
        return slots.get(slot) == null;
    }

    public @Nullable <T extends Cosmetic> T getCosmetic(Cosmetic.Type slot, Class<T> clazz) {
        var obj = slots.get(slot);
        try {
            return clazz.cast(obj);
        } catch (ClassCastException ignore) {
            return null;
        }
    }

    public boolean isEquipped(Cosmetic.Type type, Cosmetic cosmetic) {
        var obj = slots.get(type);
        if (obj == null)
            return false;
        return obj.getIdentifier().equals(cosmetic.getIdentifier());
    }
}
