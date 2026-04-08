package io.github.pouffy.immersive_weathering.datagen;

import io.github.pouffy.immersive_weathering.ImmersiveWeathering;
import io.github.pouffy.immersive_weathering.datagen.client.ModBlockStateProvider;
import io.github.pouffy.immersive_weathering.datagen.client.ModItemModelProvider;
import io.github.pouffy.immersive_weathering.datagen.client.ModLanguageProvider;
import io.github.pouffy.immersive_weathering.datagen.server.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

public class ImmersiveWeatheringDataGen {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> provider = event.getLookupProvider();
        ExistingFileHelper helper = event.getExistingFileHelper();

        final boolean includeClient = event.includeClient();
        final boolean includeServer = event.includeServer();

        var registryDataDatagen = new RegistryDataGenerator(output, provider);
        var registryProvider = registryDataDatagen.getRegistryProvider();

        generator.addProvider(includeServer, registryDataDatagen);

        var dataMaps = new ModDataMapProvider(output, registryProvider);

        ModLanguageProvider language = new ModLanguageProvider(output, ImmersiveWeathering.MOD_ID, "en_us");
        ModBlockTagProvider blockTags = new ModBlockTagProvider(output, registryProvider, ImmersiveWeathering.MOD_ID, helper);
        ModItemTagProvider itemTags = new ModItemTagProvider(output, registryProvider, blockTags.contentsGetter());
        ModBiomeTagProvider biomeTags = new ModBiomeTagProvider(output, provider, ImmersiveWeathering.MOD_ID, helper);

        var blockGrowths = new ModBlockGrowths(output, registryProvider);
        var fluidGenerators = new ModFluidGenerators(output, registryProvider);

        ModItemModelProvider itemModels = new ModItemModelProvider(output, ImmersiveWeathering.MOD_ID, helper);
        ModBlockStateProvider blockStates = new ModBlockStateProvider(output, ImmersiveWeathering.MOD_ID, helper);

        generator.addProvider(includeClient, blockTags);
        generator.addProvider(includeServer, itemTags);
        generator.addProvider(includeServer, biomeTags);
        generator.addProvider(includeServer, dataMaps);
        generator.addProvider(includeClient, blockGrowths);
        generator.addProvider(includeClient, fluidGenerators);

        generator.addProvider(includeClient, itemModels);
        generator.addProvider(includeClient, blockStates);

        generator.addProvider(includeClient && includeServer, language);
    }
}
