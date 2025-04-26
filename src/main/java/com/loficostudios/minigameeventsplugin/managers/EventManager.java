package com.loficostudios.minigameeventsplugin.managers;

import com.loficostudios.minigameeventsplugin.AetherLabsPlugin;
import com.loficostudios.minigameeventsplugin.api.event.GameEvent;
import com.loficostudios.minigameeventsplugin.game.Game;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

import static com.loficostudios.minigameeventsplugin.utils.Debug.log;
import static com.loficostudios.minigameeventsplugin.utils.Debug.logError;

public class EventManager {

    @Getter
    private GameEvent currentEvent;


    private List<BukkitTask> tasks = new ArrayList<>();

    public Collection<GameEvent> playerQueue = new ArrayList<>();

    private GameEvent lastEvent;

    private final Game game;

    public EventManager(Game game) {
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

        List<GameEvent> eventList = new ArrayList<>(AetherLabsPlugin.getInstance().getEvents().getAll());

        if (!playerQueue.isEmpty()) {

            GameEvent e = playerQueue.stream().toList().getFirst();
            playerQueue.remove(e);

            return e;
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
                try {
                    event.load(game);
                    log("loaded " + event.getId());
                } catch (Exception e) {
                    logError(event.getClass().getName() + " could not load: " + e.getMessage());
                    return null;
                }
            }

            return event;
        }
    }

    public void handleStart(GameEvent e) {
        if (e != null) {
            e.start(game);
            log("started " + e.getId());

            currentEvent = e;

            tasks.add(new BukkitRunnable() {
                @Override
                public void run() {
                    e.run(game);
                }
            }.runTaskTimer(AetherLabsPlugin.getInstance(), 0, 5));
            log("running " + e.getId() + " task");
        }
    }

    public Boolean handleEnd(GameEvent e) {
        try {
            e.end(game);
            log("ended " + e.getId());
            return true;
        } catch (Exception ex) {
            logError("could not end event " + ex);
            return false;
        }
    }

    public void handleCancel(GameEvent e) {
        if (e != null) {
            e.cancel(game);
            currentEvent = null;
            log("canceled " + e.getId());
        }
    }
}

