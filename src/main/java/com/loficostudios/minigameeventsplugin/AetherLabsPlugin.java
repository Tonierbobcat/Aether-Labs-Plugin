package com.loficostudios.minigameeventsplugin;

import com.earth2me.essentials.IEssentials;
import com.loficostudios.melodyapi.MelodyPlugin;
import com.loficostudios.melodyapi.utils.SimpleColor;

import com.loficostudios.minigameeventsplugin.commands.ArenaCommand;
import com.loficostudios.minigameeventsplugin.commands.PlayerCommand;
import com.loficostudios.minigameeventsplugin.config.ArenaConfig;
import com.loficostudios.minigameeventsplugin.arena.GameArena;
import com.loficostudios.minigameeventsplugin.api.BaseEvent;
import com.loficostudios.minigameeventsplugin.gameEvents.PlateEvents.*;
import com.loficostudios.minigameeventsplugin.gameEvents.PlayerEvents.*;
import com.loficostudios.minigameeventsplugin.gameEvents.PlayerEvents.PlayerTreeEvent;
import com.loficostudios.minigameeventsplugin.gameEvents.WorldEvents.WorldGhastEvent;
import com.loficostudios.minigameeventsplugin.gameEvents.WorldEvents.WorldPlateRepairEvent;
import com.loficostudios.minigameeventsplugin.listeners.*;
import com.loficostudios.minigameeventsplugin.managers.VoteManager;
import com.loficostudios.minigameeventsplugin.Profile.Profile;
import com.loficostudios.minigameeventsplugin.utils.Debug;
import com.loficostudios.minigameeventsplugin.gui.EventShop;
import com.loficostudios.minigameeventsplugin.managers.EventManager;
import com.loficostudios.minigameeventsplugin.managers.GameManager.GameManager;
import com.loficostudios.minigameeventsplugin.managers.ProfileManager;
import com.loficostudios.minigameeventsplugin.placeholders.MiniGamePlaceholder;
import com.loficostudios.minigameeventsplugin.utils.Selection;
import com.loficostudios.minigameeventsplugin.utils.WorldUtils;
import com.loficostudios.minigameeventsplugin.gui.VoteGui;
import com.loficostudios.minigameeventsplugin.config.Messages;
import dev.jorel.commandapi.*;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.arguments.LiteralArgument;
import dev.jorel.commandapi.arguments.LocationArgument;
import dev.jorel.commandapi.arguments.LocationType;
import lombok.Getter;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.loficostudios.minigameeventsplugin.arena.GameArena.MIN_GAME_ARENA_AREA;
import static com.loficostudios.minigameeventsplugin.managers.GameManager.GameManager.GAME_COUNTDOWN;
import static com.loficostudios.minigameeventsplugin.utils.Debug.logWarning;

public final class AetherLabsPlugin extends MelodyPlugin<AetherLabsPlugin> {

    @Getter
    public static AetherLabsPlugin instance;

    public static final boolean DEBUG_ENABLED = false;
    public static final String COMMAND_PREFIX = "randomEventsPlugin.";

    //region Variables

    @Getter
    private Collection<Player> onlinePlayers = new ArrayList<>();

    @Getter
    private ArenaConfig arenaConfig = new ArenaConfig(this);

    @Getter
    private final ProfileManager profileManager = new ProfileManager();
    @Getter
    private final EventManager eventManager = new EventManager();
    @Getter
    private final GameManager gameManager = new GameManager(this);

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
    public void onEnable() {
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
            this.getPluginLoader().disablePlugin(this);
        }

        registerListeners();
        registerGameEvents();
    }

    @Override
    public void onDisable() {
        //instance = null;

        GameArena arena = gameManager.getArena();

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
        new PlayerCommand(profileManager).get()
                .forEach(ExecutableCommand::register);

        new ArenaCommand(gameManager).get()
                .register();

        new CommandTree("start")
                .withPermission(COMMAND_PREFIX + "admin")
                .then(new LiteralArgument("countdown")
                        .then(new IntegerArgument("countdown", 3)
                                .executesPlayer((player, args) -> {
                                    if (!gameManager.inProgress()) {
                                        Integer countdown = (Integer) args.get("countdown");
                                        if (gameManager.startCountdown(countdown != null ? countdown : GAME_COUNTDOWN)) {
                                            player.sendMessage(SimpleColor.deserialize("&aSuccessfully started countdown!"));
                                        }
                                        else {
                                            player.sendMessage(SimpleColor.deserialize("&cArena is not setup! /arena set <pos1> <pos2>"));
                                        }
                                    } else {
                                        player.sendMessage(SimpleColor.deserialize("&cGame running! End it first."));
                                    }
                                }))
                );
    }

    private void registerGameEvents() {
        List.of(
                //region Player Events
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
                new PlayerTreeEvent(),
                //endregion

                //region Plate Events
                new PlateDirtEvent(),
                new PlateExpandEvent(),
                new PlateIcyEvent(),
                new PlateInvisibleEvent(),
                new PlateLightningEvent(),
                new PlateObsidianEvent(),
                new PlatePigsEvent(),
                new PlateShrinkEvent(),
                new PlateSlimeEvent(),
                new PlateZombieEvent(),
                //endregion

                //region World Events
                new WorldGhastEvent(),
                new WorldPlateRepairEvent()
                //endregion

        ).forEach(BaseEvent::register);
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
