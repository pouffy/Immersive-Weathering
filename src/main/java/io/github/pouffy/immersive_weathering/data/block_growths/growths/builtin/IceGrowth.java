package io.github.pouffy.immersive_weathering.data.block_growths.growths.builtin;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.pouffy.immersive_weathering.blocks.IcicleBlock;
import io.github.pouffy.immersive_weathering.data.block_growths.TickSource;
import io.github.pouffy.immersive_weathering.data.block_growths.growths.IBlockGrowth;
import io.github.pouffy.immersive_weathering.mixins.accessors.IceInvoker;
import io.github.pouffy.immersive_weathering.reg.ModBlocks;
import net.minecraft.core.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DripstoneThickness;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

public class IceGrowth implements IBlockGrowth {

    public static final MapCodec<IceGrowth> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            RegistryCodecs.homogeneousList(Registries.BLOCK).optionalFieldOf("owners", HolderSet.empty()).forGetter(b -> b.owners),
            TickSource.CODEC.listOf().optionalFieldOf("tick_sources", List.of(TickSource.BLOCK_TICK)).forGetter(b -> b.sources),
            Codec.FLOAT.optionalFieldOf("growth_chance", 1f).forGetter(b -> b.growthChance)
    ).apply(instance, IceGrowth::new));

    private final HolderSet<Block> owners;
    private final List<TickSource> sources;
    protected final float growthChance;

    public static final Type<IceGrowth> TYPE = new Type<>(CODEC, "ice_icicle_and_melt");

    public IceGrowth(HolderSet<Block> owners, List<TickSource> sources, float growthChance) {
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

    @Override
    public void tryGrowing(BlockPos pos, BlockState state, ServerLevel level, Supplier<Holder<Biome>> b) {
        if (!(growthChance == 1 || level.random.nextFloat() < growthChance)) return;

        RandomSource random = level.random;

        //move to json??
        if (random.nextFloat() < 0.003f) {
            BlockPos icePos = pos.below();

            if (level.getBlockState(icePos).is(Blocks.AIR)) {
                Biome biome = b.get().value();
                //to form we need hot weather in a cold biome or water above & cold biome
                if (biome.coldEnoughToSnow(pos)) {
                    if (level.getFluidState(pos.above()).is(FluidTags.WATER) || (level.isDay() && !level.isRaining() && !level.isThundering())) {
                        level.setBlockAndUpdate(icePos, ModBlocks.ICICLE.get().defaultBlockState()
                                .setValue(BlockStateProperties.VERTICAL_DIRECTION, Direction.DOWN)
                                .setValue(IcicleBlock.THICKNESS, DripstoneThickness.TIP));
                    }
                }
            }
        }

        //melt ice
        if (state.getBlock() instanceof IceInvoker ice) {
            if (level.dimensionType().ultraWarm()) {
                level.playSound(null, pos, SoundEvents.LAVA_EXTINGUISH, SoundSource.BLOCKS, 0.3F, 2.9F + (random.nextFloat() - random.nextFloat()) * 0.6F);

                float i = pos.getX() + 0.5f;
                float j = pos.getY() + 0.5f;
                float k = pos.getZ() + 0.5f;
                level.sendParticles(ParticleTypes.LARGE_SMOKE, i, j, k, 12, 0.2D, 0.2D, 0.2D, 0);
                ice.invokeMelt(state, level, pos);
            }
        }
    }
}
