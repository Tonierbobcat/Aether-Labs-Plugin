package com.loficostudios.minigameeventsplugin.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

public class ItemCreator {
    public static ItemStack createItem(@NotNull Component text, Material material, Consumer<ItemMeta> o, Object o1, Object o2) {
        var item = new ItemStack(material);
        var meta = Objects.requireNonNull(item.getItemMeta());
        meta.displayName(text.decoration(TextDecoration.ITALIC, false));
        if (o != null) {
            o.accept(meta);
        }
        item.setItemMeta(meta);
        return item;
    }
}
