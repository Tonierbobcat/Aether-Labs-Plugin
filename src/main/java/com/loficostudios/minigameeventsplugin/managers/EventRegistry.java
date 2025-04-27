package com.loficostudios.minigameeventsplugin.managers;

import com.loficostudios.minigameeventsplugin.api.event.GameEvent;
import com.loficostudios.minigameeventsplugin.utils.Debug;
import org.bukkit.Bukkit;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class EventRegistry {
    private final Map<String, GameEvent> events = new HashMap<>();

    public void subscribe(GameEvent e) {
        Debug.log("Registered " + e.getName());
        events.put(e.getIdentifier(), e);
    }

    public Collection<GameEvent> getAll() {
        return events.values();
    }

    public GameEvent getEvent(String id) {
        GameEvent event;

        try {
            event = events.get(id);
        } catch (Exception e) {
            e.printStackTrace();
            Bukkit.getLogger().severe("Failed to get event. " + e.getMessage());
            return null;
        }

        return event;
    }
}
