package com.loficostudios.minigameeventsplugin.managers;

import com.loficostudios.minigameeventsplugin.AetherLabsPlugin;
import com.loficostudios.minigameeventsplugin.api.event.GameEvent;
import com.loficostudios.minigameeventsplugin.game.Game;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import static com.loficostudios.minigameeventsplugin.utils.Debug.log;
import static com.loficostudios.minigameeventsplugin.utils.Debug.logError;

public class EventController {

    @Getter
    private GameEvent currentEvent;


    private List<BukkitTask> tasks = new ArrayList<>();

    public Collection<GameEvent> playerQueue = new ArrayList<>();

    private GameEvent lastEvent;

    private final Game game;

    public EventController(Game game) {
        this.game = game;
    }

    public Boolean queueEvent(Player player, GameEvent e) {
        if (player != null) {
            return playerQueue.add(e);
        }
        else {
            return false;
        }
    }

    public GameEvent getNextEvent() {
        Random random = new Random();

        List<GameEvent> eventList = new ArrayList<>(AetherLabsPlugin.inst().getEvents().getAll());

        if (!playerQueue.isEmpty()) {
            GameEvent event = playerQueue.stream().toList().getFirst();
            playerQueue.remove(event);
            var initialized = initializeEvent(event);
            return initialized ? event : null;
        }
        else {
            GameEvent event = null;

            boolean foundEvent = false;
            while(!foundEvent) {
                int index = random.nextInt(eventList.size());
                event = eventList.get(index);

                if (event != lastEvent || lastEvent == null) {
                    foundEvent = true;
                }
            }

            if (event != null) {
                return initializeEvent(event) ? event : null;
            }

            return event;
        }
    }

    private boolean initializeEvent(GameEvent event) {
        try {
            event.load(game);
            log("loaded " + event.getIdentifier());
            return true;
        } catch (Exception e) {
            logError(event.getClass().getName() + " could not load: " + e.getMessage());
            return false;
        }
    }

    public void handleStart(GameEvent e) {
        if (e != null) {
            e.start(game);
            log("started " + e.getIdentifier());

            currentEvent = e;

            tasks.add(new BukkitRunnable() {
                @Override
                public void run() {
                    e.run(game);
                }
            }.runTaskTimer(AetherLabsPlugin.inst(), 0, 5));
            log("running " + e.getIdentifier() + " task");
        }
    }

    public Boolean handleEnd(GameEvent e) {
        for (BukkitTask task : tasks) {
            task.cancel();
        }
        try {
            e.end(game);
            log("ended " + e.getIdentifier());
            return true;
        } catch (Exception ex) {
            logError("could not end event " + ex);
            return false;
        }
    }

    public void handleCancel(GameEvent e) {
        for (BukkitTask task : tasks) {
            task.cancel();
        }
        if (e != null) {
            e.cancel(game);
            currentEvent = null;
            log("canceled " + e.getIdentifier());
        }
    }
}

