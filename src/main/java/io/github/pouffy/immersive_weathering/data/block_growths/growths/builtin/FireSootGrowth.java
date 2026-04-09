package io.github.pouffy.immersive_weathering.data.block_growths.growths.builtin;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.pouffy.immersive_weathering.data.block_growths.TickSource;
import io.github.pouffy.immersive_weathering.data.block_growths.growths.IBlockGrowth;
import io.github.pouffy.immersive_weathering.reg.ModBlocks;
import net.minecraft.core.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

public class FireSootGrowth implements IBlockGrowth {

    public static final MapCodec<FireSootGrowth> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            RegistryCodecs.homogeneousList(Registries.BLOCK).optionalFieldOf("owners", HolderSet.empty()).forGetter(b -> b.owners),
            TickSource.CODEC.listOf().optionalFieldOf("tick_sources", List.of(TickSource.BLOCK_TICK)).forGetter(b -> b.sources),
            Codec.FLOAT.optionalFieldOf("growth_chance", 1f).forGetter(b -> b.growthChance)
    ).apply(instance, FireSootGrowth::new));

    private final HolderSet<Block> owners;
    private final List<TickSource> sources;
    protected final float growthChance;

    public static final Type<FireSootGrowth> TYPE = new Type<>(CODEC, "fire_soot");

    public FireSootGrowth(HolderSet<Block> owners, List<TickSource> sources, float growthChance) {
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

        if (level.getGameRules().getBoolean(GameRules.RULE_DOFIRETICK)) {
            spawnSootAboveFire(level, pos, 6);
        }
    }

    public static void spawnSootAboveFire(ServerLevel level, BlockPos pos, int smokeHeight) {
        BlockPos sootPos = pos;
        for (int i = 0; i < smokeHeight; i++) {
            sootPos = sootPos.above();
            BlockState above = level.getBlockState(sootPos.above());
            if (Block.isFaceFull(above.getCollisionShape(level, sootPos.above()), Direction.DOWN)) {
                if (level.getBlockState(sootPos).isAir()) {
                    level.setBlock(sootPos, ModBlocks.SOOT.get().defaultBlockState().setValue(BlockStateProperties.UP, true), Block.UPDATE_CLIENTS);
                }
                return;
            }
        }
    }
}
