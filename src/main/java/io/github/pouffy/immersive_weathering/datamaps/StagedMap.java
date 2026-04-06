package io.github.pouffy.immersive_weathering.datamaps;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;

import java.util.Optional;

public interface StagedMap {

    Optional<Block> nextStage();

    static MapCodec<Optional<Block>> fieldCodec() {
        return BuiltInRegistries.BLOCK.byNameCodec().optionalFieldOf("next_stage");
    }
}
