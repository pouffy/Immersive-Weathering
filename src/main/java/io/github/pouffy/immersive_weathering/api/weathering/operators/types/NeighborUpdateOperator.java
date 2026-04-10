package io.github.pouffy.immersive_weathering.api.weathering.operators.types;

import io.github.pouffy.immersive_weathering.api.weathering.operators.WeatheringOperator;
import io.github.pouffy.immersive_weathering.reg.ModRegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public interface NeighborUpdateOperator extends WeatheringOperator {

    default void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos neighborPos, boolean isMoving) {
        if (level instanceof ServerLevel serverLevel) {
            if (!state.hasProperty(WEATHERABLE)) return;
            WeatheringOperator.WeatheringState current = state.getValue(WEATHERABLE);
            if(!current.isStable()) {
                var wantedState = this.shouldWeather(state, pos, serverLevel) ? WeatheringOperator.WeatheringState.TRUE : WeatheringOperator.WeatheringState.FALSE;
                if (state.getValue(WEATHERABLE) != wantedState) {
                    //update weathering state
                    serverLevel.setBlock(pos, state.setValue(WEATHERABLE, wantedState),2);
                    //schedule block event in 1 tick
                    if(wantedState == WeatheringOperator.WeatheringState.TRUE) {
                        level.scheduleTick(pos, state.getBlock(), 1);
                    }
                }
            }
        }
    }

    static Set<NeighborUpdateOperator> getAll() {
        return ModRegistries.WEATHERING_OPERATOR_REGISTRY.stream().map(op -> op instanceof NeighborUpdateOperator operator ? operator : null).filter(Objects::nonNull).collect(Collectors.toSet());
    }
}
