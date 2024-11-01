package com.loficostudios.minigameeventsplugin.gui;

import com.loficostudios.melodyapi.icon.GuiIcon;
import com.loficostudios.melodyapi.utils.ItemCreator;
import com.loficostudios.melodyapi.utils.MelodyGui;
import com.loficostudios.melodyapi.utils.SimpleColor;
import com.loficostudios.minigameeventsplugin.GameTypes.BaseGameMode;
import com.loficostudios.minigameeventsplugin.Managers.GameManager.GameManager;
import com.loficostudios.minigameeventsplugin.Managers.GameManager.GameState;
import com.loficostudios.minigameeventsplugin.Managers.VoteManager;
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

    public VoteGui() {

        GameManager gameManager = AetherLabsPlugin.getInstance().getGameManager();
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

            if (AetherLabsPlugin.getInstance().getGameManager().getCurrentState().equals(GameState.COUNTDOWN)) {

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
                player.sendMessage(SimpleColor.deserialize("&cThere is a currently a game active " + AetherLabsPlugin.getInstance().getGameManager().getCurrentState()));
            }
        }, icon, mode.getId());
    }
}
