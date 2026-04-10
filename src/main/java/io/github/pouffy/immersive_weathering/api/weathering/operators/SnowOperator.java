package io.github.pouffy.immersive_weathering.api.weathering.operators;

import io.github.pouffy.immersive_weathering.api.weathering.operators.types.NeighborUpdateOperator;
import io.github.pouffy.immersive_weathering.api.weathering.operators.types.RandomTickOperator;
import io.github.pouffy.immersive_weathering.datamaps.DataMapHelpers;
import io.github.pouffy.immersive_weathering.reg.ModWeatheringOperators;
import io.github.pouffy.immersive_weathering.util.TemperatureManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

public class SnowOperator implements RandomTickOperator, NeighborUpdateOperator {

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        Optional<BlockState> unSnowy = DataMapHelpers.getPrevious(DataMapHelpers.Type.SNOW, state);
        if (!unSnowy.isPresent()) {
            return;
        }
        for (Direction dir : Direction.values()) {
            if (level.getBrightness(LightLayer.BLOCK, pos.relative(dir)) > 11) {
                level.setBlockAndUpdate(pos, unSnowy.get());
                return;
            }
        }
        if (!level.getBlockState(pos.above()).is(BlockTags.SNOW) && TemperatureManager.canSnowMelt(pos, level)) {
            level.setBlockAndUpdate(pos, unSnowy.get());
        }
    }

    @Override
    public boolean canTarget(BlockState state) {
        if (state == null) return false;
        return DataMapHelpers.canRevert(DataMapHelpers.Type.SNOW, state);
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos neighborPos, boolean isMoving) {
        Optional<BlockState> unSnowy = DataMapHelpers.getPrevious(DataMapHelpers.Type.SNOW, state);
        BlockState neighborState = level.getBlockState(neighborPos);
        if (neighborState.getFluidState().is(FluidTags.WATER) || neighborState.getFluidState().is(FluidTags.LAVA) && unSnowy.isPresent()) {
            level.setBlockAndUpdate(pos, unSnowy.get());
            level.playSound(null, pos, SoundEvents.SNOW_BREAK, SoundSource.BLOCKS, 1.0f, 1.0f);
            if (neighborState.getFluidState().is(FluidTags.LAVA)) level.playSound(null, pos, SoundEvents.GENERIC_EXTINGUISH_FIRE, SoundSource.BLOCKS, 1f, 1f);
        }
    }

    @Override
    public Holder<WeatheringOperator> getType() {
        return ModWeatheringOperators.SNOW;
    }

    @Override
    public boolean canTarget(BlockState state, BlockPos pos, Level level) {
        if (state == null) return false;
        return DataMapHelpers.canRevert(DataMapHelpers.Type.SNOW, state);
    }

    @Override
    public boolean shouldWeather(BlockState state, BlockPos pos, Level level) {
        return DataMapHelpers.canRevert(DataMapHelpers.Type.SNOW, state);
    }

    @Override
    public boolean isWeathering(BlockState state) {
        return DataMapHelpers.canRevert(DataMapHelpers.Type.SNOW, state);
    }
}
