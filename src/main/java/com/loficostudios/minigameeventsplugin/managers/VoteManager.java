package com.loficostudios.minigameeventsplugin.managers;

import com.loficostudios.minigameeventsplugin.api.BaseGameMode;
import com.loficostudios.minigameeventsplugin.managers.GameManager.GameManager;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class VoteManager {

    @Getter
    private static VoteManager instance;

    private final GameManager gameManager;

    private final Map<UUID, BaseGameMode> votes = new HashMap<>();



    public VoteManager(GameManager gameManager) {
        this.gameManager = gameManager;
        instance = this;
    }

    public int getVotes(@NotNull BaseGameMode type) {
        return (int) votes.values().stream()
                .filter(gameType -> gameType.equals(type)).count();
    }

    public @NotNull BaseGameMode getVote(@NotNull Player player) {
        return votes.get(player.getUniqueId());
    }

    public Boolean castVote(@NotNull Player player, BaseGameMode type) {

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

    public BaseGameMode getMode() {

        int eggWarsVotes = getVotes(gameManager.EGG_WARS);
        int differentHeightsVotes = getVotes(gameManager.DIFFERENT_HEIGHTS);
        int normalVotes = getVotes(gameManager.NORMAL);

        if (eggWarsVotes > differentHeightsVotes && eggWarsVotes > normalVotes) {
            //Common.broadcast("Choose bedwars");
            return gameManager.EGG_WARS;
        } else if (differentHeightsVotes > eggWarsVotes && differentHeightsVotes > normalVotes) {
            //Common.broadcast("Choose differentheights");
            return gameManager.DIFFERENT_HEIGHTS;
        } else {
            //Common.broadcast("Choose normal");
            return gameManager.NORMAL;
        }
    }

}
