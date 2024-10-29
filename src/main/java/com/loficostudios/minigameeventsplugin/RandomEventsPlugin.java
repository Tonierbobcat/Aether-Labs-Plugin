package com.loficostudios.minigameeventsplugin;

import com.earth2me.essentials.IEssentials;
import com.loficostudios.melodyapi.utils.Common;
import com.loficostudios.melodyapi.utils.SimpleColor;

import com.loficostudios.minigameeventsplugin.Config.ArenaConfig;
import com.loficostudios.minigameeventsplugin.GameArena.GameArena;
import com.loficostudios.minigameeventsplugin.GameEvents.BaseEvent;
import com.loficostudios.minigameeventsplugin.GameEvents.PlateEvents.*;
import com.loficostudios.minigameeventsplugin.GameEvents.PlayerEvents.*;
import com.loficostudios.minigameeventsplugin.GameEvents.WorldEvents.WorldGhastEvent;
import com.loficostudios.minigameeventsplugin.GameEvents.WorldEvents.WorldPlateRepairEvent;
import com.loficostudios.minigameeventsplugin.Gui.EventShop;
import com.loficostudios.minigameeventsplugin.Listeners.MiniGameListener;
import com.loficostudios.minigameeventsplugin.Listeners.PlayerListener;
import com.loficostudios.minigameeventsplugin.Managers.EventManager;
import com.loficostudios.minigameeventsplugin.Managers.GameManager.GameManager;
import com.loficostudios.minigameeventsplugin.Managers.ProfileManager;
import com.loficostudios.minigameeventsplugin.Placeholders.MiniGamePlaceholder;
import com.loficostudios.minigameeventsplugin.Utils.Selection;
import com.loficostudios.minigameeventsplugin.Utils.WorldUtils;
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
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.loficostudios.minigameeventsplugin.Managers.GameManager.GameManager.GAME_COUNTDOWN;
import static com.loficostudios.minigameeventsplugin.Utils.DebugUtil.debugWarning;

public final class RandomEventsPlugin extends JavaPlugin {

    public static final boolean DEBUG_ENABLED = false;
    private static final String COMMAND_PREFIX = "randomEventsPlugin.";
    private static final int MIN_GAME_ARENA_AREA = 146068;
    //region Variables
    @Getter
    static RandomEventsPlugin instance;

    @Getter Collection<Player> onlinePlayers = new ArrayList<>();


    @Getter
    private ArenaConfig arenaConfig = new ArenaConfig(this);

    @Getter private final ProfileManager profileManager = new ProfileManager();
    @Getter private final EventManager eventManager = new EventManager();
    @Getter private final GameManager gameManager = new GameManager(this);

    @Getter private IEssentials essentials;

    @Getter private Economy economy;

    public boolean papiHook = false;
    public boolean essentialsHook = false;
    public boolean vaultHook = false;



    //endregion


    @Override
    public void onLoad() {
        loadConfigs();
    }

    @Override
    public void onEnable() {
        instance = this;

        CommandAPI.onEnable();



        if (!setupEconomy()) {
            debugWarning("Could not load vault");
        }

        hookEssentials();
        hookPlaceHolderAPI();

        if (papiHook)
            new MiniGamePlaceholder(profileManager).register();

        registerCommands();
        registerListeners();
        registerGameEvents();

        //DEBUG WITH THIS BEFORE REGISTERCOMMANDS
        //gameManager.createGameArena();

    }

    @Override
    public void onDisable() {
        instance = null;

        GameArena arena = gameManager.getArena();

        if (arena != null) {
            arena.clear();
            arena.removeMobs();
        }

    }

    private void loadConfigs() {
        CommandAPIBukkitConfig config = new CommandAPIBukkitConfig(this);
        config.usePluginNamespace();
        config.silentLogs(true);

        CommandAPI.onLoad(config);

//        try {
//            arenaConfig = new ArenaConfig(this);
//        } catch (Exception e) {
//            debugWarning("Could not INIT arenaConfig");
//        }
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



    private void registerCommands() {

        new CommandAPICommand("shop")
                .withPermission(COMMAND_PREFIX + "shopAccess")
                .executesPlayer((player, args) -> {
                    new EventShop(player).open(player);
                }).register();

        new CommandTree("arena")
                .withPermission(COMMAND_PREFIX + "admin")
                .then(new LiteralArgument("set")
                    .then(new LocationArgument("pos1", LocationType.BLOCK_POSITION)
                            .then(new LocationArgument("pos2", LocationType.BLOCK_POSITION)
                            .executesPlayer((player, args) -> {
                                Location pos1 = (Location) args.get("pos1");
                                Location pos2 = (Location) args.get("pos2");

                                if (pos1 == null || pos2 == null) {
                                    player.sendMessage("&cSomething went wrong!");
                                    return;
                                }

                                Selection selection = new Selection(pos1, pos2);

                                int selectionBlockCount = selection.count();

                                if (selectionBlockCount >= MIN_GAME_ARENA_AREA) {
                                    gameManager.setGameArena(pos1, pos2);
                                    String pos1Msg = pos1.getBlockX() + ", " + pos1.getBlockY() + ", " + pos1.getBlockZ();
                                    String pos2Msg = pos2.getBlockX() + ", " + pos2.getBlockY() + ", " + pos2.getBlockZ();

                                    player.sendMessage(SimpleColor.deserialize("&aUpdated area to " + pos1Msg + " - " + pos2Msg + "!"));
                                }
                                else {
                                    player.sendMessage(SimpleColor.deserialize("&cCannot set arena. Selection too small! (" +selectionBlockCount+ " blocks)"));
                                }

                            }))))
                .then(new LiteralArgument("debug")
                        .then(new LiteralArgument("fill")
                                .then(new LiteralArgument("start")
                                        .executesPlayer((player, args) -> {
                                            if (!getGameManager().inProgress()) {

                                                GameArena arena = getGameManager().getArena();

                                                arena.startLevelFillTask(Material.DIAMOND_BLOCK, 1);
                                                player.sendMessage(SimpleColor.deserialize("&aStarted fill task successfully!"));
                                            }
                                            else {
                                                player.sendMessage(SimpleColor.deserialize("&cYou cannot start this task while the game is running!"));
                                            }
                                        })
                                ).then(new LiteralArgument("cancel")
                                        .executesPlayer(((player, args) -> {
                                            if (!getGameManager().inProgress()) {

                                                GameArena arena = gameManager.getArena();
                                                Selection selection = new Selection(arena.getPos1(), arena.getPos2());

                                                getGameManager().getArena().cancelLavaTask();
                                                WorldUtils.fillArea(selection, Material.AIR);

                                                player.sendMessage(SimpleColor.deserialize("&aCancel task successfully!"));
                                            }
                                            else {
                                                player.sendMessage(SimpleColor.deserialize("&cYou cannot cancel this task while the game is running!"));
                                            }
                                        }))))
                ).register(this);


        new CommandTree("start")
                .withPermission(COMMAND_PREFIX + "admin")
                .then(new LiteralArgument("countdown")
                        .then(new IntegerArgument("countdown", 3)
                            .executesPlayer((player, args) -> {
                                if (!gameManager.inProgress()) {

                                    Integer countdown = (Integer) args.get("countdown");

                                    gameManager.startGameCountdown(countdown != null ? countdown : GAME_COUNTDOWN);
                                    player.sendMessage(SimpleColor.deserialize("&aSuccessfully started countdown!"));

                                } else {
                                    player.sendMessage(SimpleColor.deserialize("&cGame running! End it first."));
                                }
                            }))
                ).register(this);



//        new CommandAPICommand("stats")
//                .executesPlayer((player, args) -> {
//
//
//
//                    profileManager
//                            .getProfile(player.getUniqueId())
//                            .ifPresent(profile -> player.sendMessage("wins: " + profile.getWins() + " kills: " + profile.getKills() + " deaths: " + profile.getDeaths()));
//
//                })
//                .register();
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

    private void registerListeners() {
        List.of(
                new MiniGameListener(gameManager),
                new PlayerListener(profileManager)
        ).forEach(listener -> Bukkit.getPluginManager().registerEvents(listener, this));
    }
    //endregion
}
