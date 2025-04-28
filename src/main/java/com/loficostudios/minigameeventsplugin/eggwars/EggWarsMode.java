package com.loficostudios.minigameeventsplugin.eggwars;

import com.loficostudios.minigameeventsplugin.api.event.impl.AbstractGameMode;
import com.loficostudios.minigameeventsplugin.game.Game;
import com.loficostudios.minigameeventsplugin.game.GameState;
import com.loficostudios.minigameeventsplugin.game.arena.GameArena;
import com.loficostudios.minigameeventsplugin.game.arena.SpawnPlatformGenerator;
import com.loficostudios.minigameeventsplugin.utils.Selection;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EggWarsMode extends AbstractGameMode {

    public EggWarsMode() {
    }
//    private final Map<UUID, Egg> spawns = new HashMap<>();

    public Collection<Egg> getEggs(Game game) {
        var spawns = game.getPersistentData().computeIfAbsent("egg-wars-spawns", string -> new HashMap<UUID, Egg>());
        return ((Map<UUID, Egg>) spawns).values();
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
    }

    @Override
    public void reset() {

    }

    @Override
    public void prepareResources(Game game, Collection<Player> participatingPlayers) {

    }

    public void handleEggBreak(Game game, @NotNull Egg egg) {

        if (game.getCurrentState().equals(GameState.SETUP))
            return;

        removeEgg(game, egg);

    }

    public void removeEgg(Game game, @NotNull Egg egg) {
        var spawns = ((Map<UUID, Egg>) game.getPersistentData().computeIfAbsent("egg-wars-spawns", string -> new HashMap<UUID, Egg>()));

        if (spawns.containsValue(egg)) {
            egg.breakEgg();
            spawns.remove(egg.getPlayer().getUniqueId());
        }
    }

    public @Nullable Egg getEgg(Game game, @NotNull Player player) {
        var spawns = ((Map<UUID, Egg>) game.getPersistentData().computeIfAbsent("egg-wars-spawns", string -> new HashMap<UUID, Egg>()));

        return spawns.get(player.getUniqueId());
    }

    public @Nullable Egg getEgg(Game game, @NotNull Block block) {
        var spawns = ((Map<UUID, Egg>) game.getPersistentData().computeIfAbsent("egg-wars-spawns", string -> new HashMap<UUID, Egg>()));

        for (Egg egg : spawns.values()) {

            if (egg.getLocation().toVector().equals(block.getLocation().toVector()))
                return egg;
        }

        return null;
    }

    @Override
    public void initializeCore(Game game, Collection<Player> participatingPlayers) {
        var spawns = ((Map<UUID, Egg>) game.getPersistentData().computeIfAbsent("egg-wars-spawns", string -> new HashMap<UUID, Egg>()));

        GameArena arena = game.getArena();

        Selection bounds = arena.getBounds();

        for (Player player : participatingPlayers) {
            game.getArena().addSpawnPlatform(player, null, null, null, spawnPlatform -> {
                Location loc = spawnPlatform.getLocation();

                World world = loc.getWorld();

                if(world != null) {
                    Block block = bounds.getBlock(loc.getBlockX(), loc.getBlockY() + 1, loc.getBlockZ());

                    if (block != null) {
                        Egg egg = new Egg(player, block, game.getPlayerManager());

                        spawns.put(player.getUniqueId(), egg);
                        player.sendMessage("Added egg");
                    }
                    else {
                        Bukkit.getLogger().severe("egg is null");
                    }
                }

                spawnPlatform.teleport(player);
            }, SpawnPlatformGenerator.SpawnAlgorithm.EQUAL_HEIGHT);
        }
    }

    @Override
    public void finalizeSetup(Game game, Collection<Player> participatingPlayers) {
    }
}
