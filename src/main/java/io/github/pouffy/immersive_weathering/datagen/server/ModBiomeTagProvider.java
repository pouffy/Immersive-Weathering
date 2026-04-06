package io.github.pouffy.immersive_weathering.datagen.server;

import io.github.pouffy.immersive_weathering.reg.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biomes;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

@SuppressWarnings("unchecked")
public class ModBiomeTagProvider extends BiomeTagsProvider {
    public ModBiomeTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, provider, modId, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(ModTags.HAS_DUNE_GRASS).add(Biomes.DESERT, Biomes.SAVANNA_PLATEAU, Biomes.SAVANNA, Biomes.BEACH);
        this.tag(ModTags.HAS_EARTHEN_CLAY).addTags(BiomeTags.IS_SAVANNA, BiomeTags.IS_BADLANDS);
        this.tag(ModTags.HAS_IVY).addTag(Tags.Biomes.IS_FOREST).add(Biomes.FOREST, Biomes.WINDSWEPT_FOREST, Biomes.FLOWER_FOREST, Biomes.DARK_FOREST, Biomes.BIRCH_FOREST, Biomes.OLD_GROWTH_BIRCH_FOREST);
        this.tag(ModTags.HAS_LAKEBED).addTag(Tags.Biomes.IS_BADLANDS).add(Biomes.BADLANDS, Biomes.ERODED_BADLANDS, Biomes.WOODED_BADLANDS, Biomes.DESERT);
        this.tag(ModTags.HAS_LOAM).addTag(Tags.Biomes.IS_JUNGLE).add(Biomes.DARK_FOREST, Biomes.LUSH_CAVES);
        this.tag(ModTags.HAS_MOSS).add(Biomes.LUSH_CAVES);
        this.tag(ModTags.HAS_PERMAFROST).add(Biomes.SNOWY_SLOPES, Biomes.SNOWY_PLAINS, Biomes.SNOWY_TAIGA, Biomes.FROZEN_PEAKS, Biomes.ICE_SPIKES, Biomes.GROVE).addTag(Tags.Biomes.IS_TAIGA);
        this.tag(ModTags.HAS_SANDSTORM).add(Biomes.DESERT, Biomes.WARM_OCEAN).addTag(Tags.Biomes.IS_DESERT);
        this.tag(ModTags.HAS_SANDY_DIRT).addTags(Tags.Biomes.IS_SAVANNA, Tags.Biomes.IS_BEACH).add(Biomes.DESERT);
        this.tag(ModTags.HAS_SILT).add(Biomes.SWAMP, Biomes.MANGROVE_SWAMP).addTag(Tags.Biomes.IS_RIVER);
        this.tag(ModTags.HOT).add(Biomes.BADLANDS, Biomes.WOODED_BADLANDS, Biomes.ERODED_BADLANDS, Biomes.SAVANNA, Biomes.WINDSWEPT_SAVANNA, Biomes.SAVANNA_PLATEAU, Biomes.DESERT).addTag(Tags.Biomes.IS_HOT_OVERWORLD).addTag(Tags.Biomes.IS_DRY_OVERWORLD);
        this.tag(ModTags.ICY).add(Biomes.ICE_SPIKES, Biomes.FROZEN_PEAKS, Biomes.JAGGED_PEAKS, Biomes.SNOWY_SLOPES, Biomes.SNOWY_PLAINS, Biomes.SNOWY_TAIGA, Biomes.FROZEN_OCEAN, Biomes.DEEP_FROZEN_OCEAN, Biomes.FROZEN_RIVER).addTag(Tags.Biomes.IS_SNOWY).addTag(Tags.Biomes.IS_ICY);
        this.tag(ModTags.UNDERGROUND_DESERT).add(Biomes.DESERT).addTag(Tags.Biomes.IS_DESERT);
        this.tag(ModTags.WET).add(Biomes.SWAMP, Biomes.JUNGLE, Biomes.SPARSE_JUNGLE).addTag(Tags.Biomes.IS_WET_OVERWORLD);
    }
}
