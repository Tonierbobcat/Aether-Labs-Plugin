package com.loficostudios.minigameeventsplugin.cosmetics;

import com.loficostudios.minigameeventsplugin.AetherLabsPlugin;
import com.loficostudios.minigameeventsplugin.cosmetics.impl.AdvancedArrowTrailCosmetic;
import com.loficostudios.minigameeventsplugin.cosmetics.impl.ArrowTrailCosmetic;
import com.loficostudios.minigameeventsplugin.cosmetics.impl.ParticleData;
import com.loficostudios.minigameeventsplugin.cosmetics.listener.CosmeticManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.ThreadLocalRandom;

public class CosmeticModule {
    private static boolean initialized;
    private final JavaPlugin plugin;
    private final CosmeticRegistry cosmeticRegistry = new CosmeticRegistry();

    public @Nullable CosmeticRegistry getCosmeticRegistry() {
        return cosmeticRegistry;
    }

    public CosmeticModule(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    private void onEnable() {
        CosmeticManager manager = new CosmeticManager(cosmeticRegistry, plugin);
        new CosmeticsCommand(manager)
                .register();

        Bukkit.getPluginManager().registerEvents(manager, plugin);

        registerCosmetics();
    }

    private void registerCosmetics() {
        cosmeticRegistry.register(new ArrowTrailCosmetic("heart_arrow_trail", "Heart Arrow Trail", Material.CAKE, Quality.COMMON, null,
                new ParticleData<>(Particle.HEART, new Vector(0,0,0), 1, 0,0,0,0,null)));
        cosmeticRegistry.register(new AdvancedArrowTrailCosmetic("music_arrow_trail", "Music Arrow Trail", Material.NOTE_BLOCK, Quality.RARE, null, (arrow) -> {
            arrow.getWorld().spawnParticle(Particle.NOTE, arrow.getLocation(), 1);
        }, (arrow, hit) -> {
            var notes = new float[] {
                    1, 1.259921f, 1.498307f, 2
            };
            arrow.getWorld().playSound(arrow.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1,notes[ThreadLocalRandom.current().nextInt(notes.length)]);
        }));
        plugin.getLogger().info("Registered " + cosmeticRegistry.getCosmetics().size() + " cosmetics!");
    }

    public static CosmeticModule onEnable(JavaPlugin plugin) {
        if (initialized)
            throw new IllegalArgumentException("Cosmetic Module is already enabled");
        plugin.getLogger().info("Enabled Cosmetics module!");
        var module = new CosmeticModule(plugin);
        module.onEnable();
        initialized = true;
        return module;
    }

    private boolean canBuy(Player player, double cost) {
        return AetherLabsPlugin.inst().getProfileManager().getProfile(player.getUniqueId()).filter(profile -> profile.getMoney() >= 300).isPresent();
    }
}
