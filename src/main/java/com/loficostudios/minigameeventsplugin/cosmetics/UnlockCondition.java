package com.loficostudios.minigameeventsplugin.cosmetics;

import org.bukkit.entity.Player;

import java.util.function.Function;

public class UnlockCondition {
    private final String message;
    private final Function<Player, Boolean> canUnlock;

    public UnlockCondition(String message, Function<Player, Boolean> canUnlock) {
        this.message = message;
        this.canUnlock = canUnlock;
    }

    public boolean has(Player player) {
        return canUnlock.apply(player);
    }

    public String getMessage() {
        return message;
    }
}
