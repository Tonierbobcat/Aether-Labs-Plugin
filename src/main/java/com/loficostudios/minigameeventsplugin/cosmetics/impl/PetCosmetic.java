package com.loficostudios.minigameeventsplugin.cosmetics.impl;

import com.loficostudios.minigameeventsplugin.cosmetics.Quality;
import com.loficostudios.minigameeventsplugin.cosmetics.UnlockCondition;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.UUID;
import java.util.function.BiConsumer;

public class PetCosmetic extends AbstractCosmetic {
    private final ParticleData<?> particles;
    private final BiConsumer<PetData, Player> onKill;
    private final BiConsumer<PetData, Player> onDeath;

    private final HashMap<UUID, Integer> playerEntityIdentifierMap = new HashMap<>();
    private final HashMap<Integer, Location> entityIdentifierLocationMap = new HashMap<>();

    public PetCosmetic(String id, String name, Material icon, Type type, Quality quality, @Nullable UnlockCondition condition, ParticleData<?> particles, BiConsumer<PetData, Player> onKill, BiConsumer<PetData, Player> onDeath) {
        super(id, name, icon, type, quality, condition);
        this.particles = particles;
        this.onKill = onKill;
        this.onDeath = onDeath;
    }

    public void spawn(Player player, Location location) {
        var id = playerEntityIdentifierMap.get(player.getUniqueId());
        if (id == null) {
        }
    }


    public void move(Player player, Location location) {

    }

    private boolean validate(Player player) {
        return true;
    }

    public record PetData(Location location, PetCosmetic pet) {
    }
}
