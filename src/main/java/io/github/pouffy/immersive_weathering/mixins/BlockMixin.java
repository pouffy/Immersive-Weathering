package io.github.pouffy.immersive_weathering.mixins;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import io.github.pouffy.immersive_weathering.api.weathering.operators.types.AnimatedOperator;
import io.github.pouffy.immersive_weathering.api.weathering.operators.types.PlacementOperator;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public abstract class BlockMixin {

    @Shadow
    public abstract BlockState defaultBlockState();

    @WrapMethod(method = "getStateForPlacement(Lnet/minecraft/world/item/context/BlockPlaceContext;)Lnet/minecraft/world/level/block/state/BlockState;")
    public BlockState weatherState(BlockPlaceContext context, Operation<BlockState> original) {
        BlockState currentState = this.defaultBlockState();
        for (PlacementOperator operator : PlacementOperator.getAll()) {
            if (!operator.canTarget(currentState, context.getClickedPos(), context.getLevel())) continue;
            var weathered = operator.getWeatheredStateForPlacement(currentState, context.getClickedPos(), context.getLevel());
            if (weathered != currentState) {
                currentState =  weathered;
            }
        }
        if (currentState != this.defaultBlockState()) {
            return currentState;
        }
        return original.call(context);
    }

    @Inject(method = "animateTick(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/util/RandomSource;)V", at = @At("HEAD"))
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random, CallbackInfo ci) {
        for (AnimatedOperator operator : AnimatedOperator.getAll()) {
            if (!operator.canTarget(state, pos, level)) continue;
            operator.animateTick(state, level, pos, random);
        }
    }
}
