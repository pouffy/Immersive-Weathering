package io.github.pouffy.immersive_weathering.api.weathering.operators;

import io.github.pouffy.immersive_weathering.api.weathering.operators.types.NeighborUpdateOperator;
import io.github.pouffy.immersive_weathering.api.weathering.operators.types.RandomTickOperator;
import io.github.pouffy.immersive_weathering.api.weathering.operators.types.SpreaderOperator;
import io.github.pouffy.immersive_weathering.api.weathering.spreaders.PatchSpreader;
import io.github.pouffy.immersive_weathering.datamaps.DataMapHelpers;
import io.github.pouffy.immersive_weathering.reg.ModSpreaders;
import io.github.pouffy.immersive_weathering.reg.ModWeatheringOperators;
import io.github.pouffy.immersive_weathering.util.Weatherable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

public class MossCrackOperator implements SpreaderOperator, NeighborUpdateOperator, RandomTickOperator {
    @Override
    public Holder<WeatheringOperator> getType() {
        return null;
    }

    @Override
    public Optional<PatchSpreader> getPatchSpreader(Holder<? extends PatchSpreader> spreader) {
        if (spreader == ModSpreaders.MOSS) {
            return Optional.of(ModSpreaders.MOSS.get());
        } else if (spreader == ModSpreaders.CRACK) {
            return Optional.of(ModSpreaders.CRACK.get());
        }
        return Optional.empty();
    }

    @Override
    public void randomTick(BlockState state, ServerLevel serverLevel, BlockPos pos, RandomSource random) {
        if (random.nextFloat() < this.getWeatherChanceSpeed()) {
            boolean isMoss = ModSpreaders.MOSS.get().getWantedWeatheringState(true, pos, serverLevel);
            Optional<BlockState> opt = Optional.empty();
            if (isMoss) {
                opt = DataMapHelpers.getNext(DataMapHelpers.Type.MOSS, state);
            } else if (ModSpreaders.CRACK.get().getWantedWeatheringState(true, pos, serverLevel)) {
                opt = DataMapHelpers.getNext(DataMapHelpers.Type.CRACK, state);
            }
            BlockState newState = opt.orElse(state.setValue(Weatherable.WEATHERABLE, Weatherable.WeatheringState.FALSE));
            if(newState != state) {
                serverLevel.setBlock(pos, newState, 2);
                //schedule block event in 1 tick
                if (!newState.hasProperty(Weatherable.WEATHERABLE)) {
                    serverLevel.scheduleTick(pos, state.getBlock(), 1);
                }
            }
        }
    }

    @Override
    public boolean canTarget(BlockState state) {
        if (state == null) return false;
        return DataMapHelpers.canAdvance(DataMapHelpers.Type.MOSS, state) || DataMapHelpers.canAdvance(DataMapHelpers.Type.CRACK, state);
    }

    @Override
    public boolean canTarget(BlockState state, BlockPos pos, Level level) {
        if (state == null) return false;
        return DataMapHelpers.canAdvance(DataMapHelpers.Type.MOSS, state) || DataMapHelpers.canAdvance(DataMapHelpers.Type.CRACK, state);
    }

    @Override
    public boolean shouldWeather(BlockState state, BlockPos pos, Level level) {
        return ModWeatheringOperators.MOSS.get().shouldWeather(state, pos, level) || ModWeatheringOperators.CRACK.get().shouldWeather(state, pos, level);
    }

    @Override
    public boolean isWeathering(BlockState state) {
        return false;
    }
}
