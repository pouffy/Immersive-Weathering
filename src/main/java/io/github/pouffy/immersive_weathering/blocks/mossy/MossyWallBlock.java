package io.github.pouffy.immersive_weathering.blocks.mossy;

import io.github.pouffy.immersive_weathering.datamaps.Mossable;
import io.github.pouffy.immersive_weathering.reg.ModDataMaps;
import io.github.pouffy.immersive_weathering.reg.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockState;

public class MossyWallBlock extends WallBlock implements IMossy, BonemealableBlock {

    public MossyWallBlock(Properties settings) {
        super(settings);
    }

    @Override
    public MossSpreader getMossSpreader() {
        return MossSpreader.INSTANCE;
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        return state.is(ModTags.MOSSY);
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        MossSpreader.growNeighbors(level, random, pos);
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return isWeathering(state);
    }

    @Override
    public boolean isWeathering(BlockState state) {
        return false;
    }
}
