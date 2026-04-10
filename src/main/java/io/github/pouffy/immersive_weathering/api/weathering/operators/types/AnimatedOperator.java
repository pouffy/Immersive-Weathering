package io.github.pouffy.immersive_weathering.api.weathering.operators.types;

import io.github.pouffy.immersive_weathering.api.weathering.operators.WeatheringOperator;
import io.github.pouffy.immersive_weathering.reg.ModRegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public interface AnimatedOperator extends WeatheringOperator {

    void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random);

    static Set<AnimatedOperator> getAll() {
        return ModRegistries.WEATHERING_OPERATOR_REGISTRY.stream().map(op -> op instanceof AnimatedOperator operator ? operator : null).filter(Objects::nonNull).collect(Collectors.toSet());
    }
}
