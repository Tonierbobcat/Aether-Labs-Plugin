package com.loficostudios.minigameeventsplugin.cosmetics.impl;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;

public class ParticleData<T> {
    private final Particle particle;
    private final Vector location;
    private final int count;
    private final double offsetX;
    private final double offsetY;
    private final double offsetZ;
    private final double extra;
    private final @Nullable T data;

    public ParticleData(Particle particle, Vector location, int count, double offsetX, double offsetY, double offsetZ, double extra, @Nullable T data) {
        this.particle = particle;
        this.location = location;
        this.count = count;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.offsetZ = offsetZ;
        this.extra = extra;
        this.data = data;
    }

    public Particle getParticle() {
        return particle;
    }

    public Vector getLocation() {
        return location;
    }

    public int getCount() {
        return count;
    }

    public double getOffsetX() {
        return offsetX;
    }

    public double getOffsetY() {
        return offsetY;
    }

    public double getOffsetZ() {
        return offsetZ;
    }

    public double getExtra() {
        return extra;
    }

    public @Nullable T getData() {
        return data;
    }
}
