package io.github.pouffy.immersive_weathering.api.weathering.operators;

import io.github.pouffy.immersive_weathering.api.weathering.operators.types.RandomTickOperator;
import io.github.pouffy.immersive_weathering.blocks.ModBlockProperties;
import io.github.pouffy.immersive_weathering.datamaps.DataMapHelpers;
import io.github.pouffy.immersive_weathering.reg.ModWeatheringOperators;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.state.BlockState;

public class FrostOperator implements RandomTickOperator {
    @Override
    public Holder<WeatheringOperator> getType() {
        return ModWeatheringOperators.FROST;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (state.hasProperty(ModBlockProperties.NATURAL) && state.getValue(ModBlockProperties.NATURAL)) {
            if (level.dimensionType().ultraWarm() || level.isRaining() || level.isDay() || (level.getBrightness(LightLayer.BLOCK, pos) > 7 - state.getLightBlock(level, pos))) {
                DataMapHelpers.getPrevious(DataMapHelpers.Type.FROST, state).ifPresent(newState -> level.setBlockAndUpdate(pos, newState));
            }
        }
    }

    @Override
    public boolean canTarget(BlockState state) {
        if (state == null) return false;
        return DataMapHelpers.canRevert(DataMapHelpers.Type.FROST, state);
    }

    @Override
    public boolean canTarget(BlockState state, BlockPos pos, Level level) {
        if (state == null) return false;
        return DataMapHelpers.canRevert(DataMapHelpers.Type.FROST, state);
    }

    @Override
    public boolean shouldWeather(BlockState state, BlockPos pos, Level level) {
        return DataMapHelpers.canRevert(DataMapHelpers.Type.FROST, state);
    }

    @Override
    public boolean isWeathering(BlockState state) {
        return DataMapHelpers.canRevert(DataMapHelpers.Type.FROST, state);
    }


}
