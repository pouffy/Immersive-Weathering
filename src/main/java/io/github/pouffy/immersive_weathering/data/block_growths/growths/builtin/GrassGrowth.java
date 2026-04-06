package io.github.pouffy.immersive_weathering.data.block_growths.growths.builtin;

import io.github.pouffy.immersive_weathering.data.block_growths.TickSource;
import io.github.pouffy.immersive_weathering.util.WeatheringHelper;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;

public class GrassGrowth extends BuiltinBlockGrowth {


    public GrassGrowth(String name, @Nullable HolderSet<Block> owners, List<TickSource> sources, float chance) {
        super(name, owners, sources, chance);
    }

    @Override
    public void tryGrowing(BlockPos pos, BlockState state, ServerLevel level, Supplier<Holder<Biome>> biome) {

        //fire turns this to dirt
        //gets the block again because we are injecting at tail and it could already be dirt

        //TODO: this can be added and converted to data. ALso add this to fire growth instead
        if (level.random.nextFloat() < 0.1f) {
            if (!PlatHelper.isAreaLoaded(level,pos, 1)) return;
            if (WeatheringHelper.hasEnoughBlocksFacingMe(pos, level, b -> b.is(BlockTags.FIRE), 1)) {
                level.setBlockAndUpdate(pos, Blocks.DIRT.defaultBlockState());
            }
        }
    }
}
