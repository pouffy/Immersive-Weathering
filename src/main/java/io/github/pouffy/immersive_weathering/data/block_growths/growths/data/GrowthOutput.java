package io.github.pouffy.immersive_weathering.data.block_growths.growths.data;

import io.github.pouffy.immersive_weathering.data.block_growths.growths.ConfigurableBlockGrowth;
import net.minecraft.resources.ResourceLocation;

public interface GrowthOutput extends IGrowthOutputExtension {
    default void accept(ResourceLocation location, ConfigurableBlockGrowth growth) {
        accept(location, growth);
    }
}
