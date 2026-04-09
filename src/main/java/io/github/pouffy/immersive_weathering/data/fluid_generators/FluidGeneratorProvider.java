package io.github.pouffy.immersive_weathering.data.fluid_generators;

import com.google.common.collect.Sets;
import com.mojang.serialization.JsonOps;
import io.github.pouffy.immersive_weathering.data.block_growths.growths.IBlockGrowth;
import io.github.pouffy.immersive_weathering.data.fluid_generators.data.AdjacentBlocksBuilder;
import io.github.pouffy.immersive_weathering.data.fluid_generators.data.GeneratorOutput;
import io.github.pouffy.immersive_weathering.data.fluid_generators.data.OtherFluidGeneratorBuilder;
import io.github.pouffy.immersive_weathering.data.fluid_generators.data.SelfFluidGeneratorBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.material.Fluid;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public abstract class FluidGeneratorProvider implements DataProvider {
    protected final PackOutput.PathProvider pathProvider;
    private final CompletableFuture<HolderLookup.Provider> registries;

    public FluidGeneratorProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries) {
        this.pathProvider = packOutput.createPathProvider(PackOutput.Target.DATA_PACK, "fluid_generators");
        this.registries = registries;
    }

    public static SelfFluidGeneratorBuilder self(Fluid fluid, Block growth, AdjacentBlocksBuilder adjacentCondition) {
        return self(fluid, growth.defaultBlockState(), adjacentCondition);
    }

    public static SelfFluidGeneratorBuilder self(Fluid fluid, BlockState growth, AdjacentBlocksBuilder adjacentCondition) {
        return new SelfFluidGeneratorBuilder(fluid, growth, adjacentCondition);
    }

    public static OtherFluidGeneratorBuilder other(Fluid fluid, Block growth, RuleTest target) {
        return other(fluid, growth.defaultBlockState(), target);
    }

    public static OtherFluidGeneratorBuilder other(Fluid fluid, BlockState growth, RuleTest target) {
        return new OtherFluidGeneratorBuilder(fluid, growth, target);
    }

    public static void builtin(GeneratorOutput output, IFluidGenerator generator, ResourceLocation location) {
        output.accept(location, generator);
    }

    protected abstract void buildGenerators(GeneratorOutput output, HolderLookup.Provider holderLookup);

    public CompletableFuture<?> run(final CachedOutput output, final HolderLookup.Provider registries) {
        Set<CompletableFuture<?>> list = new HashSet<>();
        final Set<ResourceLocation> set = Sets.newHashSet();
        this.buildGenerators(new GeneratorOutput() {
            public void accept(ResourceLocation location, IFluidGenerator generator) {
                if (!set.add(location)) {
                    throw new IllegalStateException("Duplicate generator " + location);
                } else {
                    list.add(DataProvider.saveStable(output, registries, IFluidGenerator.CODEC, generator, pathProvider.json(location)));
                }
            }
        }, registries);

        return CompletableFuture.allOf(list.toArray(CompletableFuture[]::new));
    }

    @Override
    public CompletableFuture<?> run(CachedOutput output) {
        return this.registries.thenCompose(provider -> this.run(output, provider));
    }

    @Override
    public String getName() {
        return "[Immersive Weathering] Fluid Generators";
    }
}
