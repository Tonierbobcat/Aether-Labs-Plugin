package com.loficostudios.minigameeventsplugin.GameEvents;

import com.loficostudios.minigameeventsplugin.AetherLabsPlugin;
import com.loficostudios.minigameeventsplugin.interfaces.IObjectSelector;
import org.bukkit.boss.BossBar;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

public abstract class SelectorEvent<Impl> extends BaseEvent implements IObjectSelector<Impl> {
    public static final int DEFAULT_MIN_SELECTED = 1;
    public static final int DEFAULT_MAX_SELECTED = 3;

    protected Integer amount = null;
    protected BossBar progressBar;

    protected abstract Boolean getDisplayedEnabled();

    protected abstract boolean onSelect(Impl selectedObject);

    protected void onComplete(Collection<Impl> selectObjects) {
    }

    public int getAmount() {
        if (amount == null) {
            int calculatedAmount = calculateObjects(getObjects());
            this.amount = calculatedAmount;
            return calculatedAmount;
        }

        return this.amount;
    }

    protected abstract Collection<Impl> getObjects();

    protected void selectObjects(Consumer<Impl> onSelect, Consumer<Collection<Impl>> onEnd) {

        final int amountt = getAmount();

        final List<Impl> objects = new ArrayList<>(getObjects());

        final Collection<Impl> selectedObjects = new ArrayList<>();

        tasks.add(new BukkitRunnable() {
            final Random random = new Random();

            int amountSelected = 0;

            @Override
            public void run() {
                if (amountSelected < amountt ) {

                    int index = random.nextInt(objects.size());

                    Impl selectedObject = objects
                            .get(index);

                    if (selectedObject != null && !selectedObjects.contains(selectedObject)) {
                        amountSelected++;

                        selectedObjects.add(selectedObject);

                        if (onSelect != null)
                            onSelect.accept(selectedObject);
                    }
                }
                else {
                    if (onEnd != null)
                        onEnd.accept(selectedObjects);
                    this.cancel();
                }
            }
        }.runTaskTimer(AetherLabsPlugin.getInstance(), 0, 15));
    }
}
