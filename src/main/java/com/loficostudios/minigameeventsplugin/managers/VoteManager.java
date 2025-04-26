package com.loficostudios.minigameeventsplugin.managers;

import com.loficostudios.minigameeventsplugin.api.event.impl.AbstractGameMode;
import com.loficostudios.minigameeventsplugin.game.Game;
import com.loficostudios.minigameeventsplugin.gamemode.GameMode;
import com.loficostudios.minigameeventsplugin.gamemode.GameModes;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class VoteManager {

    private final Game gameManager;

    private final Map<UUID, GameMode> votes = new HashMap<>();

    public VoteManager(Game gameManager) {
        this.gameManager = gameManager;
    }

    public int getVotes(@NotNull GameMode type) {
        return (int) votes.values().stream()
                .filter(t -> t.equals(type)).count();
    }

    public @NotNull GameMode getVote(@NotNull Player player) {
        return votes.get(player.getUniqueId());
    }

    public Boolean castVote(@NotNull Player player, GameMode type) {

        UUID uuid = player.getUniqueId();

        if (votes.containsKey(player.getUniqueId()))
            return false;

        votes.put(uuid, type);

        return true;
    }

    public void validateVotes() {
        List<UUID> uuids = new ArrayList<>(votes.keySet());


        for (UUID uuid : uuids) {

            Player player = Bukkit.getPlayer(uuid);

            if (player == null) {
                this.votes.remove(uuid);
                return;
            }

            if (!gameManager.getArena().getWorld().getPlayers().contains(player)) {
                this.votes.remove(uuid);
                return;
            }
        }
    }

    public GameMode getMode() {
        List<GameMode> best = new ArrayList<>();
        int highestVotes = -1;

        for (GameMode mode : GameModes.values()) {
            int votes = getVotes(mode);

            if (votes > highestVotes) {
                highestVotes = votes;
                best.clear();
                best.add(mode);
            } else if (votes == highestVotes) {
                best.add(mode);
            }
        }

        if (best.isEmpty())
            throw new IllegalArgumentException();

        int randomIndex = ThreadLocalRandom.current().nextInt(best.size());
        return best.get(randomIndex);
    }
}
