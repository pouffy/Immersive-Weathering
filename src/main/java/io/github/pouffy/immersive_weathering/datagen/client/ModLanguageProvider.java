package io.github.pouffy.immersive_weathering.datagen.client;

import io.github.pouffy.immersive_weathering.reg.ModBlocks;
import io.github.pouffy.immersive_weathering.reg.ModCreativeTab;
import io.github.pouffy.immersive_weathering.reg.ModItems;
import io.github.pouffy.immersive_weathering.reg.ModTags;
import net.mehvahdjukaar.moonlight.api.set.leaves.LeavesTypeRegistry;
import net.mehvahdjukaar.moonlight.api.set.wood.WoodTypeRegistry;
import net.minecraft.core.Holder;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.registries.DeferredHolder;
import oshi.util.tuples.Pair;

import java.util.NoSuchElementException;

public class ModLanguageProvider extends LanguageProvider {
    public ModLanguageProvider(PackOutput output, String modid, String locale) {
        super(output, modid, locale);
    }

    @Override
    protected void addTranslations() {
        for (DeferredHolder<Item, ? extends Item> registry : ModItems.getItems()) {
            if (registry.get() instanceof BlockItem) continue;
            this.item(registry);
        }
        for (DeferredHolder<Block, ? extends Block> registry : ModBlocks.getBlocks()) {
            this.block(registry);
        }
        WoodTypeRegistry.INSTANCE.forEach(type -> {
            this.item(ModItems.BARK.get(type).builtInRegistryHolder());
        });
        LeavesTypeRegistry.INSTANCE.forEach(type -> {
            this.block(ModBlocks.LEAF_PILES.get(type).builtInRegistryHolder());
        });

        for (Pair<TagKey<Item>, String> tag : ModTags.ITEM_TAGS) {
            ResourceLocation tagId = tag.getA().location();
            String tagNamespace = tagId.getNamespace().equals("forge") ? "c" : tagId.getNamespace();
            super.add("tag.item.%s.%s".formatted(tagNamespace, tagId.getPath().replace('/', '.')), tag.getB());
        }

        this.tab(ModCreativeTab.MOD_TAB);
    }

    private void tab(Holder<CreativeModeTab> tabHolder) {
        this.add(tabHolder, "itemGroup");
    }

    private void block(Holder<Block> blockHolder) {
        this.add(blockHolder, "block");
    }

    private void item(Holder<Item> itemHolder) {
        this.add(itemHolder, "item");
    }

    private void string(String key, String value) {
        super.add(key, value);
    }

    private void add(Holder<?> holder, String type) {
        ResourceKey<?> resourceKey = holder.unwrapKey().orElseThrow(() -> new NoSuchElementException("No respective key. Check log"));
        ResourceLocation path = resourceKey.location();
        super.add(path.toLanguageKey(type), this.transform(path));
    }

    /**
     * Use to transform a ResourceLocation-form text into a spaced-form text.
     * e.g. example_transform_text -> Example Transform Text
     */
    private String transform(ResourceLocation id) {
        return this.transform(id.getPath());
    }


    /**
     * Use to transform a ResourceLocation-form text into a spaced-form text.
     * e.g. example_transform_text -> Example Transform Text
     */
    private String transform(String path) {
        int pathLength = path.length();
        StringBuilder stringBuilder = new StringBuilder(pathLength).append(Character.toUpperCase(path.charAt(0)));
        for (int i = 1; i < pathLength; i++) {
            char posChar = path.charAt(i);
            if (posChar == '_') {
                stringBuilder.append(' ');
            } else if (path.charAt(i - 1) == '_') {
                stringBuilder.append(Character.toUpperCase(posChar));
            } else stringBuilder.append(posChar);
        }
        return stringBuilder.toString();
    }
}
