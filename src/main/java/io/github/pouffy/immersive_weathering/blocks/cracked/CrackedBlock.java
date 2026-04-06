package io.github.pouffy.immersive_weathering.blocks.cracked;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class CrackedBlock extends Block implements ICrackable {

    public CrackedBlock(Properties settings) {
        super(settings);
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return isWeathering(state);
    }

    @Override
    public boolean isWeathering(BlockState state) {
        return false;
    }

    @Override
    public CrackSpreader getCrackSpreader() {
        return CrackSpreader.INSTANCE;
    }

}
