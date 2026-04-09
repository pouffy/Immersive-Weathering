package io.github.pouffy.immersive_weathering.data.block_growths.growths.builtin;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.pouffy.immersive_weathering.blocks.LayerBlock;
import io.github.pouffy.immersive_weathering.blocks.sandy.ISandy;
import io.github.pouffy.immersive_weathering.data.block_growths.TickSource;
import io.github.pouffy.immersive_weathering.data.block_growths.growths.IBlockGrowth;
import io.github.pouffy.immersive_weathering.datamaps.DataMapHelpers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

public class SandLayerGrowth implements IBlockGrowth {

    public static final MapCodec<SandLayerGrowth> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            RegistryCodecs.homogeneousList(Registries.BLOCK).optionalFieldOf("owners", HolderSet.empty()).forGetter(b -> b.owners),
            TickSource.CODEC.listOf().optionalFieldOf("tick_sources", List.of(TickSource.BLOCK_TICK)).forGetter(b -> b.sources),
            Codec.FLOAT.optionalFieldOf("growth_chance", 1f).forGetter(b -> b.growthChance)
    ).apply(instance, SandLayerGrowth::new));

    private final HolderSet<Block> owners;
    private final List<TickSource> sources;
    protected final float growthChance;

    public static final Type<SandLayerGrowth> TYPE = new Type<>(CODEC, "sand_layer_seeping");

    public SandLayerGrowth(HolderSet<Block> owners, List<TickSource> sources, float growthChance) {
        this.owners = owners;
        this.sources = sources;
        this.growthChance = growthChance;
    }

    @Override
    public Type<?> getType() {
        return TYPE;
    }

    @Override
    public @Nullable Iterable<? extends Block> getOwners() {
        if (owners.equals(HolderSet.empty())) return null;
        return this.owners.stream().map(Holder::value).toList();
    }

    @Override
    public Collection<TickSource> getTickSources() {
        return sources;
    }

    int getAge(BlockState state) {
        return state.getValue(this.getAgeProperty());
    }

    IntegerProperty getAgeProperty() {
        return LayerBlock.LAYERS_8;
    }

    @Override
    public void tryGrowing(BlockPos pos, BlockState state, ServerLevel level, Supplier<Holder<Biome>> biome) {
        if (!(growthChance == 1 || level.random.nextFloat() < growthChance)) return;

        BlockPos belowPos = pos.below();
        BlockState belowState = level.getBlockState(belowPos);
        var sandyBlock = DataMapHelpers.getPrevious(DataMapHelpers.Type.SAND, belowState);
        if (!state.hasProperty(LayerBlock.LAYERS_8)) return;
        if (state.getValue(LayerBlock.LAYERS_8) > 1 && sandyBlock.isPresent()) {
            RandomSource random = level.random;
            int rand = random.nextInt(2);
            int rand2 = random.nextInt(5);
            if (state.hasProperty(ISandy.SANDINESS) && state.hasProperty(ISandy.SAND_AGE))
                level.setBlockAndUpdate(belowPos, sandyBlock.get().setValue(ISandy.SANDINESS, rand).setValue(ISandy.SAND_AGE, rand2));
            level.setBlockAndUpdate(pos, state.setValue(LayerBlock.LAYERS_8, getAge(state) - 1));
        }
    }
}
