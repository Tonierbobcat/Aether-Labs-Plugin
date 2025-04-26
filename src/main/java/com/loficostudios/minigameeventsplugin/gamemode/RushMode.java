package com.loficostudios.minigameeventsplugin.gamemode;

import com.loficostudios.minigameeventsplugin.api.event.impl.AbstractGameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Collection;

public class RushMode extends AbstractGameMode {
    @Override
    public String getName() {
        return "Rush Mode";
    }

    @Override
    public Material getIcon() {
        return Material.FEATHER;
    }

    @Override
    public int getFillSpeed() {
        return 2;
    }

    @Override
    public void start() {
    }

    @Override
    public void end() {
    }

    @Override
    public void reset() {
    }

    @Override
    public void prepareResources(Collection<Player> participatingPlayers) {

    }

    @Override
    public void initializeCore(Collection<Player> participatingPlayers) {

    }

    @Override
    public void finalizeSetup(Collection<Player> participatingPlayers) {

    }
}
