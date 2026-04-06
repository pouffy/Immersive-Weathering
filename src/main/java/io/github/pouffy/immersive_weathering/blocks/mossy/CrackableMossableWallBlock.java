package io.github.pouffy.immersive_weathering.blocks.mossy;

import io.github.pouffy.immersive_weathering.blocks.cracked.CrackSpreader;

public class CrackableMossableWallBlock extends MossableWallBlock implements CrackableMossable {

    public CrackableMossableWallBlock(Properties settings) {
        super(settings);
    }

    @Override
    public CrackSpreader getCrackSpreader() {
        return CrackSpreader.INSTANCE;
    }
}
