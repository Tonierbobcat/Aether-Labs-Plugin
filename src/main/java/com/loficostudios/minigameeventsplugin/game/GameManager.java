package com.loficostudios.minigameeventsplugin.game;

import com.loficostudios.minigameeventsplugin.config.Messages;
import com.loficostudios.minigameeventsplugin.game.arena.GameArena;
import com.loficostudios.minigameeventsplugin.game.player.NotificationType;
import com.loficostudios.minigameeventsplugin.utils.Countdown;
import org.bukkit.Sound;
import org.bukkit.World;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.function.Consumer;

public class GameManager {
    private final HashMap<World, Game> active = new HashMap<>();
    private final HashMap<World, Consumer<Long>> countdowns = new HashMap<>();
    public void startCountdown(Game game, int seconds) {
        if (game.getArena() == null)
            throw new IllegalArgumentException("Arena is null");

        var c = countdowns.remove(game.getArena().getWorld());
        if (c != null) {
            c.accept(System.currentTimeMillis());
        }

        try {
            var field = Game.class.getDeclaredField("currentState");
            field.setAccessible(true);
            field.set(game, GameState.COUNTDOWN);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new IllegalArgumentException();
        }

        game.getIndicator().show(GameIndicator.IndicatorType.STATUS);

        game.startVote();

        game.getIndicator().status(Messages.STATUS_COUNTDOWN);

        game.getIndicator().show(GameIndicator.IndicatorType.PROGRESS);

        var countdown = new Countdown((i) -> tick(game, i), () -> startGame(game))
                .start(seconds);

        countdowns.put(game.getArena().getWorld(), (l) -> {
            try {
                var field = Game.class.getDeclaredField("currentState");
                field.setAccessible(true);
                field.set(game, GameState.NONE);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new IllegalArgumentException();
            }
            countdown.cancel();

            var indicator = game.getIndicator();
            for (GameIndicator.IndicatorType type : GameIndicator.IndicatorType.values())
                indicator.hide(type);
        });
    }

    private void tick(Game game, int i) {
        game.getIndicator().progress("In... " + i);

        switch (i) {
            case 3 -> game.getPlayerManager().notify(NotificationType.GLOBAL, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
            case 2 -> game.getPlayerManager().notify(NotificationType.GLOBAL, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1.5f);
            case 1 -> game.getPlayerManager().notify(NotificationType.GLOBAL, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 2);
        }
    }

    private void startGame(Game game) {
        if (!game.start())
            return;
        active.put(game.getArena().getWorld(), game);
    }

    public @Nullable Game getGame(World world) {
        return active.get(world);
    }

    public void onDisable() {
        active.values().forEach(game -> {
            GameArena arena = game.getArena();
            if (arena == null)
                return;
            try {
                arena.clear();
                arena.removeEntities();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
