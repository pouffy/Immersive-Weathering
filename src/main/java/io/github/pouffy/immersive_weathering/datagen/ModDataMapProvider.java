package io.github.pouffy.immersive_weathering.datagen;

import io.github.pouffy.immersive_weathering.datamaps.*;
import io.github.pouffy.immersive_weathering.reg.ModBlocks;
import io.github.pouffy.immersive_weathering.reg.ModItems;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.mehvahdjukaar.moonlight.api.set.leaves.LeavesTypeRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.datamaps.builtin.Compostable;
import net.neoforged.neoforge.registries.datamaps.builtin.FurnaceFuel;
import net.neoforged.neoforge.registries.datamaps.builtin.Waxable;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import static io.github.pouffy.immersive_weathering.reg.ModDataMaps.*;
import static net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps.*;

@SuppressWarnings("deprecation")
public class ModDataMapProvider extends DataMapProvider {

    protected ModDataMapProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, lookupProvider);
    }

    @Override
    protected void gather(HolderLookup.@NotNull Provider provider) {
        var rustables = builder(RUSTABLES); // Rust Weathering
        rust(rustables,        ModBlocks.CUT_IRON,                                ModBlocks.EXPOSED_CUT_IRON,                             ModBlocks.WEATHERED_CUT_IRON,           ModBlocks.RUSTED_CUT_IRON);
        rust(rustables,        ModBlocks.CUT_IRON_STAIRS,                         ModBlocks.EXPOSED_CUT_IRON_STAIRS,                      ModBlocks.WEATHERED_CUT_IRON_STAIRS,    ModBlocks.RUSTED_CUT_IRON_STAIRS);
        rust(rustables,        ModBlocks.CUT_IRON_SLAB,                           ModBlocks.EXPOSED_CUT_IRON_SLAB,                        ModBlocks.WEATHERED_CUT_IRON_SLAB,      ModBlocks.RUSTED_CUT_IRON_SLAB);
        rust(rustables,        ModBlocks.PLATE_IRON,                              ModBlocks.EXPOSED_PLATE_IRON,                           ModBlocks.WEATHERED_PLATE_IRON,         ModBlocks.RUSTED_PLATE_IRON);
        rust(rustables,        ModBlocks.PLATE_IRON_STAIRS,                       ModBlocks.EXPOSED_PLATE_IRON_STAIRS,                    ModBlocks.WEATHERED_PLATE_IRON_STAIRS,  ModBlocks.RUSTED_PLATE_IRON_STAIRS);
        rust(rustables,        ModBlocks.PLATE_IRON_SLAB,                         ModBlocks.EXPOSED_PLATE_IRON_SLAB,                      ModBlocks.WEATHERED_PLATE_IRON_SLAB,    ModBlocks.RUSTED_PLATE_IRON_SLAB);
        rust(rustables,     () -> Blocks.IRON_DOOR,                               ModBlocks.EXPOSED_IRON_DOOR,                            ModBlocks.WEATHERED_IRON_DOOR,          ModBlocks.RUSTED_IRON_DOOR);
        rust(rustables,     () -> Blocks.IRON_TRAPDOOR,                           ModBlocks.EXPOSED_IRON_TRAPDOOR,                        ModBlocks.WEATHERED_IRON_TRAPDOOR,      ModBlocks.RUSTED_IRON_TRAPDOOR);
        rust(rustables,     () -> Blocks.IRON_BARS,                               ModBlocks.EXPOSED_IRON_BARS,                            ModBlocks.WEATHERED_IRON_BARS,          ModBlocks.RUSTED_IRON_BARS);
        var crackables = builder(CRACKABLES); // Crackable Weathering
        crack(crackables,   () -> Blocks.STONE_BRICKS,                         () -> Blocks.CRACKED_STONE_BRICKS,                         ModItems.STONE_BRICK);
        crack(crackables,   () -> Blocks.STONE_BRICK_STAIRS,                      ModBlocks.CRACKED_STONE_BRICK_STAIRS,                   ModItems.STONE_BRICK);
        crack(crackables,   () -> Blocks.STONE_BRICK_SLAB,                        ModBlocks.CRACKED_STONE_BRICK_SLAB,                     ModItems.STONE_BRICK);
        crack(crackables,   () -> Blocks.STONE_BRICK_WALL,                        ModBlocks.CRACKED_STONE_BRICK_WALL,                     ModItems.STONE_BRICK);
        crack(crackables,   () -> Blocks.TUFF_BRICKS,                             ModBlocks.CRACKED_TUFF_BRICKS,                          ModItems.TUFF_BRICK);
        crack(crackables,   () -> Blocks.TUFF_BRICK_STAIRS,                       ModBlocks.CRACKED_TUFF_BRICK_STAIRS,                    ModItems.TUFF_BRICK);
        crack(crackables,   () -> Blocks.TUFF_BRICK_SLAB,                         ModBlocks.CRACKED_TUFF_BRICK_SLAB,                      ModItems.TUFF_BRICK);
        crack(crackables,   () -> Blocks.TUFF_BRICK_WALL,                         ModBlocks.CRACKED_TUFF_BRICK_WALL,                      ModItems.TUFF_BRICK);
        crack(crackables,   () -> Blocks.BRICKS,                                  ModBlocks.CRACKED_BRICKS,                            () -> Items.BRICK);
        crack(crackables,   () -> Blocks.BRICK_STAIRS,                            ModBlocks.CRACKED_BRICK_STAIRS,                      () -> Items.BRICK);
        crack(crackables,   () -> Blocks.BRICK_SLAB,                              ModBlocks.CRACKED_BRICK_SLAB,                        () -> Items.BRICK);
        crack(crackables,   () -> Blocks.BRICK_WALL,                              ModBlocks.CRACKED_BRICK_WALL,                        () -> Items.BRICK);
        crack(crackables,   () -> Blocks.POLISHED_BLACKSTONE_BRICKS,           () -> Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS,           ModItems.BLACKSTONE_BRICK);
        crack(crackables,   () -> Blocks.POLISHED_BLACKSTONE_BRICK_STAIRS,        ModBlocks.CRACKED_POLISHED_BLACKSTONE_BRICK_STAIRS,     ModItems.BLACKSTONE_BRICK);
        crack(crackables,   () -> Blocks.POLISHED_BLACKSTONE_BRICK_SLAB,          ModBlocks.CRACKED_POLISHED_BLACKSTONE_BRICK_SLAB,       ModItems.BLACKSTONE_BRICK);
        crack(crackables,   () -> Blocks.POLISHED_BLACKSTONE_BRICK_WALL,          ModBlocks.CRACKED_POLISHED_BLACKSTONE_BRICK_WALL,       ModItems.BLACKSTONE_BRICK);
        crack(crackables,   () -> Blocks.NETHER_BRICKS,                        () -> Blocks.CRACKED_NETHER_BRICKS,                     () -> Items.NETHER_BRICK);
        crack(crackables,   () -> Blocks.NETHER_BRICK_STAIRS,                     ModBlocks.CRACKED_NETHER_BRICK_STAIRS,               () -> Items.NETHER_BRICK);
        crack(crackables,   () -> Blocks.NETHER_BRICK_SLAB,                       ModBlocks.CRACKED_NETHER_BRICK_SLAB,                 () -> Items.NETHER_BRICK);
        crack(crackables,   () -> Blocks.NETHER_BRICK_WALL,                       ModBlocks.CRACKED_NETHER_BRICK_WALL,                 () -> Items.NETHER_BRICK);
        crack(crackables,   () -> Blocks.DEEPSLATE_BRICKS,                     () -> Blocks.CRACKED_DEEPSLATE_BRICKS,                     ModItems.DEEPSLATE_BRICK);
        crack(crackables,   () -> Blocks.DEEPSLATE_BRICK_STAIRS,                  ModBlocks.CRACKED_DEEPSLATE_BRICK_STAIRS,               ModItems.DEEPSLATE_BRICK);
        crack(crackables,   () -> Blocks.DEEPSLATE_BRICK_SLAB,                    ModBlocks.CRACKED_DEEPSLATE_BRICK_SLAB,                 ModItems.DEEPSLATE_BRICK);
        crack(crackables,   () -> Blocks.DEEPSLATE_BRICK_WALL,                    ModBlocks.CRACKED_DEEPSLATE_BRICK_WALL,                 ModItems.DEEPSLATE_BRICK);
        crack(crackables,   () -> Blocks.DEEPSLATE_TILES,                      () -> Blocks.CRACKED_DEEPSLATE_TILES,                      ModItems.DEEPSLATE_TILE);
        crack(crackables,   () -> Blocks.DEEPSLATE_TILE_STAIRS,                   ModBlocks.CRACKED_DEEPSLATE_TILE_STAIRS,                ModItems.DEEPSLATE_TILE);
        crack(crackables,   () -> Blocks.DEEPSLATE_TILE_SLAB,                     ModBlocks.CRACKED_DEEPSLATE_TILE_SLAB,                  ModItems.DEEPSLATE_TILE);
        crack(crackables,   () -> Blocks.DEEPSLATE_TILE_WALL,                     ModBlocks.CRACKED_DEEPSLATE_TILE_WALL,                  ModItems.DEEPSLATE_TILE);
        crack(crackables,   () -> Blocks.PRISMARINE_BRICKS,                       ModBlocks.CRACKED_PRISMARINE_BRICKS,                    ModItems.PRISMARINE_BRICK);
        crack(crackables,   () -> Blocks.PRISMARINE_BRICK_STAIRS,                 ModBlocks.CRACKED_PRISMARINE_BRICK_STAIRS,              ModItems.PRISMARINE_BRICK);
        crack(crackables,   () -> Blocks.PRISMARINE_BRICK_SLAB,                   ModBlocks.CRACKED_PRISMARINE_BRICK_SLAB,                ModItems.PRISMARINE_BRICK);
        crack(crackables,      ModBlocks.PRISMARINE_BRICK_WALL,                   ModBlocks.CRACKED_PRISMARINE_BRICK_WALL,                ModItems.PRISMARINE_BRICK);
        crack(crackables,   () -> Blocks.END_STONE_BRICKS,                        ModBlocks.CRACKED_END_STONE_BRICKS,                     ModItems.END_STONE_BRICK);
        crack(crackables,   () -> Blocks.END_STONE_BRICK_STAIRS,                  ModBlocks.CRACKED_END_STONE_BRICK_STAIRS,               ModItems.END_STONE_BRICK);
        crack(crackables,   () -> Blocks.END_STONE_BRICK_SLAB,                    ModBlocks.CRACKED_END_STONE_BRICK_SLAB,                 ModItems.END_STONE_BRICK);
        crack(crackables,   () -> Blocks.END_STONE_BRICK_WALL,                    ModBlocks.CRACKED_END_STONE_BRICK_WALL,                 ModItems.END_STONE_BRICK);
        var mossables = builder(MOSSABLES); // Moss Weathering
        moss(mossables,     () -> Blocks.STONE,                                   ModBlocks.MOSSY_STONE);
        moss(mossables,     () -> Blocks.STONE_STAIRS,                            ModBlocks.MOSSY_STONE_STAIRS);
        moss(mossables,     () -> Blocks.STONE_SLAB,                              ModBlocks.MOSSY_STONE_SLAB);
        moss(mossables,        ModBlocks.STONE_WALL,                              ModBlocks.MOSSY_STONE_WALL);
        moss(mossables,     () -> Blocks.COBBLESTONE,                          () -> Blocks.MOSSY_COBBLESTONE);
        moss(mossables,     () -> Blocks.COBBLESTONE_STAIRS,                   () -> Blocks.MOSSY_COBBLESTONE_STAIRS);
        moss(mossables,     () -> Blocks.COBBLESTONE_SLAB,                     () -> Blocks.MOSSY_COBBLESTONE_SLAB);
        moss(mossables,     () -> Blocks.COBBLESTONE_WALL,                     () -> Blocks.MOSSY_COBBLESTONE_WALL);
        moss(mossables,     () -> Blocks.STONE_BRICKS,                         () -> Blocks.MOSSY_STONE_BRICKS);
        moss(mossables,     () -> Blocks.CHISELED_STONE_BRICKS,                   ModBlocks.MOSSY_CHISELED_STONE_BRICKS);
        moss(mossables,     () -> Blocks.STONE_BRICK_STAIRS,                   () -> Blocks.MOSSY_STONE_BRICK_STAIRS);
        moss(mossables,     () -> Blocks.STONE_BRICK_SLAB,                     () -> Blocks.MOSSY_STONE_BRICK_SLAB);
        moss(mossables,     () -> Blocks.STONE_BRICK_WALL,                     () -> Blocks.MOSSY_STONE_BRICK_WALL);
        moss(mossables,     () -> Blocks.BRICKS,                                  ModBlocks.MOSSY_BRICKS);
        moss(mossables,     () -> Blocks.BRICK_STAIRS,                            ModBlocks.MOSSY_BRICK_STAIRS);
        moss(mossables,     () -> Blocks.BRICK_SLAB,                              ModBlocks.MOSSY_BRICK_SLAB);
        moss(mossables,     () -> Blocks.BRICK_WALL,                              ModBlocks.MOSSY_BRICK_WALL);
        var flowerables = builder(FLOWERABLES); // Azalea Growth
        azalea(flowerables, () -> Blocks.AZALEA,                               () -> Blocks.FLOWERING_AZALEA);
        azalea(flowerables, () -> Blocks.AZALEA_LEAVES,                        () -> Blocks.FLOWERING_AZALEA_LEAVES);
        azalea(flowerables, () -> ModBlocks.LEAF_PILES.get(LeavesTypeRegistry.INSTANCE.get(ResourceLocation.withDefaultNamespace("azalea"))), () -> ModBlocks.LEAF_PILES.get(LeavesTypeRegistry.INSTANCE.get(ResourceLocation.withDefaultNamespace("flowering_azalea"))));
        var sandables = builder(SANDABLES);
        sandy(sandables,    () -> Blocks.STONE,                                   ModBlocks.SANDY_STONE);
        sandy(sandables,    () -> Blocks.STONE_STAIRS,                            ModBlocks.SANDY_STONE_STAIRS);
        sandy(sandables,    () -> Blocks.STONE_SLAB,                              ModBlocks.SANDY_STONE_SLAB);
        sandy(sandables,       ModBlocks.STONE_WALL,                              ModBlocks.SANDY_STONE_WALL);
        sandy(sandables,    () -> Blocks.COBBLESTONE,                             ModBlocks.SANDY_COBBLESTONE);
        sandy(sandables,    () -> Blocks.COBBLESTONE_STAIRS,                      ModBlocks.SANDY_COBBLESTONE_STAIRS);
        sandy(sandables,    () -> Blocks.COBBLESTONE_SLAB,                        ModBlocks.SANDY_COBBLESTONE_SLAB);
        sandy(sandables,    () -> Blocks.COBBLESTONE_WALL,                        ModBlocks.SANDY_COBBLESTONE_WALL);
        sandy(sandables,    () -> Blocks.STONE_BRICKS,                            ModBlocks.SANDY_STONE_BRICKS);
        sandy(sandables,    () -> Blocks.CHISELED_STONE_BRICKS,                   ModBlocks.SANDY_CHISELED_STONE_BRICKS);
        sandy(sandables,    () -> Blocks.STONE_BRICK_STAIRS,                      ModBlocks.SANDY_STONE_BRICK_STAIRS);
        sandy(sandables,    () -> Blocks.STONE_BRICK_SLAB,                        ModBlocks.SANDY_STONE_BRICK_SLAB);
        sandy(sandables,    () -> Blocks.STONE_BRICK_WALL,                        ModBlocks.SANDY_STONE_BRICK_WALL);
        var snowables = builder(SNOWABLES);
        snowy(snowables,    () -> Blocks.STONE,                                   ModBlocks.SNOWY_STONE);
        snowy(snowables,    () -> Blocks.STONE_STAIRS,                            ModBlocks.SNOWY_STONE_STAIRS);
        snowy(snowables,    () -> Blocks.STONE_SLAB,                              ModBlocks.SNOWY_STONE_SLAB);
        snowy(snowables,       ModBlocks.STONE_WALL,                              ModBlocks.SNOWY_STONE_WALL);
        snowy(snowables,    () -> Blocks.COBBLESTONE,                             ModBlocks.SNOWY_COBBLESTONE);
        snowy(snowables,    () -> Blocks.COBBLESTONE_STAIRS,                      ModBlocks.SNOWY_COBBLESTONE_STAIRS);
        snowy(snowables,    () -> Blocks.COBBLESTONE_SLAB,                        ModBlocks.SNOWY_COBBLESTONE_SLAB);
        snowy(snowables,    () -> Blocks.COBBLESTONE_WALL,                        ModBlocks.SNOWY_COBBLESTONE_WALL);
        snowy(snowables,    () -> Blocks.STONE_BRICKS,                            ModBlocks.SNOWY_STONE_BRICKS);
        snowy(snowables,    () -> Blocks.CHISELED_STONE_BRICKS,                   ModBlocks.SNOWY_CHISELED_STONE_BRICKS);
        snowy(snowables,    () -> Blocks.STONE_BRICK_STAIRS,                      ModBlocks.SNOWY_STONE_BRICK_STAIRS);
        snowy(snowables,    () -> Blocks.STONE_BRICK_SLAB,                        ModBlocks.SNOWY_STONE_BRICK_SLAB);
        snowy(snowables,    () -> Blocks.STONE_BRICK_WALL,                        ModBlocks.SNOWY_STONE_BRICK_WALL);
        var frostables = builder(FROSTABLES);
        frosty(frostables,  () -> Blocks.AIR,                                     ModBlocks.FROST);
        frosty(frostables,  () -> Blocks.GLASS,                                   ModBlocks.FROSTY_GLASS);
        frosty(frostables,  () -> Blocks.FERN,                                    ModBlocks.FROSTY_FERN);
        frosty(frostables,  () -> Blocks.SHORT_GRASS,                             ModBlocks.FROSTY_GRASS);
        frosty(frostables,  () -> Blocks.GLASS_PANE,                              ModBlocks.FROSTY_GLASS_PANE);
        var waxables = builder(WAXABLES);
        wax(waxables,          ModBlocks.CUT_IRON,                                ModBlocks.WAXED_CUT_IRON);
        wax(waxables,          ModBlocks.CUT_IRON_STAIRS,                         ModBlocks.WAXED_CUT_IRON_STAIRS);
        wax(waxables,          ModBlocks.CUT_IRON_SLAB,                           ModBlocks.WAXED_CUT_IRON_SLAB);
        wax(waxables,          ModBlocks.PLATE_IRON,                              ModBlocks.WAXED_PLATE_IRON);
        wax(waxables,          ModBlocks.PLATE_IRON_STAIRS,                       ModBlocks.WAXED_PLATE_IRON_STAIRS);
        wax(waxables,          ModBlocks.PLATE_IRON_SLAB,                         ModBlocks.WAXED_PLATE_IRON_SLAB);
        wax(waxables,       () -> Blocks.IRON_DOOR,                               ModBlocks.WAXED_IRON_DOOR);
        wax(waxables,       () -> Blocks.IRON_TRAPDOOR,                           ModBlocks.WAXED_IRON_TRAPDOOR);
        wax(waxables,       () -> Blocks.IRON_BARS,                               ModBlocks.WAXED_IRON_BARS);
        wax(waxables,          ModBlocks.EXPOSED_CUT_IRON,                        ModBlocks.WAXED_EXPOSED_CUT_IRON);
        wax(waxables,          ModBlocks.EXPOSED_CUT_IRON_STAIRS,                 ModBlocks.WAXED_EXPOSED_CUT_IRON_STAIRS);
        wax(waxables,          ModBlocks.EXPOSED_CUT_IRON_SLAB,                   ModBlocks.WAXED_EXPOSED_CUT_IRON_SLAB);
        wax(waxables,          ModBlocks.EXPOSED_PLATE_IRON,                      ModBlocks.WAXED_EXPOSED_PLATE_IRON);
        wax(waxables,          ModBlocks.EXPOSED_PLATE_IRON_STAIRS,               ModBlocks.WAXED_EXPOSED_PLATE_IRON_STAIRS);
        wax(waxables,          ModBlocks.EXPOSED_PLATE_IRON_SLAB,                 ModBlocks.WAXED_EXPOSED_PLATE_IRON_SLAB);
        wax(waxables,          ModBlocks.EXPOSED_IRON_DOOR,                       ModBlocks.WAXED_EXPOSED_IRON_DOOR);
        wax(waxables,          ModBlocks.EXPOSED_IRON_TRAPDOOR,                   ModBlocks.WAXED_EXPOSED_IRON_TRAPDOOR);
        wax(waxables,          ModBlocks.EXPOSED_IRON_BARS,                       ModBlocks.WAXED_EXPOSED_IRON_BARS);
        wax(waxables,          ModBlocks.WEATHERED_CUT_IRON,                      ModBlocks.WAXED_WEATHERED_CUT_IRON);
        wax(waxables,          ModBlocks.WEATHERED_CUT_IRON_STAIRS,               ModBlocks.WAXED_WEATHERED_CUT_IRON_STAIRS);
        wax(waxables,          ModBlocks.WEATHERED_CUT_IRON_SLAB,                 ModBlocks.WAXED_WEATHERED_CUT_IRON_SLAB);
        wax(waxables,          ModBlocks.WEATHERED_PLATE_IRON,                    ModBlocks.WAXED_WEATHERED_PLATE_IRON);
        wax(waxables,          ModBlocks.WEATHERED_PLATE_IRON_STAIRS,             ModBlocks.WAXED_WEATHERED_PLATE_IRON_STAIRS);
        wax(waxables,          ModBlocks.WEATHERED_PLATE_IRON_SLAB,               ModBlocks.WAXED_WEATHERED_PLATE_IRON_SLAB);
        wax(waxables,          ModBlocks.WEATHERED_IRON_DOOR,                     ModBlocks.WAXED_WEATHERED_IRON_DOOR);
        wax(waxables,          ModBlocks.WEATHERED_IRON_TRAPDOOR,                 ModBlocks.WAXED_WEATHERED_IRON_TRAPDOOR);
        wax(waxables,          ModBlocks.WEATHERED_IRON_BARS,                     ModBlocks.WAXED_WEATHERED_IRON_BARS);
        wax(waxables,          ModBlocks.RUSTED_CUT_IRON,                         ModBlocks.WAXED_RUSTED_CUT_IRON);
        wax(waxables,          ModBlocks.RUSTED_CUT_IRON_STAIRS,                  ModBlocks.WAXED_RUSTED_CUT_IRON_STAIRS);
        wax(waxables,          ModBlocks.RUSTED_CUT_IRON_SLAB,                    ModBlocks.WAXED_RUSTED_CUT_IRON_SLAB);
        wax(waxables,          ModBlocks.RUSTED_PLATE_IRON,                       ModBlocks.WAXED_RUSTED_PLATE_IRON);
        wax(waxables,          ModBlocks.RUSTED_PLATE_IRON_STAIRS,                ModBlocks.WAXED_RUSTED_PLATE_IRON_STAIRS);
        wax(waxables,          ModBlocks.RUSTED_PLATE_IRON_SLAB,                  ModBlocks.WAXED_RUSTED_PLATE_IRON_SLAB);
        wax(waxables,          ModBlocks.RUSTED_IRON_DOOR,                        ModBlocks.WAXED_RUSTED_IRON_DOOR);
        wax(waxables,          ModBlocks.RUSTED_IRON_TRAPDOOR,                    ModBlocks.WAXED_RUSTED_IRON_TRAPDOOR);
        wax(waxables,          ModBlocks.RUSTED_IRON_BARS,                        ModBlocks.WAXED_RUSTED_IRON_BARS);
        var compostables = builder(COMPOSTABLES);
        compost(compostables,  ModItems.AZALEA_FLOWER_PILE, 0.3f);
        compost(compostables,  ModItems.MOSS_CLUMP, 0.5f);
        compost(compostables,  ModItems.AZALEA_FLOWERS, 0.5f);
        compost(compostables,  () -> ModBlocks.MULCH_BLOCK.get().asItem(), 1f);
        compost(compostables,  ModItems.FLOWER_CROWN, 0.8f);
        compost(compostables,  () -> ModBlocks.WEEDS.get().asItem(), 0.3f);
        compost(compostables,  () -> ModBlocks.IVY.get().asItem(), 0.3f);
        compost(compostables,  () -> ModBlocks.DUNE_GRASS.get().asItem(), 0.3f);
        var furnaceFuels = builder(FURNACE_FUELS);
        fuel(furnaceFuels, () -> ModBlocks.CHARRED_LOG.get().asItem(), 1600);
        fuel(furnaceFuels, () -> ModBlocks.CHARRED_PLANKS.get().asItem(), 400);
        fuel(furnaceFuels, () -> ModBlocks.CHARRED_SLAB.get().asItem(), 200);
        fuel(furnaceFuels, () -> ModBlocks.CHARRED_STAIRS.get().asItem(), 200);
        fuel(furnaceFuels, () -> ModBlocks.CHARRED_FENCE.get().asItem(), 200);
        fuel(furnaceFuels, () -> ModBlocks.CHARRED_FENCE_GATE.get().asItem(), 200);
    }

    private ResourceLocation loc(String name) {
        return ResourceLocation.parse(name);
    }

    // Helpers
    private void rust(DataMapProvider.Builder<Rustable, Block> builder, Supplier<Block> unaffected, Supplier<Block> exposed, Supplier<Block> weathered, Supplier<Block> rusted) {
        builder.add(unaffected.get().builtInRegistryHolder(), Rustable.create(exposed.get()), false);
        builder.add(exposed.get().builtInRegistryHolder(), Rustable.create(weathered.get()), false);
        builder.add(weathered.get().builtInRegistryHolder(), Rustable.create(rusted.get()), false);
        builder.add(rusted.get().builtInRegistryHolder(), Rustable.create(null), false);
    }
    private void crack(DataMapProvider.Builder<Crackable, Block> builder, Supplier<Block> unaffected, Supplier<Block> cracked, Supplier<Item> repair) {
        builder.add(unaffected.get().builtInRegistryHolder(), Crackable.create(cracked.get(), repair.get()), false);
    }
    private void moss(DataMapProvider.Builder<Mossable, Block> builder, Supplier<Block> unaffected, Supplier<Block> mossy) {
        builder.add(unaffected.get().builtInRegistryHolder(), Mossable.create(mossy.get()), false);
    }
    private void azalea(DataMapProvider.Builder<Flowerable, Block> builder, Supplier<Block> unaffected, Supplier<Block> flowering) {
        builder.add(unaffected.get().builtInRegistryHolder(), Flowerable.create(flowering.get()), false);
    }
    private void sandy(DataMapProvider.Builder<Sandable, Block> builder, Supplier<Block> unaffected, Supplier<Block> sandy) {
        builder.add(unaffected.get().builtInRegistryHolder(), Sandable.create(sandy.get()), false);
    }
    private void snowy(DataMapProvider.Builder<Snowable, Block> builder, Supplier<Block> unaffected, Supplier<Block> snowy) {
        builder.add(unaffected.get().builtInRegistryHolder(), Snowable.create(snowy.get()), false);
    }
    private void frosty(DataMapProvider.Builder<Frostable, Block> builder, Supplier<? extends Block> unaffected, Supplier<? extends Block> frosty) {
        builder.add(unaffected.get().builtInRegistryHolder(), Frostable.create(frosty.get()), false);
    }
    private void wax(DataMapProvider.Builder<Waxable, Block> builder, Supplier<Block> unwaxed, Supplier<Block> waxed) {
        builder.add(unwaxed.get().builtInRegistryHolder(), new Waxable(waxed.get()), false);
    }
    private void compost(DataMapProvider.Builder<Compostable, Item> builder, Supplier<? extends Item> item, float chance) {
        builder.add(item.get().builtInRegistryHolder(), new Compostable(chance), false);
    }
    private void fuel(DataMapProvider.Builder<FurnaceFuel, Item> builder, Supplier<? extends Item> item, int time) {
        builder.add(item.get().builtInRegistryHolder(), new FurnaceFuel(time), false);
    }
}
