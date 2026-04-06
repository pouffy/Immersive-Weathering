package io.github.pouffy.immersive_weathering.blocks.cracked;

import net.mehvahdjukaar.moonlight.api.block.ModStairBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Supplier;

public class CrackedStairsBlock extends ModStairBlock implements ICrackable {

    public CrackedStairsBlock(Supplier<Block> baseBlock, Properties settings) {
        super(baseBlock, settings);
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
