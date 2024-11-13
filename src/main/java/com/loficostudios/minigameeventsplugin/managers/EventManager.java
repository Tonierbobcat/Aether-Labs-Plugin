package com.loficostudios.minigameeventsplugin.managers;

import com.loficostudios.melodyapi.utils.Common;
import com.loficostudios.minigameeventsplugin.api.BaseEvent;
import com.loficostudios.minigameeventsplugin.AetherLabsPlugin;
import com.loficostudios.minigameeventsplugin.utils.Debug;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

import static com.loficostudios.minigameeventsplugin.utils.Debug.log;
import static com.loficostudios.minigameeventsplugin.utils.Debug.logError;

public class EventManager {

    private final Map<String, BaseEvent> events = new HashMap<>();

    @Getter
    private BaseEvent currentEvent;

    public Collection<BaseEvent> playerQueue = new ArrayList<>();

    private BaseEvent lastEvent;

    public Boolean queueEvent(Player player, BaseEvent e) {
        if (player != null) {
            return playerQueue.add(e);
        }
        else {
            return false;
        }
    }

    public BaseEvent getNextEvent() {
        Random random = new Random();

        List<BaseEvent> eventList = events.values().stream()
                .toList();

        if (!playerQueue.isEmpty()) {

            BaseEvent e = playerQueue.stream().toList().getFirst();
            playerQueue.remove(e);

            return e;
        }
        else {
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
                    log("loaded " + event.getId());
                } catch (Exception e) {
                    logError(event.getClass().getName() + " could not load: " + e.getMessage());
                    return null;
                }
            }

            return event;
        }
    }

    public void handleStart(BaseEvent e) {
        if (e != null) {
            e.start();
            log("started " + e.getId());

            currentEvent = e;

            e.getTasks().add(new BukkitRunnable() {
                @Override
                public void run() {
                    e.run();
                }
            }.runTaskTimer(AetherLabsPlugin.getInstance(), 0, 5));
            log("running " + e.getId() + " task");
        }
    }

    public Boolean handleEnd(BaseEvent e) {
        try {
            e.end();
            log("ended " + e.getId());
            return true;
        } catch (Exception ex) {
            logError("could not end event " + ex);
            return false;
        }
    }

    public void handleCancel(BaseEvent e) {
        if (e != null) {
            e.cancel();
            currentEvent = null;
            log("canceled " + e.getId());
        }
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
        Debug.log("Registered " + e.getName());
        events.put(e.getId(), e);
    }
}

