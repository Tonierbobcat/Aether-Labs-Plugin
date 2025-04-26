package com.loficostudios.minigameeventsplugin.gui;

import com.loficostudios.melodyapi.gui.MelodyGui;
import com.loficostudios.melodyapi.gui.icon.GuiIcon;
import com.loficostudios.minigameeventsplugin.AetherLabsPlugin;
import com.loficostudios.minigameeventsplugin.gamemode.GameModes;
import com.loficostudios.minigameeventsplugin.api.event.impl.AbstractGameMode;
import com.loficostudios.minigameeventsplugin.game.Game;
import com.loficostudios.minigameeventsplugin.game.GameState;
import com.loficostudios.minigameeventsplugin.managers.VoteManager;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class VoteGui extends MelodyGui {
    private final Game gameManager;
    public static List<VoteGui> instances = new ArrayList<>();
    public VoteGui() {
        super(9, Component.text("Vote for next game"));
        AetherLabsPlugin plugin = AetherLabsPlugin.getInstance();
        this.gameManager = plugin.getActiveGame();
        instances.add(this);
    }

    private void create() {
        clear();
        setSlot(2, getGMIcon(GameModes.NORMAL));
        setSlot(4, getGMIcon(GameModes.DIFFERENT_HEIGHTS));
        setSlot(6, getGMIcon(GameModes.EGG_WARS));
        setSlot(7, getGMIcon(GameModes.RUSH));
    }

    @Override
    public boolean open(@NotNull Player player) {
        create();
        return super.open(player);
    }

    private GuiIcon getGMIcon(AbstractGameMode mode) {
        String name = mode.getName();

        VoteManager voteManager = AetherLabsPlugin.getInstance().getActiveGame().getVoting();
        if (voteManager == null)
            throw new IllegalArgumentException("Vote manager is null");


        var ico = new GuiIcon(new ItemStack(mode.getIcon()), Component.text(name));
        ico.description(List.of(Component.text("§7Votes: §e§l" + voteManager.getVotes(mode))));
        ico.onClick((player,c) -> {
            if (gameManager.getCurrentState().equals(GameState.COUNTDOWN)) {

//                int votes = voteManager.getVotes(mode);
//                if (votes >= 1) {
//                    ico.amount(votes);
//                }

                if (voteManager.castVote(player, mode)) {
                    for (VoteGui ignored : instances) {
                        create();
                    }
                    player.sendMessage(Component.text("&aCasted vote for " + name));
                }
                else {
                    player.sendMessage(Component.text("&cAlready voted for " + voteManager.getVote(player).getName()));
                }

            }
            else {
                player.sendMessage(Component.text("&cThere is a currently a game active " + gameManager.getCurrentState()));
            }
        });

        return ico;
    }
}
