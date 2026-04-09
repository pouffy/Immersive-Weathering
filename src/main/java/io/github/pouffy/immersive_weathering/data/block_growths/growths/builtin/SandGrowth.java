package io.github.pouffy.immersive_weathering.data.block_growths.growths.builtin;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.pouffy.immersive_weathering.blocks.ModBlockProperties;
import io.github.pouffy.immersive_weathering.blocks.sandy.ISandy;
import io.github.pouffy.immersive_weathering.data.block_growths.TickSource;
import io.github.pouffy.immersive_weathering.data.block_growths.growths.IBlockGrowth;
import io.github.pouffy.immersive_weathering.datamaps.DataMapHelpers;
import io.github.pouffy.immersive_weathering.reg.ModBlocks;
import io.github.pouffy.immersive_weathering.reg.ModTags;
import io.github.pouffy.immersive_weathering.util.TemperatureManager;
import io.github.pouffy.immersive_weathering.util.WeatheringHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

public class SandGrowth implements IBlockGrowth {

    public static final MapCodec<SandGrowth> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            TickSource.CODEC.listOf().optionalFieldOf("tick_sources", List.of(TickSource.BLOCK_TICK)).forGetter(b -> b.sources),
            Codec.FLOAT.optionalFieldOf("growth_chance", 1f).forGetter(b -> b.growthChance)
    ).apply(instance, SandGrowth::new));

    private final List<TickSource> sources;
    protected final float growthChance;

    public static final Type<SandGrowth> TYPE = new Type<>(CODEC, "sandy_stones");

    public SandGrowth(List<TickSource> sources, float growthChance) {
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
        List<Block> blocks = new ArrayList<>();
        DataMapHelpers.getAllOfType(DataMapHelpers.Type.SAND).forEach(blocks::add);
        BuiltInRegistries.BLOCK.getTag(ModTags.SANDY).get().stream().forEach(h -> blocks.add(h.value()));
        return blocks;
    }

    @Override
    public void tryGrowing(BlockPos pos, BlockState state, ServerLevel level, Supplier<Holder<Biome>> biome) {
        if (!(growthChance == 1 || level.random.nextFloat() < growthChance)) return;

        if (TemperatureManager.hasSandstorm(level, pos)) {
            var sandyBlock = DataMapHelpers.getPrevious(DataMapHelpers.Type.SAND, state);

            RandomSource random = level.random;
            int rand = random.nextInt(10);
            if (state.is(ModTags.SANDY) && state.getValue(ModBlockProperties.SANDINESS) == 0 && ISandy.isRandomSandyPos(pos)) level.setBlockAndUpdate(pos, state.setValue(ModBlockProperties.SANDINESS, 1).setValue(ModBlockProperties.SAND_AGE, rand));

            else if (sandyBlock.isPresent()) {
                BlockPos downPos = pos.below();
                BlockState downBlock = level.getBlockState(downPos);

                if (WeatheringHelper.isRandomWeatheringPos(downPos)){
                    var sandyBlock2 = DataMapHelpers.getPrevious(DataMapHelpers.Type.SAND, downBlock);
                    if (sandyBlock2.isPresent()) {
                        level.setBlockAndUpdate(pos, sandyBlock.get().setValue(ModBlockProperties.SANDINESS, 1));
                        level.setBlockAndUpdate(pos.below(), sandyBlock2.get().setValue(ModBlockProperties.SANDINESS, 0));
                        level.setBlockAndUpdate(pos.above(), ModBlocks.SAND_LAYER_BLOCK.get().defaultBlockState());
                    }
                }
                else level.setBlockAndUpdate(pos, sandyBlock.get());
            }
        }
    }
}
