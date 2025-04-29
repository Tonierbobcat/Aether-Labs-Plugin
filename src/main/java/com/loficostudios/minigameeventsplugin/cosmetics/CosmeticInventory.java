package com.loficostudios.minigameeventsplugin.cosmetics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class CosmeticInventory {
    private final HashMap<String, CosmeticInstance> cosmetics = new HashMap<>();

    private final CosmeticRegistry registry;

    public CosmeticInventory(CosmeticRegistry registry) {
        this.registry = registry;

        for (Cosmetic cosmetic : registry.getCosmetics()) {
            cosmetics.put(cosmetic.getIdentifier(), new CosmeticInstance(cosmetic, false));
        }
    }

    public List<CosmeticInstance> getCosmetics() {
        return new ArrayList<>(cosmetics.values());
    }
}
