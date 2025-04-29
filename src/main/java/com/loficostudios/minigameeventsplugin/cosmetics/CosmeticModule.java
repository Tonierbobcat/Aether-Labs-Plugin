package com.loficostudios.minigameeventsplugin.cosmetics;

import com.loficostudios.minigameeventsplugin.AetherLabsPlugin;
import com.loficostudios.minigameeventsplugin.cosmetics.listener.CosmeticManager;
import com.loficostudios.minigameeventsplugin.utils.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public class CosmeticModule {
    private static boolean initialized;
    private final JavaPlugin plugin;
    private final CosmeticRegistry registry = new CosmeticRegistry();

    public @Nullable CosmeticRegistry getRegistry() {
        return registry;
    }

    public CosmeticModule(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    private void onEnable() {
        CosmeticManager manager = new CosmeticManager(registry, plugin);
        new CosmeticsCommand(manager)
                .register();

        Bukkit.getPluginManager().registerEvents(manager, plugin);

        registerCosmetics();
    }

    private void registerCosmetics() {
        Arrays.asList(
                Cosmetics.HEART_ARROW,
                Cosmetics.MUSIC_ARROW,
                Cosmetics.YUKI_PET
        ).forEach(registry::register);

        plugin.getLogger().info("Registered " + registry.getCosmetics().size() + " cosmetics!");
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
        return Economy.getBalance(player) >= cost;
    }
}
