package com.loficostudios.minigameeventsplugin.gamemode;

import com.loficostudios.minigameeventsplugin.game.Game;
import org.bukkit.entity.Player;

import java.util.Collection;

public interface IGameInitializationReceiver {
    void prepareResources(Game game, Collection<Player> participatingPlayers);
    void initializeCore(Game game, Collection<Player> participatingPlayers);
    void finalizeSetup(Game game, Collection<Player> participatingPlayers);
}
