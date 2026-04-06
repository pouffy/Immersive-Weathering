package io.github.pouffy.immersive_weathering.mixins;

import io.github.pouffy.immersive_weathering.reg.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.StemBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(StemBlock.class)
public abstract class StemBlockMixin extends Block {

    protected StemBlockMixin(Properties settings) {
        super(settings);
    }

    @Inject(method = "mayPlaceOn", at = @At(value = "HEAD"), cancellable = true)
    protected void mayPlaceOn(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CallbackInfoReturnable<Boolean> cir) {
        if (blockState.is(ModTags.FERTILE_BLOCKS)) cir.setReturnValue(true);
    }
}
