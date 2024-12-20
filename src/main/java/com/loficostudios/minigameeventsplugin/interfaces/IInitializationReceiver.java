package com.loficostudios.minigameeventsplugin.interfaces;

import org.bukkit.entity.Player;

import java.util.Collection;

public interface IInitializationReceiver {
    void prepareResources(Collection<Player> participatingPlayers);
    void initializeCore(Collection<Player> participatingPlayers);
    void finalizeSetup(Collection<Player> participatingPlayers);
}
