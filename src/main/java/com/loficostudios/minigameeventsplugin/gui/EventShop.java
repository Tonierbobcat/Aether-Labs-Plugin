package com.loficostudios.minigameeventsplugin.gui;

import com.loficostudios.melodyapi.gui.MelodyGui;
import com.loficostudios.melodyapi.gui.icon.GuiIcon;
import com.loficostudios.minigameeventsplugin.AetherLabsPlugin;
import com.loficostudios.minigameeventsplugin.api.BaseEvent;
import com.loficostudios.minigameeventsplugin.gameEvents.EventType;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class EventShop extends MelodyGui {
    private final List<BaseEvent> baseEvents;
    public EventShop() {
        super(9);
        AetherLabsPlugin plugin = AetherLabsPlugin.getInstance();
        Collection<BaseEvent> baseEvents = plugin.getEventManager().getEvents();
        this.baseEvents = new ArrayList<>(baseEvents);
    }

    @Override
    public boolean open(@NotNull Player player) {
        create();
        return super.open(player);
    }

    private void create() {

        GuiIcon playerEventsIcon = new GuiIcon(EventType.PLAYER.getIcon(), Component.text("Player Events"), List.of(), (p, c) -> {
            new EventPage(p, baseEvents.stream().filter(event -> event.getType().equals(EventType.PLAYER)).toList())
                    .open(p);
        });

        setSlot(2, playerEventsIcon);

        GuiIcon plateEventsIcon = new GuiIcon(EventType.PLATE.getIcon(), Component.text("Plate Events"), List.of(), (p, c) -> {
            new EventPage(p, baseEvents.stream().filter(event -> event.getType().equals(EventType.PLATE)).toList())
                    .open(p);
        });

        setSlot(4, plateEventsIcon);

        GuiIcon globalEventsIcon = new GuiIcon(EventType.GLOBAL.getIcon(), Component.text("World Events"), List.of(), (p, c) -> {
            new EventPage(p, baseEvents.stream().filter(event -> event.getType().equals(EventType.GLOBAL)).toList())
                    .open(p);
        });

        setSlot(6, globalEventsIcon);
    }
}
