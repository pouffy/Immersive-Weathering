package io.github.pouffy.immersive_weathering.data.block_growths;

import com.google.common.collect.Sets;
import io.github.pouffy.immersive_weathering.data.block_growths.growths.ConfigurableBlockGrowth;
import io.github.pouffy.immersive_weathering.data.block_growths.growths.data.GrowthOutput;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public abstract class BlockGrowthProvider implements DataProvider {
    protected final PackOutput.PathProvider pathProvider;
    private final CompletableFuture<HolderLookup.Provider> registries;

    public BlockGrowthProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries) {
        this.pathProvider = packOutput.createPathProvider(PackOutput.Target.DATA_PACK, "block_growths");
        this.registries = registries;
    }

    protected abstract void buildGrowths(GrowthOutput ritualOutput, HolderLookup.Provider holderLookup);

    public CompletableFuture<?> run(final CachedOutput output, final HolderLookup.Provider registries) {
        Set<CompletableFuture<?>> list = new HashSet<>();
        final Set<ResourceLocation> set = Sets.newHashSet();
        this.buildGrowths(new GrowthOutput() {
                              @Override
                              public void accept(ResourceLocation location, ConfigurableBlockGrowth growth) {
                                  if (!set.add(location)) {
                                      throw new IllegalStateException("Duplicate growth " + location);
                                  } else {
                                      list.add(DataProvider.saveStable(output, registries, ConfigurableBlockGrowth.CODEC, new ConfigurableBlockGrowth(growth.getTickSources(), growth.getGrowthChance(), growth.getTargetPredicate(), growth.getAreaCondition(), growth.encodeRandomLists(), Optional.ofNullable(growth.getOwnersHolder()), growth.getPositionTests(), growth.targetSelf(), growth.destroyTarget()), pathProvider.json(location)));
                                  }
                              }
                          }, registries
        );

        return CompletableFuture.allOf(list.toArray(CompletableFuture[]::new));
    }

    @Override
    public CompletableFuture<?> run(CachedOutput output) {
        return this.registries.thenCompose(provider -> this.run(output, provider));
    }

    @Override
    public String getName() {
        return "";
    }
}
