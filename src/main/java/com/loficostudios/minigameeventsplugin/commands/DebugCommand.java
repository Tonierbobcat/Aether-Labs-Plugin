package com.loficostudios.minigameeventsplugin.commands;

import com.loficostudios.minigameeventsplugin.AetherLabsPlugin;
import com.loficostudios.minigameeventsplugin.game.Game;
import com.loficostudios.minigameeventsplugin.game.GameManager;
import com.loficostudios.minigameeventsplugin.game.arena.GameArena;
import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.arguments.LiteralArgument;
import dev.jorel.commandapi.executors.CommandArguments;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import static com.loficostudios.minigameeventsplugin.AetherLabsPlugin.COMMAND_PREFIX;
import static com.loficostudios.minigameeventsplugin.game.Game.GAME_COUNTDOWN;

public class DebugCommand implements Command {

    private final GameManager gameManager;

    public DebugCommand(GameManager gameManager) {
        this.gameManager = gameManager;
    }

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
        var plugin = AetherLabsPlugin.getInstance();
        var activeGame = plugin.getActiveGame(player.getWorld());

        if (activeGame == null || !activeGame.inProgress()) {
            Integer countdown = (Integer) args.get("countdown");
            var game = new Game(plugin, new GameArena(plugin, plugin.getArenaManager().getConfig(player.getWorld())));
            gameManager.startCountdown(game, countdown != null ? countdown : GAME_COUNTDOWN);
        } else {
            player.sendMessage(Component.text("§cGame running! End it first."));
        }
    }
}
