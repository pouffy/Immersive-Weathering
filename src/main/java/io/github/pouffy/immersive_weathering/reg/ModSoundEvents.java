package io.github.pouffy.immersive_weathering.reg;

import io.github.pouffy.immersive_weathering.ImmersiveWeathering;
import io.github.pouffy.immersive_weathering.util.ModUtils;
import net.mehvahdjukaar.moonlight.api.misc.RegSupplier;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class ModSoundEvents {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = ModUtils.createRegister(Registries.SOUND_EVENT);

    public static DeferredHolder<SoundEvent, SoundEvent> ICICLE_CRACK = registerSoundEvent("icicle_crack");

    @NotNull
    private static DeferredHolder<SoundEvent, SoundEvent> registerSoundEvent(@NotNull String name) {
        ResourceLocation id = ImmersiveWeathering.res(name);
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(id));
    }

    public static void staticInit() {
        ImmersiveWeathering.LOGGER.info("[Immersive Weathering] Sound Event Registry");
    }
}
