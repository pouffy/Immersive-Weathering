package io.github.pouffy.immersive_weathering.blocks.mossy;

import io.github.pouffy.immersive_weathering.blocks.cracked.ICrackable;
import io.github.pouffy.immersive_weathering.datamaps.Crackable;
import io.github.pouffy.immersive_weathering.datamaps.DataMapHelpers;
import io.github.pouffy.immersive_weathering.datamaps.Mossable;
import io.github.pouffy.immersive_weathering.util.PatchSpreader;
import io.github.pouffy.immersive_weathering.util.Weatherable;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

public interface CrackableMossable extends ICrackable, IMossy {

    @Override
    default <T extends Enum<?>> Optional<PatchSpreader<T>> getPatchSpreader(Class<T> weatheringClass) {
        if (weatheringClass == MossLevel.class) {
            return Optional.of((PatchSpreader<T>) getMossSpreader());
        } else if (weatheringClass == CrackLevel.class) {
            return Optional.of((PatchSpreader<T>) getCrackSpreader());
        }
        return Optional.empty();
    }

    @Override
    default boolean shouldWeather(BlockState state, BlockPos pos, Level level) {
        return IMossy.super.shouldWeather(state, pos, level) ||
                ICrackable.super.shouldWeather(state, pos, level);
    }

    @Override
    default void tryWeather(BlockState state, ServerLevel serverLevel, BlockPos pos, RandomSource random) {
        if (random.nextFloat() < this.getWeatherChanceSpeed()) {
            boolean isMoss = this.getMossSpreader().getWantedWeatheringState(true, pos, serverLevel);
            Optional<BlockState> opt = Optional.empty();
            if (isMoss) {
                opt = DataMapHelpers.getNext(DataMapHelpers.Type.MOSS, state);
            } else if (this.getCrackSpreader().getWantedWeatheringState(true, pos, serverLevel)) {
                opt = DataMapHelpers.getNext(DataMapHelpers.Type.CRACK, state);
            }
            BlockState newState = opt.orElse(state.setValue(Weatherable.WEATHERABLE, WeatheringState.FALSE));
            if(newState != state) {
                serverLevel.setBlock(pos, newState, 2);
                //schedule block event in 1 tick
                if (!newState.hasProperty(Weatherable.WEATHERABLE)) {
                    serverLevel.scheduleTick(pos, state.getBlock(), 1);
                }
            }
        }
    }
}
