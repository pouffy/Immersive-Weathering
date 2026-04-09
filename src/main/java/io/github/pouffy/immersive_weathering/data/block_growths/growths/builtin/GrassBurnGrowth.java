package io.github.pouffy.immersive_weathering.data.block_growths.growths.builtin;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.pouffy.immersive_weathering.data.block_growths.TickSource;
import io.github.pouffy.immersive_weathering.data.block_growths.growths.IBlockGrowth;
import io.github.pouffy.immersive_weathering.util.WeatheringHelper;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

public class GrassBurnGrowth implements IBlockGrowth {

    public static final MapCodec<GrassBurnGrowth> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            RegistryCodecs.homogeneousList(Registries.BLOCK).optionalFieldOf("owners", HolderSet.empty()).forGetter(b -> b.owners),
            TickSource.CODEC.listOf().optionalFieldOf("tick_sources", List.of(TickSource.BLOCK_TICK)).forGetter(b -> b.sources)
    ).apply(instance, GrassBurnGrowth::new));

    private final HolderSet<Block> owners;
    private final List<TickSource> sources;

    public static final Type<GrassBurnGrowth> TYPE = new Type<>(CODEC, "grass_burning");

    public GrassBurnGrowth(HolderSet<Block> owners, List<TickSource> sources) {
        this.owners = owners;
        this.sources = sources;
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

    @Override
    public void tryGrowing(BlockPos pos, BlockState state, ServerLevel level, Supplier<Holder<Biome>> biome) {
        if (level.random.nextFloat() < 0.1f) {
            if (!PlatHelper.isAreaLoaded(level,pos, 1)) return;
            if (WeatheringHelper.hasEnoughBlocksFacingMe(pos, level, b -> b.is(BlockTags.FIRE), 1)) {
                level.setBlockAndUpdate(pos, Blocks.DIRT.defaultBlockState());
            }
        }
    }
}
