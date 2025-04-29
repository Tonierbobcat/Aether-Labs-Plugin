package com.loficostudios.minigameeventsplugin.cosmetics;

import com.loficostudios.minigameeventsplugin.commands.Command;
import com.loficostudios.minigameeventsplugin.cosmetics.gui.CosmeticGui;
import com.loficostudios.minigameeventsplugin.cosmetics.listener.CosmeticManager;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.executors.CommandArguments;
import org.bukkit.entity.Player;

public class CosmeticsCommand implements Command {
    private final CosmeticManager manager;

    public CosmeticsCommand(CosmeticManager manager) {
        this.manager = manager;
    }

    @Override
    public void register() {
        new CommandAPICommand("cosmetics")
                .executesPlayer(this::openMenu)
                .register();
    }
    private void openMenu(Player player, CommandArguments args) {
        new CosmeticGui(manager).open(player);
    }
}
