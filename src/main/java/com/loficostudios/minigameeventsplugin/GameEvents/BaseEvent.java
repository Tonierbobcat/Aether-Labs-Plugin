package com.loficostudios.minigameeventsplugin.GameEvents;


import com.loficostudios.melodyapi.melodygui.GuiIcon;
import com.loficostudios.melodyapi.utils.ItemCreator;
import com.loficostudios.melodyapi.utils.SimpleColor;
import com.loficostudios.minigameeventsplugin.Managers.GameManager.GameManager;
import com.loficostudios.minigameeventsplugin.Managers.PlayerManager.PlayerManager;
import com.loficostudios.minigameeventsplugin.AetherLabsPlugin;
import com.loficostudios.minigameeventsplugin.GameArena.GameArena;
import lombok.Getter;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.loficostudios.minigameeventsplugin.Utils.Debug.logError;
import static com.loficostudios.minigameeventsplugin.Utils.Debug.logWarning;

public abstract class BaseEvent {

    public static final int DEFAULT_WARNING_TIME = 3;
    public static final int DEFAULT_EVENT_DURATION = 3;

    private AetherLabsPlugin plugin;

    private GameManager gameManager;
    private PlayerManager playerManager;

    private @NotNull Double getPrice() {
        return 100.0;
    }

    @Getter
    protected final Collection<BukkitTask> tasks = new ArrayList<>();

    public abstract @NotNull String getName();
    public abstract @NotNull String getWarningMessage();
    public abstract @NotNull Material getDisplayMaterial();

    protected GameArena getArena() {
        return gameManager.getArena();
    }

    protected GameManager getGameManager() {
        return this.gameManager;
    }

    protected PlayerManager getPlayerManager() {
        return this.playerManager;
    }

    public abstract @NotNull EventType getType();

    public @NotNull Integer getWarningTime() {
        return DEFAULT_WARNING_TIME;
    }

    public @NotNull Integer getDuration() {
        return DEFAULT_EVENT_DURATION;
    }

    public @NotNull String getId() {
        return getName().toLowerCase().replace(" ", "_");
    }

    public @NotNull GuiIcon getGUIIcon() {
        String description;

        if (this instanceof RandomPlayerSelectorEvent randomPlayerSelectorEvent) {
            int min = randomPlayerSelectorEvent.getMin();
            int max = randomPlayerSelectorEvent.getMax();

            description = "[" + min + "-" + max + "] " + getWarningMessage();
        }
        else {
            description = getWarningMessage();
        }

        String nameColor = "&e";
        String descriptionColor = "&7";

        ItemStack display = ItemCreator.createItem(getDisplayMaterial(), SimpleColor.deserialize(nameColor + getName()),
                SimpleColor.deserialize(List.of(
                        descriptionColor + description,
                        "",
                        "&7Cost: &e&l" + getPrice(),
                        "",
                        "&8" + getId()
                )),
                null);

        return new GuiIcon(player -> {

            if (plugin.vaultHook) {
                Economy economy = plugin.getEconomy();

                if (economy != null && economy.getBalance(player) >= getPrice()) {
                    player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_YES, 1, 1);
                    player.sendMessage(SimpleColor.deserialize("&ePurchased event!"));

                    if (plugin.getEventManager().queueEvent(player, this)) {
                        player.sendMessage(SimpleColor.deserialize("&7Your event has been queued up to go next!"));
                        economy.withdrawPlayer(player, getPrice());
                    }
                    else {
                        player.sendMessage(SimpleColor.deserialize("&cYour event could not be queued!"));
                    }

                }
                else {
                    player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                    player.sendMessage(SimpleColor.deserialize("&cYou don't have enough money to purchase this!"));
                }
            }
            else {
                logWarning("vault not installed");
            }

        }, display, getId());
    }

    public void load() {
    }

    public void start() {
    }

    public void end() {
    }

    public void run() {
    }

    public void cancel() {
        tasks.forEach(BukkitTask::cancel);
        tasks.clear();
    }

    public void register() {
        plugin = AetherLabsPlugin.getInstance();
        gameManager = plugin.getGameManager();

        if (gameManager != null) {
            playerManager = gameManager.getPlayerManager();
        } else {
            logError("gameManager is null in register baseEvent");
        }

        plugin.getEventManager()
                .subscribe(this);
    }
}

