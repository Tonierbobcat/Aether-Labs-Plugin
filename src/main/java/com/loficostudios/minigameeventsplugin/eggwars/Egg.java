package com.loficostudios.minigameeventsplugin.eggwars;

import com.loficostudios.melodyapi.utils.SimpleColor;
import com.loficostudios.minigameeventsplugin.managers.PlayerManager.PlayerManager;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class Egg {

    PlayerManager playerManager;

    @Getter
    final private Block block;

    @Getter
    private Location location;
    private static final Sound EGG_BREAK_SOUND = Sound.ENTITY_ENDER_DRAGON_GROWL;

    public void breakEgg() {

        if (block.getType().equals(Material.DRAGON_EGG)) {
            block.setType(Material.AIR);
        }

        player.playSound(player.getLocation(), EGG_BREAK_SOUND, 1, 1);

        player.sendMessage(SimpleColor.deserialize(
                "&cYour egg broke! You can no longer respawn"
        ));

    }

    public Egg(@NotNull Player player, Block block, PlayerManager playerManager) {

        this.player = player;

        this.block = block;

        this.location = block.getLocation();

        block.setType(Material.DRAGON_EGG);
        this.playerManager = playerManager;
    }


    @Getter
    @NotNull
    private final Player player;

    private static final Sound RESPAWN_SOUND = Sound.ITEM_TOTEM_USE;

    public void Spawn() {
        playerManager.teleportPlayer(player, new Vector(
                block.getX(),
                block.getY() + 1,
                block.getZ()));

        block.getWorld().playSound(block.getLocation(),
                RESPAWN_SOUND,
                1,
                1);
    }

    public boolean isBlockBelowEgg(Block block) {

        return block.getRelative(BlockFace.DOWN).equals(block);
    }
}
