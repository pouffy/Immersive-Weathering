package io.github.pouffy.immersive_weathering.blocks.cracked;

import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;

public class CrackedSlabBlock extends SlabBlock implements ICrackable {

    public CrackedSlabBlock(Properties settings) {
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
