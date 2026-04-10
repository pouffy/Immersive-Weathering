package io.github.pouffy.immersive_weathering.reg;

import com.mojang.serialization.Lifecycle;
import io.github.pouffy.immersive_weathering.ImmersiveWeathering;
import io.github.pouffy.immersive_weathering.api.weathering.spreaders.PatchSpreader;
import io.github.pouffy.immersive_weathering.api.weathering.operators.WeatheringOperator;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;

@EventBusSubscriber(modid = ImmersiveWeathering.MOD_ID)
public class ModRegistries {

    public static final ResourceKey<Registry<PatchSpreader>> SPREADER = ModRegistries.createRegistryKey("spreaders");
    public static final ResourceKey<Registry<WeatheringOperator>> WEATHERING_OPERATOR = ModRegistries.createRegistryKey("weathering_operators");

    public static final Registry<PatchSpreader> SPREADER_REGISTRY = ModRegistries.makeSyncedRegistry(SPREADER);
    public static final Registry<WeatheringOperator> WEATHERING_OPERATOR_REGISTRY = ModRegistries.makeSyncedRegistry(WEATHERING_OPERATOR);

    private static <T> ResourceKey<Registry<T>> createRegistryKey(String name) {
        return ResourceKey.createRegistryKey(ImmersiveWeathering.res(name));
    }

    /**
     * Creates a {@link Registry} that get synchronised to clients.
     *
     * @param <T> the entry of the registry.
     */
    private static <T> Registry<T> makeSyncedRegistry(ResourceKey<Registry<T>> registryKey) {
        return new RegistryBuilder<>(registryKey).sync(true).create();
    }

    /**
     * Creates a simple {@link Registry} that <B>won't</B> be synced to clients.
     *
     * @param <T> the entry of the registry.
     */
    private static <T> Registry<T> makeRegistry(ResourceKey<Registry<T>> registryKey) {
        return new RegistryBuilder<>(registryKey).create();
    }
    private static <T> Registry<T> registerSimpleWithIntrusiveHolders(ResourceKey<? extends Registry<T>> registryKey) {
        return new MappedRegistry<>(registryKey, Lifecycle.stable(), true);
    }

    @SubscribeEvent
    public static void newRegistry(NewRegistryEvent event) {
        event.register(SPREADER_REGISTRY);
        event.register(WEATHERING_OPERATOR_REGISTRY);
    }

    public static void staticInit() {

    }
}
