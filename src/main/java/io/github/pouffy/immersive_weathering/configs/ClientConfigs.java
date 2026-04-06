package io.github.pouffy.immersive_weathering.configs;

import io.github.pouffy.immersive_weathering.ImmersiveWeathering;
import net.mehvahdjukaar.moonlight.api.platform.configs.ConfigBuilder;
import net.mehvahdjukaar.moonlight.api.platform.configs.ConfigType;
import net.mehvahdjukaar.moonlight.api.platform.configs.ModConfigHolder;

import java.util.function.Supplier;

public class ClientConfigs {
    public static final ModConfigHolder CONFIG;

    public static Supplier<Boolean> LEAF_DECAY_PARTICLES;
    public static Supplier<Boolean> FALLING_LEAF_PARTICLES;
    public static Supplier<Boolean> LEAF_DECAY_SOUND;

    public static Supplier<Float> FALLING_LEAF_PARTICLE_RATE;
    public static Supplier<Float> RAINY_FALLING_LEAF_PARTICLE_RATE;
    public static Supplier<Float> STORMY_FALLING_LEAF_PARTICLE_RATE;

    public static void init() {
    }

    static {
        ConfigBuilder builder = ConfigBuilder.create(ImmersiveWeathering.MOD_ID, ConfigType.CLIENT);

        builder.push("general");
        LEAF_DECAY_PARTICLES = builder.define("leaves_decay_particles", true);
        FALLING_LEAF_PARTICLES = builder.define("falling_leaf_particles", true);
        LEAF_DECAY_SOUND = builder.define("decay_sound", true);

        FALLING_LEAF_PARTICLE_RATE = builder.define("falling_leaf_rate", 0.08f, 0f, 1f);
        RAINY_FALLING_LEAF_PARTICLE_RATE = builder.define("rainy_falling_leaf_rate", 0.2f, 0f, 1f);
        STORMY_FALLING_LEAF_PARTICLE_RATE = builder.define("stormy_falling_leaf_rate", 0.4f, 0f, 1f);
        builder.pop();

        CONFIG = builder.build();
        CONFIG.forceLoad();
    }
}
