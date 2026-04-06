package io.github.pouffy.immersive_weathering.datamaps;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.block.Block;

import java.util.Optional;

public record GrassGrowth(Optional<Block> nextStage) implements StagedMap {

    public static final Codec<GrassGrowth> CODEC;

    public static GrassGrowth create(Block block) {
        return new GrassGrowth(Optional.of(block));
    }

    static {
        CODEC = RecordCodecBuilder.create((in) -> in.group(
                StagedMap.fieldCodec().forGetter(GrassGrowth::nextStage)
        ).apply(in, GrassGrowth::new));
    }
}
