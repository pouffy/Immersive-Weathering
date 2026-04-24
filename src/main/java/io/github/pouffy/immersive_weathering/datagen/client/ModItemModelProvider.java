package io.github.pouffy.immersive_weathering.datagen.client;

import io.github.pouffy.immersive_weathering.ImmersiveWeathering;
import io.github.pouffy.immersive_weathering.reg.ModBlocks;
import io.github.pouffy.immersive_weathering.reg.ModItems;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.function.Supplier;

public class ModItemModelProvider extends ItemModelProvider {

    public ModItemModelProvider(PackOutput output, String modid, ExistingFileHelper existingFileHelper) {
        super(output, modid, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicItem(ModItems.MOSS_CLUMP.get());
        basicItem(ModItems.GOLDEN_MOSS_CLUMP.get());
        basicItem(ModItems.ENCHANTED_GOLDEN_MOSS_CLUMP, "immersive_weathering:item/golden_moss_clump");

        ModItems.BARK.values().forEach(this::basicItem);

        basicItem(ModItems.AZALEA_FLOWERS.get());
        basicItem(ModItems.FROST_ITEM, "immersive_weathering:block/frost");
        basicItem(ModItems.FIRE, "minecraft:block/fire_0");
        basicItem(ModItems.SOUL_FIRE, "minecraft:block/soul_fire_0");

        basicItem(ModItems.TALLOW.get());
        basicItem(ModItems.STEEL_WOOL.get());
        flowerCrown(ModItems.FLOWER_CROWN);

        basicItem(ModItems.STONE_BRICK.get());
        basicItem(ModItems.TUFF_BRICK.get());
        basicItem(ModItems.PRISMARINE_BRICK.get());
        basicItem(ModItems.END_STONE_BRICK.get());
        basicItem(ModItems.BLACKSTONE_BRICK.get());
        basicItem(ModItems.DEEPSLATE_BRICK.get());
        basicItem(ModItems.DEEPSLATE_TILE.get());
        basicItem(ModItems.MORTAR.get());

        handheldRod(ModItems.ICE_SICKLE);

        this.basicItem(ModBlocks.FROSTY_GRASS::asItem, "immersive_weathering:item/frosty_grass_item");
        this.basicItem(ModBlocks.FROSTY_FERN::asItem, "immersive_weathering:item/frosty_fern_item");
        this.basicItem(ModBlocks.WEEDS::asItem, "immersive_weathering:block/weeds_0");
        this.basicItem(ModBlocks.IVY::asItem, "immersive_weathering:item/ivy_item");
        this.basicItem(ModBlocks.DUNE_GRASS::asItem, "immersive_weathering:item/dune_grass");
        this.basicItem(ModBlocks.SOOT::asItem, "immersive_weathering:block/soot");
        this.basicItem(ModBlocks.FROSTY_GLASS_PANE::asItem, "immersive_weathering:block/frosty_glass");
        this.basicItem(ModBlocks.TINTED_GLASS_PANE::asItem, "minecraft:block/tinted_glass");
        this.basicItem(ModBlocks.SAND_LAYER_BLOCK::asItem, "immersive_weathering:item/sand");
        this.basicItem(ModBlocks.RED_SAND_LAYER_BLOCK::asItem, "immersive_weathering:item/red_sand");

        this.withExistingParent("sandy_dirt", "immersive_weathering:block/sandy_dirt");
        this.withExistingParent("grassy_sandy_dirt", "immersive_weathering:block/grassy_sandy_dirt");
        this.withExistingParent("silt", "immersive_weathering:block/silt");
        this.withExistingParent("grassy_silt", "immersive_weathering:block/grassy_silt");
        this.withExistingParent("grassy_permafrost", "immersive_weathering:block/grassy_permafrost");
        this.withExistingParent("thin_ice", "immersive_weathering:block/thin_ice_inventory");

        this.withExistingParent("mulch_block", "immersive_weathering:block/mulch_block");
        this.withExistingParent("nulch_block", "immersive_weathering:block/nulch_block");
        this.withExistingParent("rooted_grass_block", "immersive_weathering:block/rooted_grass_block");

        basicItem(ModBlocks.FULGURITE::asItem, "immersive_weathering:block/fulgurite").transforms()
                .transform(ItemDisplayContext.HEAD)
                .translation(0, 14, -5).end();

        basicItem(ModBlocks.ICICLE.asItem()).transforms()
                .transform(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND)
                .rotation(0, 100, 0).translation(-1, -1, 0).scale(0.9f).end()
                .transform(ItemDisplayContext.FIRST_PERSON_LEFT_HAND)
                .rotation(0, 100, 0).translation(0, -2, 0).scale(0.9f).end();
    }

    private ItemModelBuilder basicItem(Supplier<? extends Item> item, String texture, String nameSuffix) {
        String name = BuiltInRegistries.ITEM.getKey(item.get()).getPath();
        return getBuilder(name + nameSuffix)
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", texture);
    }

    private ItemModelBuilder basicItem(Supplier<? extends Item> item, String texture) {
        return basicItem(item, texture, "");
    }

    private ItemModelBuilder handheldRod(Supplier<Item> item) {
        var key = BuiltInRegistries.ITEM.getKey(item.get());
        return this.getBuilder(itemName(item.get())).parent(new ModelFile.UncheckedModelFile("item/handheld_rod")).texture("layer0", ResourceLocation.fromNamespaceAndPath(key.getNamespace(), "item/" + key.getPath()));
    }

    private ItemModelBuilder flowerCrown(Supplier<? extends Item> item) {
        String name = itemName(item.get());
        return getBuilder(name)
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", ImmersiveWeathering.res("item/" + name))
                .override().predicate(ImmersiveWeathering.res("supporter"), 0.10f).model(crown(name, "bee")).end()
                .override().predicate(ImmersiveWeathering.res("supporter"), 0.11f).model(crown(name, "jar")).end()
                .override().predicate(ImmersiveWeathering.res("supporter"), 0.12f).model(crown(name, "bob")).end()
                .override().predicate(ImmersiveWeathering.res("supporter"), 0.200f).model(crown(name, "ace")).end()
                .override().predicate(ImmersiveWeathering.res("supporter"), 0.201f).model(crown(name, "aro")).end()
                .override().predicate(ImmersiveWeathering.res("supporter"), 0.202f).model(crown(name, "bi")).end()
                .override().predicate(ImmersiveWeathering.res("supporter"), 0.203f).model(crown(name, "enby")).end()
                .override().predicate(ImmersiveWeathering.res("supporter"), 0.204f).model(crown(name, "gay")).end()
                .override().predicate(ImmersiveWeathering.res("supporter"), 0.205f).model(crown(name, "lesbian")).end()
                .override().predicate(ImmersiveWeathering.res("supporter"), 0.206f).model(crown(name, "rainbow")).end()
                .override().predicate(ImmersiveWeathering.res("supporter"), 0.207f).model(crown(name, "trans")).end()
                .override().predicate(ImmersiveWeathering.res("supporter"), 0.208f).model(crown(name, "pan")).end()
                .override().predicate(ImmersiveWeathering.res("supporter"), 0.209f).model(crown(name, "intersex")).end()
                .override().predicate(ImmersiveWeathering.res("supporter"), 0.210f).model(crown(name, "genderqueer")).end()
                .override().predicate(ImmersiveWeathering.res("supporter"), 0.211f).model(crown(name, "fluid")).end()
                .override().predicate(ImmersiveWeathering.res("supporter"), 0.30f).model(crown(name, "flax")).end()
                .override().predicate(ImmersiveWeathering.res("supporter"), 0.31f).model(crown(name, "nekomaster")).end()
                .override().predicate(ImmersiveWeathering.res("supporter"), 0.32f).model(crown(name, "akashii")).end()
                ;
    }

    private ItemModelBuilder crown(String name, String variant) {
        return getBuilder(name + "_" + variant)
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", ImmersiveWeathering.res("item/%s_%s".formatted(name, variant)));
    }

    public String itemName(Item item) {
        ResourceLocation location = BuiltInRegistries.ITEM.getKey(item);
        if (location != null) {
            return location.getPath();
        } else {
            throw new IllegalStateException("Unknown item: " + item.toString());
        }
    }
}
