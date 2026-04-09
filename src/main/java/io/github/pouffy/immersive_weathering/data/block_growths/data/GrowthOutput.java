package io.github.pouffy.immersive_weathering.data.block_growths.data;

import io.github.pouffy.immersive_weathering.data.block_growths.growths.ConfigurableBlockGrowth;
import io.github.pouffy.immersive_weathering.data.block_growths.growths.IBlockGrowth;
import net.minecraft.resources.ResourceLocation;

public interface GrowthOutput extends IGrowthOutputExtension {
    default void accept(ResourceLocation location, IBlockGrowth growth) {
        accept(location, growth);
    }
}
