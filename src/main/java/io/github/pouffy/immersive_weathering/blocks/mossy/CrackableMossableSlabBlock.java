package io.github.pouffy.immersive_weathering.blocks.mossy;

import io.github.pouffy.immersive_weathering.blocks.cracked.CrackSpreader;

public class CrackableMossableSlabBlock extends MossableSlabBlock implements CrackableMossable {
    public CrackableMossableSlabBlock(Properties settings) {
        super(settings);
    }

    @Override
    public CrackSpreader getCrackSpreader() {
        return CrackSpreader.INSTANCE;
    }
}
