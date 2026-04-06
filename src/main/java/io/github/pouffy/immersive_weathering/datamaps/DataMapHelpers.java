package io.github.pouffy.immersive_weathering.datamaps;

import io.github.pouffy.immersive_weathering.ImmersiveWeathering;
import io.github.pouffy.immersive_weathering.reg.ModDataMaps;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.registries.datamaps.DataMapType;
import net.neoforged.neoforge.registries.datamaps.DataMapsUpdatedEvent;

import java.util.*;

@SuppressWarnings("deprecation")
public class DataMapHelpers {

    private static final Map<Block, Block> INVERSE_CRACK_DATAMAP_INTERNAL = new HashMap<>();
    private static final Map<Block, Block> INVERSE_RUST_DATAMAP_INTERNAL = new HashMap<>();
    private static final Map<Block, Block> INVERSE_MOSS_DATAMAP_INTERNAL = new HashMap<>();
    private static final Map<Block, Block> INVERSE_FLOWER_DATAMAP_INTERNAL = new HashMap<>();
    private static final Map<Block, Block> INVERSE_SAND_DATAMAP_INTERNAL = new HashMap<>();
    private static final Map<Block, Block> INVERSE_SNOW_DATAMAP_INTERNAL = new HashMap<>();
    private static final Map<Block, Block> INVERSE_FROST_DATAMAP_INTERNAL = new HashMap<>();
    private static final Map<Block, Block> INVERSE_GRASS_DATAMAP_INTERNAL = new HashMap<>();

    @SuppressWarnings("DataFlowIssue")
    @SubscribeEvent
    public static void onDataMapsUpdated(DataMapsUpdatedEvent event) {
        event.ifRegistry(Registries.BLOCK, registry -> {
            INVERSE_CRACK_DATAMAP_INTERNAL.clear();
            INVERSE_RUST_DATAMAP_INTERNAL.clear();
            INVERSE_MOSS_DATAMAP_INTERNAL.clear();
            INVERSE_FLOWER_DATAMAP_INTERNAL.clear();
            INVERSE_SAND_DATAMAP_INTERNAL.clear();
            INVERSE_SNOW_DATAMAP_INTERNAL.clear();
            INVERSE_FROST_DATAMAP_INTERNAL.clear();
            INVERSE_GRASS_DATAMAP_INTERNAL.clear();

            registry.getDataMap(ModDataMaps.CRACKABLES).forEach((resourceKey, crackable) -> {
                var block = BuiltInRegistries.BLOCK.get(resourceKey);
                crackable.nextStage().ifPresent(next -> INVERSE_CRACK_DATAMAP_INTERNAL.put(next, block));
                for (BlockState state : block.getStateDefinition().getPossibleStates()) {
                    state.initCache();
                }
            });
            registry.getDataMap(ModDataMaps.RUSTABLES).forEach((resourceKey, rustable) -> {
                var block = BuiltInRegistries.BLOCK.get(resourceKey);
                rustable.nextStage().ifPresent(next -> INVERSE_RUST_DATAMAP_INTERNAL.put(next, block));
                for (BlockState state : block.getStateDefinition().getPossibleStates()) {
                    state.initCache();
                }
            });
            registry.getDataMap(ModDataMaps.MOSSABLES).forEach((resourceKey, mossable) -> {
                var block = BuiltInRegistries.BLOCK.get(resourceKey);
                mossable.nextStage().ifPresent(next -> INVERSE_MOSS_DATAMAP_INTERNAL.put(next, block));
                for (BlockState state : block.getStateDefinition().getPossibleStates()) {
                    state.initCache();
                }
            });
            registry.getDataMap(ModDataMaps.FLOWERABLES).forEach((resourceKey, flowerable) -> {
                var block = BuiltInRegistries.BLOCK.get(resourceKey);
                flowerable.nextStage().ifPresent(next -> INVERSE_FLOWER_DATAMAP_INTERNAL.put(next, block));
                for (BlockState state : block.getStateDefinition().getPossibleStates()) {
                    state.initCache();
                }
            });
            registry.getDataMap(ModDataMaps.SANDABLES).forEach((resourceKey, sandable) -> {
                var block = BuiltInRegistries.BLOCK.get(resourceKey);
                sandable.nextStage().ifPresent(next -> INVERSE_SAND_DATAMAP_INTERNAL.put(next, block));
                for (BlockState state : block.getStateDefinition().getPossibleStates()) {
                    state.initCache();
                }
            });
            registry.getDataMap(ModDataMaps.SNOWABLES).forEach((resourceKey, snowable) -> {
                var block = BuiltInRegistries.BLOCK.get(resourceKey);
                snowable.nextStage().ifPresent(next -> INVERSE_SNOW_DATAMAP_INTERNAL.put(next, block));
                for (BlockState state : block.getStateDefinition().getPossibleStates()) {
                    state.initCache();
                }
            });
            registry.getDataMap(ModDataMaps.FROSTABLES).forEach((resourceKey, frostable) -> {
                var block = BuiltInRegistries.BLOCK.get(resourceKey);
                frostable.nextStage().ifPresent(next -> INVERSE_FROST_DATAMAP_INTERNAL.put(next, block));
                for (BlockState state : block.getStateDefinition().getPossibleStates()) {
                    state.initCache();
                }
            });
            registry.getDataMap(ModDataMaps.GRASS_GROWTH).forEach((resourceKey, grassGrowth) -> {
                var block = BuiltInRegistries.BLOCK.get(resourceKey);
                grassGrowth.nextStage().ifPresent(next -> INVERSE_GRASS_DATAMAP_INTERNAL.put(next, block));
                for (BlockState state : block.getStateDefinition().getPossibleStates()) {
                    state.initCache();
                }
            });
        });
    }

    private static Map<Block, Block> getInternalMap(Type type) {
        return switch (type) {
            case CRACK -> INVERSE_CRACK_DATAMAP_INTERNAL;
            case RUST -> INVERSE_RUST_DATAMAP_INTERNAL;
            case MOSS -> INVERSE_MOSS_DATAMAP_INTERNAL;
            case FLOWER -> INVERSE_FLOWER_DATAMAP_INTERNAL;
            case SAND -> INVERSE_SAND_DATAMAP_INTERNAL;
            case SNOW -> INVERSE_SNOW_DATAMAP_INTERNAL;
            case FROST -> INVERSE_FROST_DATAMAP_INTERNAL;
            case GRASS -> INVERSE_GRASS_DATAMAP_INTERNAL;
        };
    }

    public static Map<Block, Block> getInverseMap(Type type) {
        return Collections.unmodifiableMap(getInternalMap(type));
    }

    public static Optional<Crackable> getCrackable(Block block) {
        return Optional.ofNullable(block.builtInRegistryHolder().getData(ModDataMaps.CRACKABLES));
    }

    public static Optional<Rustable> getRustable(Block block) {
        return Optional.ofNullable(block.builtInRegistryHolder().getData(ModDataMaps.RUSTABLES));
    }

    public static Optional<Mossable> getMossable(Block block) {
        return Optional.ofNullable(block.builtInRegistryHolder().getData(ModDataMaps.MOSSABLES));
    }

    public static Optional<Flowerable> getFlowerable(Block block) {
        return Optional.ofNullable(block.builtInRegistryHolder().getData(ModDataMaps.FLOWERABLES));
    }

    public static Optional<Sandable> getSandable(Block block) {
        return Optional.ofNullable(block.builtInRegistryHolder().getData(ModDataMaps.SANDABLES));
    }

    public static Optional<Snowable> getSnowable(Block block) {
        return Optional.ofNullable(block.builtInRegistryHolder().getData(ModDataMaps.SNOWABLES));
    }

    public static Optional<Frostable> getFrostable(Block block) {
        return Optional.ofNullable(block.builtInRegistryHolder().getData(ModDataMaps.FROSTABLES));
    }

    public static Optional<Block> getNext(Type type, Block block) {
        return switch (type) {
            case CRACK -> {
                Crackable crackable = block.builtInRegistryHolder().getData(ModDataMaps.CRACKABLES);
                yield Optional.ofNullable(crackable != null ? crackable.nextStage().orElse(null) : null);
            }
            case RUST -> {
                Rustable rustable = block.builtInRegistryHolder().getData(ModDataMaps.RUSTABLES);
                yield Optional.ofNullable(rustable != null ? rustable.nextStage().orElse(null) : null);
            }
            case MOSS -> {
                Mossable mossable = block.builtInRegistryHolder().getData(ModDataMaps.MOSSABLES);
                yield Optional.ofNullable(mossable != null ? mossable.nextStage().orElse(null) : null);
            }
            case FLOWER -> {
                Flowerable flowerable = block.builtInRegistryHolder().getData(ModDataMaps.FLOWERABLES);
                yield Optional.ofNullable(flowerable != null ? flowerable.nextStage().orElse(null) : null);
            }
            case SAND -> {
                Sandable sandable = block.builtInRegistryHolder().getData(ModDataMaps.SANDABLES);
                yield Optional.ofNullable(sandable != null ? sandable.nextStage().orElse(null) : null);
            }
            case SNOW -> {
                Snowable snowable = block.builtInRegistryHolder().getData(ModDataMaps.SNOWABLES);
                yield Optional.ofNullable(snowable != null ? snowable.nextStage().orElse(null) : null);
            }
            case FROST -> {
                Frostable frostable = block.builtInRegistryHolder().getData(ModDataMaps.FROSTABLES);
                yield Optional.ofNullable(frostable != null ? frostable.nextStage().orElse(null) : null);
            }
            case GRASS -> {
                GrassGrowth grassGrowth = block.builtInRegistryHolder().getData(ModDataMaps.GRASS_GROWTH);
                yield Optional.ofNullable(grassGrowth != null ? grassGrowth.nextStage().orElse(null) : null);
            }
        };
    }

    public static Optional<BlockState> getNext(Type type, BlockState state) {
        return getNext(type, state.getBlock()).map(block -> block.withPropertiesOf(state));
    }

    public static Optional<Block> getPrevious(Type type, Block block) {
        return Optional.ofNullable(getInverseMap(type).getOrDefault(block, null));
    }

    public static Optional<BlockState> getPrevious(Type type, BlockState state) {
        return getPrevious(type, state.getBlock()).map(block -> block.withPropertiesOf(state));
    }

    public static boolean canAdvance(Type type, Block block) {
        return getNext(type, block).isPresent();
    }

    public static boolean canAdvance(Type type, BlockState state) {
        return getNext(type, state).isPresent();
    }

    public static boolean canRevert(Type type, Block block) {
        return getPrevious(type, block).isPresent();
    }

    public static boolean canRevert(Type type, BlockState state) {
        return getPrevious(type, state).isPresent();
    }

    public static Iterable<Block> getAllOfType(Type type) {
        ArrayList<Block> blocks = new ArrayList<>();
        var map = switch (type) {
            case CRACK -> ModDataMaps.CRACKABLES;
            case RUST -> ModDataMaps.RUSTABLES;
            case MOSS -> ModDataMaps.MOSSABLES;
            case FLOWER -> ModDataMaps.FLOWERABLES;
            case SAND -> ModDataMaps.SANDABLES;
            case SNOW -> ModDataMaps.SNOWABLES;
            case FROST -> ModDataMaps.FROSTABLES;
            case GRASS -> ModDataMaps.GRASS_GROWTH;
        };
        for (Block block : BuiltInRegistries.BLOCK) {
            if (block.builtInRegistryHolder().getData(map) != null) {
                blocks.add(block);
            }
        }
        return blocks;
    }

    public enum Type {
        CRACK, RUST, MOSS, FLOWER, SAND, SNOW, FROST, GRASS
    }
}
