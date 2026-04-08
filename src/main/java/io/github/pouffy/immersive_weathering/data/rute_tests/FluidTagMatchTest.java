package io.github.pouffy.immersive_weathering.data.rute_tests;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.pouffy.immersive_weathering.reg.ModRuleTests;
import io.github.pouffy.immersive_weathering.util.StrOpt;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTestType;
import net.minecraft.world.level.material.Fluid;

public class FluidTagMatchTest extends RuleTest {

    public static final MapCodec<FluidTagMatchTest> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            TagKey.codec(Registries.FLUID).fieldOf("tag").forGetter(b -> b.tag),
            StrOpt.of(Codec.FLOAT,"probability",1f).forGetter(b->b.probability)
    ).apply(instance, FluidTagMatchTest::new));

    private final TagKey<Fluid> tag;
    private final float probability;


    public FluidTagMatchTest(TagKey<Fluid> tag, Float chance) {
        this.tag = tag;
        this.probability = chance;
    }

    @Override
    public boolean test(BlockState state, RandomSource random) {
        return state.getFluidState().is(tag) && random.nextFloat() < this.probability;
    }

    @Override
    protected RuleTestType<FluidTagMatchTest> getType() {
        return ModRuleTests.FLUID_TAG_MATCH_TEST.get();
    }
}
