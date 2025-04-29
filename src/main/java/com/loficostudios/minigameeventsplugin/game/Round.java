package com.loficostudios.minigameeventsplugin.game;

import com.loficostudios.minigameeventsplugin.api.event.GameEvent;

public class Round {
    private final GameEvent event;
    private final long startTime;
    private final int number;
    private long duration;
    private boolean completed;

    public Round(GameEvent event, long startTime, int number) {
        this.event = event;
        this.startTime = startTime;
        this.number = number;
    }

    public void complete() {
        completed = true;
        duration = System.currentTimeMillis() - startTime;
    }

    public boolean isCompleted() {
        return completed;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getDuration() {
        return duration;
    }

    public GameEvent getEvent() {
        return event;
    }

    public int getNumber() {
        return number;
    }
}
