package io.github.pouffy.immersive_weathering.util;

import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;
import io.github.pouffy.immersive_weathering.blocks.LeafPileBlock;
import io.github.pouffy.immersive_weathering.configs.CommonConfigs;
import io.github.pouffy.immersive_weathering.reg.ModBlocks;
import net.mehvahdjukaar.moonlight.api.set.BlockSetAPI;
import net.mehvahdjukaar.moonlight.api.set.wood.WoodType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class WeatheringHelper {


    /**
     * Grabs block positions around center pos. Order of these is random and depends on current blockpos
     *
     * @param centerPos center pos
     */
    public static List<BlockPos> grabBlocksAroundRandomly(BlockPos centerPos, int radiusX, int radiusY, int radiusZ) {
        var list = new ArrayList<>(BlockPos.withinManhattanStream(centerPos, radiusX, radiusY, radiusZ)
                .map(BlockPos::new)
                .toList());
        //shuffling. provides way better result that iterating through it conventionally
        Collections.shuffle(list, new Random(Mth.getSeed(centerPos)));
        return list;
    }

    /**
     * optimized version of BlockPos.withinManhattanStream / BlockPos.expandOutwards that tries to limit level.getBlockState calls
     * Remember to call  "if (!level.isAreaLoaded(pos, radius)) return" before calling this
     *
     * @param blockPredicate type of target block
     * @param requiredAmount maximum amount of blocks that we want around this
     * @return true if blocks around that match the given predicate exceed(inclusive) the maximum size given
     */
    public static boolean hasEnoughBlocksAround(BlockPos centerPos, int radiusX, int radiusY, int radiusZ, Level level,
                                                Predicate<BlockState> blockPredicate, int requiredAmount) {

        var lis = grabBlocksAroundRandomly(centerPos, radiusX, radiusY, radiusZ);

        int count = 0;
        for (BlockPos pos : lis) {
            if (blockPredicate.test(level.getBlockState(pos))) count += 1;
            if (count >= requiredAmount) return true;
        }

        return false;
    }


    public static boolean hasEnoughBlocksAround(BlockPos centerPos, int radius, Level level,
                                                Predicate<BlockState> blockPredicate, int requiredAmount) {
        return hasEnoughBlocksAround(centerPos, radius, radius, radius, level, blockPredicate, requiredAmount);
    }

    //same as before but just checks blocks facing this one (6 in total)
    public static boolean hasEnoughBlocksFacingMe(BlockPos centerPos, Level level,
                                                  Predicate<BlockState> blockPredicate, int requiredAmount) {
        int count = 0;
        //shuffling. provides way better result that iterating through it conventionally
        List<Direction> list = new ArrayList<>(List.of(Direction.values()));
        Collections.shuffle(list, new Random(Mth.getSeed(centerPos)));
        for (Direction dir : list) {
            BlockPos pos = centerPos.relative(dir);
            if (blockPredicate.test(level.getBlockState(pos))) count += 1;
            if (count >= requiredAmount) return true;
        }
        return false;
    }

    public static final Supplier<Map<Block, LeafPileBlock>> LEAVES_TO_PILES = Suppliers.memoize(() -> {
                var b = ImmutableMap.<Block, LeafPileBlock>builder();
                ModBlocks.LEAF_PILES.forEach((key, value) -> b.put(key.leaves, value));
                return b.build();
            }
    );

    public static Optional<Block> getFallenLeafPile(BlockState state) {
        Block b = state.getBlock();
        if (CommonConfigs.LEAF_PILES_BLACKLIST.get().contains(BuiltInRegistries.BLOCK.getKey(b).toString()))
            return Optional.empty();
        return Optional.ofNullable(LEAVES_TO_PILES.get().get(b));
    }

    @Nullable
    public static Item getBarkToStrip(BlockState normalLog) {
        WoodType woodType = BlockSetAPI.getBlockTypeOf(normalLog.getBlock(), WoodType.class);
        if (woodType != null) {
            boolean log = false;

            String childKey = woodType.getChildKey(normalLog.getBlock());
            if (("log".equals(childKey) && woodType.getChild("stripped_log") != null)
                    || ("wood".equals(childKey)  && woodType.getChild("stripped_wood") != null)) {
                log = true;
            }
            if (log) {
                String s = CommonConfigs.GENERIC_BARK.get();
                if (!s.isEmpty()) {
                    var bark = BuiltInRegistries.ITEM.getOptional(ResourceLocation.parse(s));
                    if (bark.isPresent()) {
                        return bark.get();
                    }
                }
                return woodType.getItemOfThis("immersive_weathering:bark");
            }
        }
        return null;
    }

    public static Optional<Pair<Item, Block>> getBarkForStrippedLog(BlockState stripped) {
        WoodType woodType = BlockSetAPI.getBlockTypeOf(stripped.getBlock(), WoodType.class);
        if (woodType != null) {
            Object log = null;
            if (woodType.getChild("stripped_log") == stripped.getBlock()) {
                log = woodType.getChild("log");
            } else if (woodType.getChild("stripped_wood") == stripped.getBlock()) {
                log = woodType.getChild("wood");
            }
            if (log instanceof Block unStripped) {
                String s = CommonConfigs.GENERIC_BARK.get();
                if (!s.isEmpty()) {
                    var bark = BuiltInRegistries.ITEM.getOptional(ResourceLocation.parse(s));
                    if (bark.isPresent()) {
                        return Optional.of(Pair.of(bark.get(), unStripped));
                    }
                } else {
                    Item bark = woodType.getItemOfThis("immersive_weathering:bark");
                    if (bark != null) return Optional.of(Pair.of(bark, unStripped));
                }
            }
        }
        return Optional.empty();
    }

    public static Optional<Pair<Item, Block>> getWoodFromLog(BlockState sourceLog) {
        WoodType woodType = BlockSetAPI.getBlockTypeOf(sourceLog.getBlock(), WoodType.class);
        if (woodType != null) {
            Object log = null;
            if (woodType.getChild("log") == sourceLog.getBlock()) {
                log = woodType.getChild("wood");
            }
            if (log instanceof Block unStripped) {
                String s = CommonConfigs.GENERIC_BARK.get();
                if (!s.isEmpty()) {
                    var bark = BuiltInRegistries.ITEM.getOptional(ResourceLocation.parse(s));
                    if (bark.isPresent()) {
                        return Optional.of(Pair.of(bark.get(), unStripped));
                    }
                } else {
                    Item bark = woodType.getItemOfThis("immersive_weathering:bark");
                    if (bark != null) return Optional.of(Pair.of(bark, unStripped));
                }
            }
        }
        return Optional.empty();
    }

    public static boolean isLog(BlockState state) {
        return state.is(BlockTags.LOGS) && (!state.hasProperty(RotatedPillarBlock.AXIS) ||
                state.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y) &&
                !BuiltInRegistries.BLOCK.getKey(state.getBlock()).getPath().contains("stripped");
    }

    public static boolean isIciclePos(BlockPos pos) {
        int rarity = CommonConfigs.ICICLE_RARITY.get();
        Random posRandom = new Random(Mth.getSeed(pos));
        if (CommonConfigs.DISABLE_ICICLES.get()) return false;
        else return posRandom.nextInt(rarity) == 0;
    }

    /**
     * @return used to randomize weathering in certain position. 1/6 on average will be true
     */
    public static boolean isRandomWeatheringPos(BlockPos pos) {
        Random posRandom = new Random(Mth.getSeed(pos));
        return posRandom.nextInt(6) == 0;
    }

    public static void onFireExpired(ServerLevel serverLevel, BlockPos pos, BlockState state) {
    }

    @Nullable
    public static BlockState getCharredState(BlockState state) {
        Block charred = null;
        if (state.is(BlockTags.WOODEN_FENCES)) {
            charred = ModBlocks.CHARRED_FENCE.get();
        } else if (state.is(BlockTags.FENCE_GATES)) {
            charred = ModBlocks.CHARRED_FENCE_GATE.get();
        } else if (state.is(BlockTags.WOODEN_SLABS)) {
            charred = ModBlocks.CHARRED_SLAB.get();
        } else if (state.is(BlockTags.WOODEN_STAIRS)) {
            charred = ModBlocks.CHARRED_STAIRS.get();
        } else if (state.is(BlockTags.PLANKS)) {
            charred = ModBlocks.CHARRED_PLANKS.get();
        } else if (state.is(BlockTags.LOGS_THAT_BURN)) {
            charred = ModBlocks.CHARRED_LOG.get();
        }
        if (charred == null) return null;
        return charred.withPropertiesOf(state);
    }

    public static void growHangingRoots(ServerLevel level, RandomSource random, BlockPos pos) {
        Direction dir = Direction.values()[1 + random.nextInt(5)].getOpposite();
        BlockPos targetPos = pos.relative(dir);
        BlockState targetState = level.getBlockState(targetPos);
        FluidState fluidState = level.getFluidState(targetPos);
        boolean bl = fluidState.is(Fluids.WATER);
        if (targetState.canBeReplaced()) {
            BlockState newState = dir == Direction.DOWN ?
                    Blocks.HANGING_ROOTS.defaultBlockState() :
                    ModBlocks.HANGING_ROOTS_WALL.get().defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, dir);
            level.setBlockAndUpdate(targetPos, newState.setValue(BlockStateProperties.WATERLOGGED, bl));
        }
    }
}
