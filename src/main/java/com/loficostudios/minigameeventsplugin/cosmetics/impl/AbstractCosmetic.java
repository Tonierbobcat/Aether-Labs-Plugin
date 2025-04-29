package com.loficostudios.minigameeventsplugin.cosmetics.impl;

import com.loficostudios.minigameeventsplugin.cosmetics.Cosmetic;
import com.loficostudios.minigameeventsplugin.cosmetics.Quality;
import com.loficostudios.minigameeventsplugin.cosmetics.UnlockCondition;
import org.bukkit.Material;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractCosmetic implements Cosmetic {
    private final String id;
    private final String name;
    private final Material icon;
    private final Type type;

    private final Quality quality;
    private final @Nullable UnlockCondition condition;

    public AbstractCosmetic(String id, String name, Material icon, Type type, Quality quality, @Nullable UnlockCondition condition) {
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.type = type;
        this.quality = quality;
        this.condition = condition;
    }

    public AbstractCosmetic(String name, Material icon, Type type, Quality quality, @Nullable UnlockCondition condition) {
        this(name.toLowerCase().replace(" ", "_"), name, icon, type, quality, condition);
    }

    @Override
    public String getIdentifier() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public Material getIcon() {
        return icon;
    }

    @Override
    public Quality getQuality() {
        return quality;
    }

    @Override
    public @Nullable UnlockCondition getCondition() {
        return condition;
    }
}
