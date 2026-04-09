package io.github.pouffy.immersive_weathering.data.block_growths.growths.builtin;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.pouffy.immersive_weathering.blocks.FulguriteBlock;
import io.github.pouffy.immersive_weathering.configs.CommonConfigs;
import io.github.pouffy.immersive_weathering.data.block_growths.TickSource;
import io.github.pouffy.immersive_weathering.data.block_growths.growths.IBlockGrowth;
import io.github.pouffy.immersive_weathering.datamaps.Crackable;
import io.github.pouffy.immersive_weathering.datamaps.DataMapHelpers;
import io.github.pouffy.immersive_weathering.reg.ModBlocks;
import io.github.pouffy.immersive_weathering.reg.ModTags;
import net.minecraft.core.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

public class LightningGrowth implements IBlockGrowth {

    public static final MapCodec<LightningGrowth> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            TickSource.CODEC.listOf().optionalFieldOf("tick_sources", List.of(TickSource.BLOCK_TICK)).forGetter(b -> b.sources),
            Codec.FLOAT.optionalFieldOf("growth_chance", 1f).forGetter(b -> b.growthChance)
    ).apply(instance, LightningGrowth::new));

    private final List<TickSource> sources;
    protected final float growthChance;

    public static final Type<LightningGrowth> TYPE = new Type<>(CODEC, "lightning_vitrified_sand");

    public LightningGrowth(List<TickSource> sources, float growthChance) {
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
        BuiltInRegistries.BLOCK.getTag(BlockTags.SAND).get().stream().forEach(h -> blocks.add(h.value()));
        DataMapHelpers.getAllOfType(DataMapHelpers.Type.CRACK).forEach(blocks::add);
        return blocks;
    }

    @Override
    public void tryGrowing(BlockPos pos, BlockState state, ServerLevel level, Supplier<Holder<Biome>> biome) {
        onLightningHit(pos, level, 0);
    }

    public void onLightningHit(BlockPos centerPos, Level level, int rec) {
        if (!(growthChance == 1 || level.random.nextFloat() < growthChance)) return;

        if (rec == 0 && !CommonConfigs.VITRIFIED_LIGHTNING.get()) return; //TODO: remove config


        this.convert(level, centerPos);
        if (rec >= 5) return;

        rec++;
        float decrement = 0.7f;
        double p = Math.pow(decrement, rec);
        if (rec == 0 || level.random.nextFloat() < 1 * p) {
            BlockPos downPos = centerPos.below();
            if (isValidTarget(level, downPos)) {
                onLightningHit(downPos, level, rec);
            }
        }
        for (BlockPos target : BlockPos.withinManhattan(centerPos, 1, 0, 1)) {
            if (level.random.nextFloat() < 0.3 * p && target != centerPos) {
                if (isValidTarget(level, target)) {
                    onLightningHit(target, level, rec);
                }
            }
        }
    }

    private boolean isValidTarget(Level level, BlockPos pos) {
        var state = level.getBlockState(pos);
        return DataMapHelpers.getNext(DataMapHelpers.Type.CRACK, state).isPresent() || state.is(BlockTags.SAND);
    }

    public static final SimpleWeightedRandomList<Direction> list = SimpleWeightedRandomList.<Direction>builder().add(Direction.UP, 7)
            .add(Direction.DOWN, 1).add(Direction.NORTH, 1)
            .add(Direction.EAST, 1).add(Direction.WEST, 1)
            .add(Direction.SOUTH, 1).build();

    private void convert(Level level, BlockPos pos) {
        var state = level.getBlockState(pos);
        var cracked = DataMapHelpers.getNext(DataMapHelpers.Type.CRACK, state);
        if (cracked.isPresent()) {
            level.setBlock(pos, cracked.get(), 3);
        } else if (state.is(BlockTags.SAND)) {
            level.setBlock(pos, ModBlocks.VITRIFIED_SAND.get().defaultBlockState(), 3);
            if (level.random.nextFloat() < CommonConfigs.FULGURITE_CHANCE.get()) {
                var dir = list.getRandom(level.random).get().data();
                var offset = pos.relative(dir);
                if(level.getBlockState(offset).isAir()){
                    level.setBlock(offset, ModBlocks.FULGURITE.get().defaultBlockState()
                            .setValue(FulguriteBlock.FACING, dir).setValue(FulguriteBlock.POWERED, true),3);
                };
            }
        }
    }
}
