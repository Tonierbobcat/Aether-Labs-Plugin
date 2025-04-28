package com.loficostudios.minigameeventsplugin.commands;

import com.loficostudios.minigameeventsplugin.AetherLabsPlugin;
import com.loficostudios.minigameeventsplugin.config.Messages;
import com.loficostudios.minigameeventsplugin.gui.EventShop;
import com.loficostudios.minigameeventsplugin.gui.VoteGui;
import com.loficostudios.minigameeventsplugin.player.profile.PlayerProfile;
import com.loficostudios.minigameeventsplugin.player.profile.ProfileManager;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.executors.CommandArguments;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import static com.loficostudios.minigameeventsplugin.AetherLabsPlugin.COMMAND_PREFIX;

public class PlayerCommand implements Command {

    private final ProfileManager profileManager;

    private final AetherLabsPlugin plugin;

    public PlayerCommand(ProfileManager profileManager, AetherLabsPlugin plugin) {
        this.profileManager = profileManager;
        this.plugin = plugin;
    }

    @Override
    public void register() {
        new CommandAPICommand("shop")
                .withPermission(COMMAND_PREFIX + "shopAccess")
                .executesPlayer(this::shop).register();

        new CommandAPICommand("vote")
                .withPermission(COMMAND_PREFIX + "vote")
                .executesPlayer(this::vote).register();

        new CommandAPICommand("optout")
                .executesPlayer(this::optOut)
                .register();
    }

    private void optOut(Player player, CommandArguments args) {

        profileManager.getProfile(player.getUniqueId())
                .ifPresent(PlayerProfile::optOutOfGame);

        player.sendMessage(Component.text(Messages.OPT_OUT));

    }

    private void vote(Player player, CommandArguments args) {
        if (plugin.getActiveGame(player.getWorld()).getVoting() == null) {
            player.sendMessage(Component.text(Messages.UNABLE_TO_VOTE));
            return;
        }

        new VoteGui().open(player);
    }
    private void shop(Player player, CommandArguments args) {
        new EventShop().open(player);
    }
}
