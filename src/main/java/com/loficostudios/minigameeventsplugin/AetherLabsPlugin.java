package com.loficostudios.minigameeventsplugin;

import com.github.retrooper.packetevents.PacketEvents;
import com.loficostudios.melodyapi.MelodyPlugin;
import com.loficostudios.melodyapi.file.impl.YamlFile;
import com.loficostudios.minigameeventsplugin.api.event.GameEvent;
import com.loficostudios.minigameeventsplugin.commands.ArenaCommand;
import com.loficostudios.minigameeventsplugin.commands.Command;
import com.loficostudios.minigameeventsplugin.commands.DebugCommand;
import com.loficostudios.minigameeventsplugin.commands.PlayerCommand;
import com.loficostudios.minigameeventsplugin.cosmetics.CosmeticModule;
import com.loficostudios.minigameeventsplugin.game.Game;
import com.loficostudios.minigameeventsplugin.game.GameManager;
import com.loficostudios.minigameeventsplugin.game.arena.ArenaManager;
import com.loficostudios.minigameeventsplugin.game.events.plate.*;
import com.loficostudios.minigameeventsplugin.game.events.player.*;
import com.loficostudios.minigameeventsplugin.game.events.world.WorldGhastEvent;
import com.loficostudios.minigameeventsplugin.game.events.world.WorldMeteoriteEvent;
import com.loficostudios.minigameeventsplugin.game.events.world.WorldPlateRepairEvent;
import com.loficostudios.minigameeventsplugin.listeners.*;
import com.loficostudios.minigameeventsplugin.managers.EventRegistry;
import com.loficostudios.minigameeventsplugin.player.profile.ProfileManager;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.util.Arrays;
import java.util.List;

public final class AetherLabsPlugin extends MelodyPlugin<AetherLabsPlugin> {

    private static AetherLabsPlugin instance;

    public static final boolean DEBUG_ENABLED = true;
    public static final String COMMAND_PREFIX = "randomEventsPlugin.";

    private final GameManager gameManager = new GameManager();

    @Getter
    private final ArenaManager arenaManager = new ArenaManager();

    @Getter
    private final ProfileManager profileManager = new ProfileManager();

    @Getter
    private final EventRegistry events = new EventRegistry();



//    private Game activeGame;

    public AetherLabsPlugin() {
        instance = instance == null ? this: instance;
    }

    public static AetherLabsPlugin inst() {
        return instance;
    }

    @Deprecated
    public Game getActiveGame(World world) {
        return gameManager.getGame(world);
    }

    @Override
    public void onLoad() {
        loadConfigs();
        PacketEvents.setAPI(SpigotPacketEventsBuilder.build(this));
        PacketEvents.getAPI().load();
    }

    @Override
    protected void onStart() {
        PacketEvents.getAPI().init();
        CommandAPI.onEnable();

        try {
            registerCommands();
        } catch (Exception e) {
            throw new IllegalArgumentException("Could not register commands");
        }

        this.arenaManager.initialize(new YamlFile("arena-config.yml", this));

        boolean hasCosmetics = true;
        if (hasCosmetics) {
            var a = CosmeticModule.onEnable(this);
        }

        registerListeners();
        registerEvents();
    }


    @Override
    public void onDisable() {
        gameManager.onDisable();
        PacketEvents.getAPI().terminate();
    }

    private void loadConfigs() {
        CommandAPIBukkitConfig config = new CommandAPIBukkitConfig(this);
        config.usePluginNamespace();
        config.silentLogs(true);

        CommandAPI.onLoad(config);
    }

    //endregion

    private void registerCommands() {
        Arrays.asList(
                new PlayerCommand(profileManager, this),
                        new ArenaCommand(),
                        new DebugCommand(gameManager)
        ).forEach(Command::register);
    }

    private void registerEvents() {
        // player events
        Arrays.asList(
                new PlayerBlindEvent(),
                new PlayerBlocksEvent(),
                new PlayerBowEvent(),
                new PlayerCakeEvent(),
                new PlayerColdEvent(),
                new PlayerFishingRodEvent(),
                new PlayerFloatEvent(),
                new PlayerFlowerEvent(),
                new PlayerGhostEvent(),
                new PlayerGravityEvent(),
                new PlayerHasteEvent(),
                new PlayerRandomPlateEvent(),
                new PlayerShearsEvent(),
                new PlayerSickEvent(),
                new PlayerSlowEvent(),
                new PlayerSpeedEvent(),
                new PlayerSpookyEvent(),
                new PlayerSwapEvent(),
                new PlayerSwordEvent(),
                new PlayerTreeEvent()
        ).forEach(GameEvent::register);

        // plate events
        Arrays.asList(
                new PlateDirtEvent(),
                new PlateExpandEvent(),
                new PlateIcyEvent(),
                new PlateInvisibleEvent(),
                new PlateLightningEvent(),
                new PlateObsidianEvent(),
                new PlatePigsEvent(),
                new PlateShrinkEvent(),
                new PlateSlimeEvent(),
                new PlateZombieEvent()
        ).forEach(GameEvent::register);

        // world events
        Arrays.asList(
                new WorldGhastEvent(),
                new WorldMeteoriteEvent(),
                new WorldPlateRepairEvent()
        ).forEach(GameEvent::register);
    }
    private void registerListeners() {
        List.of(
                new PlayerDeathListener(gameManager),
                new EggListener(gameManager),
                new ArenaListener(gameManager),
                new MiniGameListener(this, gameManager),
                new PlayerListener(this, profileManager)
        ).forEach(listener -> Bukkit.getPluginManager().registerEvents(listener, this));
    }
}
