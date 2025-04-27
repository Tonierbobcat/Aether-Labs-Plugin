package com.loficostudios.minigameeventsplugin.gui;

import com.loficostudios.melodyapi.gui.MelodyGui;
import com.loficostudios.melodyapi.gui.icon.GuiIcon;
import com.loficostudios.melodyapi.gui.impl.PopOutGui;
import com.loficostudios.minigameeventsplugin.AetherLabsPlugin;
import com.loficostudios.minigameeventsplugin.api.event.EventType;
import com.loficostudios.minigameeventsplugin.api.event.GameEvent;
import com.loficostudios.minigameeventsplugin.api.event.SelectorEvent;
import net.kyori.adventure.text.Component;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import static com.loficostudios.minigameeventsplugin.utils.Debug.logWarning;

public class EventShop extends MelodyGui {
    private final List<GameEvent> baseEvents;
    public EventShop() {
        super(9, Component.text("Shop"));
        AetherLabsPlugin plugin = AetherLabsPlugin.getInstance();
        Collection<GameEvent> baseEvents = plugin.getEvents().getAll();
        this.baseEvents = new ArrayList<>(baseEvents);
    }

    @Override
    public boolean open(@NotNull Player player) {
        create();
        return super.open(player);
    }

    private void create() {

        GuiIcon playerEventsIcon = new GuiIcon(EventType.PLAYER.getIcon(), Component.text("Player Events"), List.of(), (p, c) -> {
            new Page(baseEvents.stream().filter(event -> event.getType().equals(EventType.PLAYER)).toList())
                    .open(p);
        });

        setSlot(2, playerEventsIcon);

        GuiIcon plateEventsIcon = new GuiIcon(EventType.PLATE.getIcon(), Component.text("Plate Events"), List.of(), (p, c) -> {
            new Page(baseEvents.stream().filter(event -> event.getType().equals(EventType.PLATE)).toList())
                    .open(p);
        });

        setSlot(4, plateEventsIcon);

        GuiIcon globalEventsIcon = new GuiIcon(EventType.GLOBAL.getIcon(), Component.text("World Events"), List.of(), (p, c) -> {
            new Page(baseEvents.stream().filter(event -> event.getType().equals(EventType.GLOBAL)).toList())
                    .open(p);
        });

        setSlot(6, globalEventsIcon);
    }

    private static class Page extends PopOutGui {

        private final Collection<GameEvent> events;

        private Page(@NotNull Collection<GameEvent> events) {
            super(events.size(), Component.text(""), p -> new EventShop().open(p));
            this.events = events;
        }

        private void create() {
            int index = 0;
            for (GameEvent event : events) {

                setSlot(index, getGUIIcon(event));
                index++;
            }
        }

        @Override
        public boolean open(@NotNull Player player) {
            create();
            return super.open(player);
        }

        public @NotNull GuiIcon getGUIIcon(GameEvent event) {
            var description = Stream.of((event instanceof SelectorEvent<?> selector) ? "§7[" + selector.getMin() + "-" + selector.getMax() + "] " + event.getWarning().message() : "§7" + event.getWarning().message(),
                    "",
                    "§7Cost: §e§l" + event.getCost(),
                    "",
                    "§8" + event.getIdentifier()).map(Component::text).toList();

            AetherLabsPlugin plugin = AetherLabsPlugin.getInstance();

            return new GuiIcon(event.getIcon(), Component.text("§e" + event.getName()), description, (p, c) -> {
                if (plugin.vaultHook) {
                    Economy economy = plugin.getEconomy();

                    if (economy != null && economy.getBalance(p) >= event.getCost()) {
                        p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_YES, 1, 1);
                        p.sendMessage(Component.text("§ePurchased event!"));

                        if (plugin.getActiveGame(p.getWorld()).getEvents().queueEvent(p, event)) {
                            p.sendMessage(Component.text("§7Your event has been queued up to go next!"));
                            economy.withdrawPlayer(p, event.getCost());
                        }
                        else {
                            p.sendMessage(Component.text("§cYour event could not be queued!"));
                        }
                    }
                    else {
                        p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                        p.sendMessage(Component.text("§cYou don't have enough money to purchase this!"));
                    }
                }
                else {
                    logWarning("vault not installed");
                }
            });
        }
    }
}
