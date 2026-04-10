package io.github.pouffy.immersive_weathering.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

@FunctionalInterface
public interface InteractionEvent {
    InteractionResult run(Item i, ItemStack stack,
                          BlockPos pos,
                          BlockState state,
                          Player player, Level level,
                          InteractionHand hand,
                          BlockHitResult hit);
}
