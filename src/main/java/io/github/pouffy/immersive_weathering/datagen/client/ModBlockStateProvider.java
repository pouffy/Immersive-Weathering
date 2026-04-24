package io.github.pouffy.immersive_weathering.datagen.client;

import com.mojang.datafixers.util.Pair;
import io.github.pouffy.immersive_weathering.ImmersiveWeathering;
import io.github.pouffy.immersive_weathering.blocks.*;
import io.github.pouffy.immersive_weathering.blocks.charred.CharredBlock;
import io.github.pouffy.immersive_weathering.blocks.charred.CharredPillarBlock;
import io.github.pouffy.immersive_weathering.blocks.sandy.SandyBlock;
import io.github.pouffy.immersive_weathering.blocks.sandy.SandyWallBlock;
import io.github.pouffy.immersive_weathering.blocks.soil_types.EarthenClayBlock;
import io.github.pouffy.immersive_weathering.blocks.soil_types.EarthenClayBlockGrassy;
import io.github.pouffy.immersive_weathering.blocks.soil_types.ModFarmlandBlock;
import io.github.pouffy.immersive_weathering.reg.ModBlocks;
import net.minecraft.Util;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.models.blockstates.Condition;
import net.minecraft.data.models.blockstates.MultiPartGenerator;
import net.minecraft.data.models.blockstates.Variant;
import net.minecraft.data.models.model.ModelLocationUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.*;
import net.neoforged.neoforge.client.model.generators.*;
import net.neoforged.neoforge.common.DataMapHooks;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import static net.minecraft.data.models.BlockModelGenerators.MULTIFACE_GENERATOR;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, String modid, ExistingFileHelper exFileHelper) {
        super(output, modid, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        leafPile(ModBlocks.AZALEA_FLOWER_PILE.get());
        ModBlocks.LEAF_PILES.values().forEach(this::leafPile);
        layered(ModBlocks.SAND_LAYER_BLOCK, "minecraft:block/sand");
        layered(ModBlocks.RED_SAND_LAYER_BLOCK, "minecraft:block/red_sand");
        createMultiface(ModBlocks.FROST.get());
        createMultiface(ModBlocks.SOOT.get());
        charredLog(ModBlocks.CHARRED_LOG.get());
        charredPlanks(ModBlocks.CHARRED_PLANKS.get());
        charredSlab(ModBlocks.CHARRED_SLAB.get(), ModBlocks.CHARRED_PLANKS);
        charredStairs(ModBlocks.CHARRED_STAIRS.get(), ModBlocks.CHARRED_PLANKS);
        simpleWall(ModBlocks.STONE_WALL, () -> Blocks.STONE);
        mossyWall(ModBlocks.MOSSY_STONE_WALL, ModBlocks.MOSSY_STONE);
        snowyWall(ModBlocks.SNOWY_STONE_WALL, "snowy_stone_side");
        sandyWall(ModBlocks.SANDY_STONE_WALL, ModBlocks.SANDY_STONE);
        simpleWall(ModBlocks.SNOWY_COBBLESTONE_WALL, ModBlocks.SNOWY_COBBLESTONE);
        sandyWall(ModBlocks.SANDY_COBBLESTONE_WALL, ModBlocks.SANDY_COBBLESTONE);
        simpleWall(ModBlocks.CRACKED_STONE_BRICK_WALL, () -> Blocks.CRACKED_STONE_BRICKS);
        snowyWall(ModBlocks.SNOWY_STONE_BRICK_WALL, "snowy_stone_bricks_side");
        sandyWall(ModBlocks.SANDY_STONE_BRICK_WALL, ModBlocks.SANDY_STONE_BRICKS);
        simpleWall(ModBlocks.CRACKED_DEEPSLATE_BRICK_WALL, () -> Blocks.CRACKED_DEEPSLATE_BRICKS);
        simpleWall(ModBlocks.CRACKED_DEEPSLATE_TILE_WALL, () -> Blocks.CRACKED_DEEPSLATE_TILES);
        simpleWall(ModBlocks.CRACKED_BRICK_WALL, ModBlocks.CRACKED_BRICKS);
        mossyWall(ModBlocks.MOSSY_BRICK_WALL, "mossy_bricks", "mossy_bricks");
        simpleWall(ModBlocks.PRISMARINE_BRICK_WALL, () -> Blocks.PRISMARINE_BRICKS);
        simpleWall(ModBlocks.CRACKED_PRISMARINE_BRICK_WALL, ModBlocks.CRACKED_PRISMARINE_BRICKS);
        simpleWall(ModBlocks.DARK_PRISMARINE_WALL, () -> Blocks.DARK_PRISMARINE);
        simpleWall(ModBlocks.CRACKED_NETHER_BRICK_WALL, () -> Blocks.CRACKED_NETHER_BRICKS);
        simpleWall(ModBlocks.CRACKED_POLISHED_BLACKSTONE_BRICK_WALL, () -> Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS);
        simpleWall(ModBlocks.CRACKED_END_STONE_BRICK_WALL, ModBlocks.CRACKED_END_STONE_BRICKS);
        cubeSideBottomTop(ModBlocks.MOSSY_STONE, ImmersiveWeathering.res("block/mossy_stone_top"), ImmersiveWeathering.res("block/mossy_stone_side"), ResourceLocation.withDefaultNamespace("block/stone"));
        sidedStairs(ModBlocks.MOSSY_STONE_STAIRS, ImmersiveWeathering.res("block/mossy_stone_side"), ImmersiveWeathering.res("block/mossy_stone_top"), ResourceLocation.withDefaultNamespace("block/stone"));
        sidedSlab(ModBlocks.MOSSY_STONE_SLAB, ImmersiveWeathering.res("block/mossy_stone_side"), ImmersiveWeathering.res("block/mossy_stone_top"), ResourceLocation.withDefaultNamespace("block/stone"));
        cubeSideBottomTop(ModBlocks.SNOWY_STONE, ResourceLocation.parse("minecraft:block/snow"), ImmersiveWeathering.res("block/snowy_stone_side"), ResourceLocation.withDefaultNamespace("block/stone"));
        sidedStairs(ModBlocks.SNOWY_STONE_STAIRS, ImmersiveWeathering.res("block/snowy_stone_side"), ResourceLocation.parse("minecraft:block/snow"), ResourceLocation.withDefaultNamespace("block/stone"));
        sidedSlab(ModBlocks.SNOWY_STONE_SLAB, ImmersiveWeathering.res("block/snowy_stone_side"), ResourceLocation.parse("minecraft:block/snow"), ResourceLocation.withDefaultNamespace("block/stone"));
        simpleWall(ModBlocks.CRACKED_TUFF_BRICK_WALL, ModBlocks.CRACKED_TUFF_BRICKS);

        simpleBlockAndItem(ModBlocks.SNOWY_COBBLESTONE);
        simpleStairs(ModBlocks.SNOWY_COBBLESTONE_STAIRS, ModBlocks.SNOWY_COBBLESTONE);
        simpleSlab(ModBlocks.SNOWY_COBBLESTONE_SLAB, ModBlocks.SNOWY_COBBLESTONE);

        this.getVariantBuilder(ModBlocks.EARTHEN_CLAY.get()).forAllStatesExcept((state) -> {
            boolean waterlogged = state.getValue(EarthenClayBlock.WATERLOGGED);
            var top = ImmersiveWeathering.res("block/earthen_clay_top" + (waterlogged ? "_soaked" : ""));
            var side = ImmersiveWeathering.res("block/earthen_clay_side" + (waterlogged ? "_soaked" : ""));
            var bottom = ImmersiveWeathering.res("block/earthen_clay_bottom" + (waterlogged ? "_soaked" : ""));
            return ConfiguredModel.builder().modelFile(this.models().cubeBottomTop("earthen_clay" + (waterlogged ? "_soaked" : ""), side, bottom, top)).build();
        });
        this.simpleBlockItem(ModBlocks.EARTHEN_CLAY.get(), new ModelFile.UncheckedModelFile(ImmersiveWeathering.res("block/earthen_clay")));
        this.getVariantBuilder(ModBlocks.GRASSY_EARTHEN_CLAY.get()).forAllStatesExcept((state) -> {
            boolean waterlogged = state.getValue(EarthenClayBlockGrassy.WATERLOGGED);
            boolean snowy = state.getValue(EarthenClayBlockGrassy.SNOWY);
            var top = ImmersiveWeathering.res("block/grassy_earthen_clay_top" + (waterlogged ? "_soaked" : ""));
            var side = ImmersiveWeathering.res("block/grassy_earthen_clay_side" + (waterlogged ? "_soaked" : ""));
            var bottom = ImmersiveWeathering.res("block/earthen_clay_bottom" + (waterlogged ? "_soaked" : ""));
            var model = this.models().withExistingParent("grassy_earthen_clay" + (waterlogged ? "_soaked" : ""), ImmersiveWeathering.res("block/grassy_block"))
                    .texture("particle", bottom)
                    .texture("top", top)
                    .texture("top_overlay", ImmersiveWeathering.res("block/grassy_earthen_clay_top_overlay"))
                    .texture("bottom", bottom)
                    .texture("side", side)
                    .texture("side_overlay", ImmersiveWeathering.res("block/grassy_earthen_clay_side_overlay"));
            var snowyModel = this.models().withExistingParent("grassy_earthen_clay" + (waterlogged ? "_soaked_snowy" : "_snowy"), ImmersiveWeathering.res("block/grassy_snowy_block"))
                    .texture("particle", bottom)
                    .texture("side", side)
                    .texture("bottom", bottom);
            if (snowy) {
                return ConfiguredModel.builder().modelFile(snowyModel).build();
            }
            return ConfiguredModel.builder().modelFile(model).nextModel().modelFile(model).rotationY(90).nextModel().modelFile(model).rotationY(180).nextModel().modelFile(model).rotationY(270).build();
        });
        this.simpleBlockItem(ModBlocks.GRASSY_EARTHEN_CLAY.get(), new ModelFile.UncheckedModelFile(ImmersiveWeathering.res("block/grassy_earthen_clay")));

        this.getVariantBuilder(ModBlocks.SANDY_STONE.get()).forAllStatesExcept((state) -> {
            int sandiness = state.getValue(ModBlockProperties.SANDINESS);
            var model = this.models().cubeAll("sandy_stone_" + sandiness, ImmersiveWeathering.res("block/sandy_stone_" + sandiness));
            var modelMirrored = this.models().withExistingParent("sandy_stone_" + sandiness + "_mirrored", ResourceLocation.parse("minecraft:block/cube_mirrored_all")).texture("all", ImmersiveWeathering.res("block/sandy_stone_" + sandiness));
            return ConfiguredModel.builder().modelFile(model).nextModel().modelFile(modelMirrored).nextModel().modelFile(model).rotationY(180).nextModel().modelFile(modelMirrored).rotationY(180).build();
        }, ModBlockProperties.SAND_AGE);
        this.simpleBlockItem(ModBlocks.SANDY_STONE.get(), new ModelFile.UncheckedModelFile(ImmersiveWeathering.res("block/sandy_stone_0")));
        this.getVariantBuilder(ModBlocks.SANDY_STONE_SLAB.get()).forAllStatesExcept((state) -> {
            int sandiness = state.getValue(ModBlockProperties.SANDINESS);
            SlabType type = state.getValue(SlabBlock.TYPE);
            var texture = ImmersiveWeathering.res("block/sandy_stone_" + sandiness);
            var bottom = this.models().slab("sandy_stone_slab_"+sandiness, texture, texture, texture);
            var top = this.models().slabTop("sandy_stone_slab_top_"+sandiness, texture, texture, texture);
            var doubleSlab = ImmersiveWeathering.res("block/sandy_stone_" + sandiness);
            var model = switch (type) {
                case TOP -> top;
                case BOTTOM -> bottom;
                case DOUBLE -> this.models().getExistingFile(doubleSlab);
            };
            return ConfiguredModel.builder().modelFile(model).build();
        }, ModBlockProperties.SAND_AGE, StairBlock.WATERLOGGED);
        this.simpleBlockItem(ModBlocks.SANDY_STONE_SLAB.get(), new ModelFile.UncheckedModelFile(ImmersiveWeathering.res("block/sandy_stone_slab_0")));
        this.getVariantBuilder(ModBlocks.SANDY_STONE_STAIRS.get()).forAllStatesExcept((state) -> {
            int sandiness = state.getValue(ModBlockProperties.SANDINESS);
            Direction facing = state.getValue(StairBlock.FACING);
            Half half = state.getValue(StairBlock.HALF);
            StairsShape shape = state.getValue(StairBlock.SHAPE);
            var texture = ImmersiveWeathering.res("block/sandy_stone_" + sandiness);
            ModelFile stairs = this.models().stairs("sandy_stone_stairs_"+sandiness, texture, texture, texture);
            ModelFile stairsInner = this.models().stairsInner("sandy_stone_stairs_inner_"+sandiness, texture, texture, texture);
            ModelFile stairsOuter = this.models().stairsOuter("sandy_stone_stairs_outer_"+sandiness, texture, texture, texture);
            int yRot = (int)facing.getClockWise().toYRot();
            if (shape == StairsShape.INNER_LEFT || shape == StairsShape.OUTER_LEFT) {
                yRot += 270;
            }

            if (shape != StairsShape.STRAIGHT && half == Half.TOP) {
                yRot += 90;
            }

            yRot %= 360;
            boolean uvlock = yRot != 0 || half == Half.TOP;
            return ConfiguredModel.builder().modelFile(shape == StairsShape.STRAIGHT ? stairs : (shape != StairsShape.INNER_LEFT && shape != StairsShape.INNER_RIGHT ? stairsOuter : stairsInner)).rotationX(half == Half.BOTTOM ? 0 : 180).rotationY(yRot).uvLock(uvlock).build();
        }, ModBlockProperties.SAND_AGE, StairBlock.WATERLOGGED);
        this.simpleBlockItem(ModBlocks.SANDY_STONE_STAIRS.get(), models().stairs("sandy_stone_stairs_inventory", ImmersiveWeathering.res("block/sandy_stone_0"), ImmersiveWeathering.res("block/sandy_stone_0"), ImmersiveWeathering.res("block/sandy_stone_0")));
        this.simpleBlockItem(ModBlocks.SANDY_STONE_STAIRS.get(), new ModelFile.UncheckedModelFile(ImmersiveWeathering.res("block/sandy_stone_stairs_inventory")));

        this.getVariantBuilder(ModBlocks.SANDY_COBBLESTONE.get()).forAllStatesExcept((state) -> {
            int sandiness = state.getValue(ModBlockProperties.SANDINESS);
            var model = this.models().cubeAll("sandy_cobblestone_" + sandiness, ImmersiveWeathering.res("block/sandy_cobblestone_" + sandiness));
            return ConfiguredModel.builder().modelFile(model).build();
        }, ModBlockProperties.SAND_AGE);
        this.simpleBlockItem(ModBlocks.SANDY_COBBLESTONE.get(), new ModelFile.UncheckedModelFile(ImmersiveWeathering.res("block/sandy_cobblestone_0")));
        this.getVariantBuilder(ModBlocks.SANDY_COBBLESTONE_SLAB.get()).forAllStatesExcept((state) -> {
            int sandiness = state.getValue(ModBlockProperties.SANDINESS);
            SlabType type = state.getValue(SlabBlock.TYPE);
            var texture = ImmersiveWeathering.res("block/sandy_cobblestone_" + sandiness);
            var bottom = this.models().slab("sandy_cobblestone_slab_"+sandiness, texture, texture, texture);
            var top = this.models().slabTop("sandy_cobblestone_slab_top_"+sandiness, texture, texture, texture);
            var doubleSlab = ImmersiveWeathering.res("block/sandy_cobblestone_" + sandiness);
            var model = switch (type) {
                case TOP -> top;
                case BOTTOM -> bottom;
                case DOUBLE -> this.models().getExistingFile(doubleSlab);
            };
            return ConfiguredModel.builder().modelFile(model).build();
        }, ModBlockProperties.SAND_AGE, StairBlock.WATERLOGGED);
        this.simpleBlockItem(ModBlocks.SANDY_COBBLESTONE_SLAB.get(), new ModelFile.UncheckedModelFile(ImmersiveWeathering.res("block/sandy_cobblestone_slab_0")));
        this.getVariantBuilder(ModBlocks.SANDY_COBBLESTONE_STAIRS.get()).forAllStatesExcept((state) -> {
            int sandiness = state.getValue(ModBlockProperties.SANDINESS);
            Direction facing = state.getValue(StairBlock.FACING);
            Half half = state.getValue(StairBlock.HALF);
            StairsShape shape = state.getValue(StairBlock.SHAPE);
            var texture = ImmersiveWeathering.res("block/sandy_cobblestone_" + sandiness);
            ModelFile stairs = this.models().stairs("sandy_cobblestone_stairs_"+sandiness, texture, texture, texture);
            ModelFile stairsInner = this.models().stairsInner("sandy_cobblestone_stairs_inner_"+sandiness, texture, texture, texture);
            ModelFile stairsOuter = this.models().stairsOuter("sandy_cobblestone_stairs_outer_"+sandiness, texture, texture, texture);
            int yRot = (int)facing.getClockWise().toYRot();
            if (shape == StairsShape.INNER_LEFT || shape == StairsShape.OUTER_LEFT) {
                yRot += 270;
            }

            if (shape != StairsShape.STRAIGHT && half == Half.TOP) {
                yRot += 90;
            }

            yRot %= 360;
            boolean uvlock = yRot != 0 || half == Half.TOP;
            return ConfiguredModel.builder().modelFile(shape == StairsShape.STRAIGHT ? stairs : (shape != StairsShape.INNER_LEFT && shape != StairsShape.INNER_RIGHT ? stairsOuter : stairsInner)).rotationX(half == Half.BOTTOM ? 0 : 180).rotationY(yRot).uvLock(uvlock).build();
        }, ModBlockProperties.SAND_AGE, StairBlock.WATERLOGGED);
        this.simpleBlockItem(ModBlocks.SANDY_COBBLESTONE_STAIRS.get(), models().stairs("sandy_cobblestone_stairs_inventory", ImmersiveWeathering.res("block/sandy_cobblestone_0"), ImmersiveWeathering.res("block/sandy_cobblestone_0"), ImmersiveWeathering.res("block/sandy_cobblestone_0")));
        this.simpleBlockItem(ModBlocks.SANDY_COBBLESTONE_STAIRS.get(), new ModelFile.UncheckedModelFile(ImmersiveWeathering.res("block/sandy_cobblestone_stairs_inventory")));

        simpleBlock(ModBlocks.FROSTY_GRASS.get(), this.models().withExistingParent("frosty_grass", ImmersiveWeathering.res("block/frosty_tinted_cross"))
                .texture("grass", "immersive_weathering:block/frosty_grass_overlay")
                .texture("frosty", "immersive_weathering:block/frosty_grass")
                .texture("particle", "immersive_weathering:item/frosty_grass_item")
        );
        simpleBlock(ModBlocks.FROSTY_FERN.get(), this.models().withExistingParent("frosty_fern", ImmersiveWeathering.res("block/frosty_tinted_cross"))
                .texture("grass", "immersive_weathering:block/frosty_fern_overlay")
                .texture("frosty", "immersive_weathering:block/frosty_fern")
                .texture("particle", "immersive_weathering:item/frosty_fern_item")
        );

        this.getVariantBuilder(ModBlocks.WEEDS.get()).forAllStatesExcept((state) -> {
            int age = state.getValue(BlockStateProperties.AGE_7);
            var model = this.models().cross("weeds_" + age, ImmersiveWeathering.res("block/weeds_" + age));
            return ConfiguredModel.builder().modelFile(model).build();
        });
        var ivy = this.models().getExistingFile(ImmersiveWeathering.res("block/ivy"));
        var ivyCeiling = this.models().getExistingFile(ImmersiveWeathering.res("block/ivy_ceiling"));
        var ivyGround = this.models().getExistingFile(ImmersiveWeathering.res("block/ivy_ground"));
        this.getMultipartBuilder(ModBlocks.IVY.get())
                .part().modelFile(ivyCeiling).uvLock(true).addModel().condition(PipeBlock.UP, true).end()
                .part().modelFile(ivyCeiling).uvLock(true).addModel()
                .condition(PipeBlock.UP, false).condition(PipeBlock.NORTH, false).condition(PipeBlock.WEST, false)
                .condition(PipeBlock.SOUTH, false).condition(PipeBlock.EAST, false).condition(PipeBlock.DOWN, false).end()
                .part().modelFile(ivy).addModel().condition(PipeBlock.NORTH, true).end()
                .part().modelFile(ivy).addModel()
                .condition(PipeBlock.UP, false).condition(PipeBlock.NORTH, false).condition(PipeBlock.WEST, false)
                .condition(PipeBlock.SOUTH, false).condition(PipeBlock.EAST, false).condition(PipeBlock.DOWN, false).end()
                .part().modelFile(ivy).rotationY(270).uvLock(true).addModel().condition(PipeBlock.WEST, true).end()
                .part().modelFile(ivy).rotationY(270).uvLock(true).addModel()
                .condition(PipeBlock.UP, false).condition(PipeBlock.NORTH, false).condition(PipeBlock.WEST, false)
                .condition(PipeBlock.SOUTH, false).condition(PipeBlock.EAST, false).condition(PipeBlock.DOWN, false).end()
                .part().modelFile(ivy).rotationY(180).uvLock(true).addModel().condition(PipeBlock.SOUTH, true).end()
                .part().modelFile(ivy).rotationY(180).uvLock(true).addModel()
                .condition(PipeBlock.UP, false).condition(PipeBlock.NORTH, false).condition(PipeBlock.WEST, false)
                .condition(PipeBlock.SOUTH, false).condition(PipeBlock.EAST, false).condition(PipeBlock.DOWN, false).end()
                .part().modelFile(ivy).rotationY(90).uvLock(true).addModel().condition(PipeBlock.EAST, true).end()
                .part().modelFile(ivy).rotationY(90).uvLock(true).addModel()
                .condition(PipeBlock.UP, false).condition(PipeBlock.NORTH, false).condition(PipeBlock.WEST, false)
                .condition(PipeBlock.SOUTH, false).condition(PipeBlock.EAST, false).condition(PipeBlock.DOWN, false).end()
                .part().modelFile(ivyGround).uvLock(true).addModel().condition(PipeBlock.DOWN, true).end()
                .part().modelFile(ivyGround).uvLock(true).addModel()
                .condition(PipeBlock.UP, false).condition(PipeBlock.NORTH, false).condition(PipeBlock.WEST, false)
                .condition(PipeBlock.SOUTH, false).condition(PipeBlock.EAST, false).condition(PipeBlock.DOWN, false).end();

        this.getVariantBuilder(ModBlocks.DUNE_GRASS.get()).forAllStatesExcept((state) -> ConfiguredModel.builder().modelFile(this.models().getExistingFile(ImmersiveWeathering.res("block/dune_grass"))).build());

        this.getVariantBuilder(ModBlocks.SANDY_DIRT.get()).forAllStatesExcept((state) -> {
            var model = cubeAll(ModBlocks.SANDY_DIRT.get());
            return ConfiguredModel.builder().modelFile(model).nextModel().modelFile(model).rotationY(90).nextModel().modelFile(model).rotationY(180).nextModel().modelFile(model).rotationY(270).build();
        });
        this.getVariantBuilder(ModBlocks.GRASSY_SANDY_DIRT.get()).forAllStatesExcept((state) -> {
            boolean snowy = state.getValue(SnowyDirtBlock.SNOWY);
            var top = ImmersiveWeathering.res("minecraft:block/grass_block_top");
            var side = ImmersiveWeathering.res("block/grassy_sandy_dirt_side");
            var bottom = ImmersiveWeathering.res("block/sandy_dirt");
            var model = this.models().withExistingParent("grassy_sandy_dirt", ImmersiveWeathering.res("block/grassy_block"))
                    .texture("particle", bottom)
                    .texture("top", top)
                    .texture("top_overlay", top)
                    .texture("bottom", bottom)
                    .texture("side", side)
                    .texture("side_overlay", ImmersiveWeathering.res("block/grassy_sandy_dirt_overlay"));
            var snowyModel = this.models().withExistingParent("grassy_sandy_dirt_snowy", ImmersiveWeathering.res("block/grassy_snowy_block"))
                    .texture("particle", bottom)
                    .texture("side", bottom)
                    .texture("bottom", bottom);
            if (snowy) {
                return ConfiguredModel.builder().modelFile(snowyModel).build();
            }
            return ConfiguredModel.builder().modelFile(model).nextModel().modelFile(model).rotationY(90).nextModel().modelFile(model).rotationY(180).nextModel().modelFile(model).rotationY(270).build();
        });
        this.getVariantBuilder(ModBlocks.SILT.get()).forAllStatesExcept((state) -> {
            var model = cubeAll(ModBlocks.SILT.get());
            return ConfiguredModel.builder().modelFile(model).nextModel().modelFile(model).rotationY(90).nextModel().modelFile(model).rotationY(180).nextModel().modelFile(model).rotationY(270).build();
        });
        this.getVariantBuilder(ModBlocks.GRASSY_SILT.get()).forAllStatesExcept((state) -> {
            boolean snowy = state.getValue(SnowyDirtBlock.SNOWY);
            var top = ImmersiveWeathering.res("minecraft:block/grass_block_top");
            var side = ImmersiveWeathering.res("block/grassy_silt_side");
            var bottom = ImmersiveWeathering.res("block/silt");
            var model = this.models().withExistingParent("grassy_silt", ImmersiveWeathering.res("block/grassy_block"))
                    .texture("particle", bottom)
                    .texture("top", top)
                    .texture("top_overlay", top)
                    .texture("bottom", bottom)
                    .texture("side", side)
                    .texture("side_overlay", ImmersiveWeathering.res("block/grassy_silt_overlay"));
            var snowyModel = this.models().withExistingParent("grassy_silt_snowy", ImmersiveWeathering.res("block/grassy_snowy_block"))
                    .texture("particle", bottom)
                    .texture("side", bottom)
                    .texture("bottom", bottom);
            if (snowy) {
                return ConfiguredModel.builder().modelFile(snowyModel).build();
            }
            return ConfiguredModel.builder().modelFile(model).nextModel().modelFile(model).rotationY(90).nextModel().modelFile(model).rotationY(180).nextModel().modelFile(model).rotationY(270).build();
        });
        simpleBlockAndItem(ModBlocks.PERMAFROST);
        this.getVariantBuilder(ModBlocks.GRASSY_PERMAFROST.get()).forAllStatesExcept((state) -> {
            boolean snowy = state.getValue(SnowyDirtBlock.SNOWY);
            var side = ImmersiveWeathering.res("block/grassy_permafrost_side");
            var bottom = ImmersiveWeathering.res("block/permafrost");
            var model = this.models().withExistingParent("grassy_permafrost", ImmersiveWeathering.res("block/grassy_block"))
                    .texture("particle", bottom)
                    .texture("bottom", bottom)
                    .texture("side", side);
            var snowyModel = this.models().withExistingParent("grassy_permafrost_snowy", ImmersiveWeathering.res("block/grassy_snowy_block"))
                    .texture("particle", bottom)
                    .texture("side", side)
                    .texture("bottom", bottom);
            if (snowy) {
                return ConfiguredModel.builder().modelFile(snowyModel).build();
            }
            return ConfiguredModel.builder().modelFile(model).nextModel().modelFile(model).rotationY(90).nextModel().modelFile(model).rotationY(180).nextModel().modelFile(model).rotationY(270).build();
        });

        this.getVariantBuilder(ModBlocks.ICICLE.get()).forAllStatesExcept((state) -> {
            var thickness = state.getValue(IcicleBlock.THICKNESS);
            var direction = state.getValue(IcicleBlock.TIP_DIRECTION);
            String fileName = "icicle_" + direction.getSerializedName() + "_" + thickness.getSerializedName();
            var model = this.models().withExistingParent(fileName, ImmersiveWeathering.res("block/icicle")).texture("cross", "block/"+fileName);
            return ConfiguredModel.builder().modelFile(model).build();
        });

        this.getVariantBuilder(ModBlocks.THIN_ICE.get()).forAllStatesExcept((state) -> {
            int cracked = state.getValue(ThinIceBlock.CRACKED);
            var model = this.models().withExistingParent("thin_ice_" + cracked, ImmersiveWeathering.res("block/thin_ice_template")).texture("texture", ImmersiveWeathering.res("block/thin_ice_" + cracked));
            return ConfiguredModel.builder().modelFile(model).build();
        });

        this.getVariantBuilder(ModBlocks.MULCH_BLOCK.get()).forAllStatesExcept((state) -> {
            boolean soaked = state.getValue(BlockStateProperties.WATERLOGGED);
            var texture = ImmersiveWeathering.res("block/mulch_block" + (soaked ? "_soaked" : ""));
            return ConfiguredModel.builder().modelFile(this.models().cubeAll("mulch_block" + (soaked ? "_soaked" : ""), texture)).build();
        });
        this.getVariantBuilder(ModBlocks.NULCH_BLOCK.get()).forAllStatesExcept((state) -> {
            boolean soaked = state.getValue(ModBlockProperties.MOLTEN);
            var texture = ImmersiveWeathering.res("block/nulch_block" + (soaked ? "_molten" : ""));
            return ConfiguredModel.builder().modelFile(this.models().cubeAll("nulch_block" + (soaked ? "_molten" : ""), texture)).build();
        });

        this.getVariantBuilder(ModBlocks.ROOTED_GRASS_BLOCK.get()).forAllStatesExcept((state) -> {
            boolean snowy = state.getValue(SnowyDirtBlock.SNOWY);
            var top = ImmersiveWeathering.res("minecraft:block/grass_block_top");
            var side = ImmersiveWeathering.res("block/rooted_grass_block_side");
            var bottom = ImmersiveWeathering.res("minecraft:block/rooted_dirt");
            var model = this.models().withExistingParent("rooted_grass_block", ImmersiveWeathering.res("block/grassy_block"))
                    .texture("particle", bottom)
                    .texture("top", top)
                    .texture("top_overlay", top)
                    .texture("bottom", bottom)
                    .texture("side", side)
                    .texture("side_overlay", ImmersiveWeathering.res("block/grass_overlay"));
            var snowyModel = this.models().cubeBottomTop("rooted_grass_block_snowy", ImmersiveWeathering.res("block/rooted_grass_block_snow"), bottom, ImmersiveWeathering.res("block/rooted_grass_block_top"));
            if (snowy) {
                return ConfiguredModel.builder().modelFile(snowyModel).build();
            }
            return ConfiguredModel.builder().modelFile(model).nextModel().modelFile(model).rotationY(90).nextModel().modelFile(model).rotationY(180).nextModel().modelFile(model).rotationY(270).build();
        });

        farmland(ModBlocks.EARTHEN_CLAY_FARMLAND, (state, name) -> {var builder = ConfiguredModel.builder();
            builder.modelFile(this.models().withExistingParent(name, ImmersiveWeathering.res("minecraft:block/template_farmland"))
                    .element().from(0, 0, 0).to(16, 15, 16)
                    .face(Direction.DOWN).uvs(0, 0, 16, 16).texture("#bottom").cullface(Direction.DOWN).end()
                    .face(Direction.UP).uvs(0, 0, 16, 16).texture("#top").end()
                    .face(Direction.NORTH).uvs(0, 1, 16, 16).texture("#dirt").cullface(Direction.NORTH).end()
                    .face(Direction.SOUTH).uvs(0, 1, 16, 16).texture("#dirt").cullface(Direction.SOUTH).end()
                    .face(Direction.WEST).uvs(0, 1, 16, 16).texture("#dirt").cullface(Direction.WEST).end()
                    .face(Direction.EAST).uvs(0, 1, 16, 16).texture("#dirt").cullface(Direction.EAST).end().end()
                    .texture("dirt", ImmersiveWeathering.res("block/earthen_clay_side"))
                    .texture("top", ImmersiveWeathering.res("block/earthen_clay_farmland"))
                    .texture("bottom", ImmersiveWeathering.res("block/earthen_clay_bottom"))
            );
            return builder;}, (state, name) -> {var builder = ConfiguredModel.builder();
            builder.modelFile(this.models().withExistingParent(name + "_moist", ImmersiveWeathering.res("minecraft:block/template_farmland"))
                    .element().from(0, 0, 0).to(16, 15, 16)
                    .face(Direction.DOWN).uvs(0, 0, 16, 16).texture("#bottom").cullface(Direction.DOWN).end()
                    .face(Direction.UP).uvs(0, 0, 16, 16).texture("#top").end()
                    .face(Direction.NORTH).uvs(0, 1, 16, 16).texture("#dirt").cullface(Direction.NORTH).end()
                    .face(Direction.SOUTH).uvs(0, 1, 16, 16).texture("#dirt").cullface(Direction.SOUTH).end()
                    .face(Direction.WEST).uvs(0, 1, 16, 16).texture("#dirt").cullface(Direction.WEST).end()
                    .face(Direction.EAST).uvs(0, 1, 16, 16).texture("#dirt").cullface(Direction.EAST).end().end()
                    .texture("dirt", ImmersiveWeathering.res("block/earthen_clay_side_soaked"))
                    .texture("top", ImmersiveWeathering.res("block/earthen_clay_farmland_moist"))
                    .texture("bottom", ImmersiveWeathering.res("block/earthen_clay_bottom_soaked")));
            return builder;});
        farmland(ModBlocks.LOAMY_FARMLAND, (state, name) -> {var builder = ConfiguredModel.builder();
            builder.modelFile(this.models().withExistingParent(name, ImmersiveWeathering.res("block/earthen_clay_farmland"))
                    .texture("dirt", ImmersiveWeathering.res("block/loam_side"))
                    .texture("top", ImmersiveWeathering.res("block/loamy_farmland"))
                    .texture("bottom", ImmersiveWeathering.res("minecraft:block/dirt"))
            );
            return builder;}, (state, name) -> {var builder = ConfiguredModel.builder();
            builder.modelFile(this.models().withExistingParent(name + "_moist", ImmersiveWeathering.res("block/earthen_clay_farmland"))
                    .texture("dirt", ImmersiveWeathering.res("block/loam_side"))
                    .texture("top", ImmersiveWeathering.res("block/loamy_farmland_moist"))
                    .texture("bottom", ImmersiveWeathering.res("minecraft:block/dirt")));
            return builder;});
        farmland(ModBlocks.SANDY_FARMLAND, (state, name) -> {var builder = ConfiguredModel.builder();
            builder.modelFile(this.models().withExistingParent(name, ImmersiveWeathering.res("minecraft:block/template_farmland"))
                    .texture("dirt", ImmersiveWeathering.res("block/sandy_dirt"))
                    .texture("top", ImmersiveWeathering.res("block/sandy_farmland"))
            );
            return builder;}, (state, name) -> {var builder = ConfiguredModel.builder();
            builder.modelFile(this.models().withExistingParent(name + "_moist", ImmersiveWeathering.res("minecraft:block/template_farmland"))
                    .texture("dirt", ImmersiveWeathering.res("block/sandy_dirt"))
                    .texture("top", ImmersiveWeathering.res("block/sandy_farmland_moist"))
            );
            return builder;});
        farmland(ModBlocks.SILTY_FARMLAND, (state, name) -> {var builder = ConfiguredModel.builder();
            builder.modelFile(this.models().withExistingParent(name, ImmersiveWeathering.res("minecraft:block/template_farmland"))
                    .texture("dirt", ImmersiveWeathering.res("block/silt"))
                    .texture("top", ImmersiveWeathering.res("block/silty_farmland"))
            );
            return builder;}, (state, name) -> {var builder = ConfiguredModel.builder();
            builder.modelFile(this.models().withExistingParent(name + "_moist", ImmersiveWeathering.res("minecraft:block/template_farmland"))
                    .texture("dirt", ImmersiveWeathering.res("block/silt"))
                    .texture("top", ImmersiveWeathering.res("block/silty_farmland_moist"))
            );
            return builder;});

        directionalBlock(ModBlocks.FULGURITE.get(), this.models().withExistingParent("fulgurite", ImmersiveWeathering.res("minecraft:block/cross")).texture("cross", "immersive_weathering:block/fulgurite"));

        cubeSideBottomTop(ModBlocks.LOAM, ImmersiveWeathering.res("block/loam_top"), ImmersiveWeathering.res("block/loam_side"), ImmersiveWeathering.res("minecraft:block/dirt"));
        simpleBlockAndItem(ModBlocks.CRACKED_BRICKS);
        simpleSlab(ModBlocks.CRACKED_BRICK_SLAB, ModBlocks.CRACKED_BRICKS);
        simpleStairs(ModBlocks.CRACKED_BRICK_STAIRS, ModBlocks.CRACKED_BRICKS);
        simpleBlockAndItem(ModBlocks.CRACKED_TUFF_BRICKS);
        simpleSlab(ModBlocks.CRACKED_TUFF_BRICK_SLAB, ModBlocks.CRACKED_TUFF_BRICKS);
        simpleStairs(ModBlocks.CRACKED_TUFF_BRICK_STAIRS, ModBlocks.CRACKED_TUFF_BRICKS);
        simpleBlockAndItem(ModBlocks.CRACKED_PRISMARINE_BRICKS);
        simpleSlab(ModBlocks.CRACKED_PRISMARINE_BRICK_SLAB, ModBlocks.CRACKED_PRISMARINE_BRICKS);
        simpleStairs(ModBlocks.CRACKED_PRISMARINE_BRICK_STAIRS, ModBlocks.CRACKED_PRISMARINE_BRICKS);
        simpleBlockAndItem(ModBlocks.CRACKED_END_STONE_BRICKS);
        simpleSlab(ModBlocks.CRACKED_END_STONE_BRICK_SLAB, ModBlocks.CRACKED_END_STONE_BRICKS);
        simpleStairs(ModBlocks.CRACKED_END_STONE_BRICK_STAIRS, ModBlocks.CRACKED_END_STONE_BRICKS);
        simpleBlockAndItem(ModBlocks.MOSSY_BRICKS);
        simpleSlab(ModBlocks.MOSSY_BRICK_SLAB, ModBlocks.MOSSY_BRICKS);
        simpleStairs(ModBlocks.MOSSY_BRICK_STAIRS, ModBlocks.MOSSY_BRICKS);

        simpleBlockAndItem(ModBlocks.CHISELED_PRISMARINE_BRICKS);
        simpleBlockAndItem(ModBlocks.VITRIFIED_SAND);
        simpleBlockAndItem(ModBlocks.FROSTY_GLASS);
        paneBlock(ModBlocks.FROSTY_GLASS_PANE.get(), ImmersiveWeathering.res("block/frosty_glass"), ImmersiveWeathering.res("minecraft:block/glass_pane_top"));
        paneBlock(ModBlocks.TINTED_GLASS_PANE.get(), ImmersiveWeathering.res("minecraft:block/tinted_glass"), ImmersiveWeathering.res("block/tinted_glass_pane_top"));

        simpleSlab(ModBlocks.CRACKED_STONE_BRICK_SLAB, () -> Blocks.CRACKED_STONE_BRICKS);
        simpleStairs(ModBlocks.CRACKED_STONE_BRICK_STAIRS, () -> Blocks.CRACKED_STONE_BRICKS);
        simpleSlab(ModBlocks.CRACKED_DEEPSLATE_BRICK_SLAB, () -> Blocks.CRACKED_DEEPSLATE_BRICKS);
        simpleStairs(ModBlocks.CRACKED_DEEPSLATE_BRICK_STAIRS, () -> Blocks.CRACKED_DEEPSLATE_BRICKS);
        simpleSlab(ModBlocks.CRACKED_DEEPSLATE_TILE_SLAB, () -> Blocks.CRACKED_DEEPSLATE_TILES);
        simpleStairs(ModBlocks.CRACKED_DEEPSLATE_TILE_STAIRS, () -> Blocks.CRACKED_DEEPSLATE_TILES);
        simpleSlab(ModBlocks.CRACKED_NETHER_BRICK_SLAB, () -> Blocks.CRACKED_NETHER_BRICKS);
        simpleStairs(ModBlocks.CRACKED_NETHER_BRICK_STAIRS, () -> Blocks.CRACKED_NETHER_BRICKS);
        simpleSlab(ModBlocks.CRACKED_POLISHED_BLACKSTONE_BRICK_SLAB, () -> Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS);
        simpleStairs(ModBlocks.CRACKED_POLISHED_BLACKSTONE_BRICK_STAIRS, () -> Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS);

        horizontalBlock(ModBlocks.HANGING_ROOTS_WALL.get(), this.models().getExistingFile(ImmersiveWeathering.res("block/hanging_roots_side")));
    }

    private void simpleBlockItem(Supplier<? extends Block> block) {
        super.simpleBlockItem(block.get(), new ModelFile.UncheckedModelFile(ImmersiveWeathering.res("block/" + this.name(block.get()))));
    }
    private void simpleBlockWithItem(Supplier<? extends Block> block) {
        super.simpleBlockWithItem(block.get(), new ModelFile.UncheckedModelFile(ImmersiveWeathering.res("block/" + this.name(block.get()))));
    }
    private void simpleBlockAndItem(Supplier<? extends Block> block) {
        this.simpleBlock(block.get());
        this.simpleBlockItem(block.get(), new ModelFile.UncheckedModelFile(ImmersiveWeathering.res("block/" + this.name(block.get()))));
    }
    private void cubeSideBottomTop(Supplier<? extends Block> block, ResourceLocation top, ResourceLocation side, ResourceLocation bottom) {
        String name = this.name(block.get());
        this.simpleBlock(block.get(), this.models().cubeBottomTop(name, side, bottom, top));
        this.simpleBlockItem(block.get(), new ModelFile.UncheckedModelFile(ImmersiveWeathering.res("block/" + this.name(block.get()))));
    }
    private void cubeBottomTop(Supplier<? extends Block> block, ResourceLocation top, ResourceLocation bottom) {
        String name = this.name(block.get());
        ResourceLocation side = ImmersiveWeathering.res("block/" + name);
        this.simpleBlock(block.get(), this.models().cubeBottomTop(name, side, top, bottom));
        this.simpleBlockItem(block.get(), new ModelFile.UncheckedModelFile(ImmersiveWeathering.res("block/" + this.name(block.get()))));
    }
    private void blockBottomTop(Supplier<? extends Block> block) {
        String name = this.name(block.get());
        ResourceLocation side = ImmersiveWeathering.res("block/" + name);
        ResourceLocation top = ImmersiveWeathering.res("block/" + name + "_top");
        ResourceLocation bottom = ImmersiveWeathering.res("block/" + name + "_bottom");
        this.simpleBlock(block.get(), this.models().cubeBottomTop(name, side, bottom, top));
        this.simpleBlockItem(block.get(), new ModelFile.UncheckedModelFile(ImmersiveWeathering.res("block/" + this.name(block.get()))));
    }
    private void sidedSlab(Supplier<? extends Block> block, ResourceLocation side, ResourceLocation top, ResourceLocation bottom) {
        String name = this.name(block.get());
        ResourceLocation doubleSlab = ImmersiveWeathering.res("block/" + name.replace("_slab", ""));
        this.slabBlock((SlabBlock) block.get(), doubleSlab, side, bottom, top);
        this.simpleBlockItem(block.get(), models().slab(name + "_inventory", side, bottom, top));
        this.simpleBlockItem(block.get(), new ModelFile.UncheckedModelFile(ImmersiveWeathering.res("block/" + this.name(block.get()) + "_inventory")));
    }
    private void sidedStairs(Supplier<? extends Block> block, ResourceLocation side, ResourceLocation top, ResourceLocation bottom) {
        String name = this.name(block.get());
        this.stairsBlock((StairBlock) block.get(), name, side, bottom, top);
        this.simpleBlockItem(block.get(), models().stairs(name + "_inventory", side, bottom, top));
        this.simpleBlockItem(block.get(), new ModelFile.UncheckedModelFile(ImmersiveWeathering.res("block/" + this.name(block.get()) + "_inventory")));
    }

    private void simpleSlab(Supplier<? extends Block> block, Supplier<? extends Block> texture) {
        String name = this.name(block.get());
        ResourceLocation main = blockTexture(texture.get());
        ResourceLocation doubleSlab = ImmersiveWeathering.res(main.getNamespace() + ":block/" + this.name(texture.get()));
        this.slabBlock((SlabBlock) block.get(), doubleSlab, main, main, main);
        this.simpleBlockItem(block.get(), models().slab(name + "_inventory", main, main, main));
        this.simpleBlockItem(block.get(), new ModelFile.UncheckedModelFile(ImmersiveWeathering.res("block/" + this.name(block.get()) + "_inventory")));
    }
    private void simpleStairs(Supplier<? extends Block> block, Supplier<? extends Block> texture) {
        String name = this.name(block.get());
        ResourceLocation main = blockTexture(texture.get());
        this.stairsBlock((StairBlock) block.get(), name, main, main, main);
        this.simpleBlockItem(block.get(), models().stairs(name + "_inventory", main, main, main));
        this.simpleBlockItem(block.get(), new ModelFile.UncheckedModelFile(ImmersiveWeathering.res("block/" + this.name(block.get()) + "_inventory")));
    }
    private void simpleWall(Supplier<? extends Block> block, Supplier<? extends Block> texture) {
        String name = this.name(block.get());
        this.wallBlock((WallBlock) block.get(), name, blockTexture(texture.get()));
        this.simpleBlockItem(block.get(), models().wallInventory(name + "_inventory", blockTexture(texture.get())));
        this.simpleBlockItem(block.get(), new ModelFile.UncheckedModelFile(ImmersiveWeathering.res("block/" + this.name(block.get()) + "_inventory")));
    }
    private void simpleFence(Supplier<? extends Block> block, Supplier<? extends Block> texture) {
        String name = this.name(block.get());
        this.fenceBlock((FenceBlock) block.get(), name, blockTexture(texture.get()));
        this.simpleBlockItem(block.get(), models().fenceInventory(name + "_inventory", blockTexture(texture.get())));
        this.simpleBlockItem(block.get(), new ModelFile.UncheckedModelFile(ImmersiveWeathering.res("block/" + this.name(block.get()) + "_inventory")));
    }
    private void simpleFenceGate(Supplier<? extends Block> block, Supplier<? extends Block> texture) {
        String name = this.name(block.get());
        this.fenceGateBlock((FenceGateBlock) block.get(), name, blockTexture(texture.get()));
        this.simpleBlockItem(block.get(), models().fenceGate(name + "_inventory", blockTexture(texture.get())));
        this.simpleBlockItem(block.get(), new ModelFile.UncheckedModelFile(ImmersiveWeathering.res("block/" + this.name(block.get()) + "_inventory")));
    }

    private void simpleLogBlock(Supplier<? extends Block> block) {
        this.logBlock((RotatedPillarBlock) block.get());
        this.simpleBlockItem(block);
    }
    private void simpleWoodBlock(Supplier<? extends Block> block) {
        this.axisBlock((RotatedPillarBlock) block.get(), blockTexture(block.get()), extend(blockTexture(block.get()), ""));
        this.simpleBlockItem(block);
    }
    private void simpleDoorBlock(Supplier<? extends Block> block) {
        String name = this.name(block.get());
        ResourceLocation bottomLocation = ImmersiveWeathering.res("block/" + name + "_bottom");
        ResourceLocation topLocation = ImmersiveWeathering.res("block/" + name + "_top");
        this.doorBlockWithRenderType((DoorBlock) block.get(), bottomLocation, topLocation, "cutout");
    }
    private void simpleTrapDoorBlock(Supplier<? extends Block> block) {
        ResourceLocation location = ImmersiveWeathering.res("block/" + this.name(block.get()));
        this.trapdoorBlockWithRenderType((TrapDoorBlock) block.get(), location, true, "cutout");
        this.simpleBlockItem(block.get(), models().trapdoorBottom(this.name(block.get()), location));
    }

    private void createMultiface(Block block) {
        ResourceLocation resourcelocation = ModelLocationUtils.getModelLocation(block);
        var model = this.models().getExistingFile(resourcelocation);
        this.getMultipartBuilder(block)
                .part().modelFile(model).uvLock(true).rotationX(270).addModel().condition(PipeBlock.UP, true).end()
                .part().modelFile(model).uvLock(true).rotationX(270).addModel()
                .condition(PipeBlock.UP, false).condition(PipeBlock.NORTH, false).condition(PipeBlock.WEST, false)
                .condition(PipeBlock.SOUTH, false).condition(PipeBlock.EAST, false).condition(PipeBlock.DOWN, false).end()
                .part().modelFile(model).addModel().condition(PipeBlock.NORTH, true).end()
                .part().modelFile(model).addModel()
                .condition(PipeBlock.UP, false).condition(PipeBlock.NORTH, false).condition(PipeBlock.WEST, false)
                .condition(PipeBlock.SOUTH, false).condition(PipeBlock.EAST, false).condition(PipeBlock.DOWN, false).end()
                .part().modelFile(model).rotationY(270).uvLock(true).addModel().condition(PipeBlock.WEST, true).end()
                .part().modelFile(model).rotationY(270).uvLock(true).addModel()
                .condition(PipeBlock.UP, false).condition(PipeBlock.NORTH, false).condition(PipeBlock.WEST, false)
                .condition(PipeBlock.SOUTH, false).condition(PipeBlock.EAST, false).condition(PipeBlock.DOWN, false).end()
                .part().modelFile(model).rotationY(180).uvLock(true).addModel().condition(PipeBlock.SOUTH, true).end()
                .part().modelFile(model).rotationY(180).uvLock(true).addModel()
                .condition(PipeBlock.UP, false).condition(PipeBlock.NORTH, false).condition(PipeBlock.WEST, false)
                .condition(PipeBlock.SOUTH, false).condition(PipeBlock.EAST, false).condition(PipeBlock.DOWN, false).end()
                .part().modelFile(model).rotationY(90).uvLock(true).addModel().condition(PipeBlock.EAST, true).end()
                .part().modelFile(model).rotationY(90).uvLock(true).addModel()
                .condition(PipeBlock.UP, false).condition(PipeBlock.NORTH, false).condition(PipeBlock.WEST, false)
                .condition(PipeBlock.SOUTH, false).condition(PipeBlock.EAST, false).condition(PipeBlock.DOWN, false).end()
                .part().modelFile(model).uvLock(true).rotationX(90).addModel().condition(PipeBlock.DOWN, true).end()
                .part().modelFile(model).uvLock(true).rotationX(90).addModel()
                .condition(PipeBlock.UP, false).condition(PipeBlock.NORTH, false).condition(PipeBlock.WEST, false)
                .condition(PipeBlock.SOUTH, false).condition(PipeBlock.EAST, false).condition(PipeBlock.DOWN, false).end();
    }

    private void charredLog(Block block) {
        String name = this.name(block);
        BiFunction<Direction.Axis, Boolean, ModelFile> model = (axis, smouldering) -> {
            ResourceLocation parent = ResourceLocation.withDefaultNamespace("block/cube_column" + (axis != Direction.Axis.Y ? "_horizontal" : ""));
            String modelSuffix = (axis.isHorizontal() ? "_horizontal" : "") + (smouldering ? "_lit" : "");
            String endTex = "block/charred_log_top" + (smouldering ? "_lit" : "");
            String sideTex = "block/charred_log" + (smouldering ? "_lit" : "");
            return this.models().withExistingParent(name + modelSuffix, parent).texture("end", endTex).texture("side", sideTex);
        };
        this.getVariantBuilder(block)
                .partialState().with(CharredPillarBlock.AXIS, Direction.Axis.Y).with(CharredBlock.SMOLDERING, false)
                .modelForState().modelFile(model.apply(Direction.Axis.Y, false)).addModel()
                .partialState().with(CharredPillarBlock.AXIS, Direction.Axis.Y).with(CharredBlock.SMOLDERING, true)
                .modelForState().modelFile(model.apply(Direction.Axis.Y, true)).addModel()
                .partialState().with(CharredPillarBlock.AXIS, Direction.Axis.X).with(CharredBlock.SMOLDERING, false)
                .modelForState().modelFile(model.apply(Direction.Axis.X, false)).rotationX(90).rotationY(90).addModel()
                .partialState().with(CharredPillarBlock.AXIS, Direction.Axis.X).with(CharredBlock.SMOLDERING, true)
                .modelForState().modelFile(model.apply(Direction.Axis.X, true)).rotationX(90).rotationY(90).addModel()
                .partialState().with(CharredPillarBlock.AXIS, Direction.Axis.Z).with(CharredBlock.SMOLDERING, false)
                .modelForState().modelFile(model.apply(Direction.Axis.Z, false)).rotationX(90).addModel()
                .partialState().with(CharredPillarBlock.AXIS, Direction.Axis.Z).with(CharredBlock.SMOLDERING, true)
                .modelForState().modelFile(model.apply(Direction.Axis.Z, true)).rotationX(90).addModel();
        this.simpleBlockItem(() -> block);
    }

    private void charredPlanks(Block block) {
        String name = this.name(block);
        this.getVariantBuilder(block).forAllStatesExcept((state) -> {
            boolean smouldering = state.getValue(CharredBlock.SMOLDERING);
            ResourceLocation texture = ImmersiveWeathering.res("block/" + name + (smouldering ? "_lit" : ""));
            return ConfiguredModel.builder().modelFile(this.models().cubeAll(name + (smouldering ? "_lit" : ""), texture)).build();
        }, CharredBlock.OVERHANG);
        this.simpleBlockItem(block, new ModelFile.UncheckedModelFile(ImmersiveWeathering.res("block/" + name)));
    }

    private void charredSlab(Block block, Supplier<? extends Block> texture) {
        String name = this.name(block);
        String textureName = this.name(texture.get());
        this.getVariantBuilder(block).forAllStatesExcept((state) -> {
            boolean smouldering = state.getValue(CharredBlock.SMOLDERING);
            SlabType type = state.getValue(SlabBlock.TYPE);
            ResourceLocation tex = ImmersiveWeathering.res("block/" + textureName + (smouldering ? "_lit" : ""));
            var bottom = this.models().slab(name + (smouldering ? "_lit" : ""), tex, tex, tex);
            var top = this.models().slabTop(name + (smouldering ? "_top_lit" : "_top"), tex, tex, tex);
            var doubleSlab = ImmersiveWeathering.res("block/" + textureName + (smouldering ? "_lit" : ""));
            var model = switch (type) {
                case TOP -> top;
                case BOTTOM -> bottom;
                case DOUBLE -> this.models().getExistingFile(doubleSlab);
            };
            return ConfiguredModel.builder().modelFile(model).build();
        }, SlabBlock.WATERLOGGED, CharredBlock.OVERHANG);
        ResourceLocation item = ImmersiveWeathering.res("block/" + textureName);
        this.simpleBlockItem(block, models().slab(name + "_inventory", item, item, item));
        this.simpleBlockItem(block, new ModelFile.UncheckedModelFile(ImmersiveWeathering.res("block/" + this.name(block) + "_inventory")));
    }

    private void charredStairs(Block block, Supplier<? extends Block> texture) {
        String name = this.name(block);
        String textureName = this.name(texture.get());
        this.getVariantBuilder(block).forAllStatesExcept((state) -> {
            boolean smouldering = state.getValue(CharredBlock.SMOLDERING);
            ResourceLocation tex = ImmersiveWeathering.res("block/" + textureName + (smouldering ? "_lit" : ""));
            ModelFile stairs = this.models().stairs(name + (smouldering ? "_lit" : ""), tex, tex, tex);
            ModelFile stairsInner = this.models().stairsInner(name + "_inner" + (smouldering ? "_lit" : ""), tex, tex, tex);
            ModelFile stairsOuter = this.models().stairsOuter(name + "_outer" + (smouldering ? "_lit" : ""), tex, tex, tex);
            Direction facing = state.getValue(StairBlock.FACING);
            Half half = state.getValue(StairBlock.HALF);
            StairsShape shape = state.getValue(StairBlock.SHAPE);
            int yRot = (int)facing.getClockWise().toYRot();
            if (shape == StairsShape.INNER_LEFT || shape == StairsShape.OUTER_LEFT) {
                yRot += 270;
            }

            if (shape != StairsShape.STRAIGHT && half == Half.TOP) {
                yRot += 90;
            }

            yRot %= 360;
            boolean uvlock = yRot != 0 || half == Half.TOP;
            return ConfiguredModel.builder().modelFile(shape == StairsShape.STRAIGHT ? stairs : (shape != StairsShape.INNER_LEFT && shape != StairsShape.INNER_RIGHT ? stairsOuter : stairsInner)).rotationX(half == Half.BOTTOM ? 0 : 180).rotationY(yRot).uvLock(uvlock).build();
        }, StairBlock.WATERLOGGED, CharredBlock.OVERHANG);
        ResourceLocation item = ImmersiveWeathering.res("block/" + textureName);
        this.simpleBlockItem(block, models().stairs(name + "_inventory", item, item, item));
        this.simpleBlockItem(block, new ModelFile.UncheckedModelFile(ImmersiveWeathering.res("block/" + this.name(block) + "_inventory")));
    }

    private void leafPile(LeafPileBlock block) {
        String name = this.name(block);
        Function<Integer, String> textureFunc = (layer) -> {
            String density = layer >= 6 ? "heavy" : layer >= 2 ? "medium" : "light";
            return "immersive_weathering:block/" + density + "_" + (name.contains("leaf") ? name.replace("leaf_pile", "leaves") : name.replace("_pile", "s"));
        };
        this.getVariantBuilder(block).forAllStatesExcept((state) -> {
            int layers = state.getValue(LeafPileBlock.LAYERS);
            String texture = textureFunc.apply(layers);
            ModelFile model = this.models().withExistingParent(name + "_" + layers, ImmersiveWeathering.res("block/leaf_pile/layers_" + layers))
                    .texture("texture", texture);
            return ConfiguredModel.builder().modelFile(model).build();
        }, LeafPileBlock.AGE);
        this.simpleBlockItem(block, this.models().withExistingParent(name + "_item", ImmersiveWeathering.res("block/" + name + "_1")));
    }

    private void layered(Supplier<? extends Block> block, String texture) {
        String name = this.name(block.get());
        this.getVariantBuilder(block.get()).forAllStatesExcept((state) -> {
            int layers = state.getValue(LayerBlock.LAYERS_8);
            ModelFile model = this.models().withExistingParent(name + "_" + layers, ImmersiveWeathering.res("block/layered_" + (layers * 2)))
                    .texture("texture", texture);
            return ConfiguredModel.builder().modelFile(model).build();
        });
    }

    private void mossyWall(Supplier<? extends Block> block, String top, String side) {
        String name = this.name(block.get());
        this.mossyWallBlock((WallBlock) block.get(), name, ImmersiveWeathering.res("block/" + top), ImmersiveWeathering.res("block/" + side));
        var inventory = this.models().withExistingParent(name + "_inventory", ImmersiveWeathering.res("block/mossy_template_wall_inventory"))
                .texture("top", ImmersiveWeathering.res("block/" + top))
                .texture("side", ImmersiveWeathering.res("block/" + side))
                .texture("bottom", ImmersiveWeathering.res("block/" + side));
        this.simpleBlockItem(block.get(), inventory);
        this.simpleBlockItem(block.get(), new ModelFile.UncheckedModelFile(ImmersiveWeathering.res("block/" + this.name(block.get()) + "_inventory")));
    }

    private void mossyWall(Supplier<? extends Block> block, Supplier<? extends Block> texture) {
        String name = this.name(block.get());
        this.mossyWallBlock((WallBlock) block.get(), name, blockTexture(texture.get()));
        var inventory = this.models().withExistingParent(name + "_inventory", ImmersiveWeathering.res("block/mossy_template_wall_inventory")).texture("top", extend(blockTexture(texture.get()), "_top")).texture("side", extend(blockTexture(texture.get()), "_side")).texture("bottom", extend(blockTexture(texture.get()), "_side"));
        this.simpleBlockItem(block.get(), inventory);
        this.simpleBlockItem(block.get(), new ModelFile.UncheckedModelFile(ImmersiveWeathering.res("block/" + this.name(block.get()) + "_inventory")));
    }

    private void snowyWall(Supplier<? extends Block> block, String texture) {
        String name = this.name(block.get());
        this.snowyWallBlock((WallBlock) block.get(), name, ImmersiveWeathering.res("block/" + texture), true);
        var inventory = this.models().withExistingParent(name + "_inventory", ImmersiveWeathering.res("block/mossy_template_wall_inventory")).texture("top", "minecraft:block/snow").texture("side", ImmersiveWeathering.res("block/" + texture)).texture("bottom", ImmersiveWeathering.res("block/" + texture));
        this.simpleBlockItem(block.get(), inventory);
        this.simpleBlockItem(block.get(), new ModelFile.UncheckedModelFile(ImmersiveWeathering.res("block/" + this.name(block.get()) + "_inventory")));
    }

    private void snowyWall(Supplier<? extends Block> block, Supplier<? extends Block> texture) {
        String name = this.name(block.get());
        this.snowyWallBlock((WallBlock) block.get(), name, blockTexture(texture.get()), false);
        var inventory = this.models().withExistingParent(name + "_inventory", ImmersiveWeathering.res("block/mossy_template_wall_inventory")).texture("top", "minecraft:block/snow").texture("side", extend(blockTexture(texture.get()), "_side")).texture("bottom", extend(blockTexture(texture.get()), "_side"));
        this.simpleBlockItem(block.get(), inventory);
        this.simpleBlockItem(block.get(), new ModelFile.UncheckedModelFile(ImmersiveWeathering.res("block/" + this.name(block.get()) + "_inventory")));
    }

    private void sandyWall(Supplier<? extends Block> block, Supplier<? extends Block> texture) {
        String name = this.name(block.get());
        this.sandyWallBlock((SandyWallBlock) block.get(), name, blockTexture(texture.get()));
        var inventory = this.models().withExistingParent(name + "_inventory", ImmersiveWeathering.res("block/mossy_template_wall_inventory")).texture("top", extend(blockTexture(texture.get()), "_0")).texture("side", extend(blockTexture(texture.get()), "_0")).texture("bottom", extend(blockTexture(texture.get()), "_0"));
        this.simpleBlockItem(block.get(), inventory);
        this.simpleBlockItem(block.get(), new ModelFile.UncheckedModelFile(ImmersiveWeathering.res("block/" + this.name(block.get()) + "_inventory")));
    }

    private void farmland(DeferredBlock<? extends Block> block, BiFunction<BlockState, String, ConfiguredModel.Builder<?>> func, BiFunction<BlockState, String, ConfiguredModel.Builder<?>> moistFunc) {
        String name = this.name(block.get());
        this.getVariantBuilder(block.get()).forAllStatesExcept((state) -> {
            int moisture = state.getValue(ModFarmlandBlock.MOISTURE);
            if (moisture == 7) {
                return moistFunc.apply(state, name).build();
            } else {
                return func.apply(state, name).build();
            }
        });
        this.simpleBlockItem(block.get(), this.models().getExistingFile(ImmersiveWeathering.res("block/" + this.name(block.get()))));
    }

    public void mossyWallBlock(WallBlock block, ResourceLocation texture) {
        this.mossyWallBlockInternal(block, this.key(block).toString(), texture);
    }

    public void snowyWallBlock(WallBlock block, ResourceLocation texture) {
        this.snowyWallBlockInternal(block, this.key(block).toString(), texture, false);
    }

    public void sandyWallBlock(SandyWallBlock block, ResourceLocation texture) {
        this.sandyWallBlockInternal(block, this.key(block).toString(), texture);
    }

    public void mossyWallBlock(WallBlock block, String name, ResourceLocation texture) {
        this.mossyWallBlockInternal(block, name + "_wall", texture);
    }

    public void mossyWallBlock(WallBlock block, String name, ResourceLocation top, ResourceLocation side) {
        this.mossyWallBlockInternal(block, name + "_wall", top, side);
    }

    public void snowyWallBlock(WallBlock block, String name, ResourceLocation texture, boolean strict) {
        this.snowyWallBlockInternal(block, name + "_wall", texture, strict);
    }

    public void sandyWallBlock(SandyWallBlock block, String name, ResourceLocation texture) {
        this.sandyWallBlockInternal(block, name + "_wall", texture);
    }

    public void mossyWallBlockWithRenderType(WallBlock block, ResourceLocation texture, String renderType) {
        this.mossyWallBlockInternalWithRenderType(block, this.key(block).toString(), texture, ResourceLocation.tryParse(renderType));
    }

    public void snowyWallBlockWithRenderType(WallBlock block, ResourceLocation texture, String renderType) {
        this.snowyWallBlockInternalWithRenderType(block, this.key(block).toString(), texture, ResourceLocation.tryParse(renderType));
    }

    public void mossyWallBlockWithRenderType(WallBlock block, String name, ResourceLocation texture, String renderType) {
        this.mossyWallBlockInternalWithRenderType(block, name + "_wall", texture, ResourceLocation.tryParse(renderType));
    }

    public void snowyWallBlockWithRenderType(WallBlock block, String name, ResourceLocation texture, String renderType) {
        this.snowyWallBlockInternalWithRenderType(block, name + "_wall", texture, ResourceLocation.tryParse(renderType));
    }

    public void mossyWallBlockWithRenderType(WallBlock block, ResourceLocation texture, ResourceLocation renderType) {
        this.mossyWallBlockInternalWithRenderType(block, this.key(block).toString(), texture, renderType);
    }

    public void snowyWallBlockWithRenderType(WallBlock block, ResourceLocation texture, ResourceLocation renderType) {
        this.snowyWallBlockInternalWithRenderType(block, this.key(block).toString(), texture, renderType);
    }

    public void mossyWallBlockWithRenderType(WallBlock block, String name, ResourceLocation texture, ResourceLocation renderType) {
        this.mossyWallBlockInternalWithRenderType(block, name + "_wall", texture, renderType);
    }

    public void snowyWallBlockWithRenderType(WallBlock block, String name, ResourceLocation texture, ResourceLocation renderType) {
        this.snowyWallBlockInternalWithRenderType(block, name + "_wall", texture, renderType);
    }

    private void mossyWallBlockInternal(WallBlock block, String baseName, ResourceLocation texture) {
        var post = this.models().withExistingParent(baseName + "_post", ImmersiveWeathering.res("block/mossy_template_wall_post")).texture("top", extend(texture, "_top")).texture("side", extend(texture, "_side")).texture("bottom", extend(texture, "_side"));
        var side = this.models().withExistingParent(baseName + "_side", ImmersiveWeathering.res("block/mossy_template_wall_side")).texture("top", extend(texture, "_top")).texture("side", extend(texture, "_side")).texture("bottom", extend(texture, "_side"));
        var sideTall = this.models().withExistingParent(baseName + "_side_tall", ImmersiveWeathering.res("block/mossy_template_wall_side_tall")).texture("top", extend(texture, "_top")).texture("side", extend(texture, "_side")).texture("bottom", extend(texture, "_side"));
        this.wallBlock(block, post, side, sideTall);
    }

    private void mossyWallBlockInternal(WallBlock block, String baseName, ResourceLocation top, ResourceLocation sideTex) {
        var post = this.models().withExistingParent(baseName + "_post", ImmersiveWeathering.res("block/mossy_template_wall_post")).texture("top", top).texture("side", sideTex).texture("bottom", sideTex);
        var side = this.models().withExistingParent(baseName + "_side", ImmersiveWeathering.res("block/mossy_template_wall_side")).texture("top", top).texture("side", sideTex).texture("bottom", sideTex);
        var sideTall = this.models().withExistingParent(baseName + "_side_tall", ImmersiveWeathering.res("block/mossy_template_wall_side_tall")).texture("top", top).texture("side", sideTex).texture("bottom", sideTex);
        this.wallBlock(block, post, side, sideTall);
    }

    private void snowyWallBlockInternal(WallBlock block, String baseName, ResourceLocation texture, boolean strict) {
        var post = this.models().withExistingParent(baseName + "_post", ImmersiveWeathering.res("block/mossy_template_wall_post")).texture("top", "minecraft:block/snow").texture("side", texture).texture("bottom", texture);
        var side = this.models().withExistingParent(baseName + "_side", ImmersiveWeathering.res("block/mossy_template_wall_side")).texture("top", "minecraft:block/snow").texture("side", texture).texture("bottom", texture);
        ResourceLocation sideTex = (texture.getPath().endsWith("_side") || strict) ? texture : extend(texture, "_side");
        var sideTall = this.models().withExistingParent(baseName + "_side_tall", ImmersiveWeathering.res("block/mossy_template_wall_side_tall")).texture("top", "minecraft:block/snow").texture("side", sideTex).texture("bottom", sideTex);
        this.wallBlock(block, post, side, sideTall);
    }

    private void sandyWallBlockInternal(SandyWallBlock block, String baseName, ResourceLocation texture) {
        Function<Integer, BlockModelBuilder> postFunc = (sandiness) -> this.models().withExistingParent(baseName + "_post", ImmersiveWeathering.res("block/mossy_template_wall_post")).texture("top", extend(texture, "_" + sandiness)).texture("side", extend(texture, "_" + sandiness)).texture("bottom", extend(texture, "_" + sandiness));
        Function<Integer, BlockModelBuilder> sideFunc = (sandiness) -> this.models().withExistingParent(baseName + "_side", ImmersiveWeathering.res("block/mossy_template_wall_side")).texture("top", extend(texture, "_" + sandiness)).texture("side", extend(texture, "_" + sandiness)).texture("bottom", extend(texture, "_" + sandiness));
        Function<Integer, BlockModelBuilder> sideTallFunc = (sandiness) -> this.models().withExistingParent(baseName + "_side_tall", ImmersiveWeathering.res("block/mossy_template_wall_side_tall")).texture("top", extend(texture, "_" + sandiness)).texture("side", extend(texture, "_" + sandiness)).texture("bottom", extend(texture, "_" + sandiness));
        MultiPartBlockStateBuilder builder = this.getMultipartBuilder(block);
        builder.part().modelFile(postFunc.apply(0)).addModel().condition(WallBlock.UP, true).condition(ModBlockProperties.SANDINESS, 0).end();
        WALL_PROPS.entrySet().stream().filter((e) -> e.getKey().getAxis().isHorizontal()).forEach((e) -> {
            builder.part().modelFile(sideFunc.apply(0)).rotationY(((int) e.getKey().toYRot() + 180) % 360).uvLock(true).addModel().condition(e.getValue(), WallSide.LOW).condition(ModBlockProperties.SANDINESS, 0);
            builder.part().modelFile(sideTallFunc.apply(0)).rotationY(((int) e.getKey().toYRot() + 180) % 360).uvLock(true).addModel().condition(e.getValue(), WallSide.TALL).condition(ModBlockProperties.SANDINESS, 0);
        });
        builder.part().modelFile(postFunc.apply(1)).addModel().condition(WallBlock.UP, true).condition(ModBlockProperties.SANDINESS, 1).end();
        WALL_PROPS.entrySet().stream().filter((e) -> e.getKey().getAxis().isHorizontal()).forEach((e) -> {
            builder.part().modelFile(sideFunc.apply(1)).rotationY(((int) e.getKey().toYRot() + 180) % 360).uvLock(true).addModel().condition(e.getValue(), WallSide.LOW).condition(ModBlockProperties.SANDINESS, 1);
            builder.part().modelFile(sideTallFunc.apply(1)).rotationY(((int) e.getKey().toYRot() + 180) % 360).uvLock(true).addModel().condition(e.getValue(), WallSide.TALL).condition(ModBlockProperties.SANDINESS, 1);
        });
    }

    private void mossyWallBlockInternalWithRenderType(WallBlock block, String baseName, ResourceLocation texture, ResourceLocation renderType) {
        var post = this.models().withExistingParent(baseName + "_post", ImmersiveWeathering.res("block/mossy_template_wall_post")).texture("top", extend(texture, "_top")).texture("side", extend(texture, "_side")).texture("bottom", extend(texture, "_side")).renderType(renderType);
        var side = this.models().withExistingParent(baseName + "_side", ImmersiveWeathering.res("block/mossy_template_wall_side")).texture("top", extend(texture, "_top")).texture("side", extend(texture, "_side")).texture("bottom", extend(texture, "_side")).renderType(renderType);
        var sideTall = this.models().withExistingParent(baseName + "_side_tall", ImmersiveWeathering.res("block/mossy_template_wall_side_tall")).texture("top", extend(texture, "_top")).texture("side", extend(texture, "_side")).texture("bottom", extend(texture, "_side")).renderType(renderType);
        this.wallBlock(block, post, side, sideTall);
    }

    private void snowyWallBlockInternalWithRenderType(WallBlock block, String baseName, ResourceLocation texture, ResourceLocation renderType) {
        var post = this.models().withExistingParent(baseName + "_post", ImmersiveWeathering.res("block/mossy_template_wall_post")).texture("top", "minecraft:block/snow").texture("side", extend(texture, "_side")).texture("bottom", extend(texture, "_side")).renderType(renderType);
        var side = this.models().withExistingParent(baseName + "_side", ImmersiveWeathering.res("block/mossy_template_wall_side")).texture("top", "minecraft:block/snow").texture("side", extend(texture, "_side")).texture("bottom", extend(texture, "_side")).renderType(renderType);
        var sideTall = this.models().withExistingParent(baseName + "_side_tall", ImmersiveWeathering.res("block/mossy_template_wall_side_tall")).texture("top", "minecraft:block/snow").texture("side", extend(texture, "_side")).texture("bottom", extend(texture, "_side")).renderType(renderType);
        this.wallBlock(block, post, side, sideTall);
    }

    private void wallSidePart(MultiPartBlockStateBuilder builder, ModelFile model, Map.Entry<Direction, Property<WallSide>> entry, WallSide height) {
        builder.part().modelFile(model).rotationY(((int) entry.getKey().toYRot() + 180) % 360).uvLock(true).addModel().condition(entry.getValue(), height);
    }

    private ResourceLocation extend(ResourceLocation rl, String suffix) {
        return ResourceLocation.fromNamespaceAndPath(rl.getNamespace(), rl.getPath() + suffix);
    }

    private ResourceLocation key(Block block) {
        return BuiltInRegistries.BLOCK.getKey(block);
    }
    private String name(Block block) {
        return BuiltInRegistries.BLOCK.getKey(block).getPath();
    }
}
