package io.github.pouffy.immersive_weathering.integration;

import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiWorldInteractionRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import io.github.pouffy.immersive_weathering.ImmersiveWeathering;
import io.github.pouffy.immersive_weathering.datamaps.DataMapHelpers;
import io.github.pouffy.immersive_weathering.reg.ModBlocks;
import io.github.pouffy.immersive_weathering.reg.ModItems;
import io.github.pouffy.immersive_weathering.reg.ModTags;
import io.github.pouffy.immersive_weathering.util.WeatheringHelper;
import net.mehvahdjukaar.moonlight.api.set.BlockSetAPI;
import net.mehvahdjukaar.moonlight.api.set.wood.WoodType;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.common.DataMapHooks;

import java.util.*;

@EmiEntrypoint
public class EmiIntegration implements EmiPlugin {

    @Override
    public void register(EmiRegistry registry) {
        registry.removeRecipes(recipe -> recipe.getId() != null && recipe.getId().getNamespace().equals("emi") && recipe.getId().getPath().startsWith("/world/waxing"));
        registry.removeRecipes(recipe -> recipe.getId() != null && recipe.getId().getNamespace().equals("emi") && recipe.getId().getPath().startsWith("/world/stripping"));
        //registry.removeRecipes(recipe -> recipe.getId() != null && recipe.getId().getNamespace().equals("emi") && recipe.getId().getPath().startsWith("/composting"));

        EmiIngredient shovels = EmiIngredient.of(ItemTags.SHOVELS);
        EmiIngredient hoes = EmiIngredient.of(ItemTags.HOES);

        //OTHER
        registry.addRecipe(EmiWorldInteractionRecipe.builder()
                .id(ImmersiveWeathering.res("/rooted_grass_uprooting"))
                .leftInput(EmiStack.of(ModBlocks.ROOTED_GRASS_BLOCK.get()))
                .rightInput(hoes, true)
                .output(EmiStack.of(Items.HANGING_ROOTS))
                .output(EmiStack.of(Items.GRASS_BLOCK))
                .build());

        registry.addRecipe(EmiWorldInteractionRecipe.builder()
                .id(ImmersiveWeathering.res("/campfire_extinguishing"))
                .leftInput(EmiStack.of(Blocks.CAMPFIRE))
                .rightInput(shovels, true)
                .output(EmiStack.of(ModBlocks.SOOT.get()))
                .build());

        //TILLING
        registry.addRecipe(EmiWorldInteractionRecipe.builder()
                .id(ImmersiveWeathering.res("/clay_tilling"))
                .leftInput(EmiIngredient.of(Ingredient.of(ModBlocks.EARTHEN_CLAY.get(), ModBlocks.GRASSY_EARTHEN_CLAY.get())))
                .rightInput(hoes, true)
                .output(EmiStack.of(ModBlocks.EARTHEN_CLAY_FARMLAND.get()))
                .build());

        registry.addRecipe(EmiWorldInteractionRecipe.builder()
                .id(ImmersiveWeathering.res("/sandy_tilling"))
                .leftInput(EmiIngredient.of(Ingredient.of(ModBlocks.SANDY_DIRT.get(), ModBlocks.GRASSY_SANDY_DIRT.get())))
                .rightInput(hoes, true)
                .output(EmiStack.of(ModBlocks.SANDY_FARMLAND.get()))
                .build());

        registry.addRecipe(EmiWorldInteractionRecipe.builder()
                .id(ImmersiveWeathering.res("/silt_tilling"))
                .leftInput(EmiIngredient.of(Ingredient.of(ModBlocks.SILT.get(), ModBlocks.GRASSY_SILT.get())))
                .rightInput(hoes, true)
                .output(EmiStack.of(ModBlocks.SILTY_FARMLAND.get()))
                .build());

        registry.addRecipe(EmiWorldInteractionRecipe.builder()
                .id(ImmersiveWeathering.res("/loam_tilling"))
                .leftInput(EmiStack.of(ModBlocks.LOAM.get()))
                .rightInput(hoes, true)
                .output(EmiStack.of(ModBlocks.LOAMY_FARMLAND.get()))
                .build());

        safely("flowering", () -> addFlowering(registry));
        safely("frost", () -> addFrost(registry));
        safely("crack", () -> addCrack(registry));
        safely("moss", () -> addMoss(registry));
        safely("sand", () -> addSand(registry));
        safely("snow", () -> addSnow(registry));
        safely("metals", () -> addWaxing(registry));
        safely("woods", () -> addWoods(registry));
        safely("charring", () -> addCharring(registry));
        safely("fluid generators", () -> addFluidGenerators(registry));
    }

    private static void addFlowering(EmiRegistry registry) {
        for (Block key : BuiltInRegistries.BLOCK) {
            ResourceLocation blockId = BuiltInRegistries.ITEM.getKey(key.asItem());
            EmiStack input = EmiStack.of(key);
            if (DataMapHelpers.canAdvance(DataMapHelpers.Type.FLOWER, key)) {
                var next = DataMapHelpers.getNext(DataMapHelpers.Type.FLOWER, key);
                next.ifPresent(block -> registry.addRecipe(EmiWorldInteractionRecipe.builder()
                        .id(ImmersiveWeathering.res("/flowering/" + blockId.getNamespace() + "/" + blockId.getPath()))
                        .leftInput(input)
                        .rightInput(EmiStack.of(ModItems.AZALEA_FLOWERS.get()), false)
                        .output(EmiStack.of(block))
                        .build()));
            }
            if (DataMapHelpers.canRevert(DataMapHelpers.Type.FLOWER, key)) {
                var previous = DataMapHelpers.getPrevious(DataMapHelpers.Type.FLOWER, key);
                previous.ifPresent(block -> registry.addRecipe(EmiWorldInteractionRecipe.builder()
                        .id(ImmersiveWeathering.res("/flower_shearing/" + blockId.getNamespace() + "/" + blockId.getPath()))
                        .leftInput(input)
                        .rightInput(EmiStack.of(Items.SHEARS), true)
                        .output(EmiStack.of(block))
                        .build()));
            }
        }
    }

    private static void addFrost(EmiRegistry registry) {
        for (Block key : BuiltInRegistries.BLOCK) {
            ResourceLocation blockId = BuiltInRegistries.ITEM.getKey(key.asItem());
            EmiStack input = EmiStack.of(key);
            if (DataMapHelpers.canAdvance(DataMapHelpers.Type.FROST, key) && key != Blocks.AIR) {
                var next = DataMapHelpers.getNext(DataMapHelpers.Type.FROST, key);
                next.ifPresent(block -> registry.addRecipe(EmiWorldInteractionRecipe.builder()
                        .id(ImmersiveWeathering.res("/frosting/" + blockId.getNamespace() + "/" + blockId.getPath()))
                        .leftInput(input)
                        .rightInput(EmiStack.of(ModItems.FROST_ITEM.get()), false)
                        .output(EmiStack.of(block))
                        .build()));
            }
            if (DataMapHelpers.canRevert(DataMapHelpers.Type.FROST, key) && key != Blocks.AIR) {
                var previous = DataMapHelpers.getPrevious(DataMapHelpers.Type.FROST, key);
                previous.ifPresent(block -> registry.addRecipe(EmiWorldInteractionRecipe.builder()
                        .id(ImmersiveWeathering.res("/defrosting/" + blockId.getNamespace() + "/" + blockId.getPath()))
                        .leftInput(input)
                        .rightInput(EmiIngredient.of(ItemTags.CREEPER_IGNITERS), true)
                        .output(EmiStack.of(block))
                        .build()));
            }
        }
    }
    private static void addCrack(EmiRegistry registry) {
        for (Block key : BuiltInRegistries.BLOCK) {
            ResourceLocation blockId = BuiltInRegistries.ITEM.getKey(key.asItem());
            EmiStack input = EmiStack.of(key);
            if (DataMapHelpers.canAdvance(DataMapHelpers.Type.CRACK, key)) {
                var next = DataMapHelpers.getNext(DataMapHelpers.Type.CRACK, key);
                next.ifPresent(block -> registry.addRecipe(EmiWorldInteractionRecipe.builder()
                        .id(ImmersiveWeathering.res("/cracking/" + blockId.getNamespace() + "/" + blockId.getPath()))
                        .leftInput(input)
                        .rightInput(EmiIngredient.of(ItemTags.PICKAXES), true)
                        .output(EmiStack.of(block))
                        .build()));
            }
            if (DataMapHelpers.canRevert(DataMapHelpers.Type.CRACK, key)) {
                var crackable = DataMapHelpers.getCrackable(key);
                if (crackable.isPresent()) {
                    var previous = DataMapHelpers.getPrevious(DataMapHelpers.Type.FROST, key);
                    var brick = crackable.get().repairItem();
                    if (previous.isPresent() && brick.isPresent()) {
                        registry.addRecipe(EmiWorldInteractionRecipe.builder()
                                .id(ImmersiveWeathering.res("/bricking/" + blockId.getNamespace() + "/" + blockId.getPath()))
                                .leftInput(input)
                                .rightInput(EmiIngredient.of(Ingredient.of(brick.get(), ModItems.MORTAR.get())), false)
                                .output(EmiStack.of(previous.get()))
                                .build());
                    }
                }
            }
        }
    }

    private static void addMoss(EmiRegistry registry) {
        for (Block key : BuiltInRegistries.BLOCK) {
            ResourceLocation blockId = BuiltInRegistries.ITEM.getKey(key.asItem());
            EmiStack input = EmiStack.of(key);
            if (DataMapHelpers.canAdvance(DataMapHelpers.Type.MOSS, key)) {
                var next = DataMapHelpers.getNext(DataMapHelpers.Type.MOSS, key);
                next.ifPresent(block -> registry.addRecipe(EmiWorldInteractionRecipe.builder()
                        .id(ImmersiveWeathering.res("/mossing/" + blockId.getNamespace() + "/" + blockId.getPath()))
                        .leftInput(input)
                        .rightInput(EmiStack.of(ModItems.MOSS_CLUMP.get()), false)
                        .output(EmiStack.of(block))
                        .build()));
                next.ifPresent(block -> registry.addRecipe(EmiWorldInteractionRecipe.builder()
                        .id(ImmersiveWeathering.res("/gold_mossing/" + blockId.getNamespace() + "/" + blockId.getPath()))
                        .leftInput(input)
                        .rightInput(EmiStack.of(ModItems.ENCHANTED_GOLDEN_MOSS_CLUMP.get()), true)
                        .output(EmiStack.of(block))
                        .build()));
            }
            if (DataMapHelpers.canRevert(DataMapHelpers.Type.MOSS, key)) {
                var previous = DataMapHelpers.getPrevious(DataMapHelpers.Type.MOSS, key);
                previous.ifPresent(block -> registry.addRecipe(EmiWorldInteractionRecipe.builder()
                        .id(ImmersiveWeathering.res("/moss_shear/" + blockId.getNamespace() + "/" + blockId.getPath()))
                        .leftInput(input)
                        .rightInput(EmiStack.of(Items.SHEARS), true)
                        .output(EmiStack.of(block))
                        .output(EmiStack.of(ModItems.MOSS_CLUMP.get()))
                        .build()));
                previous.ifPresent(block -> registry.addRecipe(EmiWorldInteractionRecipe.builder()
                        .id(ImmersiveWeathering.res("/moss_burn/" + blockId.getNamespace() + "/" + blockId.getPath()))
                        .leftInput(input)
                        .rightInput(EmiIngredient.of(ItemTags.CREEPER_IGNITERS), true)
                        .output(EmiStack.of(block))
                        .build()));
            }
        }
    }

    private static void addSand(EmiRegistry registry) {
        for (Block key : BuiltInRegistries.BLOCK) {
            ResourceLocation blockId = BuiltInRegistries.ITEM.getKey(key.asItem());
            EmiStack input = EmiStack.of(key);
            if (DataMapHelpers.canAdvance(DataMapHelpers.Type.SAND, key)) {
                var next = DataMapHelpers.getNext(DataMapHelpers.Type.SAND, key);
                next.ifPresent(block -> registry.addRecipe(EmiWorldInteractionRecipe.builder()
                        .id(ImmersiveWeathering.res("/block_sanding/" + blockId.getNamespace() + "/" + blockId.getPath()))
                        .leftInput(input)
                        .rightInput(EmiStack.of(ModBlocks.SAND_LAYER_BLOCK.get()), false)
                        .output(EmiStack.of(block))
                        .build()));
            }
            if (DataMapHelpers.canRevert(DataMapHelpers.Type.SAND, key)) {
                var previous = DataMapHelpers.getPrevious(DataMapHelpers.Type.SAND, key);
                previous.ifPresent(block -> registry.addRecipe(EmiWorldInteractionRecipe.builder()
                        .id(ImmersiveWeathering.res("/unsanding/" + blockId.getNamespace() + "/" + blockId.getPath()))
                        .leftInput(input)
                        .rightInput(EmiIngredient.of(ItemTags.SHOVELS), true)
                        .output(EmiStack.of(block))
                        .output(EmiStack.of(ModBlocks.SAND_LAYER_BLOCK.get()))
                        .build()));
            }
        }
    }

    private static void addSnow(EmiRegistry registry) {
        for (Block key : BuiltInRegistries.BLOCK) {
            ResourceLocation blockId = BuiltInRegistries.ITEM.getKey(key.asItem());
            EmiStack input = EmiStack.of(key);
            if (DataMapHelpers.canAdvance(DataMapHelpers.Type.SNOW, key)) {
                var next = DataMapHelpers.getNext(DataMapHelpers.Type.SNOW, key);
                next.ifPresent(block -> registry.addRecipe(EmiWorldInteractionRecipe.builder()
                        .id(ImmersiveWeathering.res("/block_snowing/" + blockId.getNamespace() + "/" + blockId.getPath()))
                        .leftInput(input)
                        .rightInput(EmiStack.of(Items.SNOWBALL), false)
                        .output(EmiStack.of(block))
                        .build()));
            }
            if (DataMapHelpers.canRevert(DataMapHelpers.Type.SNOW, key)) {
                var previous = DataMapHelpers.getPrevious(DataMapHelpers.Type.SNOW, key);
                previous.ifPresent(block -> registry.addRecipe(EmiWorldInteractionRecipe.builder()
                        .id(ImmersiveWeathering.res("/unsnowing/" + blockId.getNamespace() + "/" + blockId.getPath()))
                        .leftInput(input)
                        .rightInput(EmiIngredient.of(ItemTags.SHOVELS), true)
                        .output(EmiStack.of(block))
                        .output(EmiStack.of(Items.SNOWBALL))
                        .build()));
            }
        }
    }

    private static void addWaxing(EmiRegistry registry) {
        for (Block key : BuiltInRegistries.BLOCK) {
            ResourceLocation blockId = BuiltInRegistries.ITEM.getKey(key.asItem());
            EmiStack input = EmiStack.of(key);
            if (DataMapHooks.getBlockWaxed(key) != null) {
                EmiStack waxed = EmiStack.of(Objects.requireNonNull(DataMapHooks.getBlockWaxed(key)));
                registry.addRecipe(EmiWorldInteractionRecipe.builder()
                        .id(ImmersiveWeathering.res("/block_waxing/" + blockId.getNamespace() + "/" + blockId.getPath()))
                        .leftInput(input)
                        .rightInput(EmiStack.of(Items.HONEYCOMB), false)
                        .output(waxed)
                        .build());
            }
            if (DataMapHooks.getBlockUnwaxed(key) != null) {
                EmiStack unwaxed = EmiStack.of(Objects.requireNonNull(DataMapHooks.getBlockUnwaxed(key)));
                registry.addRecipe(EmiWorldInteractionRecipe.builder()
                        .id(ImmersiveWeathering.res("/block_unwaxing/" + blockId.getNamespace() + "/" + blockId.getPath()))
                        .leftInput(input)
                        .rightInput(EmiIngredient.of(ItemTags.AXES), true)
                        .output(unwaxed)
                        .build());
            }
            if (DataMapHooks.getPreviousOxidizedStage(key) != null) {
                EmiStack previous = EmiStack.of(Objects.requireNonNull(DataMapHooks.getPreviousOxidizedStage(key)));
                registry.addRecipe(EmiWorldInteractionRecipe.builder()
                        .id(ImmersiveWeathering.res("/deoxidising/" + blockId.getNamespace() + "/" + blockId.getPath()))
                        .leftInput(input)
                        .rightInput(EmiIngredient.of(ItemTags.AXES), true)
                        .output(previous)
                        .build());
            }
            if (DataMapHelpers.canRevert(DataMapHelpers.Type.RUST, key)) {
                var previous = DataMapHelpers.getPrevious(DataMapHelpers.Type.RUST, key);
                previous.ifPresent(block -> registry.addRecipe(EmiWorldInteractionRecipe.builder()
                        .id(ImmersiveWeathering.res("/derusting/" + blockId.getNamespace() + "/" + blockId.getPath()))
                        .leftInput(input)
                        .rightInput(EmiIngredient.of(ItemTags.AXES), true)
                        .output(EmiStack.of(block))
                        .build()));
            }
            if (DataMapHelpers.canAdvance(DataMapHelpers.Type.RUST, key)) {
                var next = DataMapHelpers.getNext(DataMapHelpers.Type.RUST, key);
                next.ifPresent(block -> registry.addRecipe(EmiWorldInteractionRecipe.builder()
                        .id(ImmersiveWeathering.res("/sponge_rusting/" + blockId.getNamespace() + "/" + blockId.getPath()))
                        .leftInput(input)
                        .rightInput(EmiStack.of(Items.WET_SPONGE), true)
                        .output(EmiStack.of(block))
                        .build()));
            }
        }
    }

    //private static void addComposting(EmiRegistry registry) {
    //    Map<Item, Float> compostables = new HashMap<>();
    //    for (Item key : BuiltInRegistries.ITEM) {
    //        Holder<Item> holder = BuiltInRegistries.ITEM.wrapAsHolder(key);
    //        Compostable compostable = holder.getData(NeoForgeDataMaps.COMPOSTABLES);
    //        if (compostable != null) {
    //            compostables.put(key, compostable.chance());
    //        }
    //    }
    //    Map<Float, EmiIngredient> groupedCompostables = new HashMap<>();
    //    for (Map.Entry<Item, Float> entry : compostables.entrySet()) {
    //        groupedCompostables.computeIfPresent(entry.getValue(), (k, ingredient) -> mergeInto(ingredient, entry.getKey()));
    //    }
    //    groupedCompostables.forEach((chance, input) -> {
    //        registry.addRecipe(new EmiCompostingRecipe(input, chance, ImmersiveWeathering.res("/composting/item/chance_" + chance.toString())));
    //    });
    //}

    private static EmiIngredient mergeInto(EmiIngredient other, ItemLike input) {
        List<EmiStack> stacks = new ArrayList<>(other.getEmiStacks());
        stacks.add(EmiStack.of(input));
        return EmiIngredient.of(stacks);
    }

    private static void addWoods(EmiRegistry registry) {
        for (Block key : BuiltInRegistries.BLOCK) {
            ResourceLocation blockId = BuiltInRegistries.ITEM.getKey(key.asItem());
            EmiStack input = EmiStack.of(key);
            if (key.asItem() != Items.AIR) {
                WoodType woodType = BlockSetAPI.getBlockTypeOf(key, WoodType.class);
                if (woodType != null) {
                    var bark = WeatheringHelper.getBarkToStrip(key.defaultBlockState());
                    var stripped = woodType.log == key ? woodType.getBlockOfThis("stripped_log") : woodType.getBlockOfThis("wood") == key ? woodType.getBlockOfThis("stripped_wood") : null;
                    if (bark != null && stripped != null) {
                        ResourceLocation strippedId = BuiltInRegistries.ITEM.getKey(stripped.asItem());
                        EmiStack raw_log = EmiStack.of(key);
                        registry.addRecipe(EmiWorldInteractionRecipe.builder()
                                .id(ImmersiveWeathering.res("/block_stripping/" + blockId.getNamespace() + "/" + blockId.getPath()))
                                .leftInput(raw_log)
                                .rightInput(EmiIngredient.of(ItemTags.AXES), true)
                                .output(EmiStack.of(bark))
                                .output(EmiStack.of(stripped))
                                .build());
                        registry.addRecipe(EmiWorldInteractionRecipe.builder()
                                .id(ImmersiveWeathering.res("/block_unstripping/" + strippedId.getNamespace() + "/" + strippedId.getPath()))
                                .leftInput(EmiStack.of(stripped))
                                .rightInput(EmiStack.of(bark), false)
                                .output(raw_log)
                                .build());
                    }
                }
            }
        }
    }

    private static void addCharring(EmiRegistry registry) {
        Map<TagKey<Block>, Block> charred = WeatheringHelper.WOOD_TO_CHARRED.get();
        for (TagKey<Block> key : charred.keySet()) {
            EmiIngredient unburnt_block = EmiIngredient.of(key);
            EmiStack charred_block = EmiStack.of(charred.get(key));
            registry.addRecipe(EmiWorldInteractionRecipe.builder()
                    .id(ImmersiveWeathering.res("/block_charring/" + key.location().getNamespace() + "/" + key.location().getPath()))
                    .leftInput(unburnt_block)
                    .rightInput(EmiStack.of(ModItems.FIRE.get()), true)
                    .output(charred_block)
                    .build());
        }
    }

    private static void addFluidGenerators(EmiRegistry registry) {
        EmiStack water = EmiStack.of(Fluids.WATER);
        EmiStack lava = EmiStack.of(Fluids.LAVA);

        var style = Style.EMPTY.applyFormats(ChatFormatting.GREEN);
        EmiStack waterCatalyst = water.copy().setRemainder(water);
        EmiStack lavaCatalyst = lava.copy().setRemainder(lava);

        registry.addRecipe(EmiWorldInteractionRecipe.builder()
                .id(ImmersiveWeathering.res("/andesite"))
                .leftInput(waterCatalyst)
                .rightInput(lavaCatalyst, true)
                .rightInput(EmiStack.of(Blocks.DIORITE), true, s -> s.appendTooltip(
                        Component.translatable("tooltip.immersive_weathering.adjacent").setStyle(style)))
                .output(EmiStack.of(Blocks.ANDESITE))
                .build());

        registry.addRecipe(EmiWorldInteractionRecipe.builder()
                .id(ImmersiveWeathering.res("/diorite"))
                .leftInput(waterCatalyst)
                .rightInput(lavaCatalyst, true)
                .rightInput(EmiIngredient.of(ModTags.QUARTZ_BLOCKS), true, s -> s.appendTooltip(
                        Component.translatable("tooltip.immersive_weathering.adjacent").setStyle(style)))
                .output(EmiStack.of(Blocks.DIORITE))
                .build());

        registry.addRecipe(EmiWorldInteractionRecipe.builder()
                .id(ImmersiveWeathering.res("/granite"))
                .leftInput(waterCatalyst)
                .rightInput(lavaCatalyst, true)
                .rightInput(EmiStack.of(Blocks.DIORITE), true, s -> s.appendTooltip(
                        Component.translatable("tooltip.immersive_weathering.adjacent").setStyle(style)))
                .rightInput(EmiIngredient.of(ModTags.QUARTZ_BLOCKS), true, s -> s.appendTooltip(
                        Component.translatable("tooltip.immersive_weathering.adjacent").setStyle(style)))
                .output(EmiStack.of(Blocks.GRANITE))
                .build());

        registry.addRecipe(EmiWorldInteractionRecipe.builder()
                .id(ImmersiveWeathering.res("/basalt_below"))
                .leftInput(lavaCatalyst)
                .rightInput(EmiStack.of(Blocks.BASALT), true, s -> s.appendTooltip(
                        Component.translatable("tooltip.immersive_weathering.below").setStyle(style)))
                .output(EmiStack.of(Blocks.BASALT))
                .build());

        registry.addRecipe(EmiWorldInteractionRecipe.builder()
                .id(ImmersiveWeathering.res("/blackstone"))
                .leftInput(lavaCatalyst)
                .rightInput(EmiStack.of(Blocks.MAGMA_BLOCK), true, s -> s.appendTooltip(
                        Component.translatable("tooltip.immersive_weathering.adjacent").setStyle(style)))
                .rightInput(EmiStack.of(Blocks.BLUE_ICE), true, s -> s.appendTooltip(
                        Component.translatable("tooltip.immersive_weathering.adjacent").setStyle(style)))
                .output(EmiStack.of(Blocks.BLACKSTONE))
                .build());

        registry.addRecipe(EmiWorldInteractionRecipe.builder()
                .id(ImmersiveWeathering.res("/calcite"))
                .leftInput(lavaCatalyst)
                .rightInput(EmiStack.of(Blocks.MAGMA_BLOCK), true, s -> s.appendTooltip(
                        Component.translatable("tooltip.immersive_weathering.adjacent").setStyle(style)))
                .rightInput(EmiStack.of(Blocks.BLUE_ICE), true, s -> s.appendTooltip(
                        Component.translatable("tooltip.immersive_weathering.adjacent").setStyle(style)))
                .rightInput(EmiStack.of(Blocks.BONE_BLOCK), true, s -> s.appendTooltip(
                        Component.translatable("tooltip.immersive_weathering.below").setStyle(style)))
                .output(EmiStack.of(Blocks.CALCITE))
                .build());

        registry.addRecipe(EmiWorldInteractionRecipe.builder()
                .id(ImmersiveWeathering.res("/crying_obsidian"))
                .leftInput(lavaCatalyst)
                .rightInput(waterCatalyst, true)
                .rightInput(EmiStack.of(Blocks.SOUL_FIRE), true, s -> s.appendTooltip(
                        Component.translatable("tooltip.immersive_weathering.adjacent").setStyle(style)))
                .output(EmiStack.of(Blocks.CRYING_OBSIDIAN))
                .build());

        registry.addRecipe(EmiWorldInteractionRecipe.builder()
                .id(ImmersiveWeathering.res("/magma_block"))
                .leftInput(lavaCatalyst)
                .rightInput(waterCatalyst, true, s -> s.appendTooltip(
                        Component.translatable("tooltip.immersive_weathering.below").setStyle(style)).appendTooltip(
                        Component.translatable("tooltip.immersive_weathering.rising").setStyle(style)))
                .output(EmiStack.of(Blocks.MAGMA_BLOCK))
                .build());

        registry.addRecipe(EmiWorldInteractionRecipe.builder()
                .id(ImmersiveWeathering.res("/tuff"))
                .leftInput(lavaCatalyst)
                .rightInput(waterCatalyst, true, s -> s.appendTooltip(
                        Component.translatable("tooltip.immersive_weathering.below").setStyle(style)).appendTooltip(
                        Component.translatable("tooltip.immersive_weathering.sinking").setStyle(style)))
                .output(EmiStack.of(Blocks.TUFF))
                .build());

        registry.addRecipe(EmiWorldInteractionRecipe.builder()
                .id(ImmersiveWeathering.res("/smooth_basalt"))
                .leftInput(lavaCatalyst)
                .rightInput(EmiStack.of(Blocks.BLUE_ICE), true, s -> s.appendTooltip(
                        Component.translatable("tooltip.immersive_weathering.adjacent").setStyle(style)))
                .rightInput(EmiStack.of(Blocks.SOUL_SOIL), true, s -> s.appendTooltip(
                        Component.translatable("tooltip.immersive_weathering.below").setStyle(style)))
                .output(EmiStack.of(Blocks.SMOOTH_BASALT))
                .build());
    }

    private static void safely(String name, Runnable runnable) {
        try {
            runnable.run();
        } catch (Throwable t) {
            ImmersiveWeathering.LOGGER.warn("Exception thrown when reloading {} step in Immersive Weathering EMI plugin", name, t);
        }
    }

}
