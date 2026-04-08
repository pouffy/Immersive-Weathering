package io.github.pouffy.immersive_weathering.reg;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import io.github.pouffy.immersive_weathering.ImmersiveWeathering;
import io.github.pouffy.immersive_weathering.data.rute_tests.*;
import io.github.pouffy.immersive_weathering.util.ModUtils;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTestType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModRuleTests {
    public static final DeferredRegister<RuleTestType<?>> RULE_TESTS = ModUtils.createRegister(Registries.RULE_TEST);



    public static final DeferredHolder<RuleTestType<?>, RuleTestType<BlockPropertyTest>> BLOCK_PROPERTY_TEST = register(
            "block_property_test", BlockPropertyTest.CODEC);
    public static final DeferredHolder<RuleTestType<?>, RuleTestType<BlockSetMatchTest>> BLOCK_SET_MATCH_TEST = register(
            "block_set_match", BlockSetMatchTest.CODEC);
    public static final DeferredHolder<RuleTestType<?>, RuleTestType<BurnableTest>> BURNABLE_TEST = register(
            "burnable_test", BurnableTest.CODEC);
    public static final DeferredHolder<RuleTestType<?>, RuleTestType<FluidMatchTest>> FLUID_MATCH_TEST = register(
            "fluid_match", FluidMatchTest.CODEC);
    public static final DeferredHolder<RuleTestType<?>, RuleTestType<FluidTagMatchTest>> FLUID_TAG_MATCH_TEST = register(
            "fluid_tag_match", FluidTagMatchTest.CODEC);
    public static final DeferredHolder<RuleTestType<?>, RuleTestType<LogMatchTest>> LOG_TEST = register(
            "tree_log", LogMatchTest.CODEC);


    private static <P extends RuleTest> DeferredHolder<RuleTestType<?>, RuleTestType<P>> register(String name, MapCodec<P> codec) {
        return RULE_TESTS.register(name, () -> RuleTestType.register(name, codec));
    }

    public static void staticInit() {
        ImmersiveWeathering.LOGGER.info("[Immersive Weathering] Rule Test Registry");
    }
}
