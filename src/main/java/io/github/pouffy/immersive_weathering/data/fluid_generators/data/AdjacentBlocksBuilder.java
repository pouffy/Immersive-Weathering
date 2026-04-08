package io.github.pouffy.immersive_weathering.data.fluid_generators.data;

import io.github.pouffy.immersive_weathering.data.fluid_generators.SelfFluidGenerator;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class AdjacentBlocksBuilder {

    private List<RuleTest> anyBlocks = new ArrayList<>();
    private List<RuleTest> sidesBlocks = new ArrayList<>();
    private Optional<RuleTest> upBlock = Optional.empty();
    private Optional<RuleTest> downBlock = Optional.empty();

    public AdjacentBlocksBuilder() {}

    public static AdjacentBlocksBuilder builder() {
        return new AdjacentBlocksBuilder();
    }

    public AdjacentBlocksBuilder any(List<RuleTest> anyBlocks) {
        this.anyBlocks = anyBlocks;
        return this;
    }

    public AdjacentBlocksBuilder any(RuleTest... anyBlocks) {
        this.anyBlocks.addAll(Arrays.asList(anyBlocks));
        return this;
    }

    public AdjacentBlocksBuilder sides(List<RuleTest> sidesBlocks) {
        this.sidesBlocks = sidesBlocks;
        return this;
    }

    public AdjacentBlocksBuilder sides(RuleTest... sidesBlocks) {
        this.sidesBlocks.addAll(Arrays.asList(sidesBlocks));
        return this;
    }

    public AdjacentBlocksBuilder up(RuleTest upBlock) {
        this.upBlock = Optional.of(upBlock);
        return this;
    }

    public AdjacentBlocksBuilder down(RuleTest downBlock) {
        this.downBlock = Optional.of(downBlock);
        return this;
    }

    public SelfFluidGenerator.AdjacentBlocks build() {
        return new SelfFluidGenerator.AdjacentBlocks(this.anyBlocks, this.sidesBlocks, this.upBlock, this.downBlock);
    }
}
