package io.github.pouffy.immersive_weathering.blocks.frosted;

import io.github.pouffy.immersive_weathering.blocks.ModBlockProperties;
import io.github.pouffy.immersive_weathering.datamaps.DataMapHelpers;
import net.mehvahdjukaar.moonlight.api.client.util.ParticleUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.FlintAndSteelItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.gameevent.GameEvent;

public interface Frosty {
    BooleanProperty NATURAL = ModBlockProperties.NATURAL;

    default void tryUnFrost(BlockState state, ServerLevel level, BlockPos pos) {
        if (state.getValue(NATURAL)) {
            if (level.dimensionType().ultraWarm() || level.isRaining() || level.isDay() || (level.getBrightness(LightLayer.BLOCK, pos) > 7 - state.getLightBlock(level, pos))) {
                DataMapHelpers.getPrevious(DataMapHelpers.Type.FROST, state).ifPresent(newState -> level.setBlockAndUpdate(pos, newState));
            }
        }
    }

    default ItemInteractionResult interactWithPlayer(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand) {
        if (stack.getItem() instanceof FlintAndSteelItem) {
            level.playSound(player, pos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0f, 1.0f);
            if (level.isClientSide()) ParticleUtil.spawnParticlesOnBlockFaces(level, pos, ParticleTypes.SMOKE, UniformInt.of(3, 5), -0.05f, 0.05f, false);
            if (player instanceof ServerPlayer) {
                if (!player.getAbilities().instabuild) stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
                DataMapHelpers.getPrevious(DataMapHelpers.Type.FROST, state).ifPresent(newState -> level.setBlockAndUpdate(pos, newState));
                level.gameEvent(player, GameEvent.SHEAR, pos);
                player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }
}
