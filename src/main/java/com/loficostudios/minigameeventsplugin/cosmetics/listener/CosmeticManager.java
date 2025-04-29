package com.loficostudios.minigameeventsplugin.cosmetics.listener;

import com.loficostudios.minigameeventsplugin.AetherLabsPlugin;
import com.loficostudios.minigameeventsplugin.cosmetics.Cosmetic;
import com.loficostudios.minigameeventsplugin.cosmetics.CosmeticProfile;
import com.loficostudios.minigameeventsplugin.cosmetics.CosmeticRegistry;
import com.loficostudios.minigameeventsplugin.cosmetics.impl.AdvancedArrowTrailCosmetic;
import com.loficostudios.minigameeventsplugin.cosmetics.impl.ArrowTrailCosmetic;
import com.loficostudios.minigameeventsplugin.player.profile.ProfileManager;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

public class CosmeticManager implements Listener {

    private final CosmeticRegistry registry;
    private final JavaPlugin plugin;
    public CosmeticManager(CosmeticRegistry registry, JavaPlugin plugin) {
        this.registry = registry;
        this.plugin = plugin;
    }

    private final HashMap<UUID, CosmeticArrow> trackedArrows = new HashMap<>();

    private final HashMap<UUID, CosmeticProfile> profiles = new HashMap<>();

    @EventHandler
    private void onJoin(PlayerJoinEvent e) {
        profiles.put(e.getPlayer().getUniqueId(), new CosmeticProfile(e.getPlayer(), registry));
    }

    @EventHandler
    private void onQuit(PlayerQuitEvent e) {
        profiles.remove(e.getPlayer().getUniqueId());
    }

    public Optional<CosmeticProfile> getProfile(Player player) {
        return Optional.ofNullable(profiles.get(player.getUniqueId()));
    }

    @EventHandler
    private void onShoot(EntityShootBowEvent e) {
        if (!(e.getEntity() instanceof Player player))
            return;
        var profile = getProfile(player).orElse(null);
        if (profile == null)
            return;
        var container = profile.getContainer();
        var cosmetic = container.getCosmetic(Cosmetic.Type.ARROW_TRAIL, ArrowTrailCosmetic.class);
        if (cosmetic == null)
            return;

        trackedArrows.put(e.getProjectile().getUniqueId(), new CosmeticArrow(e.getProjectile(), cosmetic, new BukkitRunnable() {
            @Override
            public void run() {
                cosmetic.update(e.getProjectile());
            }
        }.runTaskTimer(plugin, 0, 2)));
    }

    @EventHandler
    private void onLand(ProjectileHitEvent e) {
        var instance = trackedArrows.get(e.getEntity().getUniqueId());
        if (instance == null)
            return;
        instance.task.cancel();
        if (instance.cosmetic instanceof AdvancedArrowTrailCosmetic advanced) {
            advanced.onLand(instance.arrow, e.getHitEntity() != null);
        }
    }

    private record CosmeticArrow(Entity arrow, Cosmetic cosmetic, BukkitTask task) {
    }
}
