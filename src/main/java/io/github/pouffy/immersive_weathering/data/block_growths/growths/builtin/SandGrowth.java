package io.github.pouffy.immersive_weathering.data.block_growths.growths.builtin;

import io.github.pouffy.immersive_weathering.blocks.ModBlockProperties;
import io.github.pouffy.immersive_weathering.blocks.sandy.ISandy;
import io.github.pouffy.immersive_weathering.data.block_growths.TickSource;
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
import java.util.List;
import java.util.function.Supplier;

public class SandGrowth extends BuiltinBlockGrowth {

    protected SandGrowth(String name, @Nullable HolderSet<Block> owners, List<TickSource> sources, float chance) {
        super(name, owners, sources, chance);
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
