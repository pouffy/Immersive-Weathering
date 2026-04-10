package io.github.pouffy.immersive_weathering.api.weathering.operators;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;

import java.util.Locale;

public interface WeatheringOperator {
    EnumProperty<WeatheringState> WEATHERABLE = EnumProperty.create("weathering", WeatheringState.class);

    Holder<WeatheringOperator> getType();

    enum WeatheringState implements StringRepresentable {
        FALSE,
        TRUE,
        STABLE;

        public boolean isWeathering() {
            return this == TRUE;
        }

        public boolean isStable() {
            return this == STABLE;
        }

        @Override
        public String getSerializedName() {
            return this.name().toLowerCase(Locale.ROOT);
        }
    }

    boolean canTarget(BlockState state, BlockPos pos, Level level);

    boolean shouldWeather(BlockState state, BlockPos pos, Level level);

    boolean isWeathering(BlockState state);

    static BlockState setStable(BlockState state){
        if(state.hasProperty(WeatheringOperator.WEATHERABLE)){
            state = state.setValue(WeatheringOperator.WEATHERABLE, WeatheringState.STABLE);
        }
        return state;
    }
}
