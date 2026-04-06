package io.github.pouffy.immersive_weathering.data.position_tests;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;

import java.util.function.Supplier;

public record PrecipitationTest(Biome.Precipitation precipitation) implements IPositionRuleTest {

    public static final String NAME = "precipitation_test";

    @Override
    public boolean test(Supplier<Holder<Biome>> biome, BlockPos pos, Level level) {
        for (var d : Direction.values()) {
            if (d != Direction.DOWN) {
                switch (precipitation) {
                    case NONE -> {
                        if (level.isRainingAt(pos.relative(d))){
                            return false;
                        }
                    }
                    case SNOW -> {
                        if (level.isRainingAt(pos.relative(d)) && biome.get().value().coldEnoughToSnow(pos.relative(d))) {
                            return true;
                        }
                    }
                    case RAIN -> {
                        if (level.isRainingAt(pos.relative(d)) && biome.get().value().warmEnoughToRain(pos.relative(d))) {
                            return true;
                        }
                    }
                }
            }
        }
        return precipitation == Biome.Precipitation.NONE;
    }

    @Override
    public Type<?> getType() {
        return null;
    }
}
