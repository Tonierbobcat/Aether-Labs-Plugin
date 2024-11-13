package com.loficostudios.minigameeventsplugin.gui;

import com.loficostudios.melodyapi.utils.MelodyGui;
import com.loficostudios.minigameeventsplugin.api.BaseEvent;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public class EventPage extends MelodyGui {

    private final Collection<BaseEvent> events;

    public EventPage(final Player player, @NotNull Collection<BaseEvent> events) {

        this.events = events;

        int index = 0;
        for (BaseEvent event : events) {

            setSlot(index, event.getGUIIcon());
            index++;
        }
    }



    @NotNull
    @Override
    protected Integer getSize() {
        return events.size();
    }

    @Nullable
    @Override
    protected String getTitle() {
        return "";
    }
}
