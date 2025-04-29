package com.loficostudios.minigameeventsplugin.cosmetics.impl;

import com.loficostudios.minigameeventsplugin.cosmetics.Quality;
import com.loficostudios.minigameeventsplugin.cosmetics.UnlockCondition;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.Nullable;

public class ArrowTrailCosmetic extends AbstractCosmetic {
    private final ParticleData<?> data;
    public ArrowTrailCosmetic(String name, Material icon, Quality quality, @Nullable UnlockCondition condition, ParticleData<?> particle) {
        super(name, icon, Type.ARROW_TRAIL, quality, condition);
        this.data = particle;
    }
    public ArrowTrailCosmetic(String id, String name, Material icon, Quality quality, @Nullable UnlockCondition condition, ParticleData<?> particle) {
        super(id, name, icon, Type.ARROW_TRAIL, quality, condition);
        this.data = particle;
    }

    public void update(Entity arrow) {
        arrow.getWorld().spawnParticle(data.getParticle(), arrow.getLocation().clone().add(data.getLocation()), data.getCount(), data.getOffsetX(), data.getOffsetY(), data.getOffsetZ(), data.getExtra(), data.getData());
    }
}
