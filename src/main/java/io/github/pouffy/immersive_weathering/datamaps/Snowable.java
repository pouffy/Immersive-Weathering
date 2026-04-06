package io.github.pouffy.immersive_weathering.datamaps;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;

import java.util.Optional;

public record Snowable(Optional<Block> nextStage) implements StagedMap {
    public static final Codec<Snowable> CODEC;

    public static Snowable create(Block block) {
        return new Snowable(Optional.of(block));
    }

    static {
        CODEC = RecordCodecBuilder.create((in) -> in.group(
                StagedMap.fieldCodec().forGetter(Snowable::nextStage)
        ).apply(in, Snowable::new));
    }


}
