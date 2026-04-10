package io.github.pouffy.immersive_weathering.events;

import com.mojang.datafixers.util.Pair;
import io.github.pouffy.immersive_weathering.api.weathering.operators.WeatheringOperator;
import io.github.pouffy.immersive_weathering.blocks.ModBlockProperties;
import io.github.pouffy.immersive_weathering.blocks.charred.CharredBlock;
import io.github.pouffy.immersive_weathering.configs.CommonConfigs;
import io.github.pouffy.immersive_weathering.data.block_growths.BlockGrowthHandler;
import io.github.pouffy.immersive_weathering.data.block_growths.TickSource;
import io.github.pouffy.immersive_weathering.datamaps.Crackable;
import io.github.pouffy.immersive_weathering.datamaps.DataMapHelpers;
import io.github.pouffy.immersive_weathering.datamaps.Rustable;
import io.github.pouffy.immersive_weathering.reg.*;
import io.github.pouffy.immersive_weathering.util.InteractionEvent;
import io.github.pouffy.immersive_weathering.util.WeatheringHelper;
import net.mehvahdjukaar.moonlight.api.client.util.ParticleUtil;
import net.mehvahdjukaar.moonlight.api.events.IFireConsumeBlockEvent;
import net.mehvahdjukaar.moonlight.api.events.ILightningStruckBlockEvent;
import net.mehvahdjukaar.moonlight.api.misc.EventCalled;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ParticleUtils;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.common.DataMapHooks;
import net.neoforged.neoforge.common.ItemAbilities;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ModEvents {

    private static final List<InteractionEvent> EVENTS = new ArrayList<>();

    static {
        EVENTS.add(ModEvents::pickaxeCracking);
        EVENTS.add(ModEvents::brickRepair);
        EVENTS.add(ModEvents::burnMoss);
        EVENTS.add(ModEvents::shearShearing);
        EVENTS.add(ModEvents::axeStripping);
        EVENTS.add(ModEvents::barkRepairing);
        EVENTS.add(ModEvents::rustScraping);
        EVENTS.add(ModEvents::rustSponging);
        EVENTS.add(ModEvents::blockSanding);
        EVENTS.add(ModEvents::sandShoveling);
        EVENTS.add(ModEvents::blockSnowing);
        EVENTS.add(ModEvents::snowShoveling);
        EVENTS.add(ModEvents::frostMelting);
    }

    public static InteractionResult onBlockCLicked(ItemStack stack, Player player, Level level, InteractionHand hand, BlockHitResult hitResult) {
        if (stack.isEmpty()) return InteractionResult.PASS;
        Item i = stack.getItem();
        BlockPos pos = hitResult.getBlockPos();
        BlockState state = level.getBlockState(pos);
        for (var event : EVENTS) {
            var result = event.run(i, stack, pos, state, player, level, hand, hitResult);
            if (result != InteractionResult.PASS) return result;
        }
        return InteractionResult.PASS;
    }

    private static InteractionResult rustSponging(Item item, ItemStack stack, BlockPos pos, BlockState state, Player player, Level level, InteractionHand hand, BlockHitResult hitResult) {
        if (item == Items.WET_SPONGE && CommonConfigs.SPONGE_RUSTING.get()) {
            Rustable rustable = state.getBlock().builtInRegistryHolder().getData(ModDataMaps.RUSTABLES);
            if (rustable != null) {
                var next = DataMapHelpers.getNext(DataMapHelpers.Type.RUST, state);
                if (next.isPresent()) {
                    level.playSound(player, pos, SoundEvents.AMBIENT_UNDERWATER_ENTER, SoundSource.BLOCKS, 1.0f, 1.0f);
                    ParticleUtils.spawnParticlesOnBlockFaces(level, pos, ModParticles.SCRAPE_RUST.get(), UniformInt.of(3, 5));
                    ParticleUtils.spawnParticlesOnBlockFaces(level, pos, ParticleTypes.SPLASH, UniformInt.of(3, 5));
                    if (player instanceof ServerPlayer serverPlayer) {
                        if (CommonConfigs.SPONGE_RUST_DRYING.get()) {
                            ItemStack itemStack2 = ItemUtils.createFilledResult(stack, player, Items.SPONGE.getDefaultInstance());
                            player.setItemInHand(hand, itemStack2);
                        }
                        CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(serverPlayer, pos, stack);
                        level.setBlockAndUpdate(pos, next.get());
                    }
                    return InteractionResult.sidedSuccess(level.isClientSide);
                }
            }
        }
        return InteractionResult.PASS;
    }

    private static InteractionResult rustScraping(Item item, ItemStack stack, BlockPos pos, BlockState state, Player player, Level level, InteractionHand hand, BlockHitResult hitResult) {
        if (CommonConfigs.AXE_SCRAPING.get()) {
            Rustable rustable = state.getBlock().builtInRegistryHolder().getData(ModDataMaps.RUSTABLES);
            if (rustable != null && !state.is(ModTags.WAXED_BLOCKS)) {
                if (stack.canPerformAction(ItemAbilities.AXE_SCRAPE)) {
                    var previous = DataMapHelpers.getPrevious(DataMapHelpers.Type.RUST, state);
                    //Can't scrape if there is no previous
                    if (previous.isPresent()) {
                        // Extra effects if it has no more progress
                        if (rustable.nextStage().isEmpty()) {
                            level.playSound(player, pos, SoundEvents.AXE_SCRAPE, SoundSource.BLOCKS, 1.0f, 1.0f);
                            level.playSound(player, pos, SoundEvents.SHIELD_BREAK, SoundSource.BLOCKS, 1.0f, 1.0f);
                            ParticleUtils.spawnParticlesOnBlockFaces(level, pos, ModParticles.SCRAPE_RUST.get(), UniformInt.of(3, 5));
                            if (level.isClientSide()) ParticleUtil.spawnParticlesOnBlockFaces(level, pos, ParticleTypes.SMOKE, UniformInt.of(3, 5), -0.05f, 0.05f, false);
                            if (player instanceof ServerPlayer serverPlayer) {
                                stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
                                player.awardStat(Stats.ITEM_USED.get(item));
                                CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(serverPlayer, pos, stack);
                            }
                            return InteractionResult.sidedSuccess(level.isClientSide);
                        }
                        // Convert to previous
                        ParticleUtils.spawnParticlesOnBlockFaces(level, pos, ModParticles.SCRAPE_RUST.get(), UniformInt.of(3, 5));
                        level.playSound(player, pos, SoundEvents.AXE_SCRAPE, SoundSource.BLOCKS, 1.0f, 1.0f);
                        if (player instanceof ServerPlayer serverPlayer) {
                            stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
                            player.awardStat(Stats.ITEM_USED.get(item));
                            level.setBlockAndUpdate(pos, previous.get());
                            CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(serverPlayer, pos, stack);
                        }
                        return InteractionResult.sidedSuccess(level.isClientSide);
                    } else {
                        return InteractionResult.PASS;
                    }
                } else if (item == ModItems.STEEL_WOOL.get()) {
                    var previous = DataMapHelpers.getPrevious(DataMapHelpers.Type.RUST, state);
                    // Steel wool reverts rust with no special effects
                    if (previous.isPresent()) {
                        level.playSound(player, pos, SoundEvents.AXE_SCRAPE, SoundSource.BLOCKS, 1.0f, 1.0f);
                        ParticleUtils.spawnParticlesOnBlockFaces(level, pos, ModParticles.SCRAPE_RUST.get(), UniformInt.of(3, 5));
                        if (player instanceof ServerPlayer serverPlayer) {
                            stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
                            CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(serverPlayer, pos, stack);
                            player.awardStat(Stats.ITEM_USED.get(item));
                            level.setBlockAndUpdate(pos, previous.get());
                        }
                        return InteractionResult.sidedSuccess(level.isClientSide);
                    }
                    if (state.is(ModTags.WAXED_BLOCKS)) {
                        level.playSound(player, pos, SoundEvents.AXE_WAX_OFF, SoundSource.BLOCKS, 1.0f, 1.0f);
                        ParticleUtils.spawnParticlesOnBlockFaces(level, pos, ParticleTypes.WAX_OFF, UniformInt.of(3, 5));
                        if (player instanceof ServerPlayer serverPlayer) {
                            stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
                            CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(serverPlayer, pos, stack);
                            player.awardStat(Stats.ITEM_USED.get(item));
                            Block waxOffBlock = DataMapHooks.getBlockUnwaxed(state.getBlock());
                            if (waxOffBlock != null) {
                                level.setBlockAndUpdate(pos, Optional.of(waxOffBlock).map(block -> block.withPropertiesOf(state)).orElse(null));
                            }
                        }
                        return InteractionResult.sidedSuccess(level.isClientSide);
                    }
                    if (state.is(ModTags.COPPER)) {
                        level.playSound(player, pos, SoundEvents.AXE_SCRAPE, SoundSource.BLOCKS, 1.0f, 1.0f);
                        ParticleUtils.spawnParticlesOnBlockFaces(level, pos, ParticleTypes.SCRAPE, UniformInt.of(3, 5));
                        if (player instanceof ServerPlayer serverPlayer) {
                            stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
                            CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(serverPlayer, pos, stack);
                            player.awardStat(Stats.ITEM_USED.get(item));
                            Block previousBlock = DataMapHooks.getPreviousOxidizedStage(state.getBlock());
                            if (previousBlock != null) {
                                level.setBlockAndUpdate(pos, Optional.of(previousBlock).map(block -> block.withPropertiesOf(state)).orElse(null));
                            }
                        }
                        return InteractionResult.sidedSuccess(level.isClientSide);
                    }
                }
            }
        }
        return InteractionResult.PASS;
    }

    private static InteractionResult pickaxeCracking(Item item, ItemStack stack, BlockPos pos, BlockState state, Player player, Level level, InteractionHand hand, BlockHitResult hitResult) {
        if (item instanceof PickaxeItem && CommonConfigs.PICKAXE_CRACKING.get()) {
            if (!player.isSecondaryUseActive() && CommonConfigs.PICKAXE_CRACKING_SHIFT.get())
                return InteractionResult.PASS;

            var broken = DataMapHelpers.getNext(DataMapHelpers.Type.CRACK, state);
            if (broken.isPresent()) {
                if (level.isClientSide) {
                    ParticleUtils.spawnParticlesOnBlockFaces(level, pos, new BlockParticleOption(ParticleTypes.BLOCK, state), UniformInt.of(3, 5));
                    level.playSound(player, pos, broken.get().getSoundType().getHitSound(), SoundSource.BLOCKS, 1.0f, 1.0f);
                } else {
                    Crackable crackable = state.getBlock().builtInRegistryHolder().getData(ModDataMaps.CRACKABLES);
                    CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger((ServerPlayer) player, pos, stack);
                    if (crackable != null) {
                        Item repairItem = crackable.repairItem().orElse(null);
                        if (repairItem != null) {
                            stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
                            player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
                            level.setBlockAndUpdate(pos, broken.get());
                            if (!player.isCreative() || CommonConfigs.CREATIVE_DROP.get()) {
                                if (CommonConfigs.CRACKING_DROPS_BRICK.get()) Block.popResourceFromFace(level, pos, hitResult.getDirection(), repairItem.getDefaultInstance());
                            }
                        }
                    }
                }
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
        }
        return InteractionResult.PASS;
    }

    private static InteractionResult brickRepair(Item item, ItemStack stack, BlockPos pos, BlockState state, Player player, Level level, InteractionHand hand, BlockHitResult hitResult) {
        var previous = DataMapHelpers.getPrevious(DataMapHelpers.Type.CRACK, state);
        if (previous.isPresent()) {
            var previousData = DataMapHelpers.getCrackable(previous.get().getBlock());
            var repairItem = previousData.flatMap(Crackable::repairItem).orElse(null);
            if (repairItem != null && stack.is(repairItem)) {
                BlockState stableFixed = WeatheringOperator.setStable(previous.get());
                if (level.isClientSide) {
                    level.playSound(player, pos, stableFixed.getSoundType().getPlaceSound(), SoundSource.BLOCKS, 1.0f, 1.0f);
                } else {
                    CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger((ServerPlayer) player, pos, stack);
                    player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
                    level.setBlockAndUpdate(pos, stableFixed);
                    if (!player.getAbilities().instabuild) {
                        stack.shrink(1);
                    }
                }
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
        }
        return InteractionResult.PASS;
    }

    private static InteractionResult shearShearing(Item item, ItemStack stack, BlockPos pos, BlockState state, Player player, Level level, InteractionHand hand, BlockHitResult hitResult) {
        if (item instanceof ShearsItem) {
            BlockState newState = null;
            if (CommonConfigs.AZALEA_SHEARING.get()) {
                newState = DataMapHelpers.getPrevious(DataMapHelpers.Type.FLOWER, state).orElse(null);
                if (newState != null) {
                    if (level.isClientSide) {
                        ParticleUtils.spawnParticlesOnBlockFaces(level, pos, ModParticles.GRAVITY_AZALEA_FLOWER.get(), UniformInt.of(4, 6));
                    } else {
                        CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger((ServerPlayer) player, pos, stack);
                        if (!player.isCreative() || CommonConfigs.CREATIVE_DROP.get()) {
                            Block.popResourceFromFace(level, pos, hitResult.getDirection(), new ItemStack(ModItems.AZALEA_FLOWERS.get()));
                        }
                    }
                }
            }
            if (newState == null && CommonConfigs.MOSS_SHEARING.get()) {
                var unmossed = DataMapHelpers.getPrevious(DataMapHelpers.Type.MOSS, state).orElse(null);
                if (unmossed != null) {
                    newState = unmossed;
                    if (newState != state) {
                        if (level.isClientSide) {
                            ParticleUtils.spawnParticlesOnBlockFaces(level, pos, ModParticles.MOSS.get(), UniformInt.of(3, 5));
                        } else {
                            CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger((ServerPlayer) player, pos, stack);
                            if (!player.isCreative() || CommonConfigs.CREATIVE_DROP.get()) {
                                Block.popResourceFromFace(level, pos, hitResult.getDirection(), new ItemStack(ModItems.MOSS_CLUMP.get()));
                            }
                        }
                    } else newState = null;
                }
            }
            if (newState != null) {
                level.playSound(player, pos, SoundEvents.GROWING_PLANT_CROP, SoundSource.BLOCKS, 1.0f, 1.0f);
                level.setBlockAndUpdate(pos, newState);

                stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));

                if (player instanceof ServerPlayer serverPlayer) {
                    CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger( serverPlayer, pos, stack);
                    level.gameEvent(player, GameEvent.SHEAR, pos);
                    player.awardStat(Stats.ITEM_USED.get(item));
                }
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
        }
        return InteractionResult.PASS;
    }

    private static InteractionResult burnMoss(Item item, ItemStack stack, BlockPos pos, BlockState state, Player player, Level level, InteractionHand hand, BlockHitResult hitResult) {

        if (item instanceof FlintAndSteelItem && CommonConfigs.MOSS_BURNING.get()) {
            var unmossed = DataMapHelpers.getPrevious(DataMapHelpers.Type.MOSS, state);
            if (unmossed.isPresent() && unmossed.get() != state) {
                BlockState stableUnmossed = WeatheringOperator.setStable(unmossed.get());
                if (level.isClientSide) {
                    ParticleUtil.spawnParticlesOnBlockFaces(level, pos, ParticleTypes.FLAME, UniformInt.of(3, 5), -0.05f, 0.05f, false);
                    level.playSound(player, pos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0f, 1.0f);
                } else {
                    CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger((ServerPlayer) player, pos, stack);
                    stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
                    player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
                    level.setBlockAndUpdate(pos, stableUnmossed);
                }
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
        }
        return InteractionResult.PASS;
    }

    private static InteractionResult axeStripping(Item item, ItemStack stack, BlockPos pos, BlockState state, Player player, Level level, InteractionHand hand, BlockHitResult hitResult) {
        if (item instanceof AxeItem && CommonConfigs.AXE_STRIPPING.get()) {
            Item bark = WeatheringHelper.getBarkToStrip(state);
            if (bark != null) {
                if (level.isClientSide) {
                    level.playSound(player, pos, SoundEvents.AXE_STRIP, SoundSource.BLOCKS, 1.0f, 1.0f);
                    var barkParticle = new BlockParticleOption(ParticleTypes.BLOCK, state);
                    ParticleUtils.spawnParticlesOnBlockFaces(level, pos, barkParticle, UniformInt.of(3, 5));
                } else {
                    CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger((ServerPlayer) player, pos, stack);
                    stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
                    player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
                    if (!player.isCreative() || CommonConfigs.CREATIVE_DROP.get()) {
                        Block.popResourceFromFace(level, pos, hitResult.getDirection(), bark.getDefaultInstance());
                    }
                }
            }
        }
        return InteractionResult.PASS;
    }

    private static InteractionResult barkRepairing(Item item, ItemStack stack, BlockPos pos, BlockState state, Player player, Level level, InteractionHand hand, BlockHitResult hitResult) {
        if (stack.is(ModTags.BARK)) {
            Pair<Item, Block> fixedLog = WeatheringHelper.getBarkForStrippedLog(state).orElse(null);
            Pair<Item, Block> woodFromLog = WeatheringHelper.getWoodFromLog(state).orElse(null);

            if (fixedLog != null && stack.getItem() == fixedLog.getFirst()) {
                BlockState newBlock = fixedLog.getSecond().withPropertiesOf(state);
                if (level.isClientSide) {
                    level.playSound(player, pos, newBlock.getSoundType().getPlaceSound(), SoundSource.BLOCKS, 1.0f, 1.0f);
                } else {
                    if (!player.getAbilities().instabuild) stack.shrink(1);
                    CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger((ServerPlayer) player, pos, stack);
                    player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
                    level.setBlockAndUpdate(pos, newBlock);
                }
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
            else if (woodFromLog != null && stack.getItem() == woodFromLog.getFirst()) {
                if (hitResult.getDirection().getAxis() == state.getValue(BlockStateProperties.AXIS)) {
                    BlockState newBlock = woodFromLog.getSecond().withPropertiesOf(state);
                    if (level.isClientSide) {
                        level.playSound(player, pos, newBlock.getSoundType().getPlaceSound(), SoundSource.BLOCKS, 1.0f, 1.0f);
                    } else {
                        if (!player.getAbilities().instabuild) stack.shrink(1);
                        CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger((ServerPlayer) player, pos, stack);
                        player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
                        level.setBlockAndUpdate(pos, newBlock);
                    }
                    return InteractionResult.sidedSuccess(level.isClientSide);

                }
            }
        }
        return InteractionResult.PASS;
    }

    private static InteractionResult blockSanding(Item item, ItemStack stack, BlockPos pos, BlockState state, Player player, Level level, InteractionHand hand, BlockHitResult hitResult) {

        var sandy = DataMapHelpers.getPrevious(DataMapHelpers.Type.SAND, state);
        if (stack.is(ModBlocks.SAND_LAYER_BLOCK.get().asItem()) && (sandy.isPresent() || (state.hasProperty(ModBlockProperties.SANDINESS) && state.getValue(ModBlockProperties.SANDINESS) == 0))) {
            level.playSound(player, pos, SoundEvents.SAND_PLACE, SoundSource.BLOCKS, 1.0f, 1.0f);
            ParticleUtils.spawnParticlesOnBlockFaces(level, pos, new BlockParticleOption(ParticleTypes.FALLING_DUST, Blocks.SAND.defaultBlockState()), UniformInt.of(3, 5));
            stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
            if (player instanceof ServerPlayer serverPlayer) {
                if (!player.getAbilities().instabuild) stack.shrink(1);
                CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(serverPlayer, pos, stack);
                if (sandy.isPresent()) {
                    level.setBlockAndUpdate(pos, sandy.get());
                } else {
                    level.setBlockAndUpdate(pos, state.setValue(ModBlockProperties.SANDINESS, 1));
                }
                player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return InteractionResult.PASS;
    }

    private static InteractionResult sandShoveling(Item item, ItemStack stack, BlockPos pos, BlockState state, Player player, Level level, InteractionHand hand, BlockHitResult hitResult) {
        var unSandy = DataMapHelpers.getPrevious(DataMapHelpers.Type.SAND, state);
        if (unSandy.isPresent() && stack.is(ItemTags.SHOVELS)) {
            level.playSound(player, pos, SoundEvents.SAND_BREAK, SoundSource.BLOCKS, 1.0f, 1.0f);
            ParticleUtils.spawnParticlesOnBlockFaces(level, pos, new BlockParticleOption(ParticleTypes.FALLING_DUST, Blocks.SAND.defaultBlockState()), UniformInt.of(3, 5));
            stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
            if (player instanceof ServerPlayer serverPlayer) {
                level.setBlockAndUpdate(pos, unSandy.get());
                if (state.getValue(ModBlockProperties.SANDINESS) == 0) level.setBlockAndUpdate(pos, unSandy.get());
                if (state.getValue(ModBlockProperties.SANDINESS) == 1) level.setBlockAndUpdate(pos, state.setValue(ModBlockProperties.SANDINESS, 0));
                if (!player.isCreative() || CommonConfigs.CREATIVE_DROP.get())
                    Block.popResourceFromFace(level, pos, hitResult.getDirection(), new ItemStack(ModBlocks.SAND_LAYER_BLOCK.get()));
                CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(serverPlayer, pos, stack);
                player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return InteractionResult.PASS;
    }

    private static InteractionResult blockSnowing(Item item, ItemStack stack, BlockPos pos, BlockState state, Player player, Level level, InteractionHand hand, BlockHitResult hitResult) {
        var snowy = DataMapHelpers.getNext(DataMapHelpers.Type.SNOW, state);
        if (stack.is(Items.SNOWBALL) && snowy.isPresent()) {
            level.playSound(player, pos, SoundEvents.SNOW_PLACE, SoundSource.BLOCKS, 1.0f, 1.0f);
            ParticleUtils.spawnParticlesOnBlockFaces(level, pos, new BlockParticleOption(ParticleTypes.FALLING_DUST, Blocks.SNOW_BLOCK.defaultBlockState()), UniformInt.of(3, 5));
            stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
            if (player instanceof ServerPlayer serverPlayer) {
                CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(serverPlayer, pos, stack);
                if (!player.getAbilities().instabuild) stack.shrink(1);
                level.setBlockAndUpdate(pos, snowy.get());
                player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return InteractionResult.PASS;
    }

    private static InteractionResult snowShoveling(Item item, ItemStack stack, BlockPos pos, BlockState state, Player player, Level level, InteractionHand hand, BlockHitResult hitResult) {
        var unSnowy = DataMapHelpers.getPrevious(DataMapHelpers.Type.SNOW, state);
        if (unSnowy.isPresent() && stack.is(ItemTags.SHOVELS)) {
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
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return InteractionResult.PASS;
    }

    private static InteractionResult frostMelting(Item item, ItemStack stack, BlockPos pos, BlockState state, Player player, Level level, InteractionHand hand, BlockHitResult hitResult) {
        var unFrosted = DataMapHelpers.getPrevious(DataMapHelpers.Type.FROST, state);
        if (unFrosted.isPresent() && stack.is(ItemTags.CREEPER_IGNITERS)) {
            level.playSound(player, pos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0f, 1.0f);
            if (level.isClientSide()) ParticleUtil.spawnParticlesOnBlockFaces(level, pos, ParticleTypes.SMOKE, UniformInt.of(3, 5), -0.05f, 0.05f, false);
            if (player instanceof ServerPlayer) {
                if (!player.getAbilities().instabuild) stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
                level.setBlockAndUpdate(pos, unFrosted.get());
                level.gameEvent(player, GameEvent.SHEAR, pos);
                player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return InteractionResult.PASS;
    }

    @EventCalled
    public static void onLightningHit(ILightningStruckBlockEvent event) {
        BlockPos blockPos = event.getPos();
        LevelAccessor level = event.getLevel();
        BlockState blockState = level.getBlockState(blockPos);
        BlockGrowthHandler.tickBlock(TickSource.LIGHTNING, blockState, (ServerLevel) level, blockPos);
    }

    @EventCalled
    public static void onFireConsume(IFireConsumeBlockEvent event) {
        var level = event.getLevel();
        if (level instanceof ServerLevel serverLevel) {

            var state = event.getState();
            double charChance = CommonConfigs.FIRE_CHARS_WOOD_CHANCE.get();
            BlockState charred = WeatheringHelper.getCharredState(state);
            if (charChance == 0 || charred == null) return;

            if (serverLevel.random.nextFloat() < charChance) {
                event.setFinalState(charred.setValue(CharredBlock.SMOLDERING, serverLevel.random.nextBoolean()));
            }

        }
    }
}
