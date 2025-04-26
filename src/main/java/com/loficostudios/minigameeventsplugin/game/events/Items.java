package com.loficostudios.minigameeventsplugin.game.events;


import com.loficostudios.minigameeventsplugin.utils.ItemCreator;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;

public class Items {
    public static final ItemStack SPOOKY_PUMPKIN = ItemCreator.createItem(Component.text("&6Spooky Pumpkin"), Material.CARVED_PUMPKIN, meta -> {
        meta.addEnchant(Enchantment.BINDING_CURSE, 1, false);
    }, null, null);

    public static final ItemStack BROKEN_SHEARS = ItemCreator.createItem(Component.text("Broken Shears"), Material.SHEARS, (meta) -> {
        if (meta instanceof Damageable damageable) {
            damageable.setDamage(228);
        }
    }, null, null);

    public static final ItemStack ROSE = ItemCreator.createItem(Component.text("Pretty Rose"), Material.POPPY, null, null, null);
    public static final ItemStack FISHERMANS_ROD = ItemCreator.createItem(Component.text("Fisherman's rod"), Material.FISHING_ROD, null, null, null);
}
