package com.loficostudios.minigameeventsplugin.Managers;

import com.loficostudios.melodyapi.utils.Common;
import com.loficostudios.minigameeventsplugin.GameEvents.BaseEvent;
import com.loficostudios.minigameeventsplugin.GameEvents.PlayerEvents.PlayerCakeEvent;
import com.loficostudios.minigameeventsplugin.MiniGameEventsPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

import static com.loficostudios.minigameeventsplugin.Utils.DebugUtil.debugError;

public class EventManager {

    private final Map<String, BaseEvent> events = new HashMap<>();

    public BaseEvent currentEvent;

    public Collection<BaseEvent> playerQueue = new ArrayList<>();



    private BaseEvent lastEvent;

    public BaseEvent getNextEvent() {
        if (!playerQueue.isEmpty()) {
            //Optional<BaseEvent> playerEvent = playerQueue.stream().findFirst();
            //playerQueue.stream().findFirst().ifPresent(BaseEvent event :);
            Common.broadcast("Player queued event");
            return new PlayerCakeEvent();
        }
        else {

            List<BaseEvent> eventList = events.values().stream()
                    .toList();

            Random random = new Random();
            int eventSize = eventList.size();

            BaseEvent event = null;

            boolean foundEvent = false;
            while(!foundEvent) {
                int index = random.nextInt(eventList.size());
                event = eventList.get(index);

                if (event != lastEvent || lastEvent == null) {
                    foundEvent = true;
                }
            }

            if (event != null) {
                try {
                    event.load();
                } catch (Exception e) {
                    debugError(event.getClass().getName() + " could not load: " + e.getMessage());
                    return null;
                }
            }

            return event;
        }
    }

    public void handleEvent(BaseEvent e) {
        if (currentEvent != null) {
            currentEvent.cancel();
            currentEvent.end();
        }

        currentEvent = e;

        currentEvent.getTasks().add(new BukkitRunnable() {
            @Override
            public void run() {
                if (currentEvent != null) {
                    currentEvent.run();
                }
            }
        }.runTaskTimer(MiniGameEventsPlugin.getInstance(), 0, 5));

        currentEvent.start();
    }

    public Collection<BaseEvent> getEvents() {
        return events.values();
    }

    public BaseEvent getEvent(String id) {
        BaseEvent event;

        try {
            event = events.get(id);
        } catch (Exception e) {
            Common.broadcast("Failed to get event. " + e);
            return null;
        }

        return event;
    }

    public void subscribe(BaseEvent e) {
        events.put(e.getId(), e);
    }


   /*public boolean addEvent(BaseEvent e) {

        if (e == null) {
            Common.broadcast("Event is null");
            return false;
        }

        playerQueue.add(e);
        return true;
    }*/

    /*public boolean AddEvent(BaseEvent e) {

        if (e == null) {
            Common.broadcast("Event is null");
            return false;
        }

        playerQueue.add(e);
        return true;
    }*/
}

