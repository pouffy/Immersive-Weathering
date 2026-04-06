package io.github.pouffy.immersive_weathering.blocks.mossy;

import io.github.pouffy.immersive_weathering.configs.CommonConfigs;
import io.github.pouffy.immersive_weathering.datamaps.DataMapHelpers;
import io.github.pouffy.immersive_weathering.datamaps.Mossable;
import io.github.pouffy.immersive_weathering.util.PatchSpreader;
import io.github.pouffy.immersive_weathering.util.Weatherable;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

public interface IMossy extends Weatherable {

    MossSpreader getMossSpreader();

    @Override
    default <T extends Enum<?>> Optional<PatchSpreader<T>> getPatchSpreader(Class<T> weatheringClass) {
        if (weatheringClass == MossLevel.class) {
            return Optional.of((PatchSpreader<T>) getMossSpreader());
        }
        return Optional.empty();
    }

    default boolean shouldWeather(BlockState state, BlockPos pos, Level level) {
        if (!CommonConfigs.MOSS_SPREADING_ENABLED.get()) return false;
        return this.getMossSpreader().getWantedWeatheringState(false, pos, level);
    }

    boolean isWeathering(BlockState state);

    @Override
    default void tryWeather(BlockState state, ServerLevel serverLevel, BlockPos pos, RandomSource random) {
        if (random.nextFloat() < this.getWeatherChanceSpeed()) {
            Optional<BlockState> opt = Optional.empty();
            if (this.getMossSpreader().getWantedWeatheringState(true, pos, serverLevel)) {
                opt = DataMapHelpers.getNext(DataMapHelpers.Type.MOSS, state);
            }
            BlockState newState = opt.orElse(state.setValue(Weatherable.WEATHERABLE, Weatherable.WeatheringState.FALSE));
            if (newState != state) {
                serverLevel.setBlock(pos, newState, 2);
                //schedule block event in 1 tick
                if (!newState.hasProperty(Weatherable.WEATHERABLE)) {
                    serverLevel.scheduleTick(pos, state.getBlock(), 1);
                }
            }
        }
    }

    enum MossLevel implements StringRepresentable {
        UNAFFECTED,
        MOSSY;

        @Override
        public String getSerializedName() {
            return this.name().toLowerCase();
        }
    }
}
