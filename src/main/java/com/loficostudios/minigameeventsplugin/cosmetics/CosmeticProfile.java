package com.loficostudios.minigameeventsplugin.cosmetics;

import org.bukkit.entity.Player;

import java.util.UUID;

public class CosmeticProfile {
    private final Player player;
    private final UUID uuid;
    private final CosmeticContainer container;
    private final CosmeticInventory inventory;

    public CosmeticProfile(Player player, CosmeticRegistry registry) {
        this.player = player;
        this.uuid = UUID.randomUUID();
        this.container = new CosmeticContainer();
        this.inventory = new CosmeticInventory(registry);
    }

    public CosmeticContainer getContainer() {
        return container;
    }

    public CosmeticInventory getInventory() {
        return inventory;
    }

    public Player getPlayer() {
        return player;
    }

    public UUID getUID() {
        return uuid;
    }
}
