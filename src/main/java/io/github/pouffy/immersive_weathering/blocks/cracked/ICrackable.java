package io.github.pouffy.immersive_weathering.blocks.cracked;

import io.github.pouffy.immersive_weathering.configs.CommonConfigs;
import io.github.pouffy.immersive_weathering.datamaps.DataMapHelpers;
import io.github.pouffy.immersive_weathering.util.PatchSpreader;
import io.github.pouffy.immersive_weathering.util.Weatherable;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

public interface ICrackable extends Weatherable {

    CrackSpreader getCrackSpreader();

    @Override
    default <T extends Enum<?>> Optional<PatchSpreader<T>> getPatchSpreader(Class<T> weatheringClass) {
        if (weatheringClass == CrackLevel.class) {
            return Optional.of((PatchSpreader<T>) getCrackSpreader());
        }
        return Optional.empty();
    }

    enum CrackLevel {
        UNCRACKED,
        CRACKED;
    }

    default boolean shouldWeather(BlockState state, BlockPos pos, Level level) {
        if(!CommonConfigs.CRACK_SPREADING_ENABLED.get())return false;
        return this.getCrackSpreader().getWantedWeatheringState(false, pos, level);
    }

    @Override
    default void tryWeather(BlockState state, ServerLevel serverLevel, BlockPos pos, RandomSource random) {
        if (random.nextFloat() < this.getWeatherChanceSpeed()) {
            Optional<BlockState> opt = Optional.empty();
            if (this.getCrackSpreader().getWantedWeatheringState(true, pos, serverLevel)) {
                opt = DataMapHelpers.getNext(DataMapHelpers.Type.CRACK, state);
            }
            BlockState newState = opt.orElse(state.setValue(Weatherable.WEATHERABLE, WeatheringState.FALSE));
            if (newState != state) {
                serverLevel.setBlock(pos, newState, 2);
                //schedule block event in 1 tick
                if (!newState.hasProperty(Weatherable.WEATHERABLE)) {
                    serverLevel.scheduleTick(pos, state.getBlock(), 1);
                }
            }
        }
    }
}
