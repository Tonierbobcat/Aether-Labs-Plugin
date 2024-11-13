package com.loficostudios.minigameeventsplugin.arena;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;

public class Hologram {


    private final ArmorStand hologramEntity;

    public Hologram(String name, Location location) {


        this.hologramEntity = (ArmorStand) location.getWorld().spawnEntity(
                location,
                EntityType.ARMOR_STAND);
        hologramEntity.setVisible(false);
        hologramEntity.setSmall(true);
        hologramEntity.setArms(false);
        hologramEntity.setVisualFire(false);
        hologramEntity.setGravity(false);
        hologramEntity.setCustomNameVisible(true);

        hologramEntity.setCustomName(name);

    }


    public void remove() {
        hologramEntity.remove();
    }

}
