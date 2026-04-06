package io.github.pouffy.immersive_weathering.reg;

import io.github.pouffy.immersive_weathering.datamaps.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.registries.datamaps.DataMapType;
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent;

import static io.github.pouffy.immersive_weathering.ImmersiveWeathering.res;

// Just better than making every compatibility require implementation of an interface.
// Ideally most of the previously hardcoded compat can be done via datapacks now.
//TODO: Add DataMaps for "Levels" of weathering instead of hardcoding values to blocks.
public class ModDataMaps {
    public static final DataMapType<Block, Rustable> RUSTABLES = DataMapType.builder(res("rustables"), Registries.BLOCK, Rustable.CODEC).synced(Rustable.CODEC, false).build();
    public static final DataMapType<Block, Crackable> CRACKABLES = DataMapType.builder(res("crackables"), Registries.BLOCK, Crackable.CODEC).synced(Crackable.CODEC, false).build();
    public static final DataMapType<Block, Mossable> MOSSABLES = DataMapType.builder(res("mossables"), Registries.BLOCK, Mossable.CODEC).synced(Mossable.CODEC, false).build();
    public static final DataMapType<Block, Flowerable> FLOWERABLES = DataMapType.builder(res("azalea_flowering"), Registries.BLOCK, Flowerable.CODEC).synced(Flowerable.CODEC, false).build();
    public static final DataMapType<Block, Sandable> SANDABLES = DataMapType.builder(res("sandables"), Registries.BLOCK, Sandable.CODEC).synced(Sandable.CODEC, false).build();
    public static final DataMapType<Block, Snowable> SNOWABLES = DataMapType.builder(res("snowables"), Registries.BLOCK, Snowable.CODEC).synced(Snowable.CODEC, false).build();
    public static final DataMapType<Block, Frostable> FROSTABLES = DataMapType.builder(res("frostables"), Registries.BLOCK, Frostable.CODEC).synced(Frostable.CODEC, false).build();
    public static final DataMapType<Block, GrassGrowth> GRASS_GROWTH = DataMapType.builder(res("grass_growth"), Registries.BLOCK, GrassGrowth.CODEC).synced(GrassGrowth.CODEC, false).build();

    @SubscribeEvent
    private static void register(RegisterDataMapTypesEvent event) {
        event.register(RUSTABLES);
        event.register(CRACKABLES);
        event.register(MOSSABLES);
        event.register(FLOWERABLES);
        event.register(SANDABLES);
        event.register(SNOWABLES);
        event.register(FROSTABLES);
    }
}
