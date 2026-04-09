package io.github.pouffy.immersive_weathering.data.block_growths.growths.builtin;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.pouffy.immersive_weathering.data.block_growths.TickSource;
import io.github.pouffy.immersive_weathering.data.block_growths.growths.IBlockGrowth;
import io.github.pouffy.immersive_weathering.data.fluid_generators.IFluidGenerator;
import io.github.pouffy.immersive_weathering.data.fluid_generators.builtin.BurnMossGenerator;
import io.github.pouffy.immersive_weathering.reg.ModBlocks;
import net.minecraft.core.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class CampfireSootGrowth implements IBlockGrowth {

    public static final MapCodec<CampfireSootGrowth> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            RegistryCodecs.homogeneousList(Registries.BLOCK).optionalFieldOf("owners", HolderSet.empty()).forGetter(b -> b.owners),
            TickSource.CODEC.listOf().optionalFieldOf("tick_sources", List.of(TickSource.BLOCK_TICK)).forGetter(b -> b.sources),
            Codec.FLOAT.optionalFieldOf("growth_chance", 1f).forGetter(b -> b.growthChance)
    ).apply(instance, CampfireSootGrowth::new));

    private final HolderSet<Block> owners;
    private final List<TickSource> sources;
    protected final float growthChance;

    public static final Type<CampfireSootGrowth> TYPE = new Type<>(CODEC, "campfire_soot");

    public CampfireSootGrowth(HolderSet<Block> owners, List<TickSource> sources, float growthChance) {
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
    public void tryGrowing(BlockPos pos, BlockState state, ServerLevel level, Supplier<Holder<Biome>> biome) {
        if (!(growthChance == 1 || level.random.nextFloat() < growthChance)) return;
        //we are accessing with tag so always check if this is campfire
        if (state.getBlock() instanceof CampfireBlock && state.getValue(CampfireBlock.LIT)) {
            RandomSource random = level.random;

            int smokeHeight = state.getValue(CampfireBlock.SIGNAL_FIRE) ? 23 : 8;

            FireSootGrowth.spawnSootAboveFire(level, pos, smokeHeight);

            BlockPos sootBlock = pos.above(random.nextInt(smokeHeight) + 1);
            int rand = random.nextInt(4);
            Direction sootDir = Direction.from2DDataValue(rand);
            BlockPos testPos = sootBlock.relative(sootDir);
            BlockState testBlock = level.getBlockState(testPos);

            if (Block.isFaceFull(testBlock.getCollisionShape(level, testPos), sootDir.getOpposite())) {
                BlockState currentState = level.getBlockState(sootBlock);
                if (currentState.is(ModBlocks.SOOT.get())) {
                    level.setBlock(sootBlock, currentState.setValue(PipeBlock.PROPERTY_BY_DIRECTION.get(sootDir), true), Block.UPDATE_CLIENTS);
                } else if (currentState.isAir()) {
                    level.setBlock(sootBlock, ModBlocks.SOOT.get().defaultBlockState().setValue(PipeBlock.PROPERTY_BY_DIRECTION.get(sootDir), true), Block.UPDATE_CLIENTS);
                }
            }
        }
    }
}
