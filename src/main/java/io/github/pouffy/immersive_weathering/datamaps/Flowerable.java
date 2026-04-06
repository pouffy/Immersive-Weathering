package io.github.pouffy.immersive_weathering.datamaps;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;

import java.util.Optional;

public record Flowerable(Optional<Block> nextStage) implements StagedMap {
    public static final Codec<Flowerable> CODEC;

    public static Flowerable create(Block block) {
        return new Flowerable(Optional.of(block));
    }

    static {
        CODEC = RecordCodecBuilder.create((in) -> in.group(
                StagedMap.fieldCodec().forGetter(Flowerable::nextStage)
        ).apply(in, Flowerable::new));
    }


}
