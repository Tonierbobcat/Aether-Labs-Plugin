package com.loficostudios.minigameeventsplugin.commands;

import com.loficostudios.minigameeventsplugin.arena.GameArena;
import com.loficostudios.minigameeventsplugin.game.Game;
import com.loficostudios.minigameeventsplugin.utils.Selection;
import com.loficostudios.minigameeventsplugin.utils.WorldUtils;
import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.arguments.LiteralArgument;
import dev.jorel.commandapi.arguments.LocationArgument;
import dev.jorel.commandapi.arguments.LocationType;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;

import static com.loficostudios.minigameeventsplugin.AetherLabsPlugin.COMMAND_PREFIX;
import static com.loficostudios.minigameeventsplugin.arena.GameArena.MIN_GAME_ARENA_AREA;

public class ArenaCommand {

    private final Game gameManager;

    public ArenaCommand(Game gameManager) {
        this.gameManager = gameManager;
    }

    public CommandTree get() {
        return new CommandTree("arena")
                .withPermission(COMMAND_PREFIX + "admin")
                .then(new LiteralArgument("set")
                        .then(new LocationArgument("pos1", LocationType.BLOCK_POSITION)
                                .then(new LocationArgument("pos2", LocationType.BLOCK_POSITION)
                                        .executesPlayer((player, args) -> {
                                            Location pos1 = (Location) args.get("pos1");
                                            Location pos2 = (Location) args.get("pos2");

                                            if (pos1 == null || pos2 == null) {
                                                player.sendMessage("&cSomething went wrong!");
                                                return;
                                            }

                                            Selection selection = new Selection(pos1, pos2);

                                            int selectionBlockCount = selection.count();

                                            if (selectionBlockCount >= MIN_GAME_ARENA_AREA) {
                                                gameManager.setGameArena(pos1, pos2);
                                                String pos1Msg = pos1.getBlockX() + ", " + pos1.getBlockY() + ", " + pos1.getBlockZ();
                                                String pos2Msg = pos2.getBlockX() + ", " + pos2.getBlockY() + ", " + pos2.getBlockZ();

                                                player.sendMessage(Component.text("&aUpdated area to " + pos1Msg + " - " + pos2Msg + "!"));
                                            }
                                            else {
                                                player.sendMessage(Component.text("&cCannot set arena. Selection too small! (" +selectionBlockCount+ " blocks)"));
                                            }

                                        }))))
                .then(new LiteralArgument("debug")
                        .then(new LiteralArgument("outline")
                                .executesPlayer((player, args) -> {

                                    GameArena arena = gameManager.getArena();

                                    Selection selection = new Selection(arena.getPos1(), arena.getPos2());

                                    Selection perimeter = selection.getPerimeter(1);

                                    perimeter.getBlocks().forEach(block -> {
                                        block.setType(Material.EMERALD_BLOCK);
                                    });
                                })
                        )
                        .then(new LiteralArgument("fill")
                                .then(new LiteralArgument("start")
                                        .executesPlayer((player, args) -> {
                                            if (!gameManager.inProgress()) {

                                                GameArena arena = gameManager.getArena();

                                                arena.startLevelFillTask(Material.DIAMOND_BLOCK, 1);
                                                player.sendMessage(Component.text("&aStarted fill task successfully!"));
                                            }
                                            else {
                                                player.sendMessage(Component.text("&cYou cannot start this task while the game is running!"));
                                            }
                                        })
                                ).then(new LiteralArgument("cancel")
                                        .executesPlayer(((player, args) -> {
                                            if (!gameManager.inProgress()) {

                                                GameArena arena = gameManager.getArena();
                                                Selection selection = new Selection(arena.getPos1(), arena.getPos2());

                                                gameManager.getArena().cancelLavaFillTask();
                                                WorldUtils.fillArea(selection, Material.AIR);

                                                player.sendMessage(Component.text("&aCancel task successfully!"));
                                            }
                                            else {
                                                player.sendMessage(Component.text("&cYou cannot cancel this task while the game is running!"));
                                            }
                                        }))))
                );
    }
}
