package io.github.pouffy.immersive_weathering.mixins;

import io.github.pouffy.immersive_weathering.api.weathering.operators.types.NeighborUpdateOperator;
import io.github.pouffy.immersive_weathering.api.weathering.operators.types.RandomTickOperator;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockBehaviour.class)
public class BlockBehaviourMixin {

    @Inject(method = "neighborChanged(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/Block;Lnet/minecraft/core/BlockPos;Z)V", at = @At("HEAD"))
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighbor, BlockPos neighborPos, boolean isMoving, CallbackInfo ci) {
        for (NeighborUpdateOperator operator : NeighborUpdateOperator.getAll()) {
            if (!operator.canTarget(state, pos, level)) continue;
            operator.neighborChanged(state, level, pos, neighbor, neighborPos, isMoving);
        }
    }

    @Inject(method = "randomTick", at = @At(value = "HEAD"))
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random, CallbackInfo ci) {
        for (RandomTickOperator operator : RandomTickOperator.getAll()) {
            if (!operator.canTarget(state, pos, level)) continue;
            operator.randomTick(state, level, pos, random);
        }
    }

    @Inject(method = "isRandomlyTicking(Lnet/minecraft/world/level/block/state/BlockState;)Z", at = @At(value = "HEAD"), cancellable = true)
    public void isRandomlyTicking(BlockState state, CallbackInfoReturnable<Boolean> cir) {
        for (RandomTickOperator operator : RandomTickOperator.getAll()) {
            if (!operator.canTarget(state)) continue;
            cir.setReturnValue(true);
        }
    }
}
