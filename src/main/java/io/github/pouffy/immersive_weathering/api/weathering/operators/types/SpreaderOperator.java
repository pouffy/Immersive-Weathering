package io.github.pouffy.immersive_weathering.api.weathering.operators.types;

import io.github.pouffy.immersive_weathering.api.weathering.operators.WeatheringOperator;
import io.github.pouffy.immersive_weathering.api.weathering.spreaders.PatchSpreader;
import io.github.pouffy.immersive_weathering.reg.ModRegistries;
import net.minecraft.core.Holder;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public interface SpreaderOperator extends WeatheringOperator {

    Optional<PatchSpreader> getPatchSpreader(Holder<? extends PatchSpreader> spreader);

    static Set<SpreaderOperator> getAll() {
        return ModRegistries.WEATHERING_OPERATOR_REGISTRY.stream().map(op -> op instanceof SpreaderOperator operator ? operator : null).filter(Objects::nonNull).collect(Collectors.toSet());
    }
}
