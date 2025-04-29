package com.loficostudios.minigameeventsplugin.cosmetics.impl;

import com.loficostudios.minigameeventsplugin.cosmetics.Quality;
import com.loficostudios.minigameeventsplugin.cosmetics.UnlockCondition;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class AdvancedArrowTrailCosmetic extends ArrowTrailCosmetic {

    private final Consumer<Entity> arrow;
    private final BiConsumer<Entity, Boolean> onLand;
    public AdvancedArrowTrailCosmetic(String id, String name, Material icon, Quality quality, @Nullable UnlockCondition condition, Consumer<Entity> arrow, BiConsumer<Entity, Boolean> onLand) {
        super(id, name, icon, quality, condition, null);
        this.arrow = arrow;
        this.onLand = onLand;
    }

    public void onLand(Entity arrow, boolean hit) {
        if (onLand != null)
            onLand.accept(arrow, hit);
    }

    @Override
    public void update(Entity arrow) {
        if (arrow != null)
            this.arrow.accept(arrow);
    }


}
