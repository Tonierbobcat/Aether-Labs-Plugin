package com.loficostudios.minigameeventsplugin.api;

import com.loficostudios.minigameeventsplugin.api.event.EventType;
import com.loficostudios.minigameeventsplugin.api.event.impl.AbstractSelectorEvent;
import com.loficostudios.minigameeventsplugin.game.Game;
import com.loficostudios.minigameeventsplugin.managers.NotificationType;
import com.loficostudios.minigameeventsplugin.utils.PlayerState;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.Collection;

public abstract class PlayerSelectorEvent extends AbstractSelectorEvent<Player> {
    protected PlayerSelectorEvent(String id, String name, Material icon, double cost, int min, int max) {
        super(id, name, EventType.PLAYER, icon, cost, min, max);
    }

    protected PlayerSelectorEvent(String id, String name, Material icon, int min, int max) {
        super(id, name, EventType.PLAYER, icon, min, max);
    }

    protected PlayerSelectorEvent(String name, Material icon, int min, int max) {
        super(name, EventType.PLAYER, icon, min, max);
    }

    @Override
    public Collection<Player> getObjects(Game game) {
        return game.getPlayers().getPlayersInGame(PlayerState.ALIVE);
    }

    @Override
    public void start(Game game) {
        var bar = game.getProgressBar();
        var message = new StringBuilder();
        selectObjects(game, player -> {
            if (onSelect(game, player)) {
                if (getDisplayedEnabled()) {
                    message.append(" ").append(player != null ? player.getName() : "NaN");
                    bar.setTitle(message.toString());
                }

                game.getPlayers().notify(
                        NotificationType.GLOBAL,
                        Sound.BLOCK_NOTE_BLOCK_PLING,
                        1, 2);
            }
        }, players -> onComplete(game, players));
    }

    @Override
    public void end(Game game) {
        game.getProgressBar().removeAll();
    }
}
