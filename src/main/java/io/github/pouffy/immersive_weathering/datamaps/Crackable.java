package io.github.pouffy.immersive_weathering.datamaps;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.Optional;

public record Crackable(Optional<Block> nextStage, Optional<Item> repairItem) implements StagedMap {
    public static final Codec<Crackable> CODEC;

    public static Crackable create(Block nextStage, Item repairItem) {
        return new Crackable(Optional.ofNullable(nextStage), Optional.ofNullable(repairItem));
    }

    static {
        CODEC = RecordCodecBuilder.create((in) -> in.group(
                StagedMap.fieldCodec().forGetter(Crackable::nextStage),
                BuiltInRegistries.ITEM.byNameCodec().optionalFieldOf("repair_item").forGetter(Crackable::repairItem)
        ).apply(in, Crackable::new));
    }
}
