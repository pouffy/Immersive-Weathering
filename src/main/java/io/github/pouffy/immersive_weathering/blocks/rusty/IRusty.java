package io.github.pouffy.immersive_weathering.blocks.rusty;

import io.github.pouffy.immersive_weathering.configs.CommonConfigs;
import io.github.pouffy.immersive_weathering.datamaps.DataMapHelpers;
import io.github.pouffy.immersive_weathering.datamaps.Rustable;
import io.github.pouffy.immersive_weathering.reg.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChangeOverTimeBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

public interface IRusty extends ChangeOverTimeBlock<IRusty.RustLevel> {

    @Override
    default Optional<BlockState> getNext(BlockState state) {
        return DataMapHelpers.getNext(DataMapHelpers.Type.RUST, state);
    }

    default float getChanceModifier() {
        if (this.getAge() == RustLevel.UNAFFECTED) {
            return 0.75f;
        }
        return 1.0f;
    }

    enum RustLevel {
        UNAFFECTED,
        EXPOSED,
        WEATHERED,
        RUSTED;
    }

    default int getInfluenceRadius() {
        return CommonConfigs.RUSTING_INFLUENCE_RADIUS.get();
    }

    @Override
    default void changeOverTime(BlockState state, ServerLevel serverLevel, BlockPos pos, RandomSource randomSource) {
        int age = this.getAge().ordinal();
        int j = 0;
        int k = 0;
        int affectingDistance = this.getInfluenceRadius();
        for (BlockPos blockpos : BlockPos.withinManhattan(pos, affectingDistance, affectingDistance, affectingDistance)) {
            int distance = blockpos.distManhattan(pos);
            if (distance > affectingDistance) {
                break;
            }

            if (!blockpos.equals(pos)) {
                BlockState blockstate = serverLevel.getBlockState(blockpos);
                Block block = blockstate.getBlock();
                if (block instanceof ChangeOverTimeBlock<?> changeOverTimeBlock) {
                    Enum<?> ageEnum = changeOverTimeBlock.getAge();
                    //checks if they are of same age class
                    if (this.getAge().getClass() == ageEnum.getClass()) {
                        int neighbourAge = ageEnum.ordinal();
                        if (neighbourAge < age) {
                            return;
                        }

                        if (neighbourAge > age) {
                            ++k;
                        } else {
                            ++j;
                        }
                    }
                }
            }
        }

        float f = (float) (k + 1) / (float) (k + j + 1);
        float f1 = f * f * this.getChanceModifier();
        if (randomSource.nextFloat() < f1) {
            this.getNext(state).ifPresent(s -> serverLevel.setBlockAndUpdate(pos, s));
        }

    }

    default void tryWeather(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!state.is(ModTags.RUSTED_IRON)) {
            var canWeather = false;
            for (Direction direction : Direction.values()) {
                var targetPos = pos.relative(direction);
                BlockState neighborState = level.getBlockState(targetPos);
                if (neighborState.is(Blocks.BUBBLE_COLUMN) && random.nextFloat() < CommonConfigs.RUSTING_RATE.get()) {
                    canWeather = true;
                } else if (neighborState.getFluidState().is(FluidTags.WATER) && random.nextFloat() < CommonConfigs.RUSTING_RATE.get() / 1.25) {
                    canWeather = true;
                } else if (state.is(ModTags.CLEAN_IRON)) {
                    if (neighborState.is(Blocks.AIR) && random.nextFloat() < CommonConfigs.RUSTING_RATE.get() / 5) {
                        canWeather = true;
                    }
                } else if (state.is(ModTags.EXPOSED_IRON) || state.is(ModTags.CLEAN_IRON) && level.isRaining() && random.nextFloat() < CommonConfigs.RUSTING_RATE.get() / 2) {
                    if (level.isRainingAt(pos.above())) {
                        canWeather = true;
                    } else if (CommonConfigs.RUST_STREAKING.get() && level.isRainingAt(targetPos) && level.getBlockState(pos.above()).is(ModTags.WEATHERED_IRON) && random.nextFloat() < CommonConfigs.RUSTING_RATE.get() / 3) {
                        if (BlockPos.withinManhattanStream(pos, 2, 2, 2)
                                .map(level::getBlockState)
                                .filter(b -> b.is(ModTags.WEATHERED_IRON))
                                .toList().size() <= 9) {
                            canWeather = true;
                        }
                    }
                }
            }
            if (canWeather) {
                changeOverTime(state, level, pos, random);
            }
        }
    }

    static BlockBehaviour.Properties setRandomTicking(BlockBehaviour.Properties properties, RustLevel rustLevel) {
        properties.isRandomlyTicking = rustLevel != RustLevel.RUSTED;
        return properties;
    }
}
