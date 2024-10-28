package com.loficostudios.minigameeventsplugin;

import com.earth2me.essentials.IEssentials;
import com.loficostudios.melodyapi.libs.boostedyaml.YamlDocument;
import com.loficostudios.melodyapi.utils.Common;
import com.loficostudios.melodyapi.utils.SimpleDocument;

import com.loficostudios.minigameeventsplugin.GameEvents.BaseEvent;
import com.loficostudios.minigameeventsplugin.GameEvents.PlateEvents.*;
import com.loficostudios.minigameeventsplugin.GameEvents.PlayerEvents.*;
import com.loficostudios.minigameeventsplugin.GameEvents.WorldEvents.WorldGhastEvent;
import com.loficostudios.minigameeventsplugin.GameEvents.WorldEvents.WorldPlateRepairEvent;
import com.loficostudios.minigameeventsplugin.Listeners.MiniGameListener;
import com.loficostudios.minigameeventsplugin.Listeners.PlayerListener;
import com.loficostudios.minigameeventsplugin.Managers.EventManager;
import com.loficostudios.minigameeventsplugin.Managers.GameManager;
import com.loficostudios.minigameeventsplugin.Managers.ProfileManager;
import com.loficostudios.minigameeventsplugin.Placeholders.MiniGamePlaceholder;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.arguments.LocationArgument;
import dev.jorel.commandapi.arguments.LocationType;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.loficostudios.minigameeventsplugin.Managers.GameManager.GAME_COUNTDOWN;

public final class MiniGameEventsPlugin extends JavaPlugin {

    //region Variables
    @Getter
    static MiniGameEventsPlugin instance;

    @Getter Collection<Player> onlinePlayers = new ArrayList<>();
    @Getter YamlDocument arenaConfig;

    @Getter final ProfileManager profileManager = new ProfileManager();
    @Getter final EventManager eventManager = new EventManager();
    @Getter final GameManager gameManager = new GameManager(this, eventManager, profileManager);

    @Getter IEssentials essentials;



    public boolean papiHook = false;
    public boolean essentialsHook = false;
    //endregion

    @Override
    public void onEnable() {
        instance = this;

        loadConfigs();

        hookEssentials();
        hookPlaceHolderAPI();

        if (papiHook)
            new MiniGamePlaceholder(profileManager).register();

        registerCommands();
        registerListeners();
        registerGameEvents();

        gameManager.createGameArena();
    }

    @Override
    public void onDisable() {
        instance = null;
    }

    private void loadConfigs() {
        arenaConfig = SimpleDocument.create(this, "arena-config.yml");


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
    //endregion

    //region Register Functions
    private void registerCommands() {
        new CommandAPICommand("updatePos")
                .withArguments(new LocationArgument("pos1", LocationType.BLOCK_POSITION),
                        new LocationArgument("pos2", LocationType.BLOCK_POSITION))
                .executesPlayer((player, args) -> {
                    Location pos1 = (Location) args.get("pos1");
                    Location pos2 = (Location) args.get("pos2");
                    gameManager.setGameArena(pos1, pos2);
                    Common.broadcast("Updated area to " + gameManager.getArena().getPos1() + ", " + gameManager.getArena().getPos2());
                })
                .register();


        new CommandAPICommand("fillArena")
                .executesPlayer((player, args) -> {

                    gameManager.getArena().startLevelFillTask();

                }).register();

        new CommandAPICommand("clearArena")
                .executesPlayer((player, args) -> {

                    //gameManager.getArena().startFillLavaTask();
                    gameManager.getArena().clear();
                }).register();

        new CommandAPICommand("start")
                .withArguments(new IntegerArgument("countdown", 3))
                .executesPlayer((player, args) -> {
                    if (!gameManager.inProgress()) {

                        Integer countdown = (Integer) args.get("countdown");

                        gameManager.startGameCountdown(countdown != null ? countdown : GAME_COUNTDOWN);
                    } else {
                        player.sendMessage("Game running! End it first.");
                    }
                })
                .register();
        new CommandAPICommand("stats")
                .executesPlayer((player, args) -> {



                    profileManager
                            .getProfile(player.getUniqueId())
                            .ifPresent(profile -> player.sendMessage("wins: " + profile.getWins() + " kills: " + profile.getKills() + " deaths: " + profile.getDeaths()));



                })
                .register();
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

    private void registerListeners() {
        List.of(
                new MiniGameListener(gameManager),
                new PlayerListener(profileManager)
        ).forEach(listener -> Bukkit.getPluginManager().registerEvents(listener, this));
    }
    //endregion
}
