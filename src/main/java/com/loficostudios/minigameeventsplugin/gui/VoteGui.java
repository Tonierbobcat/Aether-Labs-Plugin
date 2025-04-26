package com.loficostudios.minigameeventsplugin.gui;

import com.loficostudios.melodyapi.gui.MelodyGui;
import com.loficostudios.melodyapi.gui.icon.GuiIcon;
import com.loficostudios.minigameeventsplugin.AetherLabsPlugin;
import com.loficostudios.minigameeventsplugin.api.BaseGameMode;
import com.loficostudios.minigameeventsplugin.managers.GameManager.GameManager;
import com.loficostudios.minigameeventsplugin.managers.GameManager.GameState;
import com.loficostudios.minigameeventsplugin.managers.VoteManager;
import net.kyori.adventure.text.Component;

import java.util.List;

public class VoteGui extends MelodyGui {
    private final GameManager gameManager;

    public VoteGui() {
        super(9, Component.text("Vote for next game"));
        AetherLabsPlugin plugin = AetherLabsPlugin.getInstance();
        this.gameManager = plugin.getGameManager();
    }

    private void create() {
        setSlot(2, getGuiIcon(gameManager.NORMAL));
        setSlot(4, getGuiIcon(gameManager.DIFFERENT_HEIGHTS));
        setSlot(6, getGuiIcon(gameManager.EGG_WARS));
    }


    private GuiIcon getGuiIcon(BaseGameMode mode) {
        String name = mode.getName();

        return new GuiIcon(mode.getIcon(), Component.text(name), List.of(), (player,c) -> {
            VoteManager voteManager = VoteManager.getInstance();

            if (voteManager == null)
                return;

            if (gameManager.getCurrentState().equals(GameState.COUNTDOWN)) {

                int votes = voteManager.getVotes(mode);

//                if (votes >= 1) {
//                    icon.setAmount(votes);
//                }

                if (voteManager.castVote(player, mode)) {
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
    }
}
