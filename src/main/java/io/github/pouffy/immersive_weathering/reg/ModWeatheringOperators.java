package io.github.pouffy.immersive_weathering.reg;

import io.github.pouffy.immersive_weathering.ImmersiveWeathering;
import io.github.pouffy.immersive_weathering.api.weathering.operators.*;
import io.github.pouffy.immersive_weathering.util.ModUtils;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModWeatheringOperators {
    public static final DeferredRegister<WeatheringOperator> OPERATORS = ModUtils.createRegister(ModRegistries.WEATHERING_OPERATOR);

    public static final DeferredHolder<WeatheringOperator, CrackOperator> CRACK = OPERATORS.register("crack", CrackOperator::new);
    public static final DeferredHolder<WeatheringOperator, MossOperator> MOSS = OPERATORS.register("moss", MossOperator::new);
    public static final DeferredHolder<WeatheringOperator, FrostOperator> FROST = OPERATORS.register("frost", FrostOperator::new);
    public static final DeferredHolder<WeatheringOperator, SandOperator> SAND = OPERATORS.register("sand", SandOperator::new);
    public static final DeferredHolder<WeatheringOperator, SnowOperator> SNOW = OPERATORS.register("snow", SnowOperator::new);

    public static void staticInit() {
        ImmersiveWeathering.LOGGER.info("[Immersive Weathering] Weathering Operators");
    }
}
