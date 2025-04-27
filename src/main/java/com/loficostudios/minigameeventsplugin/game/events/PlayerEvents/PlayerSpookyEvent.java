package com.loficostudios.minigameeventsplugin.game.events.PlayerEvents;

import com.loficostudios.minigameeventsplugin.api.PlayerSelectorEvent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

import static com.loficostudios.minigameeventsplugin.game.events.Items.SPOOKY_PUMPKIN;

public class PlayerSpookyEvent extends PlayerSelectorEvent {


    @Override
    public boolean onSelect(Player selectedPlayer) {

        EntityEquipment equipment = selectedPlayer.getEquipment();

        if (equipment != null) {
            equipment.setHelmet(SPOOKY_PUMPKIN);
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public void onComplete(Collection<Player> selectedPlayers) {

    }





    @Override
    public @NotNull String getName() {
        return "spooky pumpkin";
    }

    @Override
    public @NotNull String getWarningMessage() {
        return "player(s) will become spooky.";
    }

    @Override
    public @NotNull Material getIcon() {
        return Material.CARVED_PUMPKIN;
    }

    @Override
    public Integer getMin() {
        return 1;
    }

    @Override
    public Integer getMax() {
        return 1;
    }
}
