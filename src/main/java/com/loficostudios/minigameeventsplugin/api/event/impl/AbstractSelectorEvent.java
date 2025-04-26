package com.loficostudios.minigameeventsplugin.api.event.impl;

import com.loficostudios.minigameeventsplugin.AetherLabsPlugin;
import com.loficostudios.minigameeventsplugin.api.event.EventType;
import com.loficostudios.minigameeventsplugin.api.event.SelectorEvent;
import com.loficostudios.minigameeventsplugin.game.Game;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

public abstract class AbstractSelectorEvent<Impl> extends AbstractGameEvent implements SelectorEvent<Impl> {

    private final int min;
    private final int max;

    protected Integer amount = null;
//    protected BossBar progressBar;

    protected AbstractSelectorEvent(String id, String name, EventType type, Material icon, double cost, int min, int max) {
        super(id, name, type, icon, cost);
        this.min = min;
        this.max = max;
    }

    protected AbstractSelectorEvent(String id, String name, EventType type, Material icon, int min, int max) {
        super(id, name, type, icon);
        this.min = min;
        this.max = max;
    }

    protected AbstractSelectorEvent(String name, EventType type, Material icon, int min, int max) {
        super(name, type, icon);
        this.min = min;
        this.max = max;
    }

    @Override
    public int getMin() {
        return min;
    }

    @Override
    public int getMax() {
        return max;
    }

    @Override
    public boolean getDisplayedEnabled() {
        return true;
    }

    @Override
    public int getAmount(Game game) {
        if (amount == null) {
            int calculatedAmount = calculateObjects(getObjects(game));
            this.amount = calculatedAmount;
            return calculatedAmount;
        }

        return this.amount;
    }

    @Override
    public void selectObjects(Game game, Consumer<Impl> onSelected, Consumer<Collection<Impl>> onComplete) {
        final int amount = getAmount(game);

        final List<Impl> objects = new ArrayList<>(getObjects(game));

        final Collection<Impl> selectedObjects = new ArrayList<>();

        StringBuilder message = new StringBuilder();

        tasks.add(new BukkitRunnable() {
            final Random random = new Random();

            int amountSelected = 0;

            @Override
            public void run() {
                if (amountSelected < amount ) {

                    int index = random.nextInt(objects.size());

                    Impl selectedObject = objects
                            .get(index);

                    if (selectedObject != null && !selectedObjects.contains(selectedObject)) {
                        amountSelected++;

                        selectedObjects.add(selectedObject);

                        if (onSelected != null) {
                            onSelected.accept(selectedObject);
                        }
                    }
                }
                else {
                    if (onComplete != null) {
                        onComplete.accept(selectedObjects);
                    }
                    onComplete(game, selectedObjects);
                    this.cancel();
                }
            }
        }.runTaskTimer(AetherLabsPlugin.getInstance(), 0, 15));
    }
}
