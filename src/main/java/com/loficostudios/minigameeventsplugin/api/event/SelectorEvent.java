package com.loficostudios.minigameeventsplugin.api.event;

import com.loficostudios.minigameeventsplugin.game.Game;
import com.loficostudios.minigameeventsplugin.game.events.IObjectSelector;
import org.bukkit.boss.BossBar;

import java.util.Collection;

public interface SelectorEvent<Impl> extends GameEvent, IObjectSelector<Impl> {
    boolean getDisplayedEnabled();

    boolean onSelect(Game game, Impl selected);

    void onComplete(Game game, Collection<Impl> selected);

    int getAmount(Game game);

    Collection<Impl> getObjects(Game game);

    void selectObjects(Game game, BossBar bar);
}
