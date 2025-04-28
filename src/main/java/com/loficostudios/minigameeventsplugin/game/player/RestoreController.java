package com.loficostudios.minigameeventsplugin.game.player;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.*;
import java.util.function.Consumer;

import static com.loficostudios.minigameeventsplugin.utils.Debug.log;

public class RestoreController {
    private final Map<UUID, ItemStack[]> savedPlayerInventory = new HashMap<>();
    private final Map<UUID, ItemStack[]> savedPlayerEquipment = new HashMap<>();

    private final Map<UUID, List<PotionEffect>> savedPotionEffects = new HashMap<>();

    private final Map<UUID, GameMode> savedGameModes = new HashMap<>();

    private final Map<UUID, Double> savedHealth = new HashMap<>();

    private final Consumer<Player> onSave;

    public RestoreController() {
        this(player -> {
            player.getEquipment().clear();
            player.getInventory().clear();
            player.clearActivePotionEffects();

            player.setGameMode(GameMode.SURVIVAL);
        });
    }

    public RestoreController(Consumer<Player> onSave) {
        this.onSave = onSave;
    }

    public void save(final Player player) {
        var uuid = player.getUniqueId();

        var equipment = player.getEquipment();

        savedHealth.put(uuid, player.getHealth());

        savedPlayerInventory.put(uuid, player.getInventory().getContents().clone());
        savedPlayerEquipment.put(uuid, equipment.getArmorContents().clone());
        savedPotionEffects.put(uuid, new ArrayList<>(player.getActivePotionEffects()));
        savedGameModes.put(uuid, player.getGameMode());

        if (onSave != null)
            onSave.accept(player);
    }

    public void restore(final Player player) {
        var uuid = player.getUniqueId();

        var health = savedHealth.remove(uuid);
        if (health != null)
            player.setHealth(health);

        var contents = savedPlayerInventory.remove(uuid);
        if (contents != null)
            player.getInventory().setContents(contents);

        var equipment = savedPlayerEquipment.remove(uuid);
        if (equipment != null)
            player.getEquipment().setArmorContents(equipment);

        var effects = savedPotionEffects.remove(uuid);
        if (effects != null)
            player.addPotionEffects(effects);

        var gm = savedGameModes.remove(uuid);
        if (gm != null)
            player.setGameMode(gm);

        log("Restored player " + player.getName());
    }
}
