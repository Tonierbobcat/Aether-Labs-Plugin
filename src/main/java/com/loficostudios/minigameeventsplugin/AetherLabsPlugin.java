package com.loficostudios.minigameeventsplugin;

import com.earth2me.essentials.IEssentials;
import com.loficostudios.melodyapi.MelodyPlugin;
import com.loficostudios.minigameeventsplugin.api.event.GameEvent;
import com.loficostudios.minigameeventsplugin.arena.GameArena;
import com.loficostudios.minigameeventsplugin.commands.ArenaCommand;
import com.loficostudios.minigameeventsplugin.commands.PlayerCommand;
import com.loficostudios.minigameeventsplugin.config.ArenaConfig;
import com.loficostudios.minigameeventsplugin.game.Game;
import com.loficostudios.minigameeventsplugin.game.events.player.*;
import com.loficostudios.minigameeventsplugin.game.events.plate.*;
import com.loficostudios.minigameeventsplugin.game.events.world.WorldGhastEvent;
import com.loficostudios.minigameeventsplugin.game.events.world.WorldPlateRepairEvent;
import com.loficostudios.minigameeventsplugin.listeners.*;
import com.loficostudios.minigameeventsplugin.managers.EventRegistry;
import com.loficostudios.minigameeventsplugin.placeholders.MiniGamePlaceholder;
import com.loficostudios.minigameeventsplugin.player.profile.ProfileManager;
import com.loficostudios.minigameeventsplugin.utils.Debug;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.ExecutableCommand;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.arguments.LiteralArgument;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static com.loficostudios.minigameeventsplugin.game.Game.GAME_COUNTDOWN;
import static com.loficostudios.minigameeventsplugin.utils.Debug.logWarning;

public final class AetherLabsPlugin extends MelodyPlugin<AetherLabsPlugin> {

    @Getter
    public static AetherLabsPlugin instance;

    public static final boolean DEBUG_ENABLED = true;
    public static final String COMMAND_PREFIX = "randomEventsPlugin.";

    //region Variables

    @Getter
    private Collection<Player> onlinePlayers = new ArrayList<>();

    @Getter
    private ArenaConfig arenaConfig = new ArenaConfig(this);

    @Getter
    private final ProfileManager profileManager = new ProfileManager();

    @Getter
    private final EventRegistry events = new EventRegistry();

    @Getter
    private final Game activeGame = new Game(this);

    @Getter
    private IEssentials essentials;

    @Getter
    private Economy economy;

    public boolean papiHook = false;
    public boolean essentialsHook = false;
    public boolean vaultHook = false;

    //endregion

    public AetherLabsPlugin() {
        instance = this;
    }

    @Override
    public void onLoad() {
        loadConfigs();
    }

    @Override
    protected void onStart() {
        CommandAPI.onEnable();

        if (!setupEconomy()) {
            logWarning("Could not load vault");
        }

        hookEssentials();
        hookPlaceHolderAPI();

        if (papiHook)
            new MiniGamePlaceholder(profileManager).register();

        try {
            registerCommands();
        } catch (Exception e) {
            Debug.logError("Could not load commands. Disabling...");
            getServer().getPluginManager().disablePlugin(this);
        }

        registerListeners();
        registerGameEvents();
    }

    @Override
    public void onDisable() {
        //instance = null;

        GameArena arena = activeGame.getArena();

        if (arena != null) {
            arena.clear();
            arena.removeEntities();
        }
    }

    private void loadConfigs() {
        CommandAPIBukkitConfig config = new CommandAPIBukkitConfig(this);
        config.usePluginNamespace();
        config.silentLogs(true);

        CommandAPI.onLoad(config);
    }

    //region Plugin Hooks
    public void hookEssentials() {
        essentials = (IEssentials) Bukkit.getPluginManager().getPlugin("Essentials");
        essentialsHook = essentials != null;
        if (essentialsHook) {
            getLogger().info("Essentials hooked successfully.");
        } else {
            getLogger().warning("Essentials not found. Plugin features may be limited.");
        }
    }

    public void hookPlaceHolderAPI() {
        papiHook = Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null;
        if (essentialsHook) {
            getLogger().info("PlaceholderAPI hooked successfully.");
        } else {
            getLogger().warning("PlaceholderAPI not found. Plugin features may be limited.");
        }
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        vaultHook = true;
        economy = rsp.getProvider();
        return true;
    }
    //endregion

    private void registerCommands() {
        new PlayerCommand(profileManager, this).get()
                .forEach(ExecutableCommand::register);

        new ArenaCommand(activeGame).get()
                .register();

        new CommandTree("start")
                .withPermission(COMMAND_PREFIX + "admin")
                .then(new LiteralArgument("countdown")
                        .then(new IntegerArgument("countdown", 3)
                                .executesPlayer((player, args) -> {
                                    if (!activeGame.inProgress()) {
                                        Integer countdown = (Integer) args.get("countdown");
                                        if (activeGame.startCountdown(countdown != null ? countdown : GAME_COUNTDOWN)) {
                                            player.sendMessage(Component.text("§aSuccessfully started countdown!"));
                                        }
                                        else {
                                            player.sendMessage(Component.text("§cArena is not setup! /arena set <pos1> <pos2>"));
                                        }
                                    } else {
                                        player.sendMessage(Component.text("§cGame running! End it first."));
                                    }
                                }))
                );
    }

    private void registerGameEvents() {
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
                new WorldPlateRepairEvent()
        ).forEach(GameEvent::register);
    }
    private void registerListeners() {
        List.of(
                new PlayerDeathListener(activeGame),
                new EggListener(activeGame),
                new ArenaListener(activeGame),
                new MiniGameListener(this, activeGame),
                new PlayerListener(this, profileManager)
        ).forEach(listener -> Bukkit.getPluginManager().registerEvents(listener, this));
    }
}
