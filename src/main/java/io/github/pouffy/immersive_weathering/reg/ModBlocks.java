package io.github.pouffy.immersive_weathering.reg;

import io.github.pouffy.immersive_weathering.ImmersiveWeathering;
import io.github.pouffy.immersive_weathering.blocks.*;
import io.github.pouffy.immersive_weathering.blocks.charred.*;
import io.github.pouffy.immersive_weathering.blocks.cracked.CrackedBlock;
import io.github.pouffy.immersive_weathering.blocks.cracked.CrackedSlabBlock;
import io.github.pouffy.immersive_weathering.blocks.cracked.CrackedStairsBlock;
import io.github.pouffy.immersive_weathering.blocks.cracked.CrackedWallBlock;
import io.github.pouffy.immersive_weathering.blocks.frosted.FrostBlock;
import io.github.pouffy.immersive_weathering.blocks.frosted.FrostyGlassBlock;
import io.github.pouffy.immersive_weathering.blocks.frosted.FrostyGlassPaneBlock;
import io.github.pouffy.immersive_weathering.blocks.frosted.FrostyGrassBlock;
import io.github.pouffy.immersive_weathering.blocks.mossy.*;
import io.github.pouffy.immersive_weathering.blocks.rusty.*;
import io.github.pouffy.immersive_weathering.blocks.sandy.SandyBlock;
import io.github.pouffy.immersive_weathering.blocks.sandy.SandySlabBlock;
import io.github.pouffy.immersive_weathering.blocks.sandy.SandyStairsBlock;
import io.github.pouffy.immersive_weathering.blocks.sandy.SandyWallBlock;
import io.github.pouffy.immersive_weathering.blocks.snowy.SnowyBlock;
import io.github.pouffy.immersive_weathering.blocks.snowy.SnowySlabBlock;
import io.github.pouffy.immersive_weathering.blocks.snowy.SnowyStairsBlock;
import io.github.pouffy.immersive_weathering.blocks.snowy.SnowyWallBlock;
import io.github.pouffy.immersive_weathering.blocks.soil_types.*;
import io.github.pouffy.immersive_weathering.util.ModUtils;
import net.mehvahdjukaar.moonlight.api.block.ModStairBlock;
import net.mehvahdjukaar.moonlight.api.misc.Registrator;
import net.mehvahdjukaar.moonlight.api.set.BlockSetAPI;
import net.mehvahdjukaar.moonlight.api.set.leaves.LeavesType;
import net.mehvahdjukaar.moonlight.api.set.leaves.VanillaLeavesTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;

@SuppressWarnings("removal")
public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = ModUtils.createRegister(DeferredRegister::createBlocks);

    private static boolean always(BlockState state, BlockGetter blockGetter, BlockPos pos) {
        return true;
    }

    public static <T extends Block> DeferredBlock<T> regBlock(String name, Supplier<T> block) {
        return BLOCKS.register(name, block);
    }

    public static <T extends Block> DeferredBlock<T> regWithItem(String name, Supplier<T> blockFactory) {
        DeferredBlock<T> block = regBlock(name, blockFactory);
        regBlockItem(block, new Item.Properties());
        return block;
    }

    public static DeferredItem<BlockItem> regBlockItem(DeferredBlock<? extends Block> blockSup, Item.Properties properties) {
        return ModItems.regBlockItem(blockSup, properties);
    }

    //predicates

    private static ToIntFunction<BlockState> litLightLevel(int litLevel) {
        return (state) -> state.getValue(ModBlockProperties.SMOLDERING) ? litLevel : 0;
    }

    private static ToIntFunction<BlockState> moltenLightLevel(int litLevel) {
        return (state) -> state.getValue(ModBlockProperties.MOLTEN) ? litLevel : 0;
    }

    private static final BlockBehaviour.StateArgumentPredicate<EntityType<?>> CAN_SPAWN_ON_LEAVES = (a, b, c, t) ->
            t == EntityType.OCELOT || t == EntityType.PARROT;

    private static final BlockBehaviour.StatePredicate NEVER = (s, w, p) -> false;

    public static final BlockBehaviour.Properties LEAF_PILE_PROPERTIES = BlockBehaviour.Properties.of()
            .randomTicks().instabreak().sound(SoundType.GRASS)
            .noOcclusion().isValidSpawn(CAN_SPAWN_ON_LEAVES)
            .isSuffocating(NEVER).isViewBlocking(NEVER);


    public static final Map<LeavesType, LeafPileBlock> LEAF_PILES = new LinkedHashMap<>();

    public static final DeferredBlock<LeafPileBlock> AZALEA_FLOWER_PILE = regBlock("azalea_flower_pile", () ->
            new LeafPileBlock(LEAF_PILE_PROPERTIES.sound(SoundType.AZALEA), VanillaLeavesTypes.OAK));

    //layer stuff

    public static final DeferredBlock<Block> SAND_LAYER_BLOCK = regWithItem("sand_layer_block", () ->
            new SandLayerBlock(14406560, BlockBehaviour.Properties.ofFullCopy(Blocks.SAND).strength(0.5f)
                    .sound(SoundType.SAND).isSuffocating(NEVER)
                    .isViewBlocking((blockState, blockView, blockPos) -> blockState.getValue(LayerBlock.LAYERS_8) >= 8)
                    .noOcclusion().requiresCorrectToolForDrops()));
    public static final DeferredBlock<Block> RED_SAND_LAYER_BLOCK = regWithItem("red_sand_layer_block", () ->
            new SandLayerBlock(11098145, BlockBehaviour.Properties.ofFullCopy(Blocks.RED_SAND).strength(0.5f)
                    .sound(SoundType.SAND).isSuffocating(NEVER)
                    .isViewBlocking((blockState, blockView, blockPos) -> blockState.getValue(LayerBlock.LAYERS_8) >= 8)
                    .noOcclusion().requiresCorrectToolForDrops()));

    //vegetation

    public static final DeferredBlock<Block> MOSS = regBlock("moss", () ->
            new MossMultifaceBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.MOSS_BLOCK).randomTicks().instabreak().sound(SoundType.MOSS_CARPET).noOcclusion().noCollission()));

    public static final DeferredBlock<Block> WEEDS = regWithItem("weeds", () ->
            new WeedsBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SHORT_GRASS).noCollission().instabreak().sound(SoundType.GRASS)));

    public static final DeferredBlock<Block> HANGING_ROOTS_WALL = regBlock("hanging_roots_wall", () ->
            new WallRootsBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.HANGING_ROOTS)));

    public static final DeferredBlock<IvyBlock> IVY = regWithItem("ivy", () ->
            new IvyBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.VINE).noCollission().strength(0.2f)
                    .sound(SoundType.AZALEA_LEAVES)));

    public static final DeferredBlock<Block> DUNE_GRASS = regWithItem("dune_grass", () ->
            new DuneGrassBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SHORT_GRASS).noCollission().instabreak().sound(SoundType.GRASS)));

    //mossy blocks

    public static final DeferredBlock<Block> MOSSY_BRICKS = regWithItem("mossy_bricks", () ->
            new MossyBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BRICKS)
                    .requiresCorrectToolForDrops().strength(2f, 6f)));
    public static final DeferredBlock<Block> MOSSY_BRICK_STAIRS = regWithItem("mossy_brick_stairs", () ->
            new MossyStairsBlock(MOSSY_BRICKS, BlockBehaviour.Properties.ofFullCopy(MOSSY_BRICKS.get())));
    public static final DeferredBlock<Block> MOSSY_BRICK_SLAB = regWithItem("mossy_brick_slab", () ->
            new MossySlabBlock(BlockBehaviour.Properties.ofFullCopy(MOSSY_BRICKS.get())));
    public static final DeferredBlock<Block> MOSSY_BRICK_WALL = regWithItem("mossy_brick_wall", () ->
            new MossyWallBlock(BlockBehaviour.Properties.ofFullCopy(MOSSY_BRICKS.get())));

    public static final DeferredBlock<Block> MOSSY_STONE = regWithItem("mossy_stone", () ->
            new MossyBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE).requiresCorrectToolForDrops().strength(1.5f, 6f)));
    public static final DeferredBlock<Block> MOSSY_CHISELED_STONE_BRICKS = regWithItem("mossy_chiseled_stone_bricks", () ->
            new MossyBlock(BlockBehaviour.Properties.ofFullCopy(MOSSY_STONE.get())));
    public static final DeferredBlock<Block> MOSSY_STONE_STAIRS = regWithItem("mossy_stone_stairs", () ->
            new MossyStairsBlock(MOSSY_STONE, BlockBehaviour.Properties.ofFullCopy(MOSSY_STONE.get())));
    public static final DeferredBlock<Block> MOSSY_STONE_SLAB = regWithItem("mossy_stone_slab", () ->
            new MossySlabBlock(BlockBehaviour.Properties.ofFullCopy(MOSSY_STONE.get())));
    public static final DeferredBlock<Block> MOSSY_STONE_WALL = regWithItem("mossy_stone_wall", () ->
            new MossyWallBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLESTONE_WALL)));

    //snowy blocks

    public static final DeferredBlock<Block> SNOWY_STONE = regWithItem("snowy_stone", () ->
            new SnowyBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE).randomTicks()));
    public static final DeferredBlock<Block> SNOWY_STONE_STAIRS = regWithItem("snowy_stone_stairs", () ->
            new SnowyStairsBlock(SNOWY_STONE, BlockBehaviour.Properties.ofFullCopy(SNOWY_STONE.get()).randomTicks()));
    public static final DeferredBlock<Block> SNOWY_STONE_SLAB = regWithItem("snowy_stone_slab", () ->
            new SnowySlabBlock(BlockBehaviour.Properties.ofFullCopy(SNOWY_STONE.get()).randomTicks()));
    public static final DeferredBlock<Block> SNOWY_STONE_WALL = regWithItem("snowy_stone_wall", () ->
            new SnowyWallBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLESTONE_WALL).randomTicks()));

    public static final DeferredBlock<Block> SNOWY_COBBLESTONE = regWithItem("snowy_cobblestone", () ->
            new SnowyBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLESTONE).randomTicks()));
    public static final DeferredBlock<Block> SNOWY_COBBLESTONE_STAIRS = regWithItem("snowy_cobblestone_stairs", () ->
            new SnowyStairsBlock(SNOWY_STONE, BlockBehaviour.Properties.ofFullCopy(SNOWY_STONE.get()).randomTicks()));
    public static final DeferredBlock<Block> SNOWY_COBBLESTONE_SLAB = regWithItem("snowy_cobblestone_slab", () ->
            new SnowySlabBlock(BlockBehaviour.Properties.ofFullCopy(SNOWY_STONE.get()).randomTicks()));
    public static final DeferredBlock<Block> SNOWY_COBBLESTONE_WALL = regWithItem("snowy_cobblestone_wall", () ->
            new SnowyWallBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLESTONE_WALL).randomTicks()));

    public static final DeferredBlock<Block> SNOWY_STONE_BRICKS = regWithItem("snowy_stone_bricks", () ->
            new SnowyBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE).randomTicks()));
    public static final DeferredBlock<Block> SNOWY_CHISELED_STONE_BRICKS = regWithItem("snowy_chiseled_stone_bricks", () ->
            new SnowyBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE).randomTicks()));
    public static final DeferredBlock<Block> SNOWY_STONE_BRICK_STAIRS = regWithItem("snowy_stone_brick_stairs", () ->
            new SnowyStairsBlock(SNOWY_STONE, BlockBehaviour.Properties.ofFullCopy(SNOWY_STONE.get()).randomTicks()));
    public static final DeferredBlock<Block> SNOWY_STONE_BRICK_SLAB = regWithItem("snowy_stone_brick_slab", () ->
            new SnowySlabBlock(BlockBehaviour.Properties.ofFullCopy(SNOWY_STONE.get()).randomTicks()));
    public static final DeferredBlock<Block> SNOWY_STONE_BRICK_WALL = regWithItem("snowy_stone_brick_wall", () ->
            new SnowyWallBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLESTONE_WALL).randomTicks()));

    //sandy blocks

    public static final DeferredBlock<Block> SANDY_STONE = regWithItem("sandy_stone", () ->
            new SandyBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE).randomTicks()));
    public static final DeferredBlock<Block> SANDY_STONE_STAIRS = regWithItem("sandy_stone_stairs", () ->
            new SandyStairsBlock(SANDY_STONE, BlockBehaviour.Properties.ofFullCopy(SANDY_STONE.get()).randomTicks()));
    public static final DeferredBlock<Block> SANDY_STONE_SLAB = regWithItem("sandy_stone_slab", () ->
            new SandySlabBlock(BlockBehaviour.Properties.ofFullCopy(SANDY_STONE.get()).randomTicks()));
    public static final DeferredBlock<Block> SANDY_STONE_WALL = regWithItem("sandy_stone_wall", () ->
            new SandyWallBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLESTONE_WALL).randomTicks()));

    public static final DeferredBlock<Block> SANDY_COBBLESTONE = regWithItem("sandy_cobblestone", () ->
            new SandyBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLESTONE).randomTicks()));
    public static final DeferredBlock<Block> SANDY_COBBLESTONE_STAIRS = regWithItem("sandy_cobblestone_stairs", () ->
            new SandyStairsBlock(SANDY_STONE, BlockBehaviour.Properties.ofFullCopy(SANDY_STONE.get()).randomTicks()));
    public static final DeferredBlock<Block> SANDY_COBBLESTONE_SLAB = regWithItem("sandy_cobblestone_slab", () ->
            new SandySlabBlock(BlockBehaviour.Properties.ofFullCopy(SANDY_STONE.get()).randomTicks()));
    public static final DeferredBlock<Block> SANDY_COBBLESTONE_WALL = regWithItem("sandy_cobblestone_wall", () ->
            new SandyWallBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLESTONE_WALL).randomTicks()));

    public static final DeferredBlock<Block> SANDY_STONE_BRICKS = regWithItem("sandy_stone_bricks", () ->
            new SandyBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE).randomTicks()));
    public static final DeferredBlock<Block> SANDY_CHISELED_STONE_BRICKS = regWithItem("sandy_chiseled_stone_bricks", () ->
            new SandyBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE).randomTicks()));
    public static final DeferredBlock<Block> SANDY_STONE_BRICK_STAIRS = regWithItem("sandy_stone_brick_stairs", () ->
            new SandyStairsBlock(SANDY_STONE, BlockBehaviour.Properties.ofFullCopy(SANDY_STONE.get()).randomTicks()));
    public static final DeferredBlock<Block> SANDY_STONE_BRICK_SLAB = regWithItem("sandy_stone_brick_slab", () ->
            new SandySlabBlock(BlockBehaviour.Properties.ofFullCopy(SANDY_STONE.get()).randomTicks()));
    public static final DeferredBlock<Block> SANDY_STONE_BRICK_WALL = regWithItem("sandy_stone_brick_wall", () ->
            new SandyWallBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLESTONE_WALL).randomTicks()));

    //cracked blocks

    public static final DeferredBlock<Block> CRACKED_BRICKS = regWithItem("cracked_bricks", () ->
            new CrackedBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BRICKS).requiresCorrectToolForDrops().strength(2f, 6f)));
    public static final DeferredBlock<Block> CRACKED_BRICK_STAIRS = regWithItem("cracked_brick_stairs", () ->
            new CrackedStairsBlock(CRACKED_BRICKS, BlockBehaviour.Properties.ofFullCopy(CRACKED_BRICKS.get())));
    public static final DeferredBlock<Block> CRACKED_BRICK_SLAB = regWithItem("cracked_brick_slab", () ->
            new CrackedSlabBlock(BlockBehaviour.Properties.ofFullCopy(CRACKED_BRICKS.get())));
    public static final DeferredBlock<Block> CRACKED_BRICK_WALL = regWithItem("cracked_brick_wall", () ->
            new CrackedWallBlock(BlockBehaviour.Properties.ofFullCopy(CRACKED_BRICKS.get())));

    public static final DeferredBlock<Block> CRACKED_STONE_BRICK_STAIRS = regWithItem("cracked_stone_brick_stairs", () ->
            new CrackedStairsBlock(() -> Blocks.CRACKED_STONE_BRICKS, BlockBehaviour.Properties.ofFullCopy(Blocks.CRACKED_STONE_BRICKS)));
    public static final DeferredBlock<Block> CRACKED_STONE_BRICK_SLAB = regWithItem("cracked_stone_brick_slab", () ->
            new CrackedSlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CRACKED_STONE_BRICKS)));
    public static final DeferredBlock<Block> CRACKED_STONE_BRICK_WALL = regWithItem("cracked_stone_brick_wall", () ->
            new CrackedWallBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CRACKED_STONE_BRICKS)));

    public static final DeferredBlock<Block> CRACKED_POLISHED_BLACKSTONE_BRICK_STAIRS = regWithItem("cracked_polished_blackstone_brick_stairs", () ->
            new CrackedStairsBlock(() -> Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS, BlockBehaviour.Properties.ofFullCopy(Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS)));
    public static final DeferredBlock<Block> CRACKED_POLISHED_BLACKSTONE_BRICK_SLAB = regWithItem("cracked_polished_blackstone_brick_slab", () ->
            new CrackedSlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS)));
    public static final DeferredBlock<Block> CRACKED_POLISHED_BLACKSTONE_BRICK_WALL = regWithItem("cracked_polished_blackstone_brick_wall", () ->
            new CrackedWallBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS)));

    public static final DeferredBlock<Block> CRACKED_NETHER_BRICK_STAIRS = regWithItem("cracked_nether_brick_stairs", () ->
            new CrackedStairsBlock(() -> Blocks.CRACKED_NETHER_BRICKS, BlockBehaviour.Properties.ofFullCopy(Blocks.NETHER_BRICKS)));
    public static final DeferredBlock<Block> CRACKED_NETHER_BRICK_SLAB = regWithItem("cracked_nether_brick_slab", () ->
            new CrackedSlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.NETHER_BRICKS)));
    public static final DeferredBlock<Block> CRACKED_NETHER_BRICK_WALL = regWithItem("cracked_nether_brick_wall", () ->
            new CrackedWallBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.NETHER_BRICKS)));

    public static final DeferredBlock<Block> CRACKED_DEEPSLATE_BRICK_STAIRS = regWithItem("cracked_deepslate_brick_stairs", () ->
            new CrackedStairsBlock(() -> Blocks.CRACKED_DEEPSLATE_BRICKS, BlockBehaviour.Properties.ofFullCopy(Blocks.CRACKED_DEEPSLATE_BRICKS)));
    public static final DeferredBlock<Block> CRACKED_DEEPSLATE_BRICK_SLAB = regWithItem("cracked_deepslate_brick_slab", () ->
            new CrackedSlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CRACKED_DEEPSLATE_BRICKS)));
    public static final DeferredBlock<Block> CRACKED_DEEPSLATE_BRICK_WALL = regWithItem("cracked_deepslate_brick_wall", () ->
            new CrackedWallBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CRACKED_DEEPSLATE_BRICKS)));

    public static final DeferredBlock<Block> CRACKED_DEEPSLATE_TILE_STAIRS = regWithItem("cracked_deepslate_tile_stairs", () ->
            new CrackedStairsBlock(() -> Blocks.CRACKED_DEEPSLATE_TILES, BlockBehaviour.Properties.ofFullCopy(Blocks.CRACKED_DEEPSLATE_TILES)));
    public static final DeferredBlock<Block> CRACKED_DEEPSLATE_TILE_SLAB = regWithItem("cracked_deepslate_tile_slab", () ->
            new CrackedSlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CRACKED_DEEPSLATE_TILES)));
    public static final DeferredBlock<Block> CRACKED_DEEPSLATE_TILE_WALL = regWithItem("cracked_deepslate_tile_wall", () ->
            new CrackedWallBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CRACKED_DEEPSLATE_TILES)));

    public static final DeferredBlock<Block> CRACKED_END_STONE_BRICKS = regWithItem("cracked_end_stone_bricks", () ->
            new CrackedBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.END_STONE)));
    public static final DeferredBlock<Block> CRACKED_END_STONE_BRICK_STAIRS = regWithItem("cracked_end_stone_brick_stairs", () ->
            new CrackedStairsBlock(CRACKED_END_STONE_BRICKS, BlockBehaviour.Properties.ofFullCopy(Blocks.END_STONE)));
    public static final DeferredBlock<Block> CRACKED_END_STONE_BRICK_SLAB = regWithItem("cracked_end_stone_brick_slab", () ->
            new CrackedSlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.END_STONE)));
    public static final DeferredBlock<Block> CRACKED_END_STONE_BRICK_WALL = regWithItem("cracked_end_stone_brick_wall", () ->
            new CrackedWallBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.END_STONE)));


    public static final DeferredBlock<Block> CRACKED_PRISMARINE_BRICKS = regWithItem("cracked_prismarine_bricks", () ->
            new CrackedBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.PRISMARINE)));
    public static final DeferredBlock<Block> CRACKED_PRISMARINE_BRICK_STAIRS = regWithItem("cracked_prismarine_brick_stairs", () ->
            new CrackedStairsBlock(CRACKED_END_STONE_BRICKS, BlockBehaviour.Properties.ofFullCopy(Blocks.PRISMARINE)));
    public static final DeferredBlock<Block> CRACKED_PRISMARINE_BRICK_SLAB = regWithItem("cracked_prismarine_brick_slab", () ->
            new CrackedSlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.PRISMARINE)));
    public static final DeferredBlock<Block> CRACKED_PRISMARINE_BRICK_WALL = regWithItem("cracked_prismarine_brick_wall", () ->
            new CrackedWallBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.PRISMARINE)));

    //vitrified sand

    public static final DeferredBlock<Block> FULGURITE = regWithItem("fulgurite", () ->
            new FulguriteBlock(7, 3, BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS)
                    .instabreak().lightLevel((s) -> s.getValue(FulguriteBlock.POWERED) ? 5 : 0)
                    .dynamicShape().requiresCorrectToolForDrops()));

    public static final DeferredBlock<Block> VITRIFIED_SAND = regWithItem("vitrified_sand", () ->
            new TransparentBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS)
                    .strength(2f, 6f).sound(SoundType.TUFF)
                    .requiresCorrectToolForDrops().noOcclusion().isViewBlocking((s, l, p) -> false)));

    //soil blocks

    public static final DeferredBlock<Block> MULCH_BLOCK = regWithItem("mulch_block", () ->
            new MulchBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.DIRT).strength(1f, 1f)
                    .sound(SoundType.ROOTED_DIRT).randomTicks()));
    public static final DeferredBlock<Block> NULCH_BLOCK = regWithItem("nulch_block", () ->
            new NulchBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.DIRT).strength(1f, 1f)
                    .sound(SoundType.NETHER_WART).lightLevel(moltenLightLevel(10)).randomTicks()));

    public static final DeferredBlock<Block> SILT = regWithItem("silt", () ->
            new SiltBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.DIRT).strength(0.5f).sound(SoundType.MUD)));
    public static final DeferredBlock<Block> GRASSY_SILT = regWithItem("grassy_silt", () ->
            new SiltBlockGrassy(BlockBehaviour.Properties.ofFullCopy(Blocks.DIRT).strength(0.5f).sound(SoundType.MUD)));
    public static final DeferredBlock<Block> SILTY_FARMLAND = regWithItem("silty_farmland", () ->
            new SiltyFarmlandBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.DIRT).strength(0.5f).sound(SoundType.MUD)));

    public static final DeferredBlock<Block> SANDY_DIRT = regWithItem("sandy_dirt", () ->
            new SandyDirtBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.DIRT).strength(0.5f).sound(SoundType.SAND)));
    public static final DeferredBlock<Block> GRASSY_SANDY_DIRT = regWithItem("grassy_sandy_dirt", () ->
            new SandyDirtBlockGrassy(BlockBehaviour.Properties.ofFullCopy(Blocks.DIRT).strength(0.5f).sound(SoundType.SAND)));
    public static final DeferredBlock<Block> SANDY_FARMLAND = regWithItem("sandy_farmland", () ->
            new SandyFarmlandBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.DIRT).strength(0.5f).sound(SoundType.SAND)));

    public static final DeferredBlock<Block> EARTHEN_CLAY = regWithItem("earthen_clay", () ->
            new EarthenClayBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.DIRT).strength(0.5f).sound(SoundType.BASALT)));
    public static final DeferredBlock<Block> GRASSY_EARTHEN_CLAY = regWithItem("grassy_earthen_clay", () ->
            new EarthenClayBlockGrassy(BlockBehaviour.Properties.ofFullCopy(Blocks.DIRT).strength(0.5f).sound(SoundType.BASALT)));
    public static final DeferredBlock<Block> EARTHEN_CLAY_FARMLAND = regWithItem("earthen_clay_farmland", () ->
            new EarthenClayFarmlandBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.DIRT).strength(0.5f).sound(SoundType.BASALT)));

    public static final DeferredBlock<Block> LOAM = regWithItem("loam", () ->
            new LoamBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.DIRT).strength(0.5f).sound(SoundType.GRAVEL)));
    public static final DeferredBlock<Block> LOAMY_FARMLAND = regWithItem("loamy_farmland", () ->
            new LoamyFarmlandBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.DIRT).strength(0.5f).sound(SoundType.GRAVEL)));

    public static final DeferredBlock<Block> PERMAFROST = regWithItem("permafrost", () ->
            new PermafrostBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.DIRT).strength(0.5f).sound(SoundType.CALCITE)));
    public static final DeferredBlock<Block> GRASSY_PERMAFROST = regWithItem("grassy_permafrost", () ->
            new PermafrostBlockGrassy(BlockBehaviour.Properties.ofFullCopy(Blocks.DIRT).strength(0.5f).sound(SoundType.CALCITE)));


    public static final DeferredBlock<Block> ROOTED_GRASS_BLOCK = regWithItem("rooted_grass_block", () ->
            new RootedGrassBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GRASS_BLOCK).randomTicks().strength(0.5F)
                    .sound(SoundType.ROOTED_DIRT)));


    //frost and ice

    public static final DeferredBlock<Block> ICICLE = regBlock("icicle", () ->
            new IcicleBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.ICE).randomTicks().instabreak()
                    .sound(SoundType.GLASS).noOcclusion().dynamicShape()));
    public static final DeferredBlock<Block> THIN_ICE = regBlock("thin_ice", () ->
            new ThinIceBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.ICE)
                    .isViewBlocking(NEVER).isSuffocating(NEVER).isViewBlocking(NEVER)));

    public static final DeferredBlock<Block> FROST = regBlock("frost", () ->
            new FrostBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SNOW)
                    .randomTicks().instabreak().sound(SoundType.POWDER_SNOW).noOcclusion().noCollission()));
    public static final DeferredBlock<Block> FROSTY_GRASS = regWithItem("frosty_grass", () ->
            new FrostyGrassBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SHORT_GRASS)
                    .randomTicks().sound(SoundType.POWDER_SNOW)));
    public static final DeferredBlock<Block> FROSTY_FERN = regWithItem("frosty_fern", () ->
            new FrostyGrassBlock(BlockBehaviour.Properties.ofFullCopy(FROSTY_GRASS.get())));
    public static final DeferredBlock<Block> FROSTY_GLASS = regWithItem("frosty_glass", () ->
            new FrostyGlassBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS).randomTicks()));
    public static final DeferredBlock<Block> FROSTY_GLASS_PANE = regWithItem("frosty_glass_pane", () ->
            new FrostyGlassPaneBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS_PANE).randomTicks()));


    //charred blocks
    public static final DeferredBlock<Block> SOOT = regWithItem("soot", () ->
            new SootBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SCULK_VEIN).noCollission().instabreak().sound(SoundType.SNOW).randomTicks()));

    public static final DeferredBlock<Block> CHARRED_LOG = regWithItem("charred_log", () ->
            new CharredPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BASALT)
                    .strength(1.5f, 0.5f).sound(SoundType.BASALT)
                    .lightLevel(litLightLevel(5)).randomTicks()));
    public static final DeferredBlock<Block> CHARRED_PLANKS = regWithItem("charred_planks", () ->
            new CharredBlock(BlockBehaviour.Properties.ofFullCopy(CHARRED_LOG.get())));
    public static final DeferredBlock<Block> CHARRED_SLAB = regWithItem("charred_slab", () ->
            new CharredSlabBlock(BlockBehaviour.Properties.ofFullCopy(CHARRED_LOG.get())));
    public static final DeferredBlock<Block> CHARRED_STAIRS = regWithItem("charred_stairs", () ->
            new CharredStairsBlock(CHARRED_PLANKS, BlockBehaviour.Properties.ofFullCopy(CHARRED_LOG.get())));
    public static final DeferredBlock<Block> CHARRED_FENCE = regWithItem("charred_fence", () ->
            new CharredFenceBlock(BlockBehaviour.Properties.ofFullCopy(CHARRED_LOG.get())));
    public static final DeferredBlock<Block> CHARRED_FENCE_GATE = regWithItem("charred_fence_gate", () ->
            new CharredFenceGateBlock(BlockBehaviour.Properties.ofFullCopy(CHARRED_LOG.get()), WoodType.OAK));


    //cut iron

    public static final DeferredBlock<Block> CUT_IRON = regWithItem("cut_iron", () ->
            new RustableBlock(IRusty.RustLevel.UNAFFECTED, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)));
    public static final DeferredBlock<Block> EXPOSED_CUT_IRON = regWithItem("exposed_cut_iron", () ->
            new RustableBlock(IRusty.RustLevel.EXPOSED, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)));
    public static final DeferredBlock<Block> WEATHERED_CUT_IRON = regWithItem("weathered_cut_iron", () ->
            new RustableBlock(IRusty.RustLevel.WEATHERED, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)));
    public static final DeferredBlock<Block> RUSTED_CUT_IRON = regWithItem("rusted_cut_iron", () ->
            new RustableBlock(IRusty.RustLevel.RUSTED, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)));

    public static final DeferredBlock<Block> CUT_IRON_STAIRS = regWithItem("cut_iron_stairs", () ->
            new RustableStairsBlock(IRusty.RustLevel.UNAFFECTED, CUT_IRON, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)));
    public static final DeferredBlock<Block> EXPOSED_CUT_IRON_STAIRS = regWithItem("exposed_cut_iron_stairs", () ->
            new RustableStairsBlock(IRusty.RustLevel.EXPOSED, CUT_IRON, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)));
    public static final DeferredBlock<Block> WEATHERED_CUT_IRON_STAIRS = regWithItem("weathered_cut_iron_stairs", () ->
            new RustableStairsBlock(IRusty.RustLevel.WEATHERED, CUT_IRON, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)));
    public static final DeferredBlock<Block> RUSTED_CUT_IRON_STAIRS = regWithItem("rusted_cut_iron_stairs", () ->
            new RustableStairsBlock(IRusty.RustLevel.RUSTED, CUT_IRON, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)));

    public static final DeferredBlock<Block> CUT_IRON_SLAB = regWithItem("cut_iron_slab", () ->
            new RustableSlabBlock(IRusty.RustLevel.UNAFFECTED, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)));
    public static final DeferredBlock<Block> EXPOSED_CUT_IRON_SLAB = regWithItem("exposed_cut_iron_slab", () ->
            new RustableSlabBlock(IRusty.RustLevel.EXPOSED, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)));
    public static final DeferredBlock<Block> WEATHERED_CUT_IRON_SLAB = regWithItem("weathered_cut_iron_slab", () ->
            new RustableSlabBlock(IRusty.RustLevel.WEATHERED, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)));
    public static final DeferredBlock<Block> RUSTED_CUT_IRON_SLAB = regWithItem("rusted_cut_iron_slab", () ->
            new RustableSlabBlock(IRusty.RustLevel.RUSTED, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)));

    //waxed
    public static final DeferredBlock<Block> WAXED_CUT_IRON = regWithItem("waxed_cut_iron", () ->
            new Block(noTick(Blocks.IRON_BLOCK)));
    public static final DeferredBlock<Block> WAXED_EXPOSED_CUT_IRON = regWithItem("waxed_exposed_cut_iron", () ->
            new Block(noTick(Blocks.IRON_BLOCK)));
    public static final DeferredBlock<Block> WAXED_WEATHERED_CUT_IRON = regWithItem("waxed_weathered_cut_iron", () ->
            new Block(noTick(Blocks.IRON_BLOCK)));
    public static final DeferredBlock<Block> WAXED_RUSTED_CUT_IRON = regWithItem("waxed_rusted_cut_iron", () ->
            new Block(noTick(Blocks.IRON_BLOCK)));

    public static final DeferredBlock<Block> WAXED_CUT_IRON_STAIRS = regWithItem("waxed_cut_iron_stairs", () ->
            new ModStairBlock(WAXED_CUT_IRON, noTick(Blocks.IRON_BLOCK)));
    public static final DeferredBlock<Block> WAXED_EXPOSED_CUT_IRON_STAIRS = regWithItem("waxed_exposed_cut_iron_stairs", () ->
            new ModStairBlock(WAXED_EXPOSED_CUT_IRON, noTick(Blocks.IRON_BLOCK)));
    public static final DeferredBlock<Block> WAXED_WEATHERED_CUT_IRON_STAIRS = regWithItem("waxed_weathered_cut_iron_stairs", () ->
            new ModStairBlock(WAXED_WEATHERED_CUT_IRON, noTick(Blocks.IRON_BLOCK)));
    public static final DeferredBlock<Block> WAXED_RUSTED_CUT_IRON_STAIRS = regWithItem("waxed_rusted_cut_iron_stairs", () ->
            new ModStairBlock(WAXED_RUSTED_CUT_IRON, noTick(Blocks.IRON_BLOCK)));

    public static final DeferredBlock<Block> WAXED_CUT_IRON_SLAB = regWithItem("waxed_cut_iron_slab", () ->
            new SlabBlock(noTick(Blocks.IRON_BLOCK)));
    public static final DeferredBlock<Block> WAXED_EXPOSED_CUT_IRON_SLAB = regWithItem("waxed_exposed_cut_iron_slab", () ->
            new SlabBlock(noTick(Blocks.IRON_BLOCK)));
    public static final DeferredBlock<Block> WAXED_WEATHERED_CUT_IRON_SLAB = regWithItem("waxed_weathered_cut_iron_slab", () ->
            new SlabBlock(noTick(Blocks.IRON_BLOCK)));
    public static final DeferredBlock<Block> WAXED_RUSTED_CUT_IRON_SLAB = regWithItem("waxed_rusted_cut_iron_slab", () ->
            new SlabBlock(noTick(Blocks.IRON_BLOCK)));

    //plate iron

    public static final DeferredBlock<Block> PLATE_IRON = regWithItem("plate_iron", () ->
            new RustableBlock(IRusty.RustLevel.UNAFFECTED, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)));
    public static final DeferredBlock<Block> EXPOSED_PLATE_IRON = regWithItem("exposed_plate_iron", () ->
            new RustableBlock(IRusty.RustLevel.EXPOSED, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)));
    public static final DeferredBlock<Block> WEATHERED_PLATE_IRON = regWithItem("weathered_plate_iron", () ->
            new RustableBlock(IRusty.RustLevel.WEATHERED, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)));
    public static final DeferredBlock<Block> RUSTED_PLATE_IRON = regWithItem("rusted_plate_iron", () ->
            new RustableBlock(IRusty.RustLevel.RUSTED, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)));

    public static final DeferredBlock<Block> PLATE_IRON_STAIRS = regWithItem("plate_iron_stairs", () ->
            new RustableStairsBlock(IRusty.RustLevel.UNAFFECTED, CUT_IRON, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)));
    public static final DeferredBlock<Block> EXPOSED_PLATE_IRON_STAIRS = regWithItem("exposed_plate_iron_stairs", () ->
            new RustableStairsBlock(IRusty.RustLevel.EXPOSED, CUT_IRON, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)));
    public static final DeferredBlock<Block> WEATHERED_PLATE_IRON_STAIRS = regWithItem("weathered_plate_iron_stairs", () ->
            new RustableStairsBlock(IRusty.RustLevel.WEATHERED, CUT_IRON, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)));
    public static final DeferredBlock<Block> RUSTED_PLATE_IRON_STAIRS = regWithItem("rusted_plate_iron_stairs", () ->
            new RustableStairsBlock(IRusty.RustLevel.RUSTED, CUT_IRON, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)));

    public static final DeferredBlock<Block> PLATE_IRON_SLAB = regWithItem("plate_iron_slab", () ->
            new RustableSlabBlock(IRusty.RustLevel.UNAFFECTED, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)));
    public static final DeferredBlock<Block> EXPOSED_PLATE_IRON_SLAB = regWithItem("exposed_plate_iron_slab", () ->
            new RustableSlabBlock(IRusty.RustLevel.EXPOSED, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)));
    public static final DeferredBlock<Block> WEATHERED_PLATE_IRON_SLAB = regWithItem("weathered_plate_iron_slab", () ->
            new RustableSlabBlock(IRusty.RustLevel.WEATHERED, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)));
    public static final DeferredBlock<Block> RUSTED_PLATE_IRON_SLAB = regWithItem("rusted_plate_iron_slab", () ->
            new RustableSlabBlock(IRusty.RustLevel.RUSTED, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)));

    //waxed
    public static final DeferredBlock<Block> WAXED_PLATE_IRON = regWithItem("waxed_plate_iron", () ->
            new Block(noTick(Blocks.IRON_BLOCK)));
    public static final DeferredBlock<Block> WAXED_EXPOSED_PLATE_IRON = regWithItem("waxed_exposed_plate_iron", () ->
            new Block(noTick(Blocks.IRON_BLOCK)));
    public static final DeferredBlock<Block> WAXED_WEATHERED_PLATE_IRON = regWithItem("waxed_weathered_plate_iron", () ->
            new Block(noTick(Blocks.IRON_BLOCK)));
    public static final DeferredBlock<Block> WAXED_RUSTED_PLATE_IRON = regWithItem("waxed_rusted_plate_iron", () ->
            new Block(noTick(Blocks.IRON_BLOCK)));

    public static final DeferredBlock<Block> WAXED_PLATE_IRON_STAIRS = regWithItem("waxed_plate_iron_stairs", () ->
            new ModStairBlock(WAXED_PLATE_IRON, noTick(Blocks.IRON_BLOCK)));
    public static final DeferredBlock<Block> WAXED_EXPOSED_PLATE_IRON_STAIRS = regWithItem("waxed_exposed_plate_iron_stairs", () ->
            new ModStairBlock(WAXED_EXPOSED_PLATE_IRON, noTick(Blocks.IRON_BLOCK)));
    public static final DeferredBlock<Block> WAXED_WEATHERED_PLATE_IRON_STAIRS = regWithItem("waxed_weathered_plate_iron_stairs", () ->
            new ModStairBlock(WAXED_WEATHERED_PLATE_IRON, noTick(Blocks.IRON_BLOCK)));
    public static final DeferredBlock<Block> WAXED_RUSTED_PLATE_IRON_STAIRS = regWithItem("waxed_rusted_plate_iron_stairs", () ->
            new ModStairBlock(WAXED_RUSTED_PLATE_IRON, noTick(Blocks.IRON_BLOCK)));

    public static final DeferredBlock<Block> WAXED_PLATE_IRON_SLAB = regWithItem("waxed_plate_iron_slab", () ->
            new SlabBlock(noTick(Blocks.IRON_BLOCK)));
    public static final DeferredBlock<Block> WAXED_EXPOSED_PLATE_IRON_SLAB = regWithItem("waxed_exposed_plate_iron_slab", () ->
            new SlabBlock(noTick(Blocks.IRON_BLOCK)));
    public static final DeferredBlock<Block> WAXED_WEATHERED_PLATE_IRON_SLAB = regWithItem("waxed_weathered_plate_iron_slab", () ->
            new SlabBlock(noTick(Blocks.IRON_BLOCK)));
    public static final DeferredBlock<Block> WAXED_RUSTED_PLATE_IRON_SLAB = regWithItem("waxed_rusted_plate_iron_slab", () ->
            new SlabBlock(noTick(Blocks.IRON_BLOCK)));

    //iron decor

    public static final DeferredBlock<Block> EXPOSED_IRON_DOOR = regWithItem("exposed_iron_door", () ->
            new RustableDoorBlock(IRusty.RustLevel.EXPOSED, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_DOOR)));
    public static final DeferredBlock<Block> WEATHERED_IRON_DOOR = regWithItem("weathered_iron_door", () ->
            new RustableDoorBlock(IRusty.RustLevel.WEATHERED, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_DOOR)));
    public static final DeferredBlock<Block> RUSTED_IRON_DOOR = regWithItem("rusted_iron_door", () ->
            new RustableDoorBlock(IRusty.RustLevel.RUSTED, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_DOOR)));

    public static final DeferredBlock<Block> EXPOSED_IRON_TRAPDOOR = regWithItem("exposed_iron_trapdoor", () ->
            new RustableTrapdoorBlock(IRusty.RustLevel.EXPOSED, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_TRAPDOOR)));
    public static final DeferredBlock<Block> WEATHERED_IRON_TRAPDOOR = regWithItem("weathered_iron_trapdoor", () ->
            new RustableTrapdoorBlock(IRusty.RustLevel.WEATHERED, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_TRAPDOOR)));
    public static final DeferredBlock<Block> RUSTED_IRON_TRAPDOOR = regWithItem("rusted_iron_trapdoor", () ->
            new RustableTrapdoorBlock(IRusty.RustLevel.RUSTED, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_TRAPDOOR)));

    public static final DeferredBlock<Block> EXPOSED_IRON_BARS = regWithItem("exposed_iron_bars", () ->
            new RustableBarsBlock(IRusty.RustLevel.EXPOSED, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BARS)));
    public static final DeferredBlock<Block> WEATHERED_IRON_BARS = regWithItem("weathered_iron_bars", () ->
            new RustableBarsBlock(IRusty.RustLevel.WEATHERED, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BARS)));
    public static final DeferredBlock<Block> RUSTED_IRON_BARS = regWithItem("rusted_iron_bars", () ->
            new RustableBarsBlock(IRusty.RustLevel.RUSTED, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BARS)));

    //waxed
    public static final DeferredBlock<Block> WAXED_IRON_DOOR = regWithItem("waxed_iron_door", () ->
            new RustAffectedDoorBlock(IRusty.RustLevel.UNAFFECTED, noTick(Blocks.IRON_DOOR)));
    public static final DeferredBlock<Block> WAXED_EXPOSED_IRON_DOOR = regWithItem("waxed_exposed_iron_door", () ->
            new RustAffectedDoorBlock(IRusty.RustLevel.EXPOSED, noTick(Blocks.IRON_DOOR)));
    public static final DeferredBlock<Block> WAXED_WEATHERED_IRON_DOOR = regWithItem("waxed_weathered_iron_door", () ->
            new RustAffectedDoorBlock(IRusty.RustLevel.WEATHERED, noTick(Blocks.IRON_DOOR)));
    public static final DeferredBlock<Block> WAXED_RUSTED_IRON_DOOR = regWithItem("waxed_rusted_iron_door", () ->
            new RustAffectedDoorBlock(IRusty.RustLevel.RUSTED, noTick(Blocks.IRON_DOOR)));

    public static final DeferredBlock<Block> WAXED_IRON_TRAPDOOR = regWithItem("waxed_iron_trapdoor", () ->
            new RustAffectedTrapdoorBlock(IRusty.RustLevel.UNAFFECTED, noTick(Blocks.IRON_TRAPDOOR), BlockSetType.IRON));
    public static final DeferredBlock<Block> WAXED_EXPOSED_IRON_TRAPDOOR = regWithItem("waxed_exposed_iron_trapdoor", () ->
            new RustAffectedTrapdoorBlock(IRusty.RustLevel.EXPOSED, noTick(Blocks.IRON_TRAPDOOR), BlockSetType.IRON));
    public static final DeferredBlock<Block> WAXED_WEATHERED_IRON_TRAPDOOR = regWithItem("waxed_weathered_iron_trapdoor", () ->
            new RustAffectedTrapdoorBlock(IRusty.RustLevel.WEATHERED, noTick(Blocks.IRON_TRAPDOOR), BlockSetType.IRON));
    public static final DeferredBlock<Block> WAXED_RUSTED_IRON_TRAPDOOR = regWithItem("waxed_rusted_iron_trapdoor", () ->
            new RustAffectedTrapdoorBlock(IRusty.RustLevel.RUSTED, noTick(Blocks.IRON_TRAPDOOR), BlockSetType.IRON));

    public static final DeferredBlock<Block> WAXED_IRON_BARS = regWithItem("waxed_iron_bars", () ->
            new IronBarsBlock(noTick(Blocks.IRON_BARS)) {});
    public static final DeferredBlock<Block> WAXED_EXPOSED_IRON_BARS = regWithItem("waxed_exposed_iron_bars", () ->
            new IronBarsBlock(noTick(Blocks.IRON_BARS)) {});
    public static final DeferredBlock<Block> WAXED_WEATHERED_IRON_BARS = regWithItem("waxed_weathered_iron_bars", () ->
            new IronBarsBlock(noTick(Blocks.IRON_BARS)) {});
    public static final DeferredBlock<Block> WAXED_RUSTED_IRON_BARS = regWithItem("waxed_rusted_iron_bars", () ->
            new IronBarsBlock(noTick(Blocks.IRON_BARS)) {});


    //vanilla completion blocks
    public static final DeferredBlock<Block> STONE_WALL = regWithItem("stone_wall", () ->
            new MossableWallBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLESTONE_WALL)));

    public static final DeferredBlock<Block> CHISELED_PRISMARINE_BRICKS = regWithItem("chiseled_prismarine_bricks", () ->
            new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.PRISMARINE)));

    public static final DeferredBlock<Block> PRISMARINE_BRICK_WALL = regWithItem("prismarine_brick_wall", () ->
            new CrackedWallBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.PRISMARINE)));

    public static final DeferredBlock<Block> DARK_PRISMARINE_WALL = regWithItem("dark_prismarine_wall", () ->
            new WallBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.DARK_PRISMARINE)));

    public static final DeferredBlock<Block> TINTED_GLASS_PANE = regWithItem("tinted_glass_pane", () ->
            new TintedGlassPane(BlockBehaviour.Properties.of().strength(0.3F).sound(SoundType.GLASS).noOcclusion().isRedstoneConductor(NEVER).isSuffocating(NEVER).isViewBlocking(NEVER)));


    private static void registerLeafPiles(Registrator<Block> event, Collection<LeavesType> leavesTypes) {
        for (LeavesType type : leavesTypes) {
            String name = type.getVariantId("leaf_pile", false);

            LeafPileBlock block = new LeafPileBlock(LEAF_PILE_PROPERTIES, type);
            event.register(ImmersiveWeathering.res(name), block);

            LEAF_PILES.put(type, block);
            type.addChild("immersive_weathering:leaf_pile", block);
        }
    }

    private static BlockBehaviour.Properties noTick(Block copyFrom){
        var p = BlockBehaviour.Properties.ofFullCopy(copyFrom);
        p.isRandomlyTicking = false;
        return p;
    }

    public static void staticInit() {
        ImmersiveWeathering.LOGGER.info("[Immersive Weathering] Block Registry");
        BlockSetAPI.addDynamicBlockRegistration(ModBlocks::registerLeafPiles, LeavesType.class);
    }

    public static Collection<DeferredHolder<Block, ? extends Block>> getBlocks() {
        return BLOCKS.getEntries();
    }
}
