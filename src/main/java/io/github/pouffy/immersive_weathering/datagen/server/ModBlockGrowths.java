package io.github.pouffy.immersive_weathering.datagen.server;

import io.github.pouffy.immersive_weathering.ImmersiveWeathering;
import io.github.pouffy.immersive_weathering.blocks.WeedsBlock;
import io.github.pouffy.immersive_weathering.blocks.frosted.FrostBlock;
import io.github.pouffy.immersive_weathering.blocks.frosted.Frosty;
import io.github.pouffy.immersive_weathering.data.block_growths.BlockGrowthProvider;
import io.github.pouffy.immersive_weathering.data.block_growths.BlockPair;
import io.github.pouffy.immersive_weathering.data.block_growths.TickSource;
import io.github.pouffy.immersive_weathering.data.block_growths.area_condition.AreaCheck;
import io.github.pouffy.immersive_weathering.data.block_growths.area_condition.NeighborCheck;
import io.github.pouffy.immersive_weathering.data.block_growths.data.BlockGrowthBuilder;
import io.github.pouffy.immersive_weathering.data.block_growths.data.GrowthOutput;
import io.github.pouffy.immersive_weathering.data.position_tests.BiomeSetMatchTest;
import io.github.pouffy.immersive_weathering.data.position_tests.IsDayTest;
import io.github.pouffy.immersive_weathering.data.rute_tests.BlockSetMatchTest;
import io.github.pouffy.immersive_weathering.reg.ModBlocks;
import io.github.pouffy.immersive_weathering.reg.ModTags;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RandomBlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class ModBlockGrowths extends BlockGrowthProvider {
    public ModBlockGrowths(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries) {
        super(packOutput, registries);
    }

    @Override
    protected void buildGrowths(GrowthOutput output, HolderLookup.Provider holderLookup) {
        var biomeLookup = holderLookup.lookupOrThrow(Registries.BIOME);
        configured(Blocks.BRAIN_CORAL_BLOCK, new RandomBlockMatchTest(Blocks.WATER, 0.8f), new AreaCheck(2, 2, 2, 6, Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty()))
                .directionalGrowth(Direction.NORTH, 1, SimpleWeightedRandomList.<BlockPair>builder().add(BlockPair.of(horizontal(waterlog(Blocks.BRAIN_CORAL_WALL_FAN.defaultBlockState()), Direction.NORTH)), 50).add(BlockPair.of(Blocks.WET_SPONGE), 1).build())
                .directionalGrowth(Direction.EAST, 1, SimpleWeightedRandomList.<BlockPair>builder().add(BlockPair.of(horizontal(waterlog(Blocks.BRAIN_CORAL_WALL_FAN.defaultBlockState()), Direction.EAST)), 50).add(BlockPair.of(Blocks.WET_SPONGE), 1).build())
                .directionalGrowth(Direction.SOUTH, 1, SimpleWeightedRandomList.<BlockPair>builder().add(BlockPair.of(horizontal(waterlog(Blocks.BRAIN_CORAL_WALL_FAN.defaultBlockState()), Direction.SOUTH)), 50).add(BlockPair.of(Blocks.WET_SPONGE), 1).build())
                .directionalGrowth(Direction.WEST, 1, SimpleWeightedRandomList.<BlockPair>builder().add(BlockPair.of(horizontal(waterlog(Blocks.BRAIN_CORAL_WALL_FAN.defaultBlockState()), Direction.WEST)), 50).add(BlockPair.of(Blocks.WET_SPONGE), 1).build())
                .directionalGrowth(Direction.UP, 3, SimpleWeightedRandomList.<BlockPair>builder().add(BlockPair.of(waterlog(Blocks.BRAIN_CORAL_WALL_FAN.defaultBlockState())), 25).add(BlockPair.of(waterlog(Blocks.BRAIN_CORAL.defaultBlockState())), 25).add(BlockPair.of(Blocks.WET_SPONGE), 1).build())
                .predicate(new BiomeSetMatchTest(HolderSet.direct(biomeLookup.getOrThrow(Biomes.WARM_OCEAN))))
                .growthChance(0.0005f)
                .save(output, ImmersiveWeathering.res("coral_brain_growth"));
        configured(Blocks.BUBBLE_CORAL_BLOCK, new RandomBlockMatchTest(Blocks.WATER, 0.8f), new AreaCheck(2, 2, 2, 6, Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty()))
                .directionalGrowth(Direction.NORTH, 1, SimpleWeightedRandomList.<BlockPair>builder().add(BlockPair.of(horizontal(waterlog(Blocks.BUBBLE_CORAL_WALL_FAN.defaultBlockState()), Direction.NORTH)), 50).add(BlockPair.of(Blocks.WET_SPONGE), 1).build())
                .directionalGrowth(Direction.EAST, 1, SimpleWeightedRandomList.<BlockPair>builder().add(BlockPair.of(horizontal(waterlog(Blocks.BUBBLE_CORAL_WALL_FAN.defaultBlockState()), Direction.EAST)), 50).add(BlockPair.of(Blocks.WET_SPONGE), 1).build())
                .directionalGrowth(Direction.SOUTH, 1, SimpleWeightedRandomList.<BlockPair>builder().add(BlockPair.of(horizontal(waterlog(Blocks.BUBBLE_CORAL_WALL_FAN.defaultBlockState()), Direction.SOUTH)), 50).add(BlockPair.of(Blocks.WET_SPONGE), 1).build())
                .directionalGrowth(Direction.WEST, 1, SimpleWeightedRandomList.<BlockPair>builder().add(BlockPair.of(horizontal(waterlog(Blocks.BUBBLE_CORAL_WALL_FAN.defaultBlockState()), Direction.WEST)), 50).add(BlockPair.of(Blocks.WET_SPONGE), 1).build())
                .directionalGrowth(Direction.UP, 3, SimpleWeightedRandomList.<BlockPair>builder().add(BlockPair.of(waterlog(Blocks.BUBBLE_CORAL_WALL_FAN.defaultBlockState())), 25).add(BlockPair.of(waterlog(Blocks.BUBBLE_CORAL.defaultBlockState())), 25).add(BlockPair.of(Blocks.WET_SPONGE), 1).build())
                .predicate(new BiomeSetMatchTest(HolderSet.direct(biomeLookup.getOrThrow(Biomes.WARM_OCEAN))))
                .growthChance(0.0005f)
                .save(output, ImmersiveWeathering.res("coral_bubble_growth"));
        configured(Blocks.FIRE_CORAL_BLOCK, new RandomBlockMatchTest(Blocks.WATER, 0.8f), new AreaCheck(2, 2, 2, 6, Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty()))
                .directionalGrowth(Direction.NORTH, 1, SimpleWeightedRandomList.<BlockPair>builder().add(BlockPair.of(horizontal(waterlog(Blocks.FIRE_CORAL_WALL_FAN.defaultBlockState()), Direction.NORTH)), 50).add(BlockPair.of(Blocks.WET_SPONGE), 1).build())
                .directionalGrowth(Direction.EAST, 1, SimpleWeightedRandomList.<BlockPair>builder().add(BlockPair.of(horizontal(waterlog(Blocks.FIRE_CORAL_WALL_FAN.defaultBlockState()), Direction.EAST)), 50).add(BlockPair.of(Blocks.WET_SPONGE), 1).build())
                .directionalGrowth(Direction.SOUTH, 1, SimpleWeightedRandomList.<BlockPair>builder().add(BlockPair.of(horizontal(waterlog(Blocks.FIRE_CORAL_WALL_FAN.defaultBlockState()), Direction.SOUTH)), 50).add(BlockPair.of(Blocks.WET_SPONGE), 1).build())
                .directionalGrowth(Direction.WEST, 1, SimpleWeightedRandomList.<BlockPair>builder().add(BlockPair.of(horizontal(waterlog(Blocks.FIRE_CORAL_WALL_FAN.defaultBlockState()), Direction.WEST)), 50).add(BlockPair.of(Blocks.WET_SPONGE), 1).build())
                .directionalGrowth(Direction.UP, 3, SimpleWeightedRandomList.<BlockPair>builder().add(BlockPair.of(waterlog(Blocks.FIRE_CORAL_WALL_FAN.defaultBlockState())), 25).add(BlockPair.of(waterlog(Blocks.FIRE_CORAL.defaultBlockState())), 25).add(BlockPair.of(Blocks.WET_SPONGE), 1).build())
                .predicate(new BiomeSetMatchTest(HolderSet.direct(biomeLookup.getOrThrow(Biomes.WARM_OCEAN))))
                .growthChance(0.0005f)
                .save(output, ImmersiveWeathering.res("coral_fire_growth"));
        configured(Blocks.HORN_CORAL_BLOCK, new RandomBlockMatchTest(Blocks.WATER, 0.8f), new AreaCheck(2, 2, 2, 6, Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty()))
                .directionalGrowth(Direction.NORTH, 1, SimpleWeightedRandomList.<BlockPair>builder().add(BlockPair.of(horizontal(waterlog(Blocks.HORN_CORAL_WALL_FAN.defaultBlockState()), Direction.NORTH)), 50).add(BlockPair.of(Blocks.WET_SPONGE), 1).build())
                .directionalGrowth(Direction.EAST, 1, SimpleWeightedRandomList.<BlockPair>builder().add(BlockPair.of(horizontal(waterlog(Blocks.HORN_CORAL_WALL_FAN.defaultBlockState()), Direction.EAST)), 50).add(BlockPair.of(Blocks.WET_SPONGE), 1).build())
                .directionalGrowth(Direction.SOUTH, 1, SimpleWeightedRandomList.<BlockPair>builder().add(BlockPair.of(horizontal(waterlog(Blocks.HORN_CORAL_WALL_FAN.defaultBlockState()), Direction.SOUTH)), 50).add(BlockPair.of(Blocks.WET_SPONGE), 1).build())
                .directionalGrowth(Direction.WEST, 1, SimpleWeightedRandomList.<BlockPair>builder().add(BlockPair.of(horizontal(waterlog(Blocks.HORN_CORAL_WALL_FAN.defaultBlockState()), Direction.WEST)), 50).add(BlockPair.of(Blocks.WET_SPONGE), 1).build())
                .directionalGrowth(Direction.UP, 3, SimpleWeightedRandomList.<BlockPair>builder().add(BlockPair.of(waterlog(Blocks.HORN_CORAL_WALL_FAN.defaultBlockState())), 25).add(BlockPair.of(waterlog(Blocks.HORN_CORAL.defaultBlockState())), 25).add(BlockPair.of(Blocks.WET_SPONGE), 1).build())
                .predicate(new BiomeSetMatchTest(HolderSet.direct(biomeLookup.getOrThrow(Biomes.WARM_OCEAN))))
                .growthChance(0.0005f)
                .save(output, ImmersiveWeathering.res("coral_horn_growth"));
        configured(Blocks.TUBE_CORAL_BLOCK, new RandomBlockMatchTest(Blocks.WATER, 0.8f), new AreaCheck(2, 2, 2, 6, Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty()))
                .directionalGrowth(Direction.NORTH, 1, SimpleWeightedRandomList.<BlockPair>builder().add(BlockPair.of(horizontal(waterlog(Blocks.TUBE_CORAL_WALL_FAN.defaultBlockState()), Direction.NORTH)), 50).add(BlockPair.of(Blocks.WET_SPONGE), 1).build())
                .directionalGrowth(Direction.EAST, 1, SimpleWeightedRandomList.<BlockPair>builder().add(BlockPair.of(horizontal(waterlog(Blocks.TUBE_CORAL_WALL_FAN.defaultBlockState()), Direction.EAST)), 50).add(BlockPair.of(Blocks.WET_SPONGE), 1).build())
                .directionalGrowth(Direction.SOUTH, 1, SimpleWeightedRandomList.<BlockPair>builder().add(BlockPair.of(horizontal(waterlog(Blocks.TUBE_CORAL_WALL_FAN.defaultBlockState()), Direction.SOUTH)), 50).add(BlockPair.of(Blocks.WET_SPONGE), 1).build())
                .directionalGrowth(Direction.WEST, 1, SimpleWeightedRandomList.<BlockPair>builder().add(BlockPair.of(horizontal(waterlog(Blocks.TUBE_CORAL_WALL_FAN.defaultBlockState()), Direction.WEST)), 50).add(BlockPair.of(Blocks.WET_SPONGE), 1).build())
                .directionalGrowth(Direction.UP, 3, SimpleWeightedRandomList.<BlockPair>builder().add(BlockPair.of(waterlog(Blocks.TUBE_CORAL_WALL_FAN.defaultBlockState())), 25).add(BlockPair.of(waterlog(Blocks.TUBE_CORAL.defaultBlockState())), 25).add(BlockPair.of(Blocks.WET_SPONGE), 1).build())
                .predicate(new BiomeSetMatchTest(HolderSet.direct(biomeLookup.getOrThrow(Biomes.WARM_OCEAN))))
                .growthChance(0.0005f)
                .save(output, ImmersiveWeathering.res("coral_tube_growth"));
        configured(Blocks.CRIMSON_NYLIUM, new RandomBlockMatchTest(Blocks.AIR, 0.75f), new AreaCheck(2, 2, 2, 5, Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty()))
                .directionalGrowth(Direction.UP, null, SimpleWeightedRandomList.<BlockPair>builder().add(BlockPair.of(Blocks.CRIMSON_ROOTS), 50).add(BlockPair.of(Blocks.CRIMSON_FUNGUS), 10).add(BlockPair.of(Blocks.WARPED_FUNGUS), 1).build())
                .growthChance(0.002f)
                .save(output, ImmersiveWeathering.res("crimson_nylium_vegetation"));
        configured(ModBlocks.EARTHEN_CLAY.get(), new RandomBlockMatchTest(Blocks.AIR, 0.7f), new AreaCheck(2, 2, 2, 3, Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty()))
                .directionalGrowth(Direction.UP, null, SimpleWeightedRandomList.<BlockPair>builder().add(BlockPair.of(Blocks.SUGAR_CANE), 1).build())
                .growthChance(0.005f)
                .predicate(new BiomeSetMatchTest(biomeLookup.getOrThrow(BiomeTags.IS_RIVER)))
                .save(output, ImmersiveWeathering.res("earthen_clay_sugarcane"));
        configured(BuiltInRegistries.BLOCK.getOrCreateTag(BlockTags.FIRE), new BlockMatchTest(Blocks.GRASS_BLOCK), AreaCheck.EMPTY)
                .directionalGrowth(Direction.DOWN, 1, SimpleWeightedRandomList.<BlockPair>builder().add(BlockPair.of(Blocks.DIRT), 1).build())
                .tickSource(TickSource.RAIN, TickSource.SNOW, TickSource.CLEAR_SKY)
                .growthChance(0.05f)
                .save(output, ImmersiveWeathering.res("fire_burns_grass_block"));
        configured(Blocks.FERN, new BlockMatchTest(Blocks.AIR), NeighborCheck.simple(new BlockMatchTest(Blocks.AIR)))
                .directionalGrowth(Direction.UP, 1, SimpleWeightedRandomList.<BlockPair>builder().add(BlockPair.of(frostNatural(ModBlocks.FROSTY_FERN.get().defaultBlockState())), 1).build())
                .growthChance(0.05f)
                .targetSelf(true)
                .predicate(new BiomeSetMatchTest(biomeLookup.getOrThrow(ModTags.ICY)), new IsDayTest(false))
                .save(output, ImmersiveWeathering.res("frost_fern"));
        configured(Blocks.FERN, new BlockMatchTest(Blocks.AIR), NeighborCheck.simple(new BlockMatchTest(Blocks.AIR)))
                .directionalGrowth(Direction.UP, 1, SimpleWeightedRandomList.<BlockPair>builder().add(BlockPair.of(frostNatural(ModBlocks.FROSTY_FERN.get().defaultBlockState())), 1).build())
                .tickSource(TickSource.SNOW)
                .growthChance(0.08f)
                .targetSelf(true)
                .save(output, ImmersiveWeathering.res("frost_fern_snow"));
        configured(Blocks.GLASS, new BlockMatchTest(Blocks.AIR), NeighborCheck.simple(new BlockMatchTest(Blocks.AIR)))
                .directionalGrowth(Direction.UP, 1, SimpleWeightedRandomList.<BlockPair>builder().add(BlockPair.of(frostNatural(ModBlocks.FROSTY_GLASS.get().defaultBlockState())), 1).build())
                .growthChance(0.05f)
                .targetSelf(true)
                .predicate(new BiomeSetMatchTest(biomeLookup.getOrThrow(ModTags.ICY)), new IsDayTest(false))
                .save(output, ImmersiveWeathering.res("frost_glass"));
        configured(Blocks.GLASS_PANE, new BlockMatchTest(Blocks.AIR), NeighborCheck.simple(new BlockMatchTest(Blocks.AIR)))
                .directionalGrowth(Direction.UP, 1, SimpleWeightedRandomList.<BlockPair>builder().add(BlockPair.of(frostNatural(ModBlocks.FROSTY_GLASS_PANE.get().defaultBlockState())), 1).build())
                .growthChance(0.05f)
                .targetSelf(true)
                .predicate(new BiomeSetMatchTest(biomeLookup.getOrThrow(ModTags.ICY)), new IsDayTest(false))
                .save(output, ImmersiveWeathering.res("frost_glass_pane"));
        configured(Blocks.GLASS, new BlockMatchTest(Blocks.AIR), NeighborCheck.simple(new BlockMatchTest(Blocks.AIR)))
                .directionalGrowth(Direction.UP, 1, SimpleWeightedRandomList.<BlockPair>builder().add(BlockPair.of(frostNatural(ModBlocks.FROSTY_GLASS.get().defaultBlockState())), 1).build())
                .tickSource(TickSource.SNOW)
                .growthChance(0.08f)
                .targetSelf(true)
                .save(output, ImmersiveWeathering.res("frost_glass_snow"));
        configured(Blocks.GLASS_PANE, new BlockMatchTest(Blocks.AIR), NeighborCheck.simple(new BlockMatchTest(Blocks.AIR)))
                .directionalGrowth(Direction.UP, 1, SimpleWeightedRandomList.<BlockPair>builder().add(BlockPair.of(frostNatural(ModBlocks.FROSTY_GLASS_PANE.get().defaultBlockState())), 1).build())
                .tickSource(TickSource.SNOW)
                .growthChance(0.08f)
                .targetSelf(true)
                .save(output, ImmersiveWeathering.res("frost_glass_pane_snow"));
        configured(Blocks.SHORT_GRASS, new BlockMatchTest(Blocks.AIR), NeighborCheck.simple(new BlockMatchTest(Blocks.AIR)))
                .directionalGrowth(Direction.UP, 1, SimpleWeightedRandomList.<BlockPair>builder().add(BlockPair.of(frostNatural(ModBlocks.FROSTY_GRASS.get().defaultBlockState())), 1).build())
                .growthChance(0.05f)
                .targetSelf(true)
                .predicate(new BiomeSetMatchTest(biomeLookup.getOrThrow(ModTags.ICY)), new IsDayTest(false))
                .save(output, ImmersiveWeathering.res("frost_grass"));
        configured(Blocks.SHORT_GRASS, new BlockMatchTest(Blocks.AIR), NeighborCheck.simple(new BlockMatchTest(Blocks.AIR)))
                .directionalGrowth(Direction.UP, 1, SimpleWeightedRandomList.<BlockPair>builder().add(BlockPair.of(frostNatural(ModBlocks.FROSTY_GRASS.get().defaultBlockState())), 1).build())
                .tickSource(TickSource.SNOW)
                .growthChance(0.08f)
                .targetSelf(true)
                .save(output, ImmersiveWeathering.res("frost_grass_snow"));
        configured(List.of(Blocks.STONE, Blocks.GRASS_BLOCK, ModBlocks.ROOTED_GRASS_BLOCK.get()), new BlockSetMatchTest(HolderSet.direct(Blocks.AIR.builtInRegistryHolder()), 0.4f), new AreaCheck(2, 2, 2, 5, Optional.empty(), Optional.empty(), Optional.of(new TagMatchTest(ModTags.MAGMA_SOURCE)), Optional.empty()))
                .directionalGrowth(Direction.UP, null, SimpleWeightedRandomList.<BlockPair>builder().add(BlockPair.of(frostNatural(ModBlocks.FROST.get().defaultBlockState().setValue(FrostBlock.getFaceProperty(Direction.DOWN), true))), 1).build())
                .predicate(new BiomeSetMatchTest(biomeLookup.getOrThrow(ModTags.ICY)), new IsDayTest(false))
                .save(output, ImmersiveWeathering.res("frost_growth"));
        configured(List.of(Blocks.STONE, Blocks.GRASS_BLOCK, ModBlocks.ROOTED_GRASS_BLOCK.get()), new BlockSetMatchTest(HolderSet.direct(Blocks.AIR.builtInRegistryHolder()), 0.4f), new AreaCheck(2, 2, 2, 5, Optional.empty(), Optional.of(new BlockMatchTest(ModBlocks.FROST.get())), Optional.of(new TagMatchTest(ModTags.MAGMA_SOURCE)), Optional.empty()))
                .directionalGrowth(Direction.UP, null, SimpleWeightedRandomList.<BlockPair>builder().add(BlockPair.of(frostNatural(ModBlocks.FROST.get().defaultBlockState().setValue(FrostBlock.getFaceProperty(Direction.DOWN), true))), 1).build())
                .predicate(new BiomeSetMatchTest(biomeLookup.getOrThrow(ModTags.ICY)), new IsDayTest(false))
                .save(output, ImmersiveWeathering.res("frost_spreading"));
        configured(BuiltInRegistries.BLOCK.getOrCreateTag(ModTags.GRASSY_BLOCKS), new RandomBlockMatchTest(Blocks.AIR, 0.8f), new AreaCheck(2, 2, 2, 4, Optional.empty(), Optional.of(new BlockSetMatchTest(BuiltInRegistries.BLOCK.getOrCreateTag(ModTags.GRASS_SPREAD_SOURCE), 1f)), Optional.empty(), Optional.empty()))
                .directionalGrowth(Direction.UP, null, SimpleWeightedRandomList.<BlockPair>builder().add(BlockPair.of(ModBlocks.DUNE_GRASS.get()), 5).add(BlockPair.of(Blocks.DEAD_BUSH), 1).add(BlockPair.of(Blocks.SHORT_GRASS), 0).build())
                .growthChance(0.001f)
                .predicate(new BiomeSetMatchTest(HolderSet.direct(biomeLookup.getOrThrow(Biomes.BADLANDS),biomeLookup.getOrThrow(Biomes.ERODED_BADLANDS),biomeLookup.getOrThrow(Biomes.DESERT))))
                .save(output, ImmersiveWeathering.res("grass_block_badlands"));
        configured(BuiltInRegistries.BLOCK.getOrCreateTag(ModTags.GRASSY_BLOCKS), new RandomBlockMatchTest(Blocks.AIR, 0.8f), new AreaCheck(2, 2, 2, 6, Optional.empty(), Optional.of(new BlockSetMatchTest(BuiltInRegistries.BLOCK.getOrCreateTag(ModTags.GRASS_SPREAD_SOURCE), 1f)), Optional.empty(), Optional.empty()))
                .directionalGrowth(Direction.UP, null, SimpleWeightedRandomList.<BlockPair>builder().add(BlockPair.of(Blocks.BAMBOO_SAPLING), 1).add(BlockPair.of(Blocks.FERN), 5).add(BlockPair.of(Blocks.MELON), 1).add(BlockPair.of(Blocks.SHORT_GRASS), 10).build())
                .growthChance(0.001f)
                .predicate(new BiomeSetMatchTest(HolderSet.direct(biomeLookup.getOrThrow(Biomes.BAMBOO_JUNGLE))))
                .save(output, ImmersiveWeathering.res("grass_block_bamboo_jungle"));
        configured(BuiltInRegistries.BLOCK.getOrCreateTag(ModTags.GRASSY_BLOCKS), new RandomBlockMatchTest(Blocks.AIR, 0.4f), new AreaCheck(2, 2, 2, 5, Optional.empty(), Optional.of(new BlockSetMatchTest(BuiltInRegistries.BLOCK.getOrCreateTag(ModTags.GRASS_SPREAD_SOURCE), 1f)), Optional.empty(), Optional.of(BuiltInRegistries.BLOCK.getOrCreateTag(ModTags.SMALL_PLANTS))))
                .directionalGrowth(Direction.UP, null, SimpleWeightedRandomList.<BlockPair>builder().add(BlockPair.of(Blocks.SHORT_GRASS), 1).build())
                .growthChance(0.003f)
                .save(output, ImmersiveWeathering.res("grass_block_base"));
        configured(BuiltInRegistries.BLOCK.getOrCreateTag(ModTags.GRASSY_BLOCKS), new RandomBlockMatchTest(Blocks.AIR, 0.8f), new AreaCheck(2, 2, 2, 6, Optional.empty(), Optional.of(new BlockSetMatchTest(BuiltInRegistries.BLOCK.getOrCreateTag(ModTags.GRASS_SPREAD_SOURCE), 1f)), Optional.empty(), Optional.of(BuiltInRegistries.BLOCK.getOrCreateTag(ModTags.SMALL_PLANTS))))
                .directionalGrowth(Direction.UP, null, SimpleWeightedRandomList.<BlockPair>builder()
                        .add(BlockPair.of(Blocks.DANDELION), 10)
                        .add(BlockPair.of(Blocks.LILY_OF_THE_VALLEY), 10)
                        .add(BlockPair.of(ModBlocks.WEEDS.get()), 10)
                        .add(BlockPair.of(Blocks.BROWN_MUSHROOM), 10)
                        .add(BlockPair.of(Blocks.CORNFLOWER), 10)
                        .add(BlockPair.of(Blocks.RED_MUSHROOM), 10)
                        .add(BlockPair.of(lower(Blocks.LILAC.defaultBlockState()), upper(Blocks.LILAC.defaultBlockState())), 70)
                        .add(BlockPair.of(Blocks.SHORT_GRASS), 100)
                        .build())
                .growthChance(0.001f)
                .predicate(new BiomeSetMatchTest(HolderSet.direct(biomeLookup.getOrThrow(Biomes.BIRCH_FOREST), biomeLookup.getOrThrow(Biomes.OLD_GROWTH_BIRCH_FOREST))))
                .save(output, ImmersiveWeathering.res("grass_block_birch_forest"));
        configured(BuiltInRegistries.BLOCK.getOrCreateTag(ModTags.GRASSY_BLOCKS), new RandomBlockMatchTest(Blocks.AIR, 0.8f), new AreaCheck(2, 2, 2, 6, Optional.empty(), Optional.of(new BlockSetMatchTest(BuiltInRegistries.BLOCK.getOrCreateTag(ModTags.GRASS_SPREAD_SOURCE), 1f)), Optional.empty(), Optional.of(BuiltInRegistries.BLOCK.getOrCreateTag(ModTags.SMALL_PLANTS))))
                .directionalGrowth(Direction.UP, null, SimpleWeightedRandomList.<BlockPair>builder()
                        .add(BlockPair.of(horizontal(Blocks.PINK_PETALS.defaultBlockState(), Direction.WEST)), 5)
                        .add(BlockPair.of(horizontal(Blocks.PINK_PETALS.defaultBlockState(), Direction.EAST)), 5)
                        .add(BlockPair.of(horizontal(Blocks.PINK_PETALS.defaultBlockState(), Direction.NORTH)), 5)
                        .add(BlockPair.of(horizontal(Blocks.PINK_PETALS.defaultBlockState(), Direction.SOUTH)), 5)
                        .add(BlockPair.of(Blocks.SHORT_GRASS), 50)
                        .build())
                .growthChance(0.001f)
                .predicate(new BiomeSetMatchTest(HolderSet.direct(biomeLookup.getOrThrow(Biomes.CHERRY_GROVE))))
                .save(output, ImmersiveWeathering.res("grass_block_cherry_grove"));
        configured(BuiltInRegistries.BLOCK.getOrCreateTag(ModTags.GRASSY_BLOCKS), new RandomBlockMatchTest(Blocks.AIR, 0.8f), new AreaCheck(2, 2, 2, 6, Optional.empty(), Optional.of(new BlockSetMatchTest(BuiltInRegistries.BLOCK.getOrCreateTag(ModTags.GRASS_SPREAD_SOURCE), 1f)), Optional.empty(), Optional.of(BuiltInRegistries.BLOCK.getOrCreateTag(ModTags.SMALL_PLANTS))))
                .directionalGrowth(Direction.UP, null, SimpleWeightedRandomList.<BlockPair>builder()
                        .add(BlockPair.of(Blocks.RED_MUSHROOM), 1000)
                        .add(BlockPair.of(Blocks.BROWN_MUSHROOM), 1000)
                        .add(BlockPair.of(Blocks.POPPY), 1000)
                        .add(BlockPair.of(lower(Blocks.ROSE_BUSH.defaultBlockState()), upper(Blocks.ROSE_BUSH.defaultBlockState())), 600)
                        .add(BlockPair.of(Blocks.DARK_OAK_FENCE.defaultBlockState(), horizontal(Blocks.CARVED_PUMPKIN.defaultBlockState(), Direction.NORTH)), 1)
                        .add(BlockPair.of(Blocks.DARK_OAK_FENCE.defaultBlockState(), horizontal(Blocks.CARVED_PUMPKIN.defaultBlockState(), Direction.EAST)), 1)
                        .add(BlockPair.of(Blocks.DARK_OAK_FENCE.defaultBlockState(), horizontal(Blocks.CARVED_PUMPKIN.defaultBlockState(), Direction.SOUTH)), 1)
                        .add(BlockPair.of(Blocks.DARK_OAK_FENCE.defaultBlockState(), horizontal(Blocks.CARVED_PUMPKIN.defaultBlockState(), Direction.WEST)), 1)
                        .add(BlockPair.of(Blocks.DARK_OAK_FENCE.defaultBlockState(), horizontal(Blocks.JACK_O_LANTERN.defaultBlockState(), Direction.NORTH)), 1)
                        .add(BlockPair.of(Blocks.DARK_OAK_FENCE.defaultBlockState(), horizontal(Blocks.JACK_O_LANTERN.defaultBlockState(), Direction.EAST)), 1)
                        .add(BlockPair.of(Blocks.DARK_OAK_FENCE.defaultBlockState(), horizontal(Blocks.JACK_O_LANTERN.defaultBlockState(), Direction.SOUTH)), 1)
                        .add(BlockPair.of(Blocks.DARK_OAK_FENCE.defaultBlockState(), horizontal(Blocks.JACK_O_LANTERN.defaultBlockState(), Direction.WEST)), 1)
                        .add(BlockPair.of(Blocks.SHORT_GRASS), 0)
                        .build())
                .growthChance(0.001f)
                .predicate(new BiomeSetMatchTest(HolderSet.direct(biomeLookup.getOrThrow(Biomes.DARK_FOREST))))
                .save(output, ImmersiveWeathering.res("grass_block_dark_forest"));
        configured(BuiltInRegistries.BLOCK.getOrCreateTag(ModTags.GRASSY_BLOCKS), new RandomBlockMatchTest(Blocks.AIR, 0.8f), new AreaCheck(2, 2, 2, 6, Optional.empty(), Optional.of(new BlockSetMatchTest(BuiltInRegistries.BLOCK.getOrCreateTag(ModTags.GRASS_SPREAD_SOURCE), 1f)), Optional.empty(), Optional.of(BuiltInRegistries.BLOCK.getOrCreateTag(ModTags.SMALL_PLANTS))))
                .directionalGrowth(Direction.UP, null, SimpleWeightedRandomList.<BlockPair>builder()
                        .add(BlockPair.of(Blocks.RED_TULIP), 10)
                        .add(BlockPair.of(Blocks.WHITE_TULIP), 10)
                        .add(BlockPair.of(Blocks.ORANGE_TULIP), 10)
                        .add(BlockPair.of(Blocks.PINK_TULIP), 10)
                        .add(BlockPair.of(Blocks.LILY_OF_THE_VALLEY), 10)
                        .add(BlockPair.of(Blocks.ALLIUM), 10)
                        .add(BlockPair.of(lower(Blocks.PEONY.defaultBlockState()), upper(Blocks.PEONY.defaultBlockState())), 50)
                        .add(BlockPair.of(lower(Blocks.LILAC.defaultBlockState()), upper(Blocks.LILAC.defaultBlockState())), 50)
                        .add(BlockPair.of(lower(Blocks.ROSE_BUSH.defaultBlockState()), upper(Blocks.ROSE_BUSH.defaultBlockState())), 50)
                        .add(BlockPair.of(Blocks.SHORT_GRASS), 100)
                        .build())
                .growthChance(0.001f)
                .predicate(new BiomeSetMatchTest(HolderSet.direct(biomeLookup.getOrThrow(Biomes.FLOWER_FOREST))))
                .save(output, ImmersiveWeathering.res("grass_block_flower_forest"));
        configured(BuiltInRegistries.BLOCK.getOrCreateTag(ModTags.GRASSY_BLOCKS), new RandomBlockMatchTest(Blocks.AIR, 0.8f), new AreaCheck(2, 2, 2, 6, Optional.empty(), Optional.of(new BlockSetMatchTest(BuiltInRegistries.BLOCK.getOrCreateTag(ModTags.GRASS_SPREAD_SOURCE), 1f)), Optional.empty(), Optional.of(BuiltInRegistries.BLOCK.getOrCreateTag(ModTags.SMALL_PLANTS))))
                .directionalGrowth(Direction.UP, null, SimpleWeightedRandomList.<BlockPair>builder()
                        .add(BlockPair.of(Blocks.POPPY), 10)
                        .add(BlockPair.of(Blocks.LILY_OF_THE_VALLEY), 10)
                        .add(BlockPair.of(Blocks.AZURE_BLUET), 10)
                        .add(BlockPair.of(lower(Blocks.PEONY.defaultBlockState()), upper(Blocks.PEONY.defaultBlockState())), 50)
                        .add(BlockPair.of(Blocks.SHORT_GRASS), 100)
                        .build())
                .growthChance(0.001f)
                .predicate(new BiomeSetMatchTest(HolderSet.direct(biomeLookup.getOrThrow(Biomes.FOREST), biomeLookup.getOrThrow(Biomes.WINDSWEPT_FOREST))))
                .save(output, ImmersiveWeathering.res("grass_block_forest"));
        configured(BuiltInRegistries.BLOCK.getOrCreateTag(ModTags.GRASSY_BLOCKS), new RandomBlockMatchTest(Blocks.AIR, 0.8f), new AreaCheck(2, 2, 2, 6, Optional.empty(), Optional.of(new BlockSetMatchTest(BuiltInRegistries.BLOCK.getOrCreateTag(ModTags.GRASS_SPREAD_SOURCE), 1f)), Optional.empty(), Optional.of(BuiltInRegistries.BLOCK.getOrCreateTag(ModTags.SMALL_PLANTS))))
                .directionalGrowth(Direction.UP, null, SimpleWeightedRandomList.<BlockPair>builder()
                        .add(BlockPair.of(Blocks.DANDELION), 1)
                        .add(BlockPair.of(Blocks.FERN), 5)
                        .add(BlockPair.of(Blocks.MELON), 1)
                        .add(BlockPair.of(Blocks.SHORT_GRASS), 0)
                        .build())
                .growthChance(0.001f)
                .predicate(new BiomeSetMatchTest(HolderSet.direct(biomeLookup.getOrThrow(Biomes.JUNGLE))))
                .save(output, ImmersiveWeathering.res("grass_block_jungle"));
        configured(BuiltInRegistries.BLOCK.getOrCreateTag(ModTags.GRASSY_BLOCKS), new RandomBlockMatchTest(Blocks.AIR, 0.8f), new AreaCheck(2, 2, 2, 6, Optional.empty(), Optional.of(new BlockSetMatchTest(BuiltInRegistries.BLOCK.getOrCreateTag(ModTags.GRASS_SPREAD_SOURCE), 1f)), Optional.empty(), Optional.of(BuiltInRegistries.BLOCK.getOrCreateTag(ModTags.SMALL_PLANTS))))
                .directionalGrowth(Direction.UP, null, SimpleWeightedRandomList.<BlockPair>builder()
                        .add(BlockPair.of(Blocks.AZALEA), 1)
                        .add(BlockPair.of(Blocks.FLOWERING_AZALEA), 1)
                        .add(BlockPair.of(Blocks.SHORT_GRASS), 10)
                        .build())
                .growthChance(0.001f)
                .predicate(new BiomeSetMatchTest(HolderSet.direct(biomeLookup.getOrThrow(Biomes.LUSH_CAVES))))
                .save(output, ImmersiveWeathering.res("grass_block_lush_caves"));
        configured(BuiltInRegistries.BLOCK.getOrCreateTag(ModTags.GRASSY_BLOCKS), new RandomBlockMatchTest(Blocks.AIR, 0.8f), new AreaCheck(2, 2, 2, 6, Optional.empty(), Optional.of(new BlockSetMatchTest(BuiltInRegistries.BLOCK.getOrCreateTag(ModTags.GRASS_SPREAD_SOURCE), 1f)), Optional.empty(), Optional.of(BuiltInRegistries.BLOCK.getOrCreateTag(ModTags.SMALL_PLANTS))))
                .directionalGrowth(Direction.UP, null, SimpleWeightedRandomList.<BlockPair>builder()
                        .add(BlockPair.of(Blocks.FERN), 4)
                        .add(BlockPair.of(Blocks.RED_MUSHROOM), 1)
                        .add(BlockPair.of(Blocks.BROWN_MUSHROOM), 1)
                        .add(BlockPair.of(Blocks.SHORT_GRASS), 10)
                        .build())
                .growthChance(0.001f)
                .predicate(new BiomeSetMatchTest(HolderSet.direct(biomeLookup.getOrThrow(Biomes.OLD_GROWTH_SPRUCE_TAIGA), biomeLookup.getOrThrow(Biomes.OLD_GROWTH_PINE_TAIGA))))
                .save(output, ImmersiveWeathering.res("grass_block_old_growth_spruce"));
        configured(BuiltInRegistries.BLOCK.getOrCreateTag(ModTags.GRASSY_BLOCKS), new RandomBlockMatchTest(Blocks.AIR, 0.8f), new AreaCheck(2, 2, 2, 6, Optional.empty(), Optional.of(new BlockSetMatchTest(BuiltInRegistries.BLOCK.getOrCreateTag(ModTags.GRASS_SPREAD_SOURCE), 1f)), Optional.empty(), Optional.of(BuiltInRegistries.BLOCK.getOrCreateTag(ModTags.SMALL_PLANTS))))
                .directionalGrowth(Direction.UP, null, SimpleWeightedRandomList.<BlockPair>builder()
                        .add(BlockPair.of(Blocks.DANDELION), 10)
                        .add(BlockPair.of(Blocks.AZURE_BLUET), 10)
                        .add(BlockPair.of(Blocks.OXEYE_DAISY), 10)
                        .add(BlockPair.of(Blocks.CORNFLOWER), 10)
                        .add(BlockPair.of(Blocks.PUMPKIN), 1)
                        .add(BlockPair.of(weeds(0)), 1)
                        .add(BlockPair.of(Blocks.SHORT_GRASS), 100)
                        .build())
                .growthChance(0.001f)
                .predicate(new BiomeSetMatchTest(HolderSet.direct(biomeLookup.getOrThrow(Biomes.PLAINS))))
                .save(output, ImmersiveWeathering.res("grass_block_plains"));
        configured(BuiltInRegistries.BLOCK.getOrCreateTag(ModTags.GRASSY_BLOCKS), new RandomBlockMatchTest(Blocks.AIR, 0.8f), new AreaCheck(2, 2, 2, 6, Optional.empty(), Optional.of(new BlockSetMatchTest(BuiltInRegistries.BLOCK.getOrCreateTag(ModTags.GRASS_SPREAD_SOURCE), 1f)), Optional.empty(), Optional.of(BuiltInRegistries.BLOCK.getOrCreateTag(ModTags.SMALL_PLANTS))))
                .directionalGrowth(Direction.UP, null, SimpleWeightedRandomList.<BlockPair>builder()
                        .add(BlockPair.of(Blocks.DANDELION), 10)
                        .add(BlockPair.of(Blocks.FERN), 50)
                        .add(BlockPair.of(Blocks.MELON), 10)
                        .add(BlockPair.of(Blocks.SHORT_GRASS), 1)
                        .build())
                .growthChance(0.001f)
                .predicate(new BiomeSetMatchTest(HolderSet.direct(biomeLookup.getOrThrow(Biomes.SPARSE_JUNGLE))))
                .save(output, ImmersiveWeathering.res("grass_block_sparse_jungle"));
        configured(BuiltInRegistries.BLOCK.getOrCreateTag(ModTags.GRASSY_BLOCKS), new RandomBlockMatchTest(Blocks.AIR, 0.8f), new AreaCheck(2, 2, 2, 6, Optional.empty(), Optional.of(new BlockSetMatchTest(BuiltInRegistries.BLOCK.getOrCreateTag(ModTags.GRASS_SPREAD_SOURCE), 1f)), Optional.empty(), Optional.of(BuiltInRegistries.BLOCK.getOrCreateTag(ModTags.SMALL_PLANTS))))
                .directionalGrowth(Direction.UP, null, SimpleWeightedRandomList.<BlockPair>builder()
                        .add(BlockPair.of(Blocks.DANDELION), 1)
                        .add(BlockPair.of(Blocks.POPPY), 1)
                        .add(BlockPair.of(Blocks.AZURE_BLUET), 1)
                        .add(BlockPair.of(Blocks.OXEYE_DAISY), 1)
                        .add(BlockPair.of(Blocks.CORNFLOWER), 1)
                        .add(BlockPair.of(lower(Blocks.SUNFLOWER.defaultBlockState()), upper(Blocks.SUNFLOWER.defaultBlockState())), 5)
                        .add(BlockPair.of(Blocks.SHORT_GRASS), 100)
                        .build())
                .growthChance(0.001f)
                .predicate(new BiomeSetMatchTest(HolderSet.direct(biomeLookup.getOrThrow(Biomes.SUNFLOWER_PLAINS))))
                .save(output, ImmersiveWeathering.res("grass_block_sunflower_plains"));
        configured(BuiltInRegistries.BLOCK.getOrCreateTag(ModTags.GRASSY_BLOCKS), new RandomBlockMatchTest(Blocks.AIR, 0.8f), new AreaCheck(2, 2, 2, 6, Optional.empty(), Optional.of(new BlockSetMatchTest(BuiltInRegistries.BLOCK.getOrCreateTag(ModTags.GRASS_SPREAD_SOURCE), 1f)), Optional.empty(), Optional.of(BuiltInRegistries.BLOCK.getOrCreateTag(ModTags.SMALL_PLANTS))))
                .directionalGrowth(Direction.UP, null, SimpleWeightedRandomList.<BlockPair>builder()
                        .add(BlockPair.of(Blocks.BLUE_ORCHID), 1)
                        .add(BlockPair.of(Blocks.SHORT_GRASS), 100)
                        .build())
                .growthChance(0.001f)
                .predicate(new BiomeSetMatchTest(HolderSet.direct(biomeLookup.getOrThrow(Biomes.SWAMP))))
                .save(output, ImmersiveWeathering.res("grass_block_swamp"));
        configured(BuiltInRegistries.BLOCK.getOrCreateTag(ModTags.GRASSY_BLOCKS), new RandomBlockMatchTest(Blocks.AIR, 0.8f), new AreaCheck(2, 2, 2, 6, Optional.empty(), Optional.of(new BlockSetMatchTest(BuiltInRegistries.BLOCK.getOrCreateTag(ModTags.GRASS_SPREAD_SOURCE), 1f)), Optional.empty(), Optional.of(BuiltInRegistries.BLOCK.getOrCreateTag(ModTags.SMALL_PLANTS))))
                .directionalGrowth(Direction.UP, null, SimpleWeightedRandomList.<BlockPair>builder()
                        .add(BlockPair.of(Blocks.FERN), 100)
                        .add(BlockPair.of(Blocks.SWEET_BERRY_BUSH), 30)
                        .add(BlockPair.of(Blocks.SHORT_GRASS), 80)
                        .build())
                .growthChance(0.001f)
                .predicate(new BiomeSetMatchTest(HolderSet.direct(biomeLookup.getOrThrow(Biomes.TAIGA))))
                .save(output, ImmersiveWeathering.res("grass_block_taiga"));
        configured(BuiltInRegistries.BLOCK.getOrCreateTag(ModTags.GRASSY_BLOCKS), new RandomBlockMatchTest(Blocks.AIR, 0.8f), new AreaCheck(2, 2, 2, 6, Optional.empty(), Optional.of(new BlockSetMatchTest(BuiltInRegistries.BLOCK.getOrCreateTag(ModTags.GRASS_SPREAD_SOURCE), 1f)), Optional.empty(), Optional.of(BuiltInRegistries.BLOCK.getOrCreateTag(ModTags.SMALL_PLANTS))))
                .directionalGrowth(Direction.UP, null, SimpleWeightedRandomList.<BlockPair>builder()
                        .add(BlockPair.of(ModBlocks.DUNE_GRASS.get()), 50)
                        .add(BlockPair.of(Blocks.DEAD_BUSH), 30)
                        .add(BlockPair.of(Blocks.SHORT_GRASS), 100)
                        .build())
                .growthChance(0.0005f)
                .predicate(new BiomeSetMatchTest(HolderSet.direct(biomeLookup.getOrThrow(Biomes.WOODED_BADLANDS), biomeLookup.getOrThrow(Biomes.SAVANNA), biomeLookup.getOrThrow(Biomes.SAVANNA_PLATEAU), biomeLookup.getOrThrow(Biomes.WINDSWEPT_SAVANNA))))
                .save(output, ImmersiveWeathering.res("grass_block_wooded_badlands"));
    }

    private BlockState waterlog(BlockState state) {
        return state.setValue(BlockStateProperties.WATERLOGGED, true);
    }

    private BlockState horizontal(BlockState state, Direction direction) {
        return state.setValue(HorizontalDirectionalBlock.FACING, direction.getOpposite());
    }

    private BlockState face(BlockState state, Direction direction) {
        return state.setValue(DirectionalBlock.FACING, direction.getOpposite());
    }

    private BlockState frostNatural(BlockState state) {
        return state.setValue(Frosty.NATURAL, true);
    }

    private BlockState lower(BlockState state) {
        return state.setValue(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.LOWER);
    }

    private BlockState upper(BlockState state) {
        return state.setValue(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.UPPER);
    }

    private BlockState weeds(int age) {
        return ModBlocks.WEEDS.get().defaultBlockState().setValue(WeedsBlock.AGE, age);
    }
}
