package com.loficostudios.minigameeventsplugin.gui;

import com.loficostudios.melodyapi.gui.MelodyGui;
import com.loficostudios.melodyapi.gui.icon.GuiIcon;
import com.loficostudios.minigameeventsplugin.AetherLabsPlugin;
import com.loficostudios.minigameeventsplugin.api.event.impl.AbstractGameMode;
import com.loficostudios.minigameeventsplugin.game.Game;
import com.loficostudios.minigameeventsplugin.game.GameState;
import com.loficostudios.minigameeventsplugin.gamemode.GameModes;
import com.loficostudios.minigameeventsplugin.managers.VoteManager;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class VoteGui extends MelodyGui {
    public static List<VoteGui> instances = new ArrayList<>();
    public VoteGui() {
        super(9, Component.text("Vote for next game"));
        instances.add(this);
    }

    private void create(Player player) {
        clear();
        var game = AetherLabsPlugin.inst().getActiveGame(player.getWorld());
        setSlot(2, getGMIcon(game, GameModes.NORMAL));
        setSlot(4, getGMIcon(game, GameModes.DIFFERENT_HEIGHTS));
        setSlot(6, getGMIcon(game, GameModes.EGG_WARS));
        setSlot(7, getGMIcon(game, GameModes.RUSH));
    }

    @Override
    public boolean open(@NotNull Player player) {
        create(player);
        return super.open(player);
    }

    private GuiIcon getGMIcon(Game game, AbstractGameMode mode) {
        String name = mode.getName();

        VoteManager voteManager = game.getVoting();
        if (voteManager == null)
            throw new IllegalArgumentException("Vote manager is null");


        var ico = new GuiIcon(new ItemStack(mode.getIcon()), Component.text(name));
        ico.description(List.of(Component.text("§7Votes: §e§l" + voteManager.getVotes(mode))));
        ico.onClick((p,c) -> {
            if (game.getCurrentState().equals(GameState.COUNTDOWN)) {
                if (voteManager.castVote(p, mode)) {
                    for (VoteGui ignored : instances) {
                        create(p);
                    }
                    p.sendMessage(Component.text("&aCasted vote for " + name));
                }
                else {
                    p.sendMessage(Component.text("&cAlready voted for " + voteManager.getVote(p).getName()));
                }
            }
            else {
                p.sendMessage(Component.text("&cThere is a currently a game active " + game.getCurrentState()));
            }
        });

        return ico;
    }
}
