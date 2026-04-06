package io.github.pouffy.immersive_weathering.items;

import io.github.pouffy.immersive_weathering.configs.CommonConfigs;
import io.github.pouffy.immersive_weathering.datamaps.DataMapHelpers;
import io.github.pouffy.immersive_weathering.datamaps.Mossable;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

public class EnchantedGoldenMossClumpItem extends Item {
    public EnchantedGoldenMossClumpItem(Properties settings) {
        super(settings);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (CommonConfigs.MOSS_SHEARING.get()) {
            Level level = context.getLevel();
            BlockPos pos = context.getClickedPos();
            BlockState state = level.getBlockState(pos);
            Optional<BlockState> mossy = DataMapHelpers.getNext(DataMapHelpers.Type.MOSS, state);
            if (mossy.isPresent() && mossy.get() != state) {
                ItemStack stack = context.getItemInHand();
                Player player = context.getPlayer();
                level.playSound(player, pos, SoundEvents.MOSS_PLACE, SoundSource.BLOCKS, 1.0f, 1.0f);

                if (player instanceof ServerPlayer serverPlayer) {
                    //todo
                    //if (IntegrationHandler.quark) mossy = QuarkPlugin.fixVerticalSlab(mossy, state);

                    player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
                    level.setBlockAndUpdate(pos, mossy.get());
                    CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(serverPlayer, pos, stack);
                }
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
        }
        return super.useOn(context);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }
}
