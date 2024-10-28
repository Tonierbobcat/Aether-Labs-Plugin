package com.loficostudios.minigameeventsplugin.GameEvents.PlayerEvents;

import com.loficostudios.minigameeventsplugin.GameEvents.RandomPlayerSelectorEvent;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

import static com.loficostudios.minigameeventsplugin.Items.SPOOKY_PUMPKIN;

public class PlayerSpookyEvent extends RandomPlayerSelectorEvent {


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
    public @NotNull String warningMessage() {
        return "1 player will become spooky.";
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
