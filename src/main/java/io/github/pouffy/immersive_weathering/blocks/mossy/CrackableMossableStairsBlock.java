package io.github.pouffy.immersive_weathering.blocks.mossy;

import io.github.pouffy.immersive_weathering.blocks.cracked.CrackSpreader;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public class CrackableMossableStairsBlock extends MossableStairsBlock implements CrackableMossable {

    public CrackableMossableStairsBlock(Supplier<Block> baseBlockState, Properties settings) {
        super(baseBlockState, settings);
    }

    @Override
    public CrackSpreader getCrackSpreader() {
        return CrackSpreader.INSTANCE;
    }
}
