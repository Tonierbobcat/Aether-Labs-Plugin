package com.loficostudios.minigameeventsplugin.gui;

import com.loficostudios.melodyapi.melodygui.GuiIcon;
import com.loficostudios.melodyapi.utils.ItemCreator;
import com.loficostudios.melodyapi.utils.MelodyGui;
import com.loficostudios.melodyapi.utils.SimpleColor;
import com.loficostudios.minigameeventsplugin.GameEvents.BaseEvent;
import com.loficostudios.minigameeventsplugin.AetherLabsPlugin;
import com.loficostudios.minigameeventsplugin.GameEvents.EventType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public class EventShop extends MelodyGui {

    @NotNull
    @Override
    protected Integer getSize() {
        return 9;
    }

    @Nullable
    @Override
    protected String getTitle() {
        return "";
    }

    public EventShop(Player player) {

        AetherLabsPlugin plugin = AetherLabsPlugin.getInstance();

        Collection<BaseEvent> baseEvents = plugin.getEventManager().getEvents();

        GuiIcon playerEventsIcon = new GuiIcon((Player) -> {

            new EventPage(player, baseEvents.stream().filter(event -> event.getType().equals(EventType.PLAYER)).toList())
                    .open(player);

        }, getEventIcon("Player Events", EventType.PLAYER.getIcon()), "playerEventIcon");

        setSlot(2, playerEventsIcon);

        GuiIcon plateEventsIcon = new GuiIcon((Player) -> {
            new EventPage(player, baseEvents.stream().filter(event -> event.getType().equals(EventType.PLATE)).toList())
                    .open(player);
        }, getEventIcon("Plate Events", EventType.PLATE.getIcon()), "plateEventIcon");

        setSlot(4, plateEventsIcon);

        GuiIcon globalEventsIcon = new GuiIcon((Player) -> {
            new EventPage(player, baseEvents.stream().filter(event -> event.getType().equals(EventType.GLOBAL)).toList())
                    .open(player);
        }, getEventIcon("World Events", EventType.GLOBAL.getIcon()), "worldEventIcon");

        setSlot(6, globalEventsIcon);

        ItemStack fillIcon = ItemCreator.createItem(Material.GRAY_STAINED_GLASS_PANE,
                SimpleColor.deserialize(" "), null, null);

        fill(
                new GuiIcon(fillIcon, null),
                0,
                9,
                false);
    }

    private ItemStack getEventIcon(String name, Material material) {
        return ItemCreator.createItem(material, SimpleColor.deserialize(name), null, null);
    }

}
