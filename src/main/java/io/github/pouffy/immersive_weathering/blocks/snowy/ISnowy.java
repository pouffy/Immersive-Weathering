package io.github.pouffy.immersive_weathering.blocks.snowy;

import io.github.pouffy.immersive_weathering.configs.CommonConfigs;
import io.github.pouffy.immersive_weathering.datamaps.DataMapHelpers;
import io.github.pouffy.immersive_weathering.util.TemperatureManager;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.ParticleUtils;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.common.data.DataMapProvider;

import java.util.Optional;

public interface ISnowy {
    default boolean interactWithPlayer(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        Item item = stack.getItem();
        Optional<BlockState> unSnowy = DataMapHelpers.getPrevious(DataMapHelpers.Type.SNOW, state);
        if (unSnowy.isPresent() && item instanceof ShovelItem) {
            level.playSound(player, pos, SoundEvents.SNOW_BREAK, SoundSource.BLOCKS, 1.0f, 1.0f);
            ParticleUtils.spawnParticlesOnBlockFaces(level, pos, new BlockParticleOption(ParticleTypes.FALLING_DUST, Blocks.SNOW.defaultBlockState()), UniformInt.of(3, 5));
            stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
            if (player instanceof ServerPlayer serverPlayer) {
                level.setBlockAndUpdate(pos, unSnowy.get());
                if (!player.isCreative() || CommonConfigs.CREATIVE_DROP.get())
                    Block.popResourceFromFace(level, pos, hitResult.getDirection(), new ItemStack(Items.SNOWBALL));
                CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(serverPlayer, pos, stack);
                player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
            }
            return true;
        }
        return false;
    }

    default void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource randomSource) {
        Optional<BlockState> unSnowy = DataMapHelpers.getPrevious(DataMapHelpers.Type.SNOW, state);

        if (!unSnowy.isPresent()) {
            return;
        }

        for (Direction dir : Direction.values()) {
            if (level.getBrightness(LightLayer.BLOCK, pos.relative(dir)) > 11) {
                level.setBlockAndUpdate(pos, unSnowy.get());
                //Block.popResourceFromFace(level, pos, dir, new ItemStack(Items.SNOWBALL));

                return;
            }
        }
        if (!level.getBlockState(pos.above()).is(BlockTags.SNOW) && TemperatureManager.canSnowMelt(pos, level)) {
            level.setBlockAndUpdate(pos, unSnowy.get());
        }
    }

    default void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos neighborPos, boolean isMoving) {
        Optional<BlockState> unSnowy = DataMapHelpers.getPrevious(DataMapHelpers.Type.SNOW, state);
        BlockState neighborState = level.getBlockState(neighborPos);
        if (neighborState.getFluidState().is(FluidTags.WATER) || neighborState.getFluidState().is(FluidTags.LAVA) && unSnowy.isPresent()) {
            level.setBlockAndUpdate(pos, unSnowy.get());
            level.playSound(null, pos, SoundEvents.SNOW_BREAK, SoundSource.BLOCKS, 1.0f, 1.0f);
            if (neighborState.getFluidState().is(FluidTags.LAVA)) level.playSound(null, pos, SoundEvents.GENERIC_EXTINGUISH_FIRE, SoundSource.BLOCKS, 1f, 1f);
        }
    }
}
