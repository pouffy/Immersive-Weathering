package io.github.pouffy.immersive_weathering.datamaps;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;

import java.util.Optional;

public record Frostable(Optional<Block> nextStage) implements StagedMap {
    public static final Codec<Frostable> CODEC;

    public static Frostable create(Block block) {
        return new Frostable(Optional.of(block));
    }

    static {
        CODEC = RecordCodecBuilder.create((in) -> in.group(
                StagedMap.fieldCodec().forGetter(Frostable::nextStage)
        ).apply(in, Frostable::new));
    }


}
