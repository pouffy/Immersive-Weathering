package io.github.pouffy.immersive_weathering.api.weathering.operators.types;

import io.github.pouffy.immersive_weathering.api.weathering.operators.WeatheringOperator;
import io.github.pouffy.immersive_weathering.reg.ModRegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public interface RandomTickOperator extends WeatheringOperator {

    default float getWeatherChanceSpeed(){
        return 0.1f;
    }

    void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random);

    boolean canTarget(BlockState state);

    static Set<RandomTickOperator> getAll() {
        return ModRegistries.WEATHERING_OPERATOR_REGISTRY.stream().map(op -> op instanceof RandomTickOperator operator ? operator : null).filter(Objects::nonNull).collect(Collectors.toSet());
    }
}
