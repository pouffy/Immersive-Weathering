package io.github.pouffy.immersive_weathering.blocks.rusty;

import io.github.pouffy.immersive_weathering.datamaps.Rustable;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;

public class RustableDoorBlock extends RustAffectedDoorBlock implements IRusty {


    public RustableDoorBlock(RustLevel rustLevel, Properties properties) {
        super(rustLevel, IRusty.setRandomTicking(properties, rustLevel));
    }

    @Override
    public void randomTick(BlockState state, ServerLevel serverLevel, BlockPos pos, RandomSource random) {
        this.tryWeather(state, serverLevel, pos, random);
    }
}
