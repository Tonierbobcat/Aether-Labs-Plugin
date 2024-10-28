package com.loficostudios.minigameeventsplugin;

import com.loficostudios.melodyapi.MelodyItem;
import com.loficostudios.melodyapi.utils.ItemCreator;
import com.loficostudios.melodyapi.utils.SimpleColor;
import com.loficostudios.melodyapi.utils.interfaces.ItemMetaFunction;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

public class Items {
    public static final ItemStack SPOOKY_PUMPKIN = ItemCreator.createItem(SimpleColor.deserialize("&6Spooky Pumpkin"), Material.CARVED_PUMPKIN, List.of(
            meta -> meta.addEnchant(Enchantment.BINDING_CURSE, 1, false)
    ), null, null);

    public static final ItemStack BROKEN_SHEARS = ItemCreator.createItem(SimpleColor.deserialize("Broken Shears"), Material.SHEARS, List.of(
            itemMeta -> {
                if (itemMeta instanceof Damageable damageable) {
                    damageable.setDamage(228);
                }
            }
    ), null, null);

    public static final ItemStack ROSE = ItemCreator.createItem(SimpleColor.deserialize("Pretty Rose"), Material.POPPY, null, null, null);
    public static final ItemStack FISHERMANS_ROD = ItemCreator.createItem(SimpleColor.deserialize("Fisherman's rod"), Material.FISHING_ROD, null, null, null);
}
