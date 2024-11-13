package com.loficostudios.minigameeventsplugin.api;

import com.loficostudios.minigameeventsplugin.interfaces.IInitializationReceiver;
import org.bukkit.Material;

public abstract class BaseGameMode implements IInitializationReceiver {


    public static final int DEFAULT_FILL_SPEED = 5;
    public static final Material DEFAULT_FILL_MATERIAL = Material.LAVA;


    public abstract String getName();

    public abstract Material getIcon();

    public String getId() {
        return getName().toLowerCase().replace(" ", "_");
    }

    public abstract void start();
    public abstract void end();
    public abstract void reset();

    public Material getFillMaterial() {
        return DEFAULT_FILL_MATERIAL;
    }

    public int getFillSpeed() {
        return DEFAULT_FILL_SPEED;
    }

}
