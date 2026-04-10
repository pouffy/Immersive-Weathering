package io.github.pouffy.immersive_weathering.datagen.server;

import io.github.pouffy.immersive_weathering.ImmersiveWeathering;
import io.github.pouffy.immersive_weathering.reg.ModItems;
import io.github.pouffy.immersive_weathering.reg.ModTags;
import net.mehvahdjukaar.moonlight.api.set.leaves.LeavesTypeRegistry;
import net.mehvahdjukaar.moonlight.api.set.wood.WoodTypeRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.neoforged.neoforge.common.Tags;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends ItemTagsProvider {
    public ModItemTagProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pLookupProvider, CompletableFuture<TagLookup<Block>> pBlockTags) {
        super(pOutput, pLookupProvider, pBlockTags);
        ModTags.staticInit();
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        this.registerModTags();
        this.registerCommonTags();
        this.registerMinecraftTags();
        this.registerCompatTags();
    }

    private void registerModTags() {
        var bark = this.tag(ModTags.BARK);
        var scales = this.tag(ModTags.SCALES);
        WoodTypeRegistry.INSTANCE.forEach(type -> {
            if (type.canBurn()) bark.add(ModItems.BARK.get(type));
            else scales.add(ModItems.BARK.get(type));
        });
    }

    private void registerMinecraftTags() {
        this.tag(ItemTags.PIGLIN_LOVED).add(
                ModItems.GOLDEN_MOSS_CLUMP.get(),
                ModItems.ENCHANTED_GOLDEN_MOSS_CLUMP.get());
        this.tag(ItemTags.FLOWERS).add(
                ModItems.AZALEA_FLOWERS.get(),
                ModItems.FLOWER_CROWN.get(),
                ModItems.LEAF_PILES.get(LeavesTypeRegistry.INSTANCE.get(ResourceLocation.withDefaultNamespace("flowering_azalea"))),
                ModItems.AZALEA_FLOWER_PILE.get());
    }

    private void registerCommonTags() {
        this.tag(Tags.Items.BRICKS).add(
                ModItems.DEEPSLATE_BRICK.get(),
                ModItems.STONE_BRICK.get(),
                ModItems.TUFF_BRICK.get(),
                ModItems.END_STONE_BRICK.get(),
                ModItems.BLACKSTONE_BRICK.get(),
                ModItems.PRISMARINE_BRICK.get()
        );
    }

    private void registerCompatTags() {
        this.tag(ItemTags.create(ImmersiveWeathering.res("curios:head"))).add(ModItems.FLOWER_CROWN.get());
        this.tag(ItemTags.create(ImmersiveWeathering.res("trinkets:head/hat"))).add(ModItems.FLOWER_CROWN.get());
    }
}
