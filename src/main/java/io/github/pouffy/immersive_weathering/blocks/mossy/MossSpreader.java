package io.github.pouffy.immersive_weathering.blocks.mossy;

import io.github.pouffy.immersive_weathering.configs.CommonConfigs;
import io.github.pouffy.immersive_weathering.datamaps.DataMapHelpers;
import io.github.pouffy.immersive_weathering.datamaps.Mossable;
import io.github.pouffy.immersive_weathering.reg.ModTags;
import io.github.pouffy.immersive_weathering.util.PatchSpreader;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class MossSpreader implements PatchSpreader<IMossy.MossLevel> {

    public static MossSpreader INSTANCE = new MossSpreader();

    @Override
    public Class<IMossy.MossLevel> getType() {
        return IMossy.MossLevel.class;
    }

    //basically how big those patches will be
    @Override
    public double getInterestForDirection(Level level, BlockPos pos) {
        return CommonConfigs.MOSS_INTERESTS_FOR_FACE.get();
    }

    @Override
    public double getDisjointGrowthChance(Level level, BlockPos pos) {
        return CommonConfigs.MOSS_PATCHINESS.get();
    }

    //chance to have blocks that wont weather but still be able to make others weather if getDisjointGrowthChance is high enough
    @Override
    public double getUnWeatherableChance(Level level, BlockPos pos) {
        return CommonConfigs.MOSS_IMMUNE_CHANCE.get();
    }

    @Override
    public WeatheringAgent getWeatheringEffect(BlockState state, Level level, BlockPos pos) {
        if (state.is(ModTags.MOSSY)) return WeatheringAgent.WEATHER;
        return WeatheringAgent.NONE;
    }


    @Override
    public WeatheringAgent getHighInfluenceWeatheringEffect(BlockState state, Level level, BlockPos pos) {
        var fluid = state.getFluidState();
        if (fluid.is(FluidTags.LAVA)) return WeatheringAgent.PREVENT_WEATHERING;
        if (state.is(ModTags.MOSS_SOURCE) || fluid.is(FluidTags.WATER)) return WeatheringAgent.WEATHER;
        return WeatheringAgent.NONE;
    }

    @Override
    public boolean needsAirToSpread(Level level, BlockPos pos) {
        return CommonConfigs.MOSS_NEEDS_AIR.get();
    }

    //utility to grow stuff
    static void growNeighbors(ServerLevel level, RandomSource random, BlockPos pos) {
        for (var direction : Direction.values()) {
            if (random.nextFloat() > 0.5f) {
                var targetPos = pos.relative(direction);
                BlockState targetBlock = level.getBlockState(targetPos);
                var mossy = DataMapHelpers.getNext(DataMapHelpers.Type.MOSS, targetBlock);
                if (mossy.isPresent()) {
                    mossy.ifPresent(s -> level.setBlockAndUpdate(targetPos, s));
                }
            }
        }
    }
}
