package com.loficostudios.minigameeventsplugin.cosmetics;

import com.loficostudios.minigameeventsplugin.cosmetics.impl.AdvancedArrowTrailCosmetic;
import com.loficostudios.minigameeventsplugin.cosmetics.impl.ArrowTrailCosmetic;
import com.loficostudios.minigameeventsplugin.cosmetics.impl.ParticleData;
import com.loficostudios.minigameeventsplugin.cosmetics.impl.pet.PetCosmetic;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

import java.util.concurrent.ThreadLocalRandom;

public class Cosmetics {
    public static final AdvancedArrowTrailCosmetic MUSIC_ARROW = new AdvancedArrowTrailCosmetic("music_arrow_trail", "Music Arrow Trail", Material.NOTE_BLOCK, Quality.RARE, null, (arrow) -> {
        arrow.getWorld().spawnParticle(Particle.NOTE, arrow.getLocation(), 1);
    }, Cosmetics::playRandomNote);

    public static final PetCosmetic YUKI_PET = new PetCosmetic("yuki_pet", "Yuki", Material.PUMPKIN_PIE, Quality.EPIC, null,
            new ParticleData<>(Particle.END_ROD, new Vector(0,0,0), 1, 0,0,0,0,null), (data, victim) -> {}, (data) -> {}, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDM5ZjMzNjY3Y2U3YjI4M2I4ZmI5NmQ2MWIxZTlhYmQ0YjNiZjA3NjM2MGFmZTU5NDJhNzhjNmZiNTg4NDgxIn19fQ==");

    public static final ArrowTrailCosmetic HEART_ARROW = new ArrowTrailCosmetic("heart_arrow_trail", "Heart Arrow Trail", Material.CAKE, Quality.COMMON, null,
            new ParticleData<>(Particle.HEART, new Vector(0,0,0), 1, 0,0,0,0,null));

    private static void playRandomNote(Entity arrow, boolean hit) {
        var notes = new float[] { 1, 1.259921f, 1.498307f, 2 };
        arrow.getWorld().playSound(
                arrow.getLocation(),
                Sound.BLOCK_NOTE_BLOCK_PLING,
                1,
                notes[ThreadLocalRandom.current().nextInt(notes.length)]
        );
    }
}
