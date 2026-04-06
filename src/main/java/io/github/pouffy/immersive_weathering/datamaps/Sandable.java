package io.github.pouffy.immersive_weathering.datamaps;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;

import java.util.Optional;

public record Sandable(Optional<Block> nextStage) implements StagedMap {
    public static final Codec<Sandable> CODEC;

    public static Sandable create(Block block) {
        return new Sandable(Optional.of(block));
    }

    static {
        CODEC = RecordCodecBuilder.create((in) -> in.group(
                StagedMap.fieldCodec().forGetter(Sandable::nextStage)
        ).apply(in, Sandable::new));
    }
}
