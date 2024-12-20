package com.loficostudios.minigameeventsplugin.commands;

import com.loficostudios.melodyapi.utils.SimpleColor;
import com.loficostudios.minigameeventsplugin.Profile.Profile;
import com.loficostudios.minigameeventsplugin.config.Messages;
import com.loficostudios.minigameeventsplugin.gui.EventShop;
import com.loficostudios.minigameeventsplugin.gui.VoteGui;
import com.loficostudios.minigameeventsplugin.managers.GameManager.GameManager;
import com.loficostudios.minigameeventsplugin.managers.ProfileManager;
import com.loficostudios.minigameeventsplugin.managers.VoteManager;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.arguments.LiteralArgument;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import java.util.List;

import static com.loficostudios.minigameeventsplugin.AetherLabsPlugin.COMMAND_PREFIX;
import static com.loficostudios.minigameeventsplugin.managers.GameManager.GameManager.GAME_COUNTDOWN;

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

                            player.sendMessage(SimpleColor.deserialize(
                                    Messages.OPT_OUT
                            ));

                        }),

        new CommandAPICommand("shop")
                .withPermission(COMMAND_PREFIX + "shopAccess")
                .executesPlayer((player, args) -> {

                    new EventShop(player).open(player);

                }),

        new CommandAPICommand("vote")
                .withPermission(COMMAND_PREFIX + "vote")
                .executesPlayer((player, args) -> {

                    if (VoteManager.getInstance() == null) {
                        player.sendMessage(SimpleColor.deserialize(
                                Messages.UNABLE_TO_VOTE
                        ));
                        return;
                    }

                    new VoteGui().open(player);

                })
        );
    }
}
