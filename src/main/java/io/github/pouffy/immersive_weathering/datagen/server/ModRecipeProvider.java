package io.github.pouffy.immersive_weathering.datagen.server;

import io.github.pouffy.immersive_weathering.ImmersiveWeathering;
import io.github.pouffy.immersive_weathering.reg.ModBlocks;
import io.github.pouffy.immersive_weathering.reg.ModItems;
import net.mehvahdjukaar.moonlight.api.set.leaves.LeavesTypeRegistry;
import net.mehvahdjukaar.moonlight.api.set.wood.WoodTypeRegistry;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.StonecutterRecipe;
import net.minecraft.world.level.block.Blocks;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider {
    public ModRecipeProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries) {
        super(packOutput, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput pRecipeOutput, HolderLookup.Provider holderLookup) {
        craftingRecipes(pRecipeOutput);
        stonecuttingRecipes(pRecipeOutput);
        dynamicRecipes(pRecipeOutput);
    }

    private void craftingRecipes(RecipeOutput pRecipeOutput) {
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModItems.AZALEA_FLOWER_PILE, 6)
                .group("leaf_pile")
                .pattern("###")
                .define('#', ModItems.AZALEA_FLOWERS)
                .unlockedBy("has_flowers", has(ModItems.AZALEA_FLOWERS))
                .save(pRecipeOutput, ImmersiveWeathering.res("crafting/azalea_flower_pile"));
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModBlocks.PRISMARINE_BRICK_WALL, 6)
                .group("iw_wall")
                .pattern("SSS").pattern("SSS")
                .define('S', Blocks.PRISMARINE_BRICKS)
                .unlockedBy("has_bricks", has(Blocks.PRISMARINE_BRICKS))
                .save(pRecipeOutput, ImmersiveWeathering.res("crafting/masonry/wall/prismarine_brick"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.BLACK_DYE)
                .requires(ModBlocks.SOOT)
                .unlockedBy("has_soot", has(ModBlocks.SOOT))
                .save(pRecipeOutput, ImmersiveWeathering.res("crafting/misc/black_dye_from_soot"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, Blocks.STONE_BRICKS)
                .group("iw_bricks")
                .pattern("SS").pattern("SS")
                .define('S', ModItems.STONE_BRICK)
                .unlockedBy("has_brick", has(ModItems.STONE_BRICK))
                .save(pRecipeOutput, ImmersiveWeathering.res("crafting/bricks_from_brick/stone"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, Blocks.POLISHED_BLACKSTONE_BRICKS)
                .group("iw_bricks")
                .pattern("SS").pattern("SS")
                .define('S', ModItems.BLACKSTONE_BRICK)
                .unlockedBy("has_brick", has(ModItems.BLACKSTONE_BRICK))
                .save(pRecipeOutput, ImmersiveWeathering.res("crafting/bricks_from_brick/blackstone"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, Blocks.DEEPSLATE_BRICKS)
                .group("iw_bricks")
                .pattern("SS").pattern("SS")
                .define('S', ModItems.DEEPSLATE_BRICK)
                .unlockedBy("has_brick", has(ModItems.DEEPSLATE_BRICK))
                .save(pRecipeOutput, ImmersiveWeathering.res("crafting/bricks_from_brick/deepslate"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, Blocks.DEEPSLATE_TILES)
                .group("iw_bricks")
                .pattern("SS").pattern("SS")
                .define('S', ModItems.DEEPSLATE_TILE)
                .unlockedBy("has_tile", has(ModItems.DEEPSLATE_TILE))
                .save(pRecipeOutput, ImmersiveWeathering.res("crafting/tiles_from_tile/deepslate"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, Blocks.END_STONE_BRICKS)
                .group("iw_bricks")
                .pattern("SS").pattern("SS")
                .define('S', ModItems.END_STONE_BRICK)
                .unlockedBy("has_brick", has(ModItems.END_STONE_BRICK))
                .save(pRecipeOutput, ImmersiveWeathering.res("crafting/bricks_from_brick/end_stone"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, Blocks.PRISMARINE_BRICKS)
                .group("iw_bricks")
                .pattern("SS").pattern("SS")
                .define('S', ModItems.PRISMARINE_BRICK)
                .unlockedBy("has_brick", has(ModItems.PRISMARINE_BRICK))
                .save(pRecipeOutput, ImmersiveWeathering.res("crafting/bricks_from_brick/prismarine"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, Blocks.TUFF_BRICKS)
                .group("iw_bricks")
                .pattern("SS").pattern("SS")
                .define('S', ModItems.TUFF_BRICK)
                .unlockedBy("has_brick", has(ModItems.TUFF_BRICK))
                .save(pRecipeOutput, ImmersiveWeathering.res("crafting/bricks_from_brick/tuff"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, Blocks.RED_SAND)
                .group("iw_sand")
                .pattern("##").pattern("##")
                .define('#', ModBlocks.RED_SAND_LAYER_BLOCK)
                .unlockedBy("has_pile", has(ModBlocks.RED_SAND_LAYER_BLOCK))
                .save(pRecipeOutput, ImmersiveWeathering.res("crafting/sand/red_sand"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, Blocks.SAND)
                .group("iw_sand")
                .pattern("##").pattern("##")
                .define('#', ModBlocks.SAND_LAYER_BLOCK)
                .unlockedBy("has_pile", has(ModBlocks.SAND_LAYER_BLOCK))
                .save(pRecipeOutput, ImmersiveWeathering.res("crafting/sand/sand"));
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModBlocks.RED_SAND_LAYER_BLOCK, 6)
                .group("iw_sand_pile")
                .pattern("###")
                .define('#', Blocks.RED_SAND)
                .unlockedBy("has_sand", has(Blocks.RED_SAND))
                .save(pRecipeOutput, ImmersiveWeathering.res("crafting/sand/red_sand_pile"));
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModBlocks.SAND_LAYER_BLOCK, 6)
                .group("iw_sand_pile")
                .pattern("###")
                .define('#', Blocks.SAND)
                .unlockedBy("has_sand", has(Blocks.SAND))
                .save(pRecipeOutput, ImmersiveWeathering.res("crafting/sand/sand_pile"));
    }

    private void stonecuttingRecipes(RecipeOutput pRecipeOutput) {
        SingleItemRecipeBuilder.stonecutting(Ingredient.of(Blocks.STONE), RecipeCategory.BUILDING_BLOCKS, ModItems.STONE_BRICK, 16)
                .group("iw_brick")
                .unlockedBy("has", has(Blocks.STONE))
                .save(pRecipeOutput, ImmersiveWeathering.res("stonecutting/bricks/stone"));
        SingleItemRecipeBuilder.stonecutting(Ingredient.of(Blocks.POLISHED_BLACKSTONE), RecipeCategory.BUILDING_BLOCKS, ModItems.BLACKSTONE_BRICK, 16)
                .group("iw_brick")
                .unlockedBy("has", has(Blocks.POLISHED_BLACKSTONE))
                .save(pRecipeOutput, ImmersiveWeathering.res("stonecutting/bricks/blackstone"));
        SingleItemRecipeBuilder.stonecutting(Ingredient.of(Blocks.BRICKS), RecipeCategory.BUILDING_BLOCKS, Items.BRICK, 4)
                .group("iw_brick")
                .unlockedBy("has", has(Blocks.BRICKS))
                .save(pRecipeOutput, ImmersiveWeathering.res("stonecutting/bricks/terracotta"));
        SingleItemRecipeBuilder.stonecutting(Ingredient.of(Blocks.COBBLED_DEEPSLATE, Blocks.DEEPSLATE), RecipeCategory.BUILDING_BLOCKS, ModItems.DEEPSLATE_BRICK, 16)
                .group("iw_brick")
                .unlockedBy("has", inventoryTrigger(ItemPredicate.Builder.item().of(Blocks.COBBLED_DEEPSLATE, Blocks.DEEPSLATE)))
                .save(pRecipeOutput, ImmersiveWeathering.res("stonecutting/bricks/deepslate"));
        SingleItemRecipeBuilder.stonecutting(Ingredient.of(Blocks.COBBLED_DEEPSLATE, Blocks.DEEPSLATE), RecipeCategory.BUILDING_BLOCKS, ModItems.DEEPSLATE_TILE, 16)
                .group("iw_brick")
                .unlockedBy("has", inventoryTrigger(ItemPredicate.Builder.item().of(Blocks.COBBLED_DEEPSLATE, Blocks.DEEPSLATE)))
                .save(pRecipeOutput, ImmersiveWeathering.res("stonecutting/tiles/deepslate"));
        SingleItemRecipeBuilder.stonecutting(Ingredient.of(Blocks.END_STONE), RecipeCategory.BUILDING_BLOCKS, ModItems.END_STONE_BRICK, 16)
                .group("iw_brick")
                .unlockedBy("has", has(Blocks.END_STONE))
                .save(pRecipeOutput, ImmersiveWeathering.res("stonecutting/bricks/end_stone"));
        SingleItemRecipeBuilder.stonecutting(Ingredient.of(Blocks.PRISMARINE), RecipeCategory.BUILDING_BLOCKS, ModItems.PRISMARINE_BRICK, 16)
                .group("iw_brick")
                .unlockedBy("has", has(Blocks.PRISMARINE))
                .save(pRecipeOutput, ImmersiveWeathering.res("stonecutting/bricks/prismarine"));
        SingleItemRecipeBuilder.stonecutting(Ingredient.of(Blocks.TUFF), RecipeCategory.BUILDING_BLOCKS, ModItems.TUFF_BRICK, 16)
                .group("iw_brick")
                .unlockedBy("has", has(Blocks.TUFF))
                .save(pRecipeOutput, ImmersiveWeathering.res("stonecutting/bricks/tuff"));
        SingleItemRecipeBuilder.stonecutting(Ingredient.of(Blocks.PRISMARINE_BRICKS), RecipeCategory.BUILDING_BLOCKS, ModBlocks.PRISMARINE_BRICK_WALL)
                .group("iw_wall")
                .unlockedBy("has", has(Blocks.PRISMARINE_BRICKS))
                .save(pRecipeOutput, ImmersiveWeathering.res("stonecutting/wall/prismarine"));
    }

    private void dynamicRecipes(RecipeOutput pRecipeOutput) {
        for (var type : LeavesTypeRegistry.INSTANCE) {
            ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModBlocks.LEAF_PILES.get(type), 6)
                    .group("leaf_pile")
                    .pattern("###")
                    .define('#', type.leaves)
                    .unlockedBy("has_leaves", has(type.leaves))
                    .save(pRecipeOutput, ImmersiveWeathering.res("crafting/"+type.id.getPath()+"_leaf_pile"));
        }
        for (var type : WoodTypeRegistry.INSTANCE) {
            var stripped = type.getBlockOfThis("stripped_log");
            var wood = type.getBlockOfThis("wood");
            var strippedWood = type.getBlockOfThis("stripped_wood");
            if (stripped != null) {
                ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, type.log)
                        .group("log_unstrip")
                        .requires(ModItems.BARK.get(type))
                        .requires(stripped)
                        .unlockedBy("has_bark", has(ModItems.BARK.get(type)))
                        .save(pRecipeOutput, ImmersiveWeathering.res("crafting/log_unstrip/"+type.id.getPath()));
            }
            if (wood != null) {
                ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, wood)
                        .group("wood_from_bark")
                        .requires(ModItems.BARK.get(type))
                        .requires(type.log)
                        .unlockedBy("has_bark", has(ModItems.BARK.get(type)))
                        .save(pRecipeOutput, ImmersiveWeathering.res("crafting/wood_from_bark/"+type.id.getPath()));
                if (strippedWood != null) {
                    ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, wood)
                            .group("wood_unstrip")
                            .requires(ModItems.BARK.get(type))
                            .requires(strippedWood)
                            .unlockedBy("has_bark", has(ModItems.BARK.get(type)))
                            .save(pRecipeOutput, ImmersiveWeathering.res("crafting/wood_unstrip/"+type.id.getPath()));
                }
            }
        }
    }
}
