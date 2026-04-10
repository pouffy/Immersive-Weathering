package io.github.pouffy.immersive_weathering.api.weathering.operators;

import io.github.pouffy.immersive_weathering.api.weathering.operators.types.AnimatedOperator;
import io.github.pouffy.immersive_weathering.api.weathering.operators.types.NeighborUpdateOperator;
import io.github.pouffy.immersive_weathering.api.weathering.operators.types.RandomTickOperator;
import io.github.pouffy.immersive_weathering.blocks.ModBlockProperties;
import io.github.pouffy.immersive_weathering.datamaps.DataMapHelpers;
import io.github.pouffy.immersive_weathering.reg.ModBlocks;
import io.github.pouffy.immersive_weathering.reg.ModTags;
import io.github.pouffy.immersive_weathering.reg.ModWeatheringOperators;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.ParticleUtils;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;

import java.util.Optional;
import java.util.Random;

public class SandOperator implements NeighborUpdateOperator, AnimatedOperator, RandomTickOperator {

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos neighborPos, boolean isMoving) {
        Optional<BlockState> unSandy = DataMapHelpers.getPrevious(DataMapHelpers.Type.SAND, state);
        BlockState neighborState = level.getBlockState(neighborPos);
        if (neighborState.getFluidState().is(Fluids.FLOWING_WATER) && unSandy.isPresent()) {
            level.setBlockAndUpdate(pos, unSandy.get());
            ItemStack stack = new ItemStack(ModBlocks.SAND_LAYER_BLOCK.get());
            Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), stack);
            level.playSound(null, pos, SoundEvents.SAND_BREAK, SoundSource.BLOCKS, 1.0f, 1.0f);

            if (level instanceof ServerLevel serverLevel)
                serverLevel.sendParticles(new BlockParticleOption(ParticleTypes.FALLING_DUST, Blocks.SAND.defaultBlockState()),
                        pos.getX() + 0.5D,
                        pos.getY() + 0.5D,
                        pos.getZ() + 0.5D,
                        10,
                        0.5D, 0.5D, 0.5D,
                        0.0D);

            ParticleUtils.spawnParticlesOnBlockFaces(level, pos, new BlockParticleOption(ParticleTypes.FALLING_DUST, Blocks.SAND.defaultBlockState()), UniformInt.of(3, 5));
        }
    }

    @Override
    public Holder<WeatheringOperator> getType() {
        return ModWeatheringOperators.SAND;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        BlockPos belowPos = pos.below();
        BlockState belowState = level.getBlockState(belowPos);
        Optional<BlockState> unSandy = DataMapHelpers.getPrevious(DataMapHelpers.Type.SAND, state);
        Optional<BlockState> sandy = DataMapHelpers.getNext(DataMapHelpers.Type.SAND, belowState);
        if (state.hasProperty(ModBlockProperties.SAND_AGE)) {
            if (belowState.isAir() && state.getValue(ModBlockProperties.SAND_AGE) > 0) {
                level.setBlockAndUpdate(pos, state.setValue(ModBlockProperties.SAND_AGE, getAge(state) - 1));
                level.setBlockAndUpdate(belowPos, ModBlocks.SAND_LAYER_BLOCK.get().defaultBlockState());
            }
        } else if (belowState.is(ModTags.DOUBLE_SANDABLE) && isRandomSandyPos(pos) && sandy.isPresent()) {
            if (state.hasProperty(ModBlockProperties.SANDINESS)) {
                if (state.getValue(ModBlockProperties.SANDINESS) == 1) level.setBlockAndUpdate(pos, state.setValue(ModBlockProperties.SANDINESS, 0));
                if (state.getValue(ModBlockProperties.SANDINESS) == 0 && unSandy.isPresent()) level.setBlockAndUpdate(pos, unSandy.get());
            }
            level.setBlockAndUpdate(belowPos, sandy.get());
        }
    }

    @Override
    public boolean canTarget(BlockState state) {
        if (state == null) return false;
        return DataMapHelpers.canRevert(DataMapHelpers.Type.SAND, state);
    }

    public static boolean isRandomSandyPos(BlockPos pos) {
        Random posRandom = new Random(Mth.getSeed(pos));
        return posRandom.nextInt(5) > 2;
    }

    int getAge(BlockState state) {
        return state.getValue(ModBlockProperties.SAND_AGE);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (random.nextInt(10) == 1) {
            BlockPos blockpos = pos.below();
            BlockState blockstate = level.getBlockState(blockpos);
            if (!blockstate.isFaceSturdy(level, blockpos, Direction.UP)) {
                double d0 = pos.getX() + random.nextDouble();
                double d1 = pos.getY() - 0.05D;
                double d2 = pos.getZ() + random.nextDouble();
                if (state.hasProperty(ModBlockProperties.SANDINESS) && state.getValue(ModBlockProperties.SANDINESS) == 0 && random.nextInt(10) == 1) {
                    level.addParticle(new BlockParticleOption(ParticleTypes.FALLING_DUST, Blocks.SAND.defaultBlockState()), d0, d1, d2, 0.0D, 0.0D, 0.0D);
                } else
                    level.addParticle(new BlockParticleOption(ParticleTypes.FALLING_DUST, Blocks.SAND.defaultBlockState()), d0, d1, d2, 0.0D, 0.0D, 0.0D);
            }
        }
    }

    @Override
    public boolean canTarget(BlockState state, BlockPos pos, Level level) {
        if (state == null) return false;
        return DataMapHelpers.canRevert(DataMapHelpers.Type.SAND, state);
    }

    @Override
    public boolean shouldWeather(BlockState state, BlockPos pos, Level level) {
        return DataMapHelpers.canRevert(DataMapHelpers.Type.SAND, state);
    }

    @Override
    public boolean isWeathering(BlockState state) {
        return DataMapHelpers.canRevert(DataMapHelpers.Type.SAND, state);
    }
}
