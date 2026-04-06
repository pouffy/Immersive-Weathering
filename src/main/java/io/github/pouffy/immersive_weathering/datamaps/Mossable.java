package io.github.pouffy.immersive_weathering.datamaps;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;

import java.util.Optional;

public record Mossable(Optional<Block> nextStage) implements StagedMap {
    public static final Codec<Mossable> CODEC;

    public static Mossable create(Block block) {
        return new Mossable(Optional.of(block));
    }

    static {
        CODEC = RecordCodecBuilder.create((in) -> in.group(
                StagedMap.fieldCodec().forGetter(Mossable::nextStage)
        ).apply(in, Mossable::new));
    }


}
