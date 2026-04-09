package io.github.pouffy.immersive_weathering.data.block_growths.growths.builtin;

import io.github.pouffy.immersive_weathering.data.block_growths.growths.ConfigurableBlockGrowth;
import io.github.pouffy.immersive_weathering.data.block_growths.growths.IBlockGrowth;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class BuiltinGrowthsRegistry {

    public static void register(){}

    private static final Map<String, IBlockGrowth.Type<?>> TYPES;

    static {
        TYPES = new HashMap<>();
        TYPES.put(ConfigurableBlockGrowth.TYPE.name(), ConfigurableBlockGrowth.TYPE);
        TYPES.put(CampfireSootGrowth.TYPE.name(), CampfireSootGrowth.TYPE);
        TYPES.put(FireSootGrowth.TYPE.name(), FireSootGrowth.TYPE);
        TYPES.put(GrassBurnGrowth.TYPE.name(), GrassBurnGrowth.TYPE);
        TYPES.put(IceGrowth.TYPE.name(), IceGrowth.TYPE);
        TYPES.put(LeavesGrowth.TYPE.name(), LeavesGrowth.TYPE);
        TYPES.put(SnowIcicleGrowth.TYPE.name(), SnowIcicleGrowth.TYPE);
        TYPES.put(LightningGrowth.TYPE.name(), LightningGrowth.TYPE);
        TYPES.put(SnowGrowth.TYPE.name(), SnowGrowth.TYPE);
        TYPES.put(SandGrowth.TYPE.name(), SandGrowth.TYPE);
        TYPES.put(SandLayerGrowth.TYPE.name(), SandLayerGrowth.TYPE);
    }

    public static Optional<? extends IBlockGrowth.Type<? extends IBlockGrowth>> get(String name) {
        return Optional.ofNullable(TYPES.get(name));
    }

    public static <B extends IBlockGrowth.Type<?>> B register(B newType){
        TYPES.put(newType.name(), newType);
        return newType;
    }
}
