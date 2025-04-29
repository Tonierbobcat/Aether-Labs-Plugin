package com.loficostudios.minigameeventsplugin.cosmetics;

import java.util.Collection;
import java.util.HashMap;

public class CosmeticRegistry {
    private HashMap<String, Cosmetic> cosmetics = new HashMap<>();

    public CosmeticRegistry(HashMap<String, Cosmetic> cosmetics) {
        this.cosmetics = cosmetics;
    }

    public CosmeticRegistry() {
    }

    public void register(Cosmetic cosmetic) {
        cosmetics.put(cosmetic.getIdentifier(), cosmetic);
    }

    public Collection<Cosmetic> getCosmetics() {
        return cosmetics.values();
    }

    public Cosmetic get(String id) {
        return cosmetics.get(id);
    }

}
