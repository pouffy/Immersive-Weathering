package io.github.pouffy.immersive_weathering.data.block_growths.growths.builtin;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.pouffy.immersive_weathering.data.block_growths.TickSource;
import io.github.pouffy.immersive_weathering.data.block_growths.growths.IBlockGrowth;
import io.github.pouffy.immersive_weathering.datamaps.DataMapHelpers;
import io.github.pouffy.immersive_weathering.reg.ModTags;
import io.github.pouffy.immersive_weathering.util.TemperatureManager;
import io.github.pouffy.immersive_weathering.util.WeatheringHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

public class SnowGrowth implements IBlockGrowth {

    public static final MapCodec<SnowGrowth> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            TickSource.CODEC.listOf().optionalFieldOf("tick_sources", List.of(TickSource.BLOCK_TICK)).forGetter(b -> b.sources),
            Codec.FLOAT.optionalFieldOf("growth_chance", 1f).forGetter(b -> b.growthChance)
    ).apply(instance, SnowGrowth::new));

    private final List<TickSource> sources;
    protected final float growthChance;

    public static final Type<SnowGrowth> TYPE = new Type<>(CODEC, "snowy_stones");

    public SnowGrowth(List<TickSource> sources, float growthChance) {
        this.sources = sources;
        this.growthChance = growthChance;
    }

    @Override
    public Type<?> getType() {
        return TYPE;
    }

    @Override
    public Collection<TickSource> getTickSources() {
        return sources;
    }

    @Override
    public @Nullable Iterable<Block> getOwners() {
        return DataMapHelpers.getAllOfType(DataMapHelpers.Type.SNOW);
    }

    @Override
    public void tryGrowing(BlockPos pos, BlockState state, ServerLevel level, Supplier<Holder<Biome>> biome) {
        if (!(growthChance == 1 || level.random.nextFloat() < growthChance)) return;

        if (TemperatureManager.snowGrowthCanGrowSnowyBlock(pos, biome)) {
            var snowyBlock = DataMapHelpers.getNext(DataMapHelpers.Type.SNOW, state);
            if (snowyBlock.isPresent() && level.getBrightness(LightLayer.BLOCK, pos.above()) <= 11) {
                level.setBlockAndUpdate(pos, snowyBlock.get());
                BlockPos downPos = pos.below();
                BlockState downBlock = level.getBlockState(downPos);

                if (WeatheringHelper.isRandomWeatheringPos(downPos)) {
                    var snowyBlock2 = DataMapHelpers.getNext(DataMapHelpers.Type.SNOW, downBlock);
                    if (downBlock.is(ModTags.DOUBLE_SNOWABLE) && snowyBlock2.isPresent()) {
                        level.setBlockAndUpdate(pos.below(), snowyBlock2.get());
                    }
                }
            }
        }
    }
}
