package com.loficostudios.minigameeventsplugin.eggwars;

import com.loficostudios.minigameeventsplugin.arena.GameArena;
import com.loficostudios.minigameeventsplugin.arena.SpawnPlatform;
import com.loficostudios.minigameeventsplugin.api.event.impl.AbstractGameMode;
import com.loficostudios.minigameeventsplugin.game.Game;
import com.loficostudios.minigameeventsplugin.game.GameState;
import com.loficostudios.minigameeventsplugin.utils.Selection;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class EggWarsMode extends AbstractGameMode {

    private final Game gameManager;

    public EggWarsMode(Game gameManager) {
        this.gameManager = gameManager;
    }

    public Collection<Egg> getEggs() {
        return spawns.values();
    }

    @Override
    public String getName() {
        return "Egg Wars";
    }

    @Override
    public Material getIcon() {
        return Material.DRAGON_EGG;
    }

    @Override
    public void start() {

    }

    @Override
    public void end() {
        spawns.clear();
    }

    @Override
    public void reset() {

    }

    @Override
    public void prepareResources(Collection<Player> participatingPlayers) {

    }

    Map<UUID, Egg> spawns = new HashMap<>();


    public void handleEggBreak(BlockBreakEvent e, @NotNull Egg egg) {

        if (gameManager.getCurrentState().equals(GameState.SETUP))
            return;

        removeEgg(egg);

    }

    public void removeEgg(@NotNull Egg egg) {

        if (spawns.containsValue(egg)) {
            egg.breakEgg();
            spawns.remove(egg.getPlayer().getUniqueId());
        }
    }



    public @Nullable Egg getEgg(@NotNull Player player) {

        return spawns.get(player.getUniqueId());
    }

    public @Nullable Egg getEgg(@NotNull Block block) {

        for (Egg egg : spawns.values()) {

            if (egg.getLocation().toVector().equals(block.getLocation().toVector()))
                return egg;
        }

        return null;
    }

    @Override
    public void initializeCore(Collection<Player> participatingPlayers) {

        GameArena arena = gameManager.getArena();

        Selection bounds = new Selection(arena.getPos1(), arena.getPos2());

        for (Player player : participatingPlayers) {
            gameManager.getArena().addSpawnPlatform(
                    player,
                    null,
                    null,
                    null,
                    spawnPlatform -> {


                        Location loc = spawnPlatform.getLocation();

                        World world = loc.getWorld();

                        if(world != null) {
                            Block block = bounds.getBlock(loc.getBlockX(), loc.getBlockY() + 1, loc.getBlockZ());

                            if (block != null) {
                                Egg egg = new Egg(player, block, gameManager.getPlayers());

                                spawns.put(player.getUniqueId(), egg);
                                player.sendMessage("Added egg");
                            }
                            else {
                                Bukkit.getLogger().severe("egg is null");
//                                Common.broadcast();
                            }
                        }

                        spawnPlatform.teleportCenter();
                    },
                    SpawnPlatform.SpawnAlgorithm.EQUAL_HEIGHT);
        }
    }

    public boolean hasSpawn(@NotNull Player player) {

        Egg egg = spawns.get(player.getUniqueId());

        return egg != null;
    }

    @Override
    public void finalizeSetup(Collection<Player> participatingPlayers) {
    }
}
