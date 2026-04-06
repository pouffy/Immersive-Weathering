package io.github.pouffy.immersive_weathering.blocks;

import com.mojang.serialization.MapCodec;
import io.github.pouffy.immersive_weathering.reg.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;

public class DuneGrassBlock extends BushBlock {
    public DuneGrassBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends DuneGrassBlock> codec() {
        return simpleCodec(DuneGrassBlock::new);
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return state.is(BlockTags.DIRT) || state.is(BlockTags.SAND) || state.is(ModTags.GRASSY_BLOCKS);
    }
}
