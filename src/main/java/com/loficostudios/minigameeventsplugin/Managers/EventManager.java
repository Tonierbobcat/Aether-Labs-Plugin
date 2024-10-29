package com.loficostudios.minigameeventsplugin.Managers;

import com.loficostudios.melodyapi.utils.Common;
import com.loficostudios.minigameeventsplugin.GameEvents.BaseEvent;
import com.loficostudios.minigameeventsplugin.RandomEventsPlugin;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

import static com.loficostudios.minigameeventsplugin.Utils.DebugUtil.debug;
import static com.loficostudios.minigameeventsplugin.Utils.DebugUtil.debugError;

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
                    debug("loaded " + event.getId());
                } catch (Exception e) {
                    debugError(event.getClass().getName() + " could not load: " + e.getMessage());
                    return null;
                }
            }

            return event;
        }
    }

    public void handleStart(BaseEvent e) {
        if (e != null) {
            e.start();
            debug("started " + e.getId());

            currentEvent = e;

            e.getTasks().add(new BukkitRunnable() {
                @Override
                public void run() {
                    e.run();
                }
            }.runTaskTimer(RandomEventsPlugin.getInstance(), 0, 5));
            debug("running " + e.getId() + " task");
        }
    }

    public Boolean handleEnd(BaseEvent e) {
        try {
            e.end();
            debug("ended " + e.getId());
            return true;
        } catch (Exception ex) {
            debugError("could not end event " + ex);
            return false;
        }
    }

    public void handleCancel(BaseEvent e) {
        if (e != null) {
            e.cancel();
            currentEvent = null;
            debug("canceled " + e.getId());
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
        events.put(e.getId(), e);
    }
}

