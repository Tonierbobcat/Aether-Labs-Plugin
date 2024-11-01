package com.loficostudios.minigameeventsplugin.GameEvents.PlayerEvents;

import com.loficostudios.minigameeventsplugin.GameEvents.RandomPlayerSelectorEvent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

import static com.loficostudios.minigameeventsplugin.GameEvents.Items.SPOOKY_PUMPKIN;

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
    public @NotNull String getWarningMessage() {
        return "player(s) will become spooky.";
    }

    @Override
    public @NotNull Material getDisplayMaterial() {
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
