package com.loficostudios.minigameeventsplugin.gui;

import com.loficostudios.melodyapi.melodygui.GuiIcon;
import com.loficostudios.melodyapi.utils.ItemCreator;
import com.loficostudios.melodyapi.utils.MelodyGui;
import com.loficostudios.melodyapi.utils.SimpleColor;
import com.loficostudios.minigameeventsplugin.api.BaseGameMode;
import com.loficostudios.minigameeventsplugin.managers.GameManager.GameManager;
import com.loficostudios.minigameeventsplugin.managers.GameManager.GameState;
import com.loficostudios.minigameeventsplugin.managers.VoteManager;
import com.loficostudios.minigameeventsplugin.AetherLabsPlugin;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class VoteGui extends MelodyGui {
    @NotNull
    @Override
    protected Integer getSize() {
        return 9;
    }

    @Nullable
    @Override
    protected String getTitle() {
        return "Vote for next game";
    }


    private final GameManager gameManager;

    public VoteGui() {
        AetherLabsPlugin plugin = AetherLabsPlugin.getInstance();
        this.gameManager = plugin.getGameManager();
        setSlot(2, getGuiIcon(gameManager.NORMAL));
        setSlot(4, getGuiIcon(gameManager.DIFFERENT_HEIGHTS));
        setSlot(6, getGuiIcon(gameManager.EGG_WARS));
    }



    private GuiIcon getGuiIcon(BaseGameMode mode) {

        String name = mode.getName();

        ItemStack icon = ItemCreator.createItem(mode.getIcon(), SimpleColor.deserialize(name), null, null);



        //icon.setAmount();

        return new GuiIcon(player -> {
            VoteManager voteManager = VoteManager.getInstance();

            if (voteManager == null)
                return;

            if (gameManager.getCurrentState().equals(GameState.COUNTDOWN)) {

                int votes = voteManager.getVotes(mode);

                if (votes >= 1) {
                    icon.setAmount(votes);
                }

                if (voteManager.castVote(player, mode)) {
                    player.sendMessage(SimpleColor.deserialize("&aCasted vote for " + name));
                    this.refreshGui(player, this);
                }
                else {
                    player.sendMessage(SimpleColor.deserialize("&cAlready voted for " + voteManager.getVote(player).getName()));
                }
            }
            else {
                player.sendMessage(SimpleColor.deserialize("&cThere is a currently a game active " + gameManager.getCurrentState()));
            }
        }, icon, mode.getId());
    }
}
