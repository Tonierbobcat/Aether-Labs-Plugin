package com.loficostudios.minigameeventsplugin.commands;

import com.loficostudios.minigameeventsplugin.AetherLabsPlugin;
import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.arguments.LiteralArgument;
import dev.jorel.commandapi.executors.CommandArguments;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import static com.loficostudios.minigameeventsplugin.AetherLabsPlugin.COMMAND_PREFIX;
import static com.loficostudios.minigameeventsplugin.game.Game.GAME_COUNTDOWN;

public class DebugCommand implements Command {

    @Override
    public void register() {
        new CommandTree("start")
                .withPermission(COMMAND_PREFIX + "admin")

                .then(new LiteralArgument("countdown")
                        .then(new IntegerArgument("countdown", 3)
                                .executesPlayer(this::start))

                ).register();
    }

    private void start(Player player, CommandArguments args) {
        var activeGame = AetherLabsPlugin.getInstance().getActiveGame(player.getWorld());
        if (!activeGame.inProgress()) {
            Integer countdown = (Integer) args.get("countdown");
            if (activeGame.startCountdown(countdown != null ? countdown : GAME_COUNTDOWN)) {
                player.sendMessage(Component.text("§aSuccessfully started countdown!"));
            }
            else {
                player.sendMessage(Component.text("§cArena is not setup! /arena set <pos1> <pos2>"));
            }
        } else {
            player.sendMessage(Component.text("§cGame running! End it first."));
        }
    }
}
