package io.github.pouffy.immersive_weathering.mixins;

import io.github.pouffy.immersive_weathering.reg.ModBlocks;
import io.github.pouffy.immersive_weathering.util.WeatheringHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RootedDirtBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RootedDirtBlock.class)
public abstract class RootedDirtBlockMixin extends Block {

    protected RootedDirtBlockMixin(Properties settings) {
        super(settings);
    }

    @Inject(method = "isValidBonemealTarget(Lnet/minecraft/world/level/LevelReader;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)Z", at = @At("HEAD"), cancellable = true)
    public void isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state, CallbackInfoReturnable<Boolean> cir) {
        boolean space = false;
        for (Direction dir : Direction.values()) {
            var targetState = level.getBlockState(pos.relative(dir));
            if (dir != Direction.UP &&
                    targetState.canBeReplaced() &&
                    !targetState.is(Blocks.HANGING_ROOTS) &&
                    !targetState.is(ModBlocks.HANGING_ROOTS_WALL.get())) space = true;
        }
        cir.setReturnValue(space);
    }

    @Inject(method = "performBonemeal(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/util/RandomSource;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)V", at = @At("HEAD"))
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state, CallbackInfo ci) {
        WeatheringHelper.growHangingRoots(level, random, pos);
    }
}
