package io.github.pouffy.immersive_weathering.util;

import io.github.pouffy.immersive_weathering.ImmersiveWeathering;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Consumer;
import java.util.function.Function;

public class ModUtils {
    private static final Consumer<?> NO_ACTION = (a) -> {};

    public static ResourceLocation location(String path) {
        return ImmersiveWeathering.res(path);
    }

    public static <DR extends DeferredRegister<T>, T> DR createRegister(Function<String, DR> factory) {
        return registerToBus(factory.apply(ImmersiveWeathering.MOD_ID));
    }

    public static <T> DeferredRegister<T> createRegister(ResourceKey<Registry<T>> registry) {
        return registerToBus(DeferredRegister.create(registry, ImmersiveWeathering.MOD_ID));
    }

    private static <DR extends DeferredRegister<T>, T> DR registerToBus(DR deferredRegister) {
        deferredRegister.register(ImmersiveWeathering.getEventBus());
        return deferredRegister;
    }

    @SuppressWarnings("unchecked")
    public static <T> Consumer<T> noAction() {
        return ((Consumer<T>) NO_ACTION);
    }
}
