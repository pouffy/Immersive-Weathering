package io.github.pouffy.immersive_weathering.reg;

import io.github.pouffy.immersive_weathering.ImmersiveWeathering;
import io.github.pouffy.immersive_weathering.api.weathering.spreaders.CrackSpreader;
import io.github.pouffy.immersive_weathering.api.weathering.spreaders.MossSpreader;
import io.github.pouffy.immersive_weathering.api.weathering.spreaders.PatchSpreader;
import io.github.pouffy.immersive_weathering.util.ModUtils;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModSpreaders {
    public static final DeferredRegister<PatchSpreader> SPREADERS = ModUtils.createRegister(ModRegistries.SPREADER);

    public static final DeferredHolder<PatchSpreader, CrackSpreader> CRACK = SPREADERS.register("crack", CrackSpreader::new);
    public static final DeferredHolder<PatchSpreader, MossSpreader> MOSS = SPREADERS.register("moss", MossSpreader::new);

    public static void staticInit() {
        ImmersiveWeathering.LOGGER.info("[Immersive Weathering] Patch Spreaders");
    }
}
