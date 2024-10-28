package com.loficostudios.minigameeventsplugin.Utils;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Collection;

public class EventUtils {

    /**
     * @param duration in seconds
     */
    public static void effectPlayer(final Player player, PotionEffectType type, int duration, int amplifier) {
        PotionEffect potionEffect = new PotionEffect(type, duration * 20, amplifier, false, false);
        player.addPotionEffect(potionEffect);
    }

    /**
     * @param duration in seconds
     */
    public static void effectPlayers(final Collection<Player> players, PotionEffectType type, int duration, int amplifier) {
        PotionEffect potionEffect = new PotionEffect(type, duration * 20, amplifier, false, false);
        players.forEach(player -> player.addPotionEffect(potionEffect));
    }

}
