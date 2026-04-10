package io.github.pouffy.immersive_weathering.blocks.frosted;

import com.mojang.serialization.MapCodec;
import io.github.pouffy.immersive_weathering.blocks.ModBlockProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.MultifaceBlock;
import net.minecraft.world.level.block.MultifaceSpreader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;

public class FrostBlock extends MultifaceBlock {

    private final MultifaceSpreader spreader = new MultifaceSpreader(this);

    public FrostBlock(Properties settings) {
        super(settings);
        this.registerDefaultState(this.defaultBlockState().setValue(ModBlockProperties.NATURAL, false));
    }

    @Override
    protected MapCodec<? extends MultifaceBlock> codec() {
        return simpleCodec(FrostBlock::new);
    }

    @Override
    public MultifaceSpreader getSpreader() {
        return this.spreader;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(ModBlockProperties.NATURAL);
    }

    @Override
    public boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
        return !context.getItemInHand().is(this.asItem()) || super.canBeReplaced(state, context);
    }
}
