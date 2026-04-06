package io.github.pouffy.immersive_weathering.datamaps;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public record Rustable(Optional<Block> nextStage) implements StagedMap {
    public static final Codec<Rustable> CODEC;

    public static Rustable create(@Nullable Block nextStage) {
        return new Rustable(Optional.ofNullable(nextStage));
    }

    static {
        CODEC = RecordCodecBuilder.create((in) -> in.group(
                StagedMap.fieldCodec().forGetter(Rustable::nextStage)
        ).apply(in, Rustable::new));
    }
}
