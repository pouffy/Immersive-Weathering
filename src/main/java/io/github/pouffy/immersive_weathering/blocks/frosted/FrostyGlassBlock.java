package io.github.pouffy.immersive_weathering.blocks.frosted;

import io.github.pouffy.immersive_weathering.blocks.ModBlockProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.TransparentBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;

public class FrostyGlassBlock extends TransparentBlock {

    public FrostyGlassBlock(Properties settings) {
        super(settings);
        this.registerDefaultState(this.stateDefinition.any().setValue(ModBlockProperties.NATURAL, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(ModBlockProperties.NATURAL);
    }

    @Override
    public boolean skipRendering(BlockState blockState, BlockState neighborState, Direction direction) {
        if (neighborState.is(Blocks.GLASS)) return true;
        return super.skipRendering(blockState, neighborState, direction);
    }

    public boolean hidesNeighborFace(BlockGetter level, BlockPos pos, BlockState state, BlockState neighborState, Direction dir) {
        if (neighborState.is(this) || neighborState.is(Blocks.GLASS)) return true;
        return false;
    }
}
