package com.loficostudios.minigameeventsplugin.gui;

import com.loficostudios.melodyapi.gui.MelodyGui;
import com.loficostudios.melodyapi.gui.icon.GuiIcon;
import com.loficostudios.minigameeventsplugin.AetherLabsPlugin;
import com.loficostudios.minigameeventsplugin.api.event.GameEvent;
import com.loficostudios.minigameeventsplugin.api.PlayerSelectorEvent;
import net.kyori.adventure.text.Component;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.stream.Stream;

import static com.loficostudios.minigameeventsplugin.utils.Debug.logWarning;

public class EventPage extends MelodyGui {

    private final Collection<GameEvent> events;

    public EventPage(final Player player, @NotNull Collection<GameEvent> events) {
        super(events.size(), Component.text(""));
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
        String description;

        if (event instanceof PlayerSelectorEvent randomPlayerSelectorEvent) {
            int min = randomPlayerSelectorEvent.getMin();
            int max = randomPlayerSelectorEvent.getMax();

            description = "[" + min + "-" + max + "] " + event.getWarning().message();
        }
        else {
            description = event.getWarning().message();
        }

        String nameColor = "&e";
        String descriptionColor = "&7";

        AetherLabsPlugin plugin = AetherLabsPlugin.getInstance();

        return new GuiIcon(event.getIcon(), Component.text(nameColor + event.getName()), Stream.of(
                descriptionColor + description,
                "",
                "&7Cost: &e&l" + event.getCost(),
                "",
                "&8" + event.getId()
        ).map(Component::text).toList(), (p, c) -> {
            if (plugin.vaultHook) {
                Economy economy = plugin.getEconomy();

                if (economy != null && economy.getBalance(p) >= event.getCost()) {
                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_YES, 1, 1);
                    p.sendMessage(Component.text("&ePurchased event!"));

                    if (plugin.getGameManager().getEvents().queueEvent(p, event)) {
                        p.sendMessage(Component.text("&7Your event has been queued up to go next!"));
                        economy.withdrawPlayer(p, event.getCost());
                    }
                    else {
                        p.sendMessage(Component.text("&cYour event could not be queued!"));
                    }
                }
                else {
                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                    p.sendMessage(Component.text("&cYou don't have enough money to purchase this!"));
                }
            }
            else {
                logWarning("vault not installed");
            }
        });
    }
}
