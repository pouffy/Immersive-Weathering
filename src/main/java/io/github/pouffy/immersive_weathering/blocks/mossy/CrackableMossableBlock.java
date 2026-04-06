package io.github.pouffy.immersive_weathering.blocks.mossy;

import io.github.pouffy.immersive_weathering.blocks.cracked.CrackSpreader;

public class CrackableMossableBlock extends MossableBlock implements CrackableMossable {

    public CrackableMossableBlock(Properties settings) {
        super(settings);
    }

    @Override
    public CrackSpreader getCrackSpreader() {
        return CrackSpreader.INSTANCE;
    }
}
