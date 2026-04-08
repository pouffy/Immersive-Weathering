package io.github.pouffy.immersive_weathering.data.fluid_generators.data;

import io.github.pouffy.immersive_weathering.data.fluid_generators.IFluidGenerator;
import net.minecraft.resources.ResourceLocation;

public interface GeneratorOutput extends IGeneratorOutputExtension {
    default void accept(ResourceLocation location, IFluidGenerator generator) {
        accept(location, generator);
    }
}
