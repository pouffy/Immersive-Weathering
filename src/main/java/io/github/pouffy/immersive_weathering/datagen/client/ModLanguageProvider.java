package io.github.pouffy.immersive_weathering.datagen.client;

import com.mojang.datafixers.util.Pair;
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
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.NoSuchElementException;
import java.util.Set;
import java.util.function.Supplier;

public class ModLanguageProvider extends LanguageProvider {
    public ModLanguageProvider(PackOutput output, String modid, String locale) {
        super(output, modid, locale);
    }

    @Override
    protected void addTranslations() {
        Set<Holder<Block>> blockOverrides = Set.of(
                ModBlocks.SAND_LAYER_BLOCK, ModBlocks.RED_SAND_LAYER_BLOCK
        );

        for (DeferredHolder<Item, ? extends Item> registry : ModItems.getItems()) {
            if (registry.get() instanceof BlockItem) continue;
            this.item(registry);
        }
        for (DeferredHolder<Block, ? extends Block> registry : ModBlocks.getBlocks()) {
            if (blockOverrides.contains(registry)) continue;
            this.block(registry);
        }
        WoodTypeRegistry.INSTANCE.forEach(type -> {
            this.item(ModItems.BARK.get(type).builtInRegistryHolder());
        });
        LeavesTypeRegistry.INSTANCE.forEach(type -> {
            this.block(ModBlocks.LEAF_PILES.get(type).builtInRegistryHolder());
        });

        this.addBlock(ModBlocks.SAND_LAYER_BLOCK, "Sand Pile");
        this.addBlock(ModBlocks.RED_SAND_LAYER_BLOCK, "Red Sand Pile");

        for (Pair<TagKey<Item>, String> tag : ModTags.ITEM_TAGS) {
            ResourceLocation tagId = tag.getFirst().location();
            String tagNamespace = tagId.getNamespace().equals("forge") ? "c" : tagId.getNamespace();
            super.add("tag.item.%s.%s".formatted(tagNamespace, tagId.getPath().replace('/', '.')), tag.getSecond());
        }

        this.tab(ModCreativeTab.MOD_TAB);

        for (Pair<Supplier<SoundEvent>, String> entry : ModSoundsProvider.subtitles) {
            ResourceLocation id = entry.getFirst().get().getLocation();
            super.add("%s.subtitle.%s".formatted(id.getNamespace(), id.getPath()), entry.getSecond());
        }

        this.add("tooltip.immersive_weathering.adjacent", "Placed next to flowing lava");
        this.add("tooltip.immersive_weathering.below", "Placed below flowing lava");
        this.add("tooltip.immersive_weathering.rising", "Rising Bubble Column");
        this.add("tooltip.immersive_weathering.sinking", "Sinking Bubble Column");
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
