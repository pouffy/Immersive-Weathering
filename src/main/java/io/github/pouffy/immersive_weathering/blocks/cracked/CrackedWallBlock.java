package io.github.pouffy.immersive_weathering.blocks.cracked;

import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockState;

public class CrackedWallBlock extends WallBlock implements ICrackable{

    public CrackedWallBlock(Properties settings) {
        super(settings);
    }

    @Override
    public CrackSpreader getCrackSpreader() {
        return CrackSpreader.INSTANCE;
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
