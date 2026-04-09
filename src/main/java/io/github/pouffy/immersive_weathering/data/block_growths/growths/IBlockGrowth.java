package io.github.pouffy.immersive_weathering.data.block_growths.growths;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import io.github.pouffy.immersive_weathering.data.block_growths.TickSource;
import io.github.pouffy.immersive_weathering.data.block_growths.growths.builtin.BuiltinGrowthsRegistry;
import io.github.pouffy.immersive_weathering.data.fluid_generators.IFluidGenerator;
import io.github.pouffy.immersive_weathering.data.fluid_generators.ModFluidGenerators;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.function.Supplier;

public interface IBlockGrowth {

    Codec<IBlockGrowth> CODEC = Type.CODEC.dispatch("type", IBlockGrowth::getType, Type::codec);

    Type<?> getType();

    @Nullable
    Iterable<? extends Block> getOwners();

    Collection<TickSource> getTickSources();

    void tryGrowing(BlockPos pos, BlockState state, ServerLevel level, Supplier<Holder<Biome>> biome);


    record Type<T extends IBlockGrowth>(MapCodec<T> codec, String name) {

        private static final Codec<Type<?>> CODEC = Codec.STRING.flatXmap(
                (name) -> BuiltinGrowthsRegistry.get(name).map(DataResult::success).orElseGet(
                        () -> DataResult.error(() -> "Unknown Block Growth type: " + name)),
                (t) -> DataResult.success(t.name()));

    }
}
