package io.github.pouffy.immersive_weathering.data.block_growths.data;

import io.github.pouffy.immersive_weathering.data.block_growths.BlockPair;
import io.github.pouffy.immersive_weathering.data.block_growths.TickSource;
import io.github.pouffy.immersive_weathering.data.block_growths.area_condition.AreaCondition;
import io.github.pouffy.immersive_weathering.data.block_growths.growths.ConfigurableBlockGrowth;
import io.github.pouffy.immersive_weathering.data.position_tests.IPositionRuleTest;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BlockGrowthBuilder {

    private final List<TickSource> tickSources = new ArrayList<>();
    private float growthChance = 0.0f;
    private final RuleTest targetPredicate;
    private final AreaCondition areaCheck;
    private final List<ConfigurableBlockGrowth.DirectionalList> growthForDirection = new ArrayList<>();
    private final Optional<HolderSet<Block>> owners;
    private final List<IPositionRuleTest> biomePredicates = new ArrayList<>();
    private boolean targetSelf = false;
    private boolean destroyTarget = false;

    public BlockGrowthBuilder(HolderSet<Block> owners, RuleTest targetPredicate, AreaCondition areaCheck) {
        this.targetPredicate = targetPredicate;
        this.areaCheck = areaCheck;
        this.owners = Optional.of(owners);
    }

    public static BlockGrowthBuilder growth(HolderSet<Block> owners, RuleTest targetPredicate, AreaCondition areaCheck) {
        return new BlockGrowthBuilder(owners, targetPredicate, areaCheck);
    }

    public static BlockGrowthBuilder growth(Block owner, RuleTest targetPredicate, AreaCondition areaCheck) {
        HolderSet<Block> direct = HolderSet.direct(owner.builtInRegistryHolder());
        return new BlockGrowthBuilder(direct, targetPredicate, areaCheck);
    }

    public static BlockGrowthBuilder growth(List<Block> owners, RuleTest targetPredicate, AreaCondition areaCheck) {
        List<Holder.Reference<Block>> holders = owners.stream().map(Block::builtInRegistryHolder).toList();
        HolderSet<Block> direct = HolderSet.direct(holders);
        return new BlockGrowthBuilder(direct, targetPredicate, areaCheck);
    }

    public BlockGrowthBuilder tickSource(TickSource... sources) {
        tickSources.addAll(List.of(sources));
        return this;
    }

    public BlockGrowthBuilder growthChance(float growthChance) {
        this.growthChance = growthChance;
        return this;
    }

    public BlockGrowthBuilder directionalGrowth(ConfigurableBlockGrowth.DirectionalList... growthsForDirection) {
        growthForDirection.addAll(List.of(growthsForDirection));
        return this;
    }

    public BlockGrowthBuilder directionalGrowth(Direction direction, Integer weight, SimpleWeightedRandomList<BlockPair> randomList) {
        growthForDirection.add(new ConfigurableBlockGrowth.DirectionalList(Optional.ofNullable(direction), Optional.ofNullable(weight), randomList));
        return this;
    }

    public BlockGrowthBuilder predicate(IPositionRuleTest... predicates) {
        biomePredicates.addAll(List.of(predicates));
        return this;
    }

    public BlockGrowthBuilder targetSelf(boolean targetSelf) {
        this.targetSelf = targetSelf;
        return this;
    }

    public BlockGrowthBuilder destroyTarget(boolean destroyTarget) {
        this.destroyTarget = destroyTarget;
        return this;
    }

    public ConfigurableBlockGrowth build() {
        if (this.tickSources.isEmpty()) {
            this.tickSources.add(TickSource.BLOCK_TICK);
        }
        return new ConfigurableBlockGrowth(this.tickSources, this.growthChance, this.targetPredicate, this.areaCheck, this.growthForDirection, this.owners, this.biomePredicates, this.targetSelf, this.destroyTarget);
    }

    public void save(GrowthOutput output, String id) {
        save(output, ResourceLocation.parse(id));
    }

    public void save(GrowthOutput output, ResourceLocation recipeId) {
        ConfigurableBlockGrowth growth = this.build();
        output.accept(recipeId, growth);
    }
}
