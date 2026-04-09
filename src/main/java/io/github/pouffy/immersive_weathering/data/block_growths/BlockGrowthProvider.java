package io.github.pouffy.immersive_weathering.data.block_growths;

import com.google.common.collect.Sets;
import com.mojang.serialization.JsonOps;
import io.github.pouffy.immersive_weathering.data.block_growths.area_condition.AreaCondition;
import io.github.pouffy.immersive_weathering.data.block_growths.data.BlockGrowthBuilder;
import io.github.pouffy.immersive_weathering.data.block_growths.data.GrowthOutput;
import io.github.pouffy.immersive_weathering.data.block_growths.growths.IBlockGrowth;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public abstract class BlockGrowthProvider implements DataProvider {
    protected final PackOutput.PathProvider pathProvider;
    private final CompletableFuture<HolderLookup.Provider> registries;

    public BlockGrowthProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries) {
        this.pathProvider = packOutput.createPathProvider(PackOutput.Target.DATA_PACK, "block_growths");
        this.registries = registries;
    }

    public static BlockGrowthBuilder configured(HolderSet<Block> owners, RuleTest targetPredicate, AreaCondition areaCheck) {
        return BlockGrowthBuilder.growth(owners, targetPredicate, areaCheck);
    }

    public static BlockGrowthBuilder configured(Block owner, RuleTest targetPredicate, AreaCondition areaCheck) {
        return BlockGrowthBuilder.growth(HolderSet.direct(owner.builtInRegistryHolder()), targetPredicate, areaCheck);
    }

    public static BlockGrowthBuilder configured(List<Block> owners, RuleTest targetPredicate, AreaCondition areaCheck) {
        List<Holder.Reference<Block>> holders = owners.stream().map(Block::builtInRegistryHolder).toList();
        HolderSet<Block> direct = HolderSet.direct(holders);
        return BlockGrowthBuilder.growth(direct, targetPredicate, areaCheck);
    }

    public static void builtin(GrowthOutput output, IBlockGrowth growth, ResourceLocation location) {
        output.accept(location, growth);
    }

    protected abstract void buildGrowths(GrowthOutput output, HolderLookup.Provider holderLookup);

    public final CompletableFuture<?> run(CachedOutput output) {
        return this.registries.thenCompose((provider) -> this.run(output, provider));
    }

    public CompletableFuture<?> run(final CachedOutput output, final HolderLookup.Provider registries) {
        Set<CompletableFuture<?>> list = new HashSet<>();
        final Set<ResourceLocation> set = Sets.newHashSet();
        this.buildGrowths(new GrowthOutput() {
            public void accept(ResourceLocation location, IBlockGrowth growth) {
                if (!set.add(location)) {
                    throw new IllegalStateException("Duplicate growth " + location);
                } else {
                    list.add(DataProvider.saveStable(output, registries, IBlockGrowth.CODEC, growth, pathProvider.json(location)));
                }
            }
        }, registries);

        return CompletableFuture.allOf(list.toArray(CompletableFuture[]::new));
    }



    @Override
    public String getName() {
        return "[Immersive Weathering] Block Growths";
    }
}
