package com.loficostudios.minigameeventsplugin.cosmetics.gui;

import com.loficostudios.melodyapi.gui.MelodyGui;
import com.loficostudios.melodyapi.gui.Paginated;
import com.loficostudios.melodyapi.gui.icon.GuiIcon;
import com.loficostudios.minigameeventsplugin.cosmetics.Cosmetic;
import com.loficostudios.minigameeventsplugin.cosmetics.CosmeticInstance;
import com.loficostudios.minigameeventsplugin.cosmetics.CosmeticProfile;
import com.loficostudios.minigameeventsplugin.cosmetics.Quality;
import com.loficostudios.minigameeventsplugin.cosmetics.listener.CosmeticManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CosmeticGui extends MelodyGui implements Paginated {
    private final CosmeticManager manager;

    private int page;

    public CosmeticGui(CosmeticManager manager) {
        super(6*9);
        this.manager = manager;
    }

    private void create(Player player) {
        clear();
        loadPage(player, 0);
    }


    @Override
    public boolean open(@NotNull Player player) {
        create(player);
        return super.open(player);
    }

    private void loadPage(Player player, int page) {
        this.page = page;
        var profile = manager.getProfile(player).orElseThrow();
        var paginated = new ArrayList<>(Paginated.paginate(profile.getInventory().getCosmetics(), page, 3*7));
        List<Integer> slots = new ArrayList<>();

        for (int i = 10; i <= 16 ; i++) {
            slots.add(i);
        }
        for (int i = 19; i <= 25 ; i++) {
            slots.add(i);
        }
        for (int i = 28; i <= 34 ; i++) {
            slots.add(i);
        }

        for (int i = 0; i < paginated.size(); i++) {
            setSlot(slots.get(i), getIcon(profile, paginated.get(i)));
        }
    }

    public GuiIcon getIcon(CosmeticProfile profile, CosmeticInstance instance) {
        var cosmetic = instance.getCosmetic();
        var quality = getFormattedQuality(cosmetic.getQuality());
        var condition = cosmetic.getCondition();
        var meets = condition == null || condition.has(profile.getPlayer());
        boolean equipped = profile.getContainer().isEquipped(cosmetic.getType(), cosmetic);

        var inSlot = !profile.getContainer().isEmpty(cosmetic.getType());


        var name = Component.text("§a" + cosmetic.getName() + (instance.isUnlocked() ? equipped ? " §f[Equipped]" : " §f[Owned]" : meets ? " §a[Purchasable]" : " §c[Locked]"))
                .decoration(TextDecoration.ITALIC, false);

        double cost = 300;

        List<String> description = new ArrayList<>(List.of(
                "§8" + getFormattedType(cosmetic.getType()) + (inSlot ? " §7[*]" : ""),
//                " ",
//                "§7{description}",
                " ",
                "§7Rarity: " + quality
        ));

        if (!instance.isUnlocked()) {
            if (meets) {
                description.add(" ");
                description.add("§7Cost: §a§l$" + cost);
//                description.add(" ");
//                description.add("§aLeft Click to purchase");
            } else {
                description.add(" ");
                description.add("§c" + condition.getMessage());
            }
        } else {
            long millis = instance.getTimeBought();
            Date date = new Date(millis);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
            description.add(" ");
            description.add("§7Purchase Date: §a§l" + formatter.format(date));
        }

        return new GuiIcon(cosmetic.getIcon(), name, description.stream().map(Component::text).toList(), (player,click) -> onClick(profile, instance, !equipped));
    }

    private void onClick(CosmeticProfile profile, CosmeticInstance instance, boolean equip) {
        var cosmetic = instance.getCosmetic();
        var condition = cosmetic.getCondition();
        var meets = condition == null || condition.has(profile.getPlayer());
        boolean unlocked = instance.isUnlocked();
        var player = profile.getPlayer();
        if (unlocked) {
            if (equip) {
                handleEquip(profile, cosmetic);
            } else {
                handleUnequip(profile, cosmetic);
            }
            player.playSound(player, equip ? Sound.BLOCK_BEEHIVE_EXIT : Sound.BLOCK_BEEHIVE_ENTER, 1, 1);
            create(player);
            return;
        }

        if (!meets) {
            player.playSound(player, Sound.ENTITY_VILLAGER_NO, 1, 1);
            return;
        }

        player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
        instance.setUnlocked(true);
        create(player);
    }

    private void handleUnequip(CosmeticProfile profile, Cosmetic cosmetic) {
        var type = cosmetic.getType();
        switch (type) {
            case ARROW_TRAIL -> profile.getContainer().setSlot(Cosmetic.Type.ARROW_TRAIL, null);
        }
    }

    private void handleEquip(CosmeticProfile profile, Cosmetic cosmetic) {
        var type = cosmetic.getType();

        switch (type) {
            case ARROW_TRAIL -> profile.getContainer().setSlot(Cosmetic.Type.ARROW_TRAIL, cosmetic);
        }
    }

    public String getFormattedType(Cosmetic.Type type) {
        var words = type.name().toLowerCase().split("_");
        var builder = new StringBuilder();
        for (String word : words) {
            var chars = word.toCharArray();
            chars[0] = Character.toUpperCase(chars[0]);
            builder.append(chars).append(" ");
        }
        return builder.toString().trim();
    }

    public String getFormattedQuality(Quality quality) {
        return switch (quality) {
            case COMMON -> "§fCOMMON";
            case UNCOMMON -> "§aUNCOMMON";
            case RARE -> "§9RARE";
            case EPIC -> "§5EPIC";
            case LEGENDARY -> "§6LEGENDARY";
            case QUANTUM -> "§3QUANTUM";
        };
    }

    @Override
    public void nextPage(Player player) {
        loadPage(player, page + 1);
    }

    @Override
    public void previousPage(Player player) {
        loadPage(player, Math.max(0, page - 1));
    }
}
