package io.github.pouffy.immersive_weathering.api.weathering.operators.types;

import io.github.pouffy.immersive_weathering.api.weathering.operators.WeatheringOperator;
import io.github.pouffy.immersive_weathering.reg.ModRegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public interface PlacementOperator extends WeatheringOperator {

    default BlockState getWeatheredStateForPlacement(BlockState state, BlockPos pos, Level level){
        if (state != null) {
            WeatheringOperator.WeatheringState weathering = this.shouldWeather(state, pos, level) ? WeatheringOperator.WeatheringState.TRUE : WeatheringOperator.WeatheringState.FALSE;
            if (state.hasProperty(WEATHERABLE))
                state = state.setValue(WEATHERABLE, weathering);
        }
        return state;
    }

    static Set<PlacementOperator> getAll() {
        return ModRegistries.WEATHERING_OPERATOR_REGISTRY.stream().map(op -> op instanceof PlacementOperator operator ? operator : null).filter(Objects::nonNull).collect(Collectors.toSet());
    }
}
