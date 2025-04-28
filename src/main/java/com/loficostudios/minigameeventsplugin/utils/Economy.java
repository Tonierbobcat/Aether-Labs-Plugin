package com.loficostudios.minigameeventsplugin.utils;

import com.loficostudios.minigameeventsplugin.AetherLabsPlugin;
import org.bukkit.entity.Player;

public class Economy {
    public static void deposit(Player player, double d) {
        AetherLabsPlugin.getInstance().getProfileManager().getProfile(player.getUniqueId()).ifPresent(profile -> {
            profile.setMoney(profile.getMoney() + d);
        });
    }

    public static void withdrawal(Player player, double d) {
        AetherLabsPlugin.getInstance().getProfileManager().getProfile(player.getUniqueId()).ifPresent(profile -> {
            profile.setMoney(profile.getMoney() - d);
        });
    }

    public static double getBalance(Player p) {
        var profile = AetherLabsPlugin.getInstance().getProfileManager().getProfile(p.getUniqueId()).orElseThrow();
        return profile.getMoney();
    }
}
