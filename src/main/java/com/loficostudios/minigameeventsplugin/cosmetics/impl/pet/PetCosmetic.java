package com.loficostudios.minigameeventsplugin.cosmetics.impl.pet;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import com.loficostudios.minigameeventsplugin.cosmetics.CosmeticProfile;
import com.loficostudios.minigameeventsplugin.cosmetics.Quality;
import com.loficostudios.minigameeventsplugin.cosmetics.UnlockCondition;
import com.loficostudios.minigameeventsplugin.cosmetics.impl.AbstractCosmetic;
import com.loficostudios.minigameeventsplugin.cosmetics.impl.ParticleData;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class PetCosmetic extends AbstractCosmetic {
    private final ParticleData<?> particles;
    private final BiConsumer<PetData, Player> onKill;
    private final Consumer<PetData> onDeath;

    private final HashMap<UUID, Entity> entities = new HashMap<>();

    private final ItemStack skull;

    public PetCosmetic(String id, String name, Material icon, Quality quality, @Nullable UnlockCondition condition, ParticleData<?> particles, BiConsumer<PetData, Player> onKill, Consumer<PetData> onDeath, String base64Texture) {
        super(id, name, icon, Type.PET, quality, condition);
        this.particles = particles;
        this.onKill = onKill;
        this.onDeath = onDeath;
        this.skull = create(base64Texture);
    }

    private ItemStack create(String texture) {
        var item = new ItemStack(Material.PLAYER_HEAD);

        SkullMeta meta = (SkullMeta) item.getItemMeta();

        final UUID uuid = UUID.randomUUID();
        PlayerProfile profile = Bukkit.createProfile(uuid, uuid.toString().substring(0, 16));

        profile.setProperty(new ProfileProperty("textures", texture));
        meta.setPlayerProfile(profile);
        item.setItemMeta(meta);
        return item;
    }

    public void spawn(Player owner, Location location) {
        var pet = (ArmorStand) entities.computeIfAbsent(owner.getUniqueId(), uuid -> location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND));
        pet.getEquipment().setHelmet(skull);

        pet.setCustomNameVisible(true);
        pet.customName(Component.text(getName()));
        pet.setInvisible(true);
        pet.setInvulnerable(true);
        pet.teleport(location);
    }

    public void onKill(Player owner, Player victim) {
        var pet = (ArmorStand) entities.get(owner.getUniqueId());
        if (pet == null)
            return;
        var location = pet.getLocation();
        if (onKill != null)
            onKill.accept(new PetData(owner, location, this), victim);
    }

    public void onDeath(Player owner) {
        var pet = (ArmorStand) entities.get(owner.getUniqueId());
        if (pet == null)
            return;
        var location = pet.getLocation();
        if (onDeath != null)
            onDeath.accept(new PetData(owner, location, this));
    }

    private void moveTowards(Player owner, Entity pet) {
        var loc = pet.getLocation();
        org.bukkit.util.Vector currentPos = loc.toVector();

        org.bukkit.util.Vector targetPos = owner.getLocation().toVector().subtract(new org.bukkit.util.Vector(0, 0.35, 0));

        org.bukkit.util.Vector toTarget = targetPos.clone().subtract(currentPos);
        double distance = toTarget.length();

        if (distance < 0.01) return;

        org.bukkit.util.Vector direction = toTarget.clone().normalize();

        double maxStep = 0.25;

        org.bukkit.util.Vector smoothedTarget = lerp(currentPos, targetPos, Math.min(distance / 5.0, 1.0) * 0.3);
        Vector step = smoothedTarget.subtract(currentPos);

        if (step.length() > maxStep) {
            step = step.normalize().multiply(maxStep);
        }

        Location location = currentPos.clone().add(step).toLocation(owner.getWorld());

        location.setDirection(direction);

        pet.teleport(location);

        location.getWorld().spawnParticle(particles.getParticle(), particles.getLocation().toLocation(location.getWorld()), particles.getCount(), particles.getOffsetX(), particles.getOffsetY(), particles.getOffsetZ(), particles.getExtra());
    }

    public void update(Player owner) {
        var pet = (ArmorStand) entities.get(owner.getUniqueId());
        if (pet == null)
            return;
        var location = pet.getLocation();
        if (!validate(owner)) {
            despawn(owner);
            return;
        }
        playIdleAnimation(pet);
        var distanceFromOwner = owner.getLocation().distance(location);
        if (distanceFromOwner >= 15) {
            pet.teleport(owner.getLocation());
            return;
        }
        if (distanceFromOwner <= 2.5)
            return;
        moveTowards(owner, pet);
    }

    private void playIdleAnimation(Entity entity) {
        var location = entity.getLocation();
        double amplitude = 0.025;
        double frequency = 0.15;
        double yOffset = Math.sin((System.currentTimeMillis() / 50.0) * frequency) * amplitude;

        entity.teleport(location.clone().add(0, yOffset, 0));
    }

    private Vector lerp(Vector from, Vector to, double t) {
        return from.clone().multiply(1 - t).add(to.clone().multiply(t));
    }

    private boolean validate(Player owner) {
        return owner.isOnline();
    }

    private void despawn(Player owner) {
        var pet = entities.remove(owner.getUniqueId());
        if (pet != null)
            pet.remove();
    }

    @Override
    public void equip(CosmeticProfile profile) {
        spawn(profile.getPlayer(), profile.getPlayer().getLocation());
    }

    @Override
    public void unequip(CosmeticProfile profile) {
        despawn(profile.getPlayer());
    }

    public record PetData(Player owner, Location location, PetCosmetic pet) {
    }
}
