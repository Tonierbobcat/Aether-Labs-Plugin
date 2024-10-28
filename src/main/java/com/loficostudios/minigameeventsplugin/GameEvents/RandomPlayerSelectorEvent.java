package com.loficostudios.minigameeventsplugin.GameEvents;

import com.loficostudios.melodyapi.utils.Common;
import com.loficostudios.minigameeventsplugin.Interfaces.IPlayerSelector;
import com.loficostudios.minigameeventsplugin.Managers.GameManager;
import com.loficostudios.minigameeventsplugin.Utils.PlayerState;
import com.loficostudios.minigameeventsplugin.MiniGameEventsPlugin;
import lombok.Getter;
import org.bukkit.Sound;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import static com.loficostudios.minigameeventsplugin.Utils.DebugUtil.debugWarning;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import static com.loficostudios.minigameeventsplugin.Utils.DebugUtil.debug;

public abstract class RandomPlayerSelectorEvent extends BaseEvent implements IPlayerSelector {

    public static final int DEFAULT_MIN_SELECTED = 1;
    public static final int DEFAULT_MAX_SELECTED = 3;

    @Getter private int amount;
    protected BossBar progressBar;

    public abstract boolean onSelect(Player selectedPlayer);
    public abstract void onComplete(Collection<Player> selectedPlayers);

    public Boolean getDisplayedEnabled() {
        return true;
    }

    public Collection<Player> getPlayers() {
        return getPlayerManager().getPlayers(PlayerState.ALIVE);
    }

    @Override
    public void load() {
        amount = calculateObjects(getPlayers());
    }

    @Override
    public void start() {

        AtomicReference<String> message = new AtomicReference<>("");

        progressBar = getGameManager().getProgressBar();

        selectRandomPlayers(amount, (Player player) -> {
            boolean selected = false;

            if (onSelect(player)) {
                selected = true;
                debug("selected " + player.getName());
            }
            else {
                debugWarning("could not select " + player);
            }

            if (getDisplayedEnabled()) {
                message.set(message + " " + (selected ? player.getName() : "NaN"));
                progressBar.setTitle(message.get());
            }

            getGameManager().notify(GameManager.NotificationType.GLOBAL, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 2);

        }, this::onComplete);
    }

    @Override
    public void end() {
        progressBar.removeAll();
    }

    protected void selectRandomPlayers(int amount, Consumer<Player> onSelect, Consumer<Collection<Player>> onEnd) {
        final List<Player> playersInGame = new ArrayList<>(getPlayerManager().getPlayers(PlayerState.ALIVE));
        final Collection<Player> selectedPlayers = new ArrayList<>();

        tasks.add(new BukkitRunnable() {
            final Random random = new Random();

            int playersSelected = 0;

            @Override
            public void run() {
                if (playersSelected < amount ) {


                    int index = random.nextInt(playersInGame.size());

                    Player selectedPlayer = playersInGame
                            .get(index);

                    if (selectedPlayer != null && !selectedPlayers.contains(selectedPlayer)) {
                        playersSelected++;

                        selectedPlayers.add(selectedPlayer);

                        if (onSelect != null)
                            onSelect.accept(selectedPlayer);
                    }
                }
                else {

                    Common.broadcast("reached end of selectrandomPlayer statement");

                    if (onEnd != null)
                        onEnd.accept(selectedPlayers);
                    this.cancel();
                }

            }
        }.runTaskTimer(MiniGameEventsPlugin.getInstance(), 0, 15));
    }
}
