package io.github.pouffy.immersive_weathering.datagen;

import io.github.pouffy.immersive_weathering.ImmersiveWeathering;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.PackOutput;
import net.minecraft.data.registries.RegistryPatchGenerator;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class RegistryDataGenerator extends DatapackBuiltinEntriesProvider {

    private static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            //.add(Registries.ENCHANTMENT, MalumEnchantmentDatagen::bootstrap)
            //.add(Registries.DAMAGE_TYPE, MalumDamageTypeDatagen::bootstrap)
            //.add(Registries.STRUCTURE, StructureDatagen::structureBootstrap)
            //.add(Registries.STRUCTURE_SET, StructureDatagen::structureSetBootstrap)
            //.add(Registries.CONFIGURED_FEATURE, ConfiguredFeatureDatagen::bootstrap)
            //.add(Registries.PLACED_FEATURE, PlacedFeatureDatagen::bootstrap)
            //.add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, BiomeModificationDatagen::bootstrap)
            ;

    public RegistryDataGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, RegistryPatchGenerator.createLookup(registries, BUILDER), Set.of("minecraft", ImmersiveWeathering.MOD_ID));
    }
}
