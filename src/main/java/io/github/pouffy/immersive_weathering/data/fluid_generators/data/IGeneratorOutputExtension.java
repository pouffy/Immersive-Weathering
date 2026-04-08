package io.github.pouffy.immersive_weathering.data.fluid_generators.data;

import io.github.pouffy.immersive_weathering.data.fluid_generators.IFluidGenerator;
import net.minecraft.resources.ResourceLocation;

public interface IGeneratorOutputExtension {
    private GeneratorOutput self() {
        return (GeneratorOutput) this;
    }
    void accept(ResourceLocation id, IFluidGenerator generator);
}
