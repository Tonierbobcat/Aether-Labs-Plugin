package com.loficostudios.minigameeventsplugin.api.event;

import com.loficostudios.minigameeventsplugin.game.Game;
import com.loficostudios.minigameeventsplugin.game.events.IObjectSelector;

import java.util.Collection;
import java.util.function.Consumer;

public interface SelectorEvent<Impl> extends GameEvent, IObjectSelector<Impl> {
    boolean getDisplayedEnabled();

    boolean onSelect(Game game, Impl selected);

    void onComplete(Game game, Collection<Impl> selected);

    int getAmount(Game game);

    Collection<Impl> getObjects(Game game);

    void selectObjects(Game game, Consumer<Impl> onSelected, Consumer<Collection<Impl>> onComplete);
}
