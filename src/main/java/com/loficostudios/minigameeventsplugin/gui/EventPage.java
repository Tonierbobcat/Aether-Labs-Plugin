package com.loficostudios.minigameeventsplugin.gui;

import com.loficostudios.melodyapi.gui.MelodyGui;
import com.loficostudios.minigameeventsplugin.api.BaseEvent;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class EventPage extends MelodyGui {

    private final Collection<BaseEvent> events;

    public EventPage(final Player player, @NotNull Collection<BaseEvent> events) {
        super(events.size(), Component.text(""));
        this.events = events;
    }

    private void create() {
        int index = 0;
        for (BaseEvent event : events) {

            setSlot(index, event.getGUIIcon());
            index++;
        }
    }

    @Override
    public boolean open(@NotNull Player player) {
        create();
        return super.open(player);
    }
}
