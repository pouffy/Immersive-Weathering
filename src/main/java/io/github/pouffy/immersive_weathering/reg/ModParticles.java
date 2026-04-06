package io.github.pouffy.immersive_weathering.reg;

import io.github.pouffy.immersive_weathering.ImmersiveWeathering;
import io.github.pouffy.immersive_weathering.util.ModUtils;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModParticles {

    public static final DeferredRegister<ParticleType<?>> PARTICLES = ModUtils.createRegister(Registries.PARTICLE_TYPE);

    public static DeferredHolder<ParticleType<?>, SimpleParticleType> registerParticle(String name) {
        return PARTICLES.register(name, () -> new SimpleParticleType(true));
    }

    //custom flower crowns

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> GRAVITY_AZALEA_FLOWER = registerParticle("gravity_azalea_flower");
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> SCRAPE_RUST = registerParticle("scrape_rust");
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> EMBERSPARK = registerParticle("emberspark");
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> MOSS = registerParticle("moss");

    //pride particles
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> AZALEA_FLOWER = registerParticle("azalea_flower");
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> FLOWER_ACE = registerParticle("flower_ace");
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> FLOWER_ARO = registerParticle("flower_aro");
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> FLOWER_BI = registerParticle("flower_bi");
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> FLOWER_ENBY = registerParticle("flower_enby");
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> FLOWER_GAY = registerParticle("flower_gay");
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> FLOWER_LESBIAN = registerParticle("flower_lesbian");
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> FLOWER_RAINBOW = registerParticle("flower_rainbow");
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> FLOWER_TRANS = registerParticle("flower_trans");
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> FLOWER_FLUID = registerParticle("flower_fluid");
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> FLOWER_GENDERQUEER = registerParticle("flower_genderqueer");
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> FLOWER_INTERSEX = registerParticle("flower_intersex");
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> FLOWER_PAN = registerParticle("flower_pan");

    //supporter particles
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> FLOWER_FLAX = registerParticle("flower_flax");
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> FLOWER_NEKOMASTER = registerParticle("flower_nekomaster");
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> FLOWER_AKASHII = registerParticle("flower_akashii");

    //dev and gift particles
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> FLOWER_BEE = registerParticle("flower_bee");
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> FLOWER_JAR = registerParticle("flower_jar");
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> FLOWER_BOB = registerParticle("flower_bob");

    public static void staticInit() {
        ImmersiveWeathering.LOGGER.info("[Immersive Weathering] Particle Registry");
    }
}
