package io.github.pouffy.immersive_weathering.reg;

import io.github.pouffy.immersive_weathering.ImmersiveWeathering;
import io.github.pouffy.immersive_weathering.blocks.LeafPileBlock;
import io.github.pouffy.immersive_weathering.items.*;
import io.github.pouffy.immersive_weathering.util.ModUtils;
import net.mehvahdjukaar.moonlight.api.item.WoodBasedItem;
import net.mehvahdjukaar.moonlight.api.misc.Registrator;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.mehvahdjukaar.moonlight.api.set.BlockSetAPI;
import net.mehvahdjukaar.moonlight.api.set.leaves.LeavesType;
import net.mehvahdjukaar.moonlight.api.set.wood.WoodType;
import net.mehvahdjukaar.moonlight.api.util.Utils;
import net.minecraft.core.Holder;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

@SuppressWarnings("removal")
public class ModItems {
    public static final DeferredRegister.Items ITEMS = ModUtils.createRegister(DeferredRegister::createItems);

    public static <T extends Item> DeferredItem<T> regItem(String name, Supplier<T> itemSup) {
        return ITEMS.register(name, itemSup);
    }

    //helpers

    private static DeferredItem<BlockItem> regLeafPile(String name, Supplier<LeafPileBlock> oakLeafPile) {
        return ITEMS.register(name, () -> new LeafPileBlockItem(oakLeafPile.get(), new Item.Properties()));
    }

    static DeferredItem<BlockItem> regBlockItem(Holder<Block> blockHolder) {
        return regBlockItem(blockHolder, new Item.Properties());
    }

    static DeferredItem<BlockItem> regBlockItem(Holder<Block> blockHolder, Item.Properties properties) {
        return ITEMS.registerSimpleBlockItem(blockHolder, properties);
    }

    //icicle

    public static final DeferredItem<BlockItem> ICICLE = regItem("icicle", () -> new IcicleItem(
            ModBlocks.ICICLE.get(), new Item.Properties().food(ModFoods.ICICLE)));

    //leaf pile
    public static final Map<LeavesType, BlockItem> LEAF_PILES = new LinkedHashMap<>();
    public static final DeferredItem<BlockItem> AZALEA_FLOWER_PILE = regLeafPile("azalea_flower_pile", ModBlocks.AZALEA_FLOWER_PILE);

    //bricks

    public static final DeferredItem<Item> STONE_BRICK = regItem("stone_brick", () ->
            new Item(new Item.Properties()));
    public static final DeferredItem<Item> PRISMARINE_BRICK = regItem("prismarine_brick", () ->
            new Item(new Item.Properties()));
    public static final DeferredItem<Item> END_STONE_BRICK = regItem("end_stone_brick", () ->
            new Item(new Item.Properties()));
    public static final DeferredItem<Item> BLACKSTONE_BRICK = regItem("blackstone_brick", () ->
            new Item(new Item.Properties()));
    public static final DeferredItem<Item> DEEPSLATE_BRICK = regItem("deepslate_brick", () ->
            new Item(new Item.Properties()));
    public static final DeferredItem<Item> DEEPSLATE_TILE = regItem("deepslate_tile", () ->
            new Item(new Item.Properties()));
    public static final DeferredItem<Item> MORTAR = regItem("mortar", () ->
            new Item(new Item.Properties()));

    //flowers

    public static final DeferredItem<Item> AZALEA_FLOWERS = regItem("azalea_flowers", () ->
            new AzaleaFlowersItem(new Item.Properties().food(ModFoods.AZALEA_FLOWER)));

    public static final DeferredItem<Item> FLOWER_CROWN = regItem("flower_crown", () ->
            new FlowerCrownItem(ModArmorMaterials.FLOWER_CROWN, ArmorItem.Type.HELMET,
                    new Item.Properties().durability(ArmorItem.Type.HELMET.getDurability(64))));

    public static final DeferredItem<Item> MOSS_CLUMP = regItem("moss_clump", () ->
            new MossClumpItem(ModBlocks.MOSS.get(), new Item.Properties().food(ModFoods.MOSS_CLUMP)));

    public static final DeferredItem<Item> GOLDEN_MOSS_CLUMP = regItem("golden_moss_clump", () ->
            new Item(new Item.Properties().food(ModFoods.GOLDEN_MOSS_CLUMP)));

    public static final DeferredItem<Item> ENCHANTED_GOLDEN_MOSS_CLUMP = regItem("enchanted_golden_moss_clump", () ->
            new EnchantedGoldenMossClumpItem(new Item.Properties()
                    .rarity(Rarity.EPIC).food(ModFoods.ENCHANTED_GOLDEN_MOSS_CLUMP)));

    //bark

    public static final Map<WoodType, Item> BARK = new LinkedHashMap<>();

    public static final DeferredItem<Item> TALLOW = regItem("tallow",
            () -> new HoneycombItem(new Item.Properties()));

    public static final DeferredItem<Item> STEEL_WOOL = regItem("steel_wool", () ->
            new Item(new Item.Properties().durability(128)));

    public static final DeferredItem<Item> ICE_SICKLE = regItem("ice_sickle", () ->
            new IceSickleItem(ModToolMaterials.ICICLE, new Item.Properties().attributes(SwordItem.createAttributes(ModToolMaterials.ICICLE, 5, -1f))));

    public static final DeferredItem<Item> THIN_ICE_ITEM = regItem("thin_ice", () ->
            new ThinIceItem(ModBlocks.THIN_ICE.get(), new Item.Properties()));

    public static final DeferredItem<Item> FROST_ITEM = regItem("frost", () ->
            new FrostItem(ModBlocks.FROST.get(), new Item.Properties()));

    public static final DeferredItem<Item> FIRE = regItem("fire", () ->
            new BlockItem(Blocks.FIRE, new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> SOUL_FIRE = regItem("soul_fire", () ->
            new BlockItem(Blocks.SOUL_FIRE, new Item.Properties().stacksTo(1)));


    private static void registerBark(Registrator<Item> event, Collection<WoodType> woodTypes) {
        for (WoodType type : woodTypes) {
            String name = !type.canBurn() ? type.getVariantId("scales", false) : type.getVariantId("bark", false);

            Item item = new ModWoodBasedItem(new Item.Properties(), type, 200);
            event.register(ImmersiveWeathering.res(name), item);
            BARK.put(type, item);
            type.addChild("immersive_weathering:bark", item);
        }
    }

    private static void registerLeafPilesItems(Registrator<Item> event, Collection<LeavesType> leavesTypes) {
        for (LeavesType type : leavesTypes) {
            var b = ModBlocks.LEAF_PILES.get(type);
            BlockItem i = new LeafPileBlockItem(b, new Item.Properties());
            event.register(Utils.getID(b), i);
            LEAF_PILES.put(type, i);
        }
    }

    public static void staticInit() {
        ImmersiveWeathering.LOGGER.info("[Immersive Weathering] Item Registry");
        BlockSetAPI.addDynamicItemRegistration(ModItems::registerLeafPilesItems, LeavesType.class);
        BlockSetAPI.addDynamicItemRegistration(ModItems::registerBark, WoodType.class);
    }

    public static Collection<DeferredHolder<Item, ? extends Item>> getItems() {
        return ITEMS.getEntries();
    }
}
