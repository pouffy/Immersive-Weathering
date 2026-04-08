package io.github.pouffy.immersive_weathering.data.block_growths.data;

import io.github.pouffy.immersive_weathering.data.block_growths.growths.ConfigurableBlockGrowth;
import net.minecraft.resources.ResourceLocation;

public interface IGrowthOutputExtension {
    private GrowthOutput self() {
        return (GrowthOutput) this;
    }
    void accept(ResourceLocation id, ConfigurableBlockGrowth growth);
}
