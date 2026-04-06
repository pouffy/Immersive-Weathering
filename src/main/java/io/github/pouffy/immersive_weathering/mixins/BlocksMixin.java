package io.github.pouffy.immersive_weathering.mixins;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import io.github.pouffy.immersive_weathering.blocks.ModPropaguleBlock;
import io.github.pouffy.immersive_weathering.blocks.cracked.*;
import io.github.pouffy.immersive_weathering.blocks.mossy.*;
import io.github.pouffy.immersive_weathering.blocks.rusty.IRusty;
import io.github.pouffy.immersive_weathering.blocks.rusty.RustableBarsBlock;
import io.github.pouffy.immersive_weathering.blocks.rusty.RustableDoorBlock;
import io.github.pouffy.immersive_weathering.blocks.rusty.RustableTrapdoorBlock;
import io.github.pouffy.immersive_weathering.reg.ModItems;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(Blocks.class)
public abstract class BlocksMixin {

    @ModifyReturnValue(method = "legacyStair(Lnet/minecraft/world/level/block/Block;)Lnet/minecraft/world/level/block/Block;", at = @At("RETURN"))
    private static Block legacyStairs(Block original) {
        if (original == Blocks.DEEPSLATE_BRICKS) {
            return new CrackableStairsBlock(() -> Blocks.DEEPSLATE_BRICKS, BlockBehaviour.Properties.ofLegacyCopy(original));
        }
        if (original == Blocks.DEEPSLATE_TILES) {
            return new CrackableStairsBlock(() -> Blocks.DEEPSLATE_TILES, BlockBehaviour.Properties.ofLegacyCopy(original));
        }
        return original;
    }

    @Redirect(method = "<clinit>", at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/world/level/block/grower/TreeGrower;Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/MangrovePropaguleBlock;",
            ordinal = 0
    ),
            slice = @Slice(
                    from = @At(
                            value = "CONSTANT",
                            args = "stringValue=mangrove_propagule"
                    )
            )
    )
    private static MangrovePropaguleBlock mangrovePropaguleBlock(TreeGrower grower, BlockBehaviour.Properties properties) {
        return new ModPropaguleBlock(grower, properties);
    }

    @Redirect(method = "<clinit>", at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/Block;",
            ordinal = 0
    ),
            slice = @Slice(
                    from = @At(
                            value = "CONSTANT",
                            args = "stringValue=polished_blackstone_bricks"
                    )
            )
    )
    private static Block polishedBlackstoneBricks(BlockBehaviour.Properties settings) {
        return new CrackableBlock(settings);
    }

    @Redirect(method = "<clinit>",
            at = @At(
                    value = "NEW",
                    target = "(Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/SlabBlock;",
                    ordinal = 0
            ),
            slice = @Slice(
                    from = @At(
                            value = "CONSTANT",
                            args = "stringValue=polished_blackstone_brick_slab"
                    )
            )
    )
    private static SlabBlock polishedBlackstoneBrickSlab(BlockBehaviour.Properties settings) {
        return new CrackableSlabBlock(settings);
    }

    @Redirect(
            method = "<clinit>",
            at = @At(
                    value = "NEW",
                    target = "(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/StairBlock;",
                    ordinal = 0
            ),
            slice = @Slice(
                    from = @At(
                            value = "CONSTANT",
                            args = "stringValue=polished_blackstone_brick_stairs"
                    )
            )
    )
    private static StairBlock polishedBlackstoneBricksStairs(BlockState baseBlockState, BlockBehaviour.Properties settings) {
        return new CrackableStairsBlock(() -> Blocks.POLISHED_BLACKSTONE_BRICKS, settings);
    }

    @Redirect(
            method = "<clinit>",
            at = @At(
                    value = "NEW",
                    target = "(Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/WallBlock;",
                    ordinal = 0
            ),
            slice = @Slice(
                    from = @At(
                            value = "CONSTANT",
                            args = "stringValue=polished_blackstone_brick_wall"
                    )
            )
    )
    private static WallBlock polishedBlackstoneBrickWall(BlockBehaviour.Properties settings) {
        return new CrackableWallBlock(settings);
    }


    @Redirect(
            method = "<clinit>",
            at = @At(
                    value = "NEW",
                    target = "(Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/Block;",
                    ordinal = 0
            ),
            slice = @Slice
                    (
                            from = @At
                                    (
                                            value = "CONSTANT",
                                            args = "stringValue=cracked_polished_blackstone_bricks"
                                    )
                    )
    )
    private static Block crackedPolishedBlackstoneBricks(BlockBehaviour.Properties settings) {
        return new CrackedBlock(settings);
    }

    @Redirect(
            method = "<clinit>",
            at = @At(
                    value = "NEW",
                    target = "(Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/Block;",
                    ordinal = 0
            ),
            slice = @Slice
                    (
                            from = @At
                                    (
                                            value = "CONSTANT",
                                            args = "stringValue=cracked_stone_bricks"
                                    )
                    )
    )
    private static Block crackedStoneBricks(BlockBehaviour.Properties settings) {
        return new CrackedBlock(settings);
    }

    @Redirect(
            method = "<clinit>",
            at = @At(
                    value = "NEW",
                    target = "(Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/Block;",
                    ordinal = 0
            ),
            slice = @Slice
                    (
                            from = @At
                                    (
                                            value = "CONSTANT",
                                            args = "stringValue=cracked_deepslate_bricks"
                                    )
                    )
    )
    private static Block crackedDeepslateBricks(BlockBehaviour.Properties settings) {
        return new CrackedBlock(settings);
    }

    @Redirect(
            method = "<clinit>",
            at = @At(
                    value = "NEW",
                    target = "(Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/Block;",
                    ordinal = 0
            ),
            slice = @Slice
                    (
                            from = @At
                                    (
                                            value = "CONSTANT",
                                            args = "stringValue=cracked_deepslate_tiles"
                                    )
                    )
    )
    private static Block crackedDeepslateTiles(BlockBehaviour.Properties settings) {
        return new CrackedBlock(settings);
    }


    @Redirect(
            method = "<clinit>",
            at = @At(
                    value = "NEW",
                    target = "(Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/Block;",
                    ordinal = 0
            ),
            slice = @Slice(
                    from = @At(
                            value = "CONSTANT",
                            args = "stringValue=deepslate_bricks"
                    )
            )
    )
    private static Block deepslateBricks(BlockBehaviour.Properties settings) {
        return new CrackableBlock(settings);
    }

    @Redirect(
            method = "<clinit>",
            at = @At(
                    value = "NEW",
                    target = "(Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/SlabBlock;",
                    ordinal = 0
            ),
            slice = @Slice(
                    from = @At(
                            value = "CONSTANT",
                            args = "stringValue=deepslate_brick_slab"
                    )
            )
    )
    private static SlabBlock deepslateBrickSlab(BlockBehaviour.Properties settings) {
        return new CrackableSlabBlock(settings);
    }

    @Redirect(
            method = "<clinit>",
            at = @At(
                    value = "NEW",
                    target = "(Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/WallBlock;",
                    ordinal = 0
            ),
            slice = @Slice(
                    from = @At(
                            value = "CONSTANT",
                            args = "stringValue=deepslate_brick_wall"
                    )
            )
    )
    private static WallBlock deepslateBrickWall(BlockBehaviour.Properties settings) {
        return new CrackableWallBlock(settings);
    }

    @Redirect(
            method = "<clinit>",
            at = @At(
                    value = "NEW",
                    target = "(Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/Block;",
                    ordinal = 0
            ),
            slice = @Slice(
                    from = @At(
                            value = "CONSTANT",
                            args = "stringValue=deepslate_tiles"
                    )
            )
    )
    private static Block deepslateTiles(BlockBehaviour.Properties settings) {
        return new CrackableBlock(settings);
    }

    @Redirect(
            method = "<clinit>",
            at = @At(
                    value = "NEW",
                    target = "(Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/SlabBlock;",
                    ordinal = 0
            ),
            slice = @Slice(
                    from = @At(
                            value = "CONSTANT",
                            args = "stringValue=deepslate_tile_slab"
                    )
            )
    )
    private static SlabBlock deepslateTileSlab(BlockBehaviour.Properties settings) {
        return new CrackableSlabBlock(settings);
    }

    @Redirect(
            method = "<clinit>",
            at = @At(
                    value = "NEW",
                    target = "(Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/WallBlock;",
                    ordinal = 0
            ),
            slice = @Slice(
                    from = @At(
                            value = "CONSTANT",
                            args = "stringValue=deepslate_tile_wall"
                    )
            )
    )
    private static WallBlock deepslateTileWall(BlockBehaviour.Properties settings) {
        return new CrackableWallBlock(settings);
    }

    @Redirect
            (
                    method = "<clinit>",
                    at = @At(
                            value = "NEW",
                            target = "(Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/Block;",
                            ordinal = 0
                    ),
                    slice = @Slice(
                            from = @At(
                                    value = "CONSTANT",
                                    args = "stringValue=nether_bricks"
                            )
                    )
            )
    private static Block netherBricks(BlockBehaviour.Properties settings) {
        return new CrackableBlock(settings);
    }

    @Redirect(
            method = "<clinit>",
            at = @At(
                    value = "NEW",
                    target = "(Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/SlabBlock;",
                    ordinal = 0
            ),
            slice = @Slice(
                    from = @At(
                            value = "CONSTANT",
                            args = "stringValue=nether_brick_slab"
                    )
            )
    )
    private static SlabBlock netherBrickSlab(BlockBehaviour.Properties settings) {
        return new CrackableSlabBlock(settings);
    }

    @Redirect(
            method = "<clinit>",
            at = @At(
                    value = "NEW",
                    target = "(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/StairBlock;",
                    ordinal = 0
            ),
            slice = @Slice
                    (
                            from = @At
                                    (
                                            value = "CONSTANT",
                                            args = "stringValue=nether_brick_stairs"
                                    )
                    )
    )
    private static StairBlock netherBrickStairs(BlockState baseBlockState, BlockBehaviour.Properties settings) {
        return new CrackableStairsBlock(() -> Blocks.NETHER_BRICKS, settings);
    }

    @Redirect(
            method = "<clinit>",
            at = @At(
                    value = "NEW",
                    target = "(Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/WallBlock;",
                    ordinal = 0
            ),
            slice = @Slice(
                    from = @At(
                            value = "CONSTANT",
                            args = "stringValue=nether_brick_wall"
                    )
            )
    )
    private static WallBlock netherBrickWall(BlockBehaviour.Properties settings) {
        return new CrackableWallBlock(settings);
    }


    @Redirect(
            method = "<clinit>",
            at = @At(
                    value = "NEW",
                    target = "(Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/Block;",
                    ordinal = 0
            ),
            slice = @Slice
                    (
                            from = @At(
                                    value = "CONSTANT",
                                    args = "stringValue=cracked_nether_bricks"
                            )
                    )
    )
    private static Block crackedNetherBricks(BlockBehaviour.Properties settings) {
        return new CrackedBlock(settings);
    }


    @Redirect(
            method = "<clinit>",
            at = @At(
                    value = "NEW",
                    target = "(Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/Block;",
                    ordinal = 0
            ),
            slice = @Slice(
                    from = @At(
                            value = "CONSTANT",
                            args = "stringValue=bricks"
                    )
            )
    )
    private static Block bricks(BlockBehaviour.Properties settings) {
        return new CrackableMossableBlock(settings);
    }

    @Redirect(
            method = "<clinit>",
            at = @At(
                    value = "NEW",
                    target = "(Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/SlabBlock;",
                    ordinal = 0
            ),
            slice = @Slice(
                    from = @At(
                            value = "CONSTANT",
                            args = "stringValue=brick_slab"
                    )
            )
    )
    private static SlabBlock brickSlab(BlockBehaviour.Properties settings) {
        return new CrackableMossableSlabBlock(settings);
    }

    @Redirect(
            method = "<clinit>",
            at = @At(
                    value = "NEW",
                    target = "(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/StairBlock;",
                    ordinal = 0
            ),
            slice = @Slice(
                    from = @At(
                            value = "CONSTANT",
                            args = "stringValue=brick_stairs"
                    )
            )
    )
    private static StairBlock brickStairs(BlockState baseBlockState, BlockBehaviour.Properties settings) {
        return new CrackableMossableStairsBlock(() -> Blocks.BRICKS, settings);
    }

    @Redirect(
            method = "<clinit>",
            at = @At(
                    value = "NEW",
                    target = "(Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/WallBlock;",
                    ordinal = 0
            ),
            slice = @Slice(
                    from = @At(
                            value = "CONSTANT",
                            args = "stringValue=brick_wall"
                    )
            )
    )
    private static WallBlock brickWall(BlockBehaviour.Properties settings) {
        return new CrackableMossableWallBlock(settings);
    }


    @Redirect(
            method = "<clinit>",
            at = @At(
                    value = "NEW",
                    target = "(Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/Block;",
                    ordinal = 0
            ),
            slice = @Slice(
                    from = @At(
                            value = "CONSTANT",
                            args = "stringValue=stone_bricks"
                    )
            )
    )
    private static Block stoneBricks(BlockBehaviour.Properties settings) {
        return new CrackableMossableBlock(settings);
    }

    @Redirect(
            method = "<clinit>",
            at = @At(
                    value = "NEW",
                    target = "(Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/SlabBlock;",
                    ordinal = 0
            ),
            slice = @Slice(
                    from = @At(
                            value = "CONSTANT",
                            args = "stringValue=stone_brick_slab"
                    )
            )
    )
    private static SlabBlock stoneBrickSlab(BlockBehaviour.Properties settings) {
        return new CrackableMossableSlabBlock(settings);
    }

    @Redirect(
            method = "<clinit>",
            at = @At(
                    value = "NEW",
                    target = "(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/StairBlock;",
                    ordinal = 0
            ),
            slice = @Slice(
                    from = @At(
                            value = "CONSTANT",
                            args = "stringValue=stone_brick_stairs"
                    )
            )
    )
    private static StairBlock stoneBricksStairs(BlockState baseBlockState, BlockBehaviour.Properties settings) {
        return new CrackableMossableStairsBlock(() -> Blocks.STONE_BRICKS, settings);
    }

    @Redirect(
            method = "<clinit>",
            at = @At(
                    value = "NEW",
                    target = "(Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/WallBlock;",
                    ordinal = 0
            ),
            slice = @Slice(
                    from = @At(
                            value = "CONSTANT",
                            args = "stringValue=stone_brick_wall"
                    )
            )
    )
    private static WallBlock stoneBrickWall(BlockBehaviour.Properties settings) {
        return new CrackableMossableWallBlock(settings);
    }

    @Redirect(
            method = "<clinit>",
            at = @At(
                    value = "NEW",
                    target = "(Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/Block;",
                    ordinal = 0
            ),
            slice = @Slice(
                    from = @At(
                            value = "CONSTANT",
                            args = "stringValue=cobblestone"
                    )
            )
    )
    private static Block cobblestone(BlockBehaviour.Properties settings) {
        return new MossableBlock(settings);
    }

    @Redirect(
            method = "<clinit>",
            at = @At(
                    value = "NEW",
                    target = "(Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/Block;",
                    ordinal = 0
            ),
            slice = @Slice(
                    from = @At(
                            value = "CONSTANT",
                            args = "stringValue=chiseled_stone_bricks"
                    )
            )
    )
    private static Block chiseled_stone_bricks(BlockBehaviour.Properties settings) {
        return new MossableBlock(settings);
    }

    @Redirect(
            method = "<clinit>",
            at = @At(
                    value = "NEW",
                    target = "(Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/SlabBlock;",
                    ordinal = 0
            ),
            slice = @Slice(
                    from = @At(
                            value = "CONSTANT",
                            args = "stringValue=cobblestone_slab"
                    )
            )
    )
    private static SlabBlock cobblestoneSlab(BlockBehaviour.Properties settings) {
        return new MossableSlabBlock(settings);
    }

    @Redirect(
            method = "<clinit>",
            at = @At(
                    value = "NEW",
                    target = "(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/StairBlock;",
                    ordinal = 0
            ),
            slice = @Slice(
                    from = @At(
                            value = "CONSTANT",
                            args = "stringValue=cobblestone_stairs"
                    )
            )
    )
    private static StairBlock cobblestoneStairs(BlockState baseBlockState, BlockBehaviour.Properties settings) {
        return new MossableStairsBlock(() -> Blocks.COBBLESTONE, settings);
    }

    @Redirect(
            method = "<clinit>",
            at = @At(
                    value = "NEW",
                    target = "(Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/WallBlock;",
                    ordinal = 0
            ),
            slice = @Slice(
                    from = @At(
                            value = "CONSTANT",
                            args = "stringValue=cobblestone_wall"
                    )
            )
    )
    private static WallBlock cobblestoneWall(BlockBehaviour.Properties settings) {
        return new MossableWallBlock(settings);
    }

    @Redirect(
            method = "<clinit>",
            at = @At(
                    value = "NEW",
                    target = "(Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/Block;",
                    ordinal = 0
            ),
            slice = @Slice(
                    from = @At(
                            value = "CONSTANT",
                            args = "stringValue=mossy_stone_bricks"
                    )
            )
    )
    private static Block mossyStoneBricks(BlockBehaviour.Properties settings) {
        return new MossyBlock(settings);
    }

    @Redirect(
            method = "<clinit>",
            at = @At(
                    value = "NEW",
                    target = "(Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/SlabBlock;",
                    ordinal = 0
            ),
            slice = @Slice(
                    from = @At(
                            value = "CONSTANT",
                            args = "stringValue=mossy_stone_brick_slab"
                    )
            )
    )
    private static SlabBlock mossyStoneBrickSlab(BlockBehaviour.Properties settings) {
        return new MossySlabBlock(settings);
    }

    @Redirect(
            method = "<clinit>",
            at = @At(
                    value = "NEW",
                    target = "(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/StairBlock;",
                    ordinal = 0
            ),
            slice = @Slice(
                    from = @At(
                            value = "CONSTANT",
                            args = "stringValue=mossy_stone_brick_stairs"
                    )
            )
    )
    private static StairBlock mossyStoneBrickStairs(BlockState baseBlockState, BlockBehaviour.Properties settings) {
        return new MossyStairsBlock(() -> Blocks.MOSSY_STONE_BRICKS, settings);
    }

    @Redirect(
            method = "<clinit>",
            at = @At(
                    value = "NEW",
                    target = "(Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/WallBlock;",
                    ordinal = 0
            ),
            slice = @Slice(
                    from = @At(
                            value = "CONSTANT",
                            args = "stringValue=mossy_stone_brick_wall"
                    )
            )
    )
    private static WallBlock mossyStoneBrickWall(BlockBehaviour.Properties settings) {
        return new MossyWallBlock(settings);
    }


    @Redirect(
            method = "<clinit>",
            at = @At(
                    value = "NEW",
                    target = "(Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/Block;",
                    ordinal = 0
            ),
            slice = @Slice(
                    from = @At(
                            value = "CONSTANT",
                            args = "stringValue=mossy_cobblestone"
                    )
            )
    )
    private static Block mossyCobblestone(BlockBehaviour.Properties settings) {
        return new MossyBlock(settings);
    }

    @Redirect(
            method = "<clinit>",
            at = @At(
                    value = "NEW",
                    target = "(Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/SlabBlock;",
                    ordinal = 0
            ),
            slice = @Slice(
                    from = @At(
                            value = "CONSTANT",
                            args = "stringValue=mossy_cobblestone_slab"
                    )
            )
    )
    private static SlabBlock mossyCobblestoneSlab(BlockBehaviour.Properties settings) {
        return new MossySlabBlock(settings);
    }

    @Redirect(
            method = "<clinit>",
            at = @At(
                    value = "NEW",
                    target = "(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/StairBlock;",
                    ordinal = 0
            ),
            slice = @Slice(
                    from = @At(
                            value = "CONSTANT",
                            args = "stringValue=mossy_cobblestone_stairs"
                    )
            )
    )
    private static StairBlock mossyCobblestoneStairs(BlockState baseBlockState, BlockBehaviour.Properties settings) {
        return new MossyStairsBlock(() -> Blocks.MOSSY_COBBLESTONE, settings);
    }

    @Redirect(
            method = "<clinit>",
            at = @At(
                    value = "NEW",
                    target = "(Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/WallBlock;",
                    ordinal = 0
            ),
            slice = @Slice(
                    from = @At(
                            value = "CONSTANT",
                            args = "stringValue=mossy_cobblestone_wall"
                    )
            )
    )
    private static WallBlock mossyCobblestoneWall(BlockBehaviour.Properties settings) {
        return new MossyWallBlock(settings);
    }


    @Redirect(
            method = "<clinit>",
            at = @At(
                    value = "NEW",
                    target = "(Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/IronBarsBlock;",
                    ordinal = 0
            ),
            slice = @Slice(
                    from = @At(
                            value = "CONSTANT",
                            args = "stringValue=iron_bars"
                    )
            )
    )
    private static IronBarsBlock ironBars(BlockBehaviour.Properties settings) {
        return new RustableBarsBlock(IRusty.RustLevel.UNAFFECTED, settings);
    }

    @Redirect(
            method = "<clinit>",
            at = @At(
                    value = "NEW",
                    target = "(Lnet/minecraft/world/level/block/state/properties/BlockSetType;Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/DoorBlock;",
                    ordinal = 0
            ),
            slice = @Slice(
                    from = @At(
                            value = "CONSTANT",
                            args = "stringValue=iron_door"
                    )
            )
    )

    private static DoorBlock ironDoor(BlockSetType type, BlockBehaviour.Properties properties) {
        return new RustableDoorBlock(IRusty.RustLevel.UNAFFECTED, properties);
    }

    @Redirect(
            method = "<clinit>",
            at = @At(
                    value = "NEW",
                    target = "(Lnet/minecraft/world/level/block/state/properties/BlockSetType;Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/TrapDoorBlock;",
                    ordinal = 0
            ),
            slice = @Slice(
                    from = @At(
                            value = "CONSTANT",
                            args = "stringValue=iron_trapdoor"
                    )
            )
    )
    private static TrapDoorBlock ironTrapdoor(BlockSetType type, BlockBehaviour.Properties properties) {
        return new RustableTrapdoorBlock(IRusty.RustLevel.UNAFFECTED, properties);
    }


    @Redirect(
            method = "<clinit>",
            at = @At(
                    value = "NEW",
                    target = "(Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/Block;",
                    ordinal = 0
            ),
            slice = @Slice(
                    from = @At(
                            value = "CONSTANT",
                            args = "stringValue=prismarine_bricks"
                    )
            )
    )
    private static Block prismarineBricks(BlockBehaviour.Properties settings) {
        return new CrackableBlock(settings);
    }

    @Redirect(
            method = "<clinit>",
            at = @At(
                    value = "NEW",
                    target = "(Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/SlabBlock;",
                    ordinal = 0
            ),
            slice = @Slice(
                    from = @At(
                            value = "CONSTANT",
                            args = "stringValue=prismarine_brick_slab"
                    )
            )
    )
    private static SlabBlock prismarineBrickSlab(BlockBehaviour.Properties settings) {
        return new CrackableSlabBlock(settings);
    }

    @Shadow
    @Final
    public static Block PRISMARINE_BRICKS;

    @Redirect(
            method = "<clinit>",
            at = @At(
                    value = "NEW",
                    target = "(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/StairBlock;",
                    ordinal = 0
            ),
            slice = @Slice(
                    from = @At(
                            value = "CONSTANT",
                            args = "stringValue=prismarine_brick_stairs"
                    )
            )
    )
    private static StairBlock prismarineBricksStairs(BlockState baseBlockState, BlockBehaviour.Properties settings) {
        return new CrackableStairsBlock(() -> PRISMARINE_BRICKS, settings);
    }


    @Redirect(
            method = "<clinit>",
            at = @At(
                    value = "NEW",
                    target = "(Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/Block;",
                    ordinal = 0
            ),
            slice = @Slice(
                    from = @At(
                            value = "CONSTANT",
                            args = "stringValue=end_stone_bricks"
                    )
            )
    )
    private static Block endStoneBricks(BlockBehaviour.Properties settings) {
        return new CrackableBlock(settings);
    }

    @Redirect(
            method = "<clinit>",
            at = @At(
                    value = "NEW",
                    target = "(Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/SlabBlock;",
                    ordinal = 0
            ),
            slice = @Slice(
                    from = @At(
                            value = "CONSTANT",
                            args = "stringValue=end_stone_brick_slab"
                    )
            )
    )
    private static SlabBlock endStoneBrickSlab(BlockBehaviour.Properties settings) {
        return new CrackableSlabBlock(settings);
    }

    @Shadow
    @Final
    public static Block END_STONE_BRICKS;

    @Redirect(
            method = "<clinit>",
            at = @At(
                    value = "NEW",
                    target = "(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/StairBlock;",
                    ordinal = 0
            ),
            slice = @Slice(
                    from = @At(
                            value = "CONSTANT",
                            args = "stringValue=end_stone_brick_stairs"
                    )
            )
    )
    private static StairBlock endStoneBricksStairs(BlockState baseBlockState, BlockBehaviour.Properties settings) {
        return new CrackableStairsBlock(() -> END_STONE_BRICKS, settings);
    }

    @Redirect(
            method = "<clinit>",
            at = @At(
                    value = "NEW",
                    target = "(Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/WallBlock;",
                    ordinal = 0
            ),
            slice = @Slice(
                    from = @At(
                            value = "CONSTANT",
                            args = "stringValue=end_stone_brick_wall"
                    )
            )
    )
    private static WallBlock endStoneBrickWall(BlockBehaviour.Properties settings) {
        return new CrackableWallBlock(settings);
    }

    @Redirect(
            method = "<clinit>",
            at = @At(
                    value = "NEW",
                    target = "(Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/Block;",
                    ordinal = 0
            ),
            slice = @Slice(
                    from = @At(
                            value = "CONSTANT",
                            args = "stringValue=stone"
                    )
            )
    )
    private static Block stone(BlockBehaviour.Properties settings) {
        return new MossableBlock(settings);
    }

    @Redirect(
            method = "<clinit>",
            at = @At(
                    value = "NEW",
                    target = "(Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/SlabBlock;",
                    ordinal = 0
            ),
            slice = @Slice(
                    from = @At(
                            value = "CONSTANT",
                            args = "stringValue=stone_slab"
                    )
            )
    )
    private static SlabBlock stoneSlab(BlockBehaviour.Properties settings) {
        return new MossableSlabBlock(settings);
    }

    @Redirect(
            method = "<clinit>",
            at = @At(
                    value = "NEW",
                    target = "(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/StairBlock;",
                    ordinal = 0
            ),
            slice = @Slice(
                    from = @At(
                            value = "CONSTANT",
                            args = "stringValue=stone_stairs"
                    )
            )
    )
    private static StairBlock stoneStairs(BlockState baseBlockState, BlockBehaviour.Properties settings) {
        return new MossableStairsBlock(() -> Blocks.STONE, settings);
    }
}
