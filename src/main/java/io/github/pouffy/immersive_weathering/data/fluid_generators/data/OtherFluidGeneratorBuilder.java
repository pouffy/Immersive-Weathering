package io.github.pouffy.immersive_weathering.data.fluid_generators.data;

import io.github.pouffy.immersive_weathering.data.fluid_generators.IFluidGenerator;
import io.github.pouffy.immersive_weathering.data.fluid_generators.OtherFluidGenerator;
import io.github.pouffy.immersive_weathering.data.position_tests.AndTest;
import io.github.pouffy.immersive_weathering.data.position_tests.IPositionRuleTest;
import io.github.pouffy.immersive_weathering.data.position_tests.NandTest;
import io.github.pouffy.immersive_weathering.data.position_tests.OrTest;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.material.Fluid;

import java.util.Arrays;
import java.util.Optional;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class OtherFluidGeneratorBuilder {

    private final Fluid fluid;
    private  IFluidGenerator.FluidType fluidType = IFluidGenerator.FluidType.BOTH;
    private final BlockState growth;
    private final RuleTest target;
    private Optional<IPositionRuleTest> extraCheck = Optional.empty();
    private int priority = 0;

    public OtherFluidGeneratorBuilder(Fluid fluid, BlockState growth, RuleTest target) {
        this.fluid = fluid;
        this.growth = growth;
        this.target = target;
    }

    public OtherFluidGeneratorBuilder priority(int priority) {
        this.priority = priority;
        return this;
    }

    public OtherFluidGeneratorBuilder type(IFluidGenerator.FluidType fluidType) {
        this.fluidType = fluidType;
        return this;
    }

    public OtherFluidGeneratorBuilder flowing() {
        this.fluidType = IFluidGenerator.FluidType.FLOWING;
        return this;
    }

    public OtherFluidGeneratorBuilder still() {
        this.fluidType = IFluidGenerator.FluidType.STILL;
        return this;
    }

    public OtherFluidGeneratorBuilder check(IPositionRuleTest check) {
        this.extraCheck = Optional.of(check);
        return this;
    }

    public OtherFluidGeneratorBuilder checkAll(IPositionRuleTest... checks) {
        this.extraCheck = Optional.of(new AndTest(Arrays.asList(checks)));
        return this;
    }

    public OtherFluidGeneratorBuilder checkAny(IPositionRuleTest... checks) {
        this.extraCheck = Optional.of(new OrTest(Arrays.asList(checks)));
        return this;
    }

    public OtherFluidGeneratorBuilder checkNone(IPositionRuleTest... checks) {
        this.extraCheck = Optional.of(new NandTest(Arrays.asList(checks)));
        return this;
    }

    public OtherFluidGenerator build() {
        return new OtherFluidGenerator(this.fluid, this.fluidType, this.growth, this.target, this.extraCheck, this.priority);
    }

    public void save(GeneratorOutput output, String id) {
        save(output, ResourceLocation.parse(id));
    }

    public void save(GeneratorOutput output, ResourceLocation recipeId) {
        OtherFluidGenerator generator = this.build();
        output.accept(recipeId, generator);
    }
}
