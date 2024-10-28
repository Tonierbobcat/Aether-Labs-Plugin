package com.loficostudios.minigameeventsplugin.Utils;

import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class Generator {

    Consumer<Collection<Block>> onGenerate;
    Material material;

    public Generator(Material material, Consumer<Collection<Block>> onGenerate) {
        this.onGenerate = onGenerate;
        this.material = material;
    }

    public void generate(Set<Block> blocks) {
        List<Block> generatedBlocks = new ArrayList<>(blocks);

        generatedBlocks.forEach(block -> {
            block.setType(material);
        });

        if (onGenerate != null) {
            onGenerate.accept(generatedBlocks);
        }
    }
}
