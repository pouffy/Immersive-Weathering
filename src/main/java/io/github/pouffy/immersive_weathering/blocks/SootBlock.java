package io.github.pouffy.immersive_weathering.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.MultifaceBlock;
import net.minecraft.world.level.block.MultifaceSpreader;

public class SootBlock extends MultifaceBlock {
    private final MultifaceSpreader spreader = new MultifaceSpreader(this);

    public SootBlock(Properties settings) {
        super(settings);
        this.registerDefaultState(this.defaultBlockState());
    }

    @Override
    protected MapCodec<? extends MultifaceBlock> codec() {
        return simpleCodec(SootBlock::new);
    }

    @Override
    public MultifaceSpreader getSpreader() {
        return this.spreader;
    }
}
