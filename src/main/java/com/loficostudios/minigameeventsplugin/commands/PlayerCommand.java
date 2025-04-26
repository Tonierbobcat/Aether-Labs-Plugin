package com.loficostudios.minigameeventsplugin.commands;

import com.loficostudios.minigameeventsplugin.player.profile.Profile;
import com.loficostudios.minigameeventsplugin.config.Messages;
import com.loficostudios.minigameeventsplugin.gui.EventShop;
import com.loficostudios.minigameeventsplugin.gui.VoteGui;
import com.loficostudios.minigameeventsplugin.player.profile.ProfileManager;
import com.loficostudios.minigameeventsplugin.managers.VoteManager;
import dev.jorel.commandapi.CommandAPICommand;
import net.kyori.adventure.text.Component;

import java.util.List;

import static com.loficostudios.minigameeventsplugin.AetherLabsPlugin.COMMAND_PREFIX;

public class PlayerCommand {

    private final ProfileManager profileManager;

    public PlayerCommand(ProfileManager profileManager) {
        this.profileManager = profileManager;
    }

    public List<CommandAPICommand> get() {
        return List.of(
                new CommandAPICommand("optout")
                        .executesPlayer((player, args) -> {

                            profileManager.getProfile(player.getUniqueId())
                                    .ifPresent(Profile::optOutOfGame);

                            player.sendMessage(Component.text(Messages.OPT_OUT));

                        }),

        new CommandAPICommand("shop")
                .withPermission(COMMAND_PREFIX + "shopAccess")
                .executesPlayer((player, args) -> {
                    new EventShop()
                            .open(player);
                }),

        new CommandAPICommand("vote")
                .withPermission(COMMAND_PREFIX + "vote")
                .executesPlayer((player, args) -> {

                    if (VoteManager.getInstance() == null) {
                        player.sendMessage(Component.text(Messages.UNABLE_TO_VOTE));
                        return;
                    }

                    new VoteGui().open(player);

                })
        );
    }
}
