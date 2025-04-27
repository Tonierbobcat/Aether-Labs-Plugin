package com.loficostudios.minigameeventsplugin.game.events.PlayerEvents;

import com.loficostudios.minigameeventsplugin.api.PlayerSelectorEvent;
import com.loficostudios.minigameeventsplugin.game.Game;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

import static com.loficostudios.minigameeventsplugin.game.events.Items.SPOOKY_PUMPKIN;

public class PlayerSpookyEvent extends PlayerSelectorEvent {

    public PlayerSpookyEvent() {
        super("spooky pumpkin", Material.CARVED_PUMPKIN, 1, 2);
    }

    @Override
    public boolean onSelect(Game game, Player selectedPlayer) {
        EntityEquipment equipment = selectedPlayer.getEquipment();
        equipment.setHelmet(SPOOKY_PUMPKIN);
        return true;
    }

    @Override
    public @NotNull String getWarningMessage() {
        return "player(s) will become spooky.";
    }
}
