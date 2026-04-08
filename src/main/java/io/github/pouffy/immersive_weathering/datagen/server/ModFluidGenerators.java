package io.github.pouffy.immersive_weathering.datagen.server;

import io.github.pouffy.immersive_weathering.ImmersiveWeathering;
import io.github.pouffy.immersive_weathering.blocks.soil_types.NulchBlock;
import io.github.pouffy.immersive_weathering.data.fluid_generators.FluidGeneratorProvider;
import io.github.pouffy.immersive_weathering.data.fluid_generators.builtin.BurnMossGenerator;
import io.github.pouffy.immersive_weathering.data.fluid_generators.data.AdjacentBlocksBuilder;
import io.github.pouffy.immersive_weathering.data.fluid_generators.data.GeneratorOutput;
import io.github.pouffy.immersive_weathering.data.rute_tests.FluidTagMatchTest;
import io.github.pouffy.immersive_weathering.reg.ModBlocks;
import io.github.pouffy.immersive_weathering.reg.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockStateMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import net.minecraft.world.level.material.Fluids;

import java.util.concurrent.CompletableFuture;

public class ModFluidGenerators extends FluidGeneratorProvider {

    public ModFluidGenerators(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries) {
        super(packOutput, registries);
    }

    @Override
    protected void buildGenerators(GeneratorOutput output, HolderLookup.Provider holderLookup) {
        var water = new FluidTagMatchTest(FluidTags.WATER, 1f);
        self(Fluids.LAVA, Blocks.ANDESITE, AdjacentBlocksBuilder.builder()
                .sides(water)
                .any(new BlockStateMatchTest(Blocks.DIORITE.defaultBlockState())))
                .priority(1)
                .save(output, ImmersiveWeathering.res("andesite"));
        self(Fluids.LAVA, Blocks.DIORITE, AdjacentBlocksBuilder.builder()
                .sides(water)
                .any(new TagMatchTest(ModTags.QUARTZ_BLOCKS)))
                .priority(1)
                .save(output, ImmersiveWeathering.res("diorite"));
        self(Fluids.LAVA, Blocks.GRANITE, AdjacentBlocksBuilder.builder()
                .sides(water)
                .any(new BlockStateMatchTest(Blocks.DIORITE.defaultBlockState()), new TagMatchTest(ModTags.QUARTZ_BLOCKS)))
                .save(output, ImmersiveWeathering.res("granite"));
        self(Fluids.LAVA, Blocks.CALCITE, AdjacentBlocksBuilder.builder()
                .sides(new BlockStateMatchTest(Blocks.MAGMA_BLOCK.defaultBlockState()), new BlockStateMatchTest(Blocks.BLUE_ICE.defaultBlockState()))
                .any(new BlockStateMatchTest(Blocks.BONE_BLOCK.defaultBlockState())))
                .save(output, ImmersiveWeathering.res("calcite"));
        self(Fluids.LAVA, Blocks.TUFF, AdjacentBlocksBuilder.builder()
                .down(new BlockStateMatchTest(Blocks.BUBBLE_COLUMN.defaultBlockState())))
                .save(output, ImmersiveWeathering.res("tuff"));
        self(Fluids.LAVA, Blocks.SMOOTH_BASALT, AdjacentBlocksBuilder.builder()
                .sides(water)
                .any(new BlockStateMatchTest(Blocks.BLUE_ICE.defaultBlockState()))
                .down(new BlockStateMatchTest(Blocks.SOUL_SOIL.defaultBlockState())))
                .save(output, ImmersiveWeathering.res("smooth_basalt"));
        self(Fluids.LAVA, Blocks.MAGMA_BLOCK, AdjacentBlocksBuilder.builder()
                .down(new BlockStateMatchTest(Blocks.BUBBLE_COLUMN.defaultBlockState())))
                .save(output, ImmersiveWeathering.res("magma_block"));
        self(Fluids.LAVA, Blocks.CRYING_OBSIDIAN, AdjacentBlocksBuilder.builder()
                .any(new BlockStateMatchTest(Blocks.SOUL_FIRE.defaultBlockState()), water))
                .still()
                .save(output, ImmersiveWeathering.res("crying_obsidian"));
        self(Fluids.LAVA, Blocks.BLACKSTONE, AdjacentBlocksBuilder.builder()
                .any(new BlockStateMatchTest(Blocks.MAGMA_BLOCK.defaultBlockState()), new BlockStateMatchTest(Blocks.BLUE_ICE.defaultBlockState())))
                .priority(1)
                .save(output, ImmersiveWeathering.res("blackstone"));
        self(Fluids.LAVA, Blocks.BASALT, AdjacentBlocksBuilder.builder()
                .down(new BlockStateMatchTest(Blocks.BASALT.defaultBlockState())))
                .flowing()
                .save(output, ImmersiveWeathering.res("basalt"));
        other(Fluids.LAVA, Blocks.TERRACOTTA, new BlockStateMatchTest(Blocks.CLAY.defaultBlockState())).save(output, ImmersiveWeathering.res("terracotta"));
        other(Fluids.LAVA, ModBlocks.VITRIFIED_SAND.get(), new TagMatchTest(BlockTags.SAND)).save(output, ImmersiveWeathering.res("vitrified_sand"));
        other(Fluids.WATER, ModBlocks.NULCH_BLOCK.get(), new BlockStateMatchTest(ModBlocks.NULCH_BLOCK.get().defaultBlockState().setValue(NulchBlock.MOLTEN, true))).save(output, ImmersiveWeathering.res("nulch_cooling"));
        builtin(output, new BurnMossGenerator(), ImmersiveWeathering.res("burn_moss"));
    }
}
