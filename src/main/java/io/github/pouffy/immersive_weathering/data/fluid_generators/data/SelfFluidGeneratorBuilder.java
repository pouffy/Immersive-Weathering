package io.github.pouffy.immersive_weathering.data.fluid_generators.data;

import io.github.pouffy.immersive_weathering.data.fluid_generators.IFluidGenerator;
import io.github.pouffy.immersive_weathering.data.fluid_generators.SelfFluidGenerator;
import io.github.pouffy.immersive_weathering.data.position_tests.AndTest;
import io.github.pouffy.immersive_weathering.data.position_tests.IPositionRuleTest;
import io.github.pouffy.immersive_weathering.data.position_tests.NandTest;
import io.github.pouffy.immersive_weathering.data.position_tests.OrTest;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;

import java.util.Arrays;
import java.util.Optional;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class SelfFluidGeneratorBuilder {

    private final Fluid fluid;
    private  IFluidGenerator.FluidType fluidType = IFluidGenerator.FluidType.BOTH;
    private final BlockState growth;
    private Optional<IPositionRuleTest> positionTests = Optional.empty();
    private int priority = 0;
    private final AdjacentBlocksBuilder adjacentCondition;

    public SelfFluidGeneratorBuilder(Fluid fluid, BlockState growth, AdjacentBlocksBuilder adjacentCondition) {
        this.fluid = fluid;
        this.growth = growth;
        this.adjacentCondition = adjacentCondition;
    }

    public SelfFluidGeneratorBuilder priority(int priority) {
        this.priority = priority;
        return this;
    }

    public SelfFluidGeneratorBuilder type(IFluidGenerator.FluidType fluidType) {
        this.fluidType = fluidType;
        return this;
    }

    public SelfFluidGeneratorBuilder flowing() {
        this.fluidType = IFluidGenerator.FluidType.FLOWING;
        return this;
    }

    public SelfFluidGeneratorBuilder still() {
        this.fluidType = IFluidGenerator.FluidType.STILL;
        return this;
    }

    public SelfFluidGeneratorBuilder check(IPositionRuleTest check) {
        this.positionTests = Optional.of(check);
        return this;
    }

    public SelfFluidGeneratorBuilder checkAll(IPositionRuleTest... checks) {
        this.positionTests = Optional.of(new AndTest(Arrays.asList(checks)));
        return this;
    }

    public SelfFluidGeneratorBuilder checkAny(IPositionRuleTest... checks) {
        this.positionTests = Optional.of(new OrTest(Arrays.asList(checks)));
        return this;
    }

    public SelfFluidGeneratorBuilder checkNone(IPositionRuleTest... checks) {
        this.positionTests = Optional.of(new NandTest(Arrays.asList(checks)));
        return this;
    }

    public SelfFluidGenerator build() {
        return new SelfFluidGenerator(this.fluid, this.fluidType, this.growth, this.adjacentCondition.build(), this.positionTests, this.priority);
    }

    public void save(GeneratorOutput output, String id) {
        save(output, ResourceLocation.parse(id));
    }

    public void save(GeneratorOutput output, ResourceLocation recipeId) {
        SelfFluidGenerator generator = this.build();
        output.accept(recipeId, generator);
    }
}
