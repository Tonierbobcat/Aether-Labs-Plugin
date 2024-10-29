package com.loficostudios.minigameeventsplugin.Gui;

import com.loficostudios.melodyapi.icon.GuiIcon;
import com.loficostudios.melodyapi.utils.ItemCreator;
import com.loficostudios.melodyapi.utils.MelodyGui;
import com.loficostudios.melodyapi.utils.SimpleColor;
import com.loficostudios.minigameeventsplugin.GameEvents.BaseEvent;
import com.loficostudios.minigameeventsplugin.Interfaces.IPlateEvent;
import com.loficostudios.minigameeventsplugin.Interfaces.IPlayerEvent;
import com.loficostudios.minigameeventsplugin.Interfaces.IWorldEvent;
import com.loficostudios.minigameeventsplugin.RandomEventsPlugin;
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
        Collection<BaseEvent> baseEvents = RandomEventsPlugin.getInstance().getEventManager().getEvents();

        GuiIcon playerEventsIcon = new GuiIcon((Player) -> {

            new EventPage(player, baseEvents.stream().filter(event -> event instanceof IPlayerEvent).toList())
                    .open(player);

        }, getEventIcon("Player Events", IPlayerEvent.getIcon()), "playerEventIcon");

        setSlot(2, playerEventsIcon);

        GuiIcon plateEventsIcon = new GuiIcon((Player) -> {
            new EventPage(player, baseEvents.stream().filter(event -> event instanceof IPlateEvent).toList())
                    .open(player);
        }, getEventIcon("Plate Events", IPlateEvent.getIcon()), "plateEventIcon");

        setSlot(4, plateEventsIcon);

        GuiIcon worldEventsIcon = new GuiIcon((Player) -> {
            new EventPage(player, baseEvents.stream().filter(event -> event instanceof IWorldEvent).toList())
                    .open(player);
        }, getEventIcon("World Events", IWorldEvent.getIcon()), "worldEventIcon");

        setSlot(6, worldEventsIcon);

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
