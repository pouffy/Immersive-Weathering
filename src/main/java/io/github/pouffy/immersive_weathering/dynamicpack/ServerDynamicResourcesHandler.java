package io.github.pouffy.immersive_weathering.dynamicpack;

import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import io.github.pouffy.immersive_weathering.ImmersiveWeathering;
import io.github.pouffy.immersive_weathering.configs.CommonConfigs;
import io.github.pouffy.immersive_weathering.reg.ModBlocks;
import io.github.pouffy.immersive_weathering.reg.ModItems;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.mehvahdjukaar.moonlight.api.resources.ResType;
import net.mehvahdjukaar.moonlight.api.resources.SimpleTagBuilder;
import net.mehvahdjukaar.moonlight.api.resources.StaticResource;
import net.mehvahdjukaar.moonlight.api.resources.pack.DynServerResourcesGenerator;
import net.mehvahdjukaar.moonlight.api.resources.pack.DynamicDataPack;
import net.mehvahdjukaar.moonlight.api.set.leaves.LeavesType;
import net.mehvahdjukaar.moonlight.api.set.wood.WoodType;
import net.mehvahdjukaar.moonlight.api.util.Utils;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import org.apache.logging.log4j.Logger;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Objects;

@SuppressWarnings("removal")
public class ServerDynamicResourcesHandler extends DynServerResourcesGenerator {

    public static final ServerDynamicResourcesHandler INSTANCE = new ServerDynamicResourcesHandler();

    public ServerDynamicResourcesHandler() {
        super(new DynamicDataPack(ImmersiveWeathering.res("generated_pack")));
        this.dynamicPack.setGenerateDebugResources(PlatHelper.isDev() || CommonConfigs.DEBUG_RESOURCES.get());
    }

    @Override
    public Logger getLogger() {
        return ImmersiveWeathering.LOGGER;
    }

    @Override
    public void regenerateDynamicAssets(ResourceManager manager) {
        //tag
        SimpleTagBuilder tag = SimpleTagBuilder.of(ImmersiveWeathering.res("leaf_piles"));
        tag.addEntries(ModBlocks.LEAF_PILES.values());
        dynamicPack.addTag(tag, Registries.BLOCK);
        dynamicPack.addTag(tag, Registries.ITEM);

        dynamicPack.addTag(SimpleTagBuilder.of(ImmersiveWeathering.res("bark")).addEntries(ModItems.BARK.values()), Registries.ITEM);

        dynamicPack.addResource(ResourceLocation.parse("neoforge:data_maps/item/compostables.json"), createCompostables(ModItems.LEAF_PILES.values(), ModItems.BARK.values()));

        dynamicLeaves(manager);
        dynamicBark(manager);
    }

    private void dynamicBark(ResourceManager manager) {
        StaticResource log_unstrip = StaticResource.getOrLog(manager, ResType.RECIPES.getPath(ImmersiveWeathering.res("crafting/log_unstrip/oak")));
        StaticResource log_unstrip_adv = StaticResource.getOrLog(manager, ResType.ADVANCEMENTS.getPath(ImmersiveWeathering.res("recipes/building_blocks/crafting/log_unstrip/oak")));
        StaticResource wood_unstrip = StaticResource.getOrLog(manager, ResType.RECIPES.getPath(ImmersiveWeathering.res("crafting/wood_unstrip/oak")));
        StaticResource wood_unstrip_adv = StaticResource.getOrLog(manager, ResType.ADVANCEMENTS.getPath(ImmersiveWeathering.res("recipes/building_blocks/crafting/wood_unstrip/oak")));
        StaticResource wood_from_bark = StaticResource.getOrLog(manager, ResType.RECIPES.getPath(ImmersiveWeathering.res("crafting/wood_from_bark/oak")));
        StaticResource wood_from_bark_adv = StaticResource.getOrLog(manager, ResType.ADVANCEMENTS.getPath(ImmersiveWeathering.res("recipes/building_blocks/crafting/wood_from_bark/oak")));

        for (var e : ModItems.BARK.entrySet()) {
            WoodType woodType = e.getKey();

            if (!woodType.isVanilla()) {
                var v = e.getKey();

                String path = woodType.getNamespace() + "/" + woodType.getTypeName();

                String logId = Utils.getID(woodType.log).toString();

                var stripped = woodType.getBlockOfThis("stripped_log");
                var wood = woodType.getBlockOfThis("wood");
                var strippedWood = woodType.getBlockOfThis("stripped_wood");

                if (stripped != null) {
                    try {
                        addBarkJson(Objects.requireNonNull(log_unstrip), woodType, Pair.of("oak_bark", woodType.getTypeName() + "_bark"), Pair.of("minecraft:stripped_oak_log", Utils.getID(stripped).toString()), Pair.of("minecraft:oak_log", logId));
                    } catch (Exception ex) {
                        getLogger().error("Failed to generate Log Unstrip recipe for {} : {}", v, ex);
                    }
                    try {
                        addBarkJson(Objects.requireNonNull(log_unstrip_adv), woodType, Pair.of("oak_bark", woodType.getTypeName() + "_bark"), Pair.of("log_unstrip/oak", "log_unstrip/"+path));
                    } catch (Exception ex) {
                        getLogger().error("Failed to generate Log Unstrip advancement for {} : {}", v, ex);
                    }
                }
                if (wood != null) {
                    try {
                        addBarkJson(Objects.requireNonNull(wood_from_bark), woodType, Pair.of("oak_bark", woodType.getTypeName() + "_bark"), Pair.of("minecraft:oak_wood", Utils.getID(wood).toString()), Pair.of("minecraft:oak_log", logId));
                    } catch (Exception ex) {
                        getLogger().error("Failed to generate Wood From Bark recipe for {} : {}", v, ex);
                    }
                    try {
                        addBarkJson(Objects.requireNonNull(wood_from_bark_adv), woodType, Pair.of("oak_bark", woodType.getTypeName() + "_bark"), Pair.of("wood_from_bark/oak", "wood_from_bark/"+path));
                    } catch (Exception ex) {
                        getLogger().error("Failed to generate Wood From Bark advancement for {} : {}", v, ex);
                    }
                    if (strippedWood != null) {
                        try {
                            addBarkJson(Objects.requireNonNull(wood_unstrip), woodType, Pair.of("oak_bark", woodType.getTypeName() + "_bark"), Pair.of("minecraft:oak_wood", Utils.getID(wood).toString()), Pair.of("minecraft:stripped_oak_wood", Utils.getID(strippedWood).toString()));
                        } catch (Exception ex) {
                            getLogger().error("Failed to generate Wood Unstrip recipe for {} : {}", v, ex);
                        }
                        try {
                            addBarkJson(Objects.requireNonNull(wood_unstrip_adv), woodType, Pair.of("oak_bark", woodType.getTypeName() + "_bark"), Pair.of("wood_unstrip/oak", "wood_unstrip/"+path));
                        } catch (Exception ex) {
                            getLogger().error("Failed to generate Wood Unstrip advancement for {} : {}", v, ex);
                        }
                    }
                }
            }
        }
    }

    private void dynamicLeaves(ResourceManager manager) {
        StaticResource lootTable = StaticResource.getOrLog(manager, ResType.BLOCK_LOOT_TABLES.getPath(ImmersiveWeathering.res("oak_leaf_pile")));
        StaticResource recipe = StaticResource.getOrLog(manager, ResType.RECIPES.getPath(ImmersiveWeathering.res("crafting/oak_leaf_pile")));
        StaticResource advancement = StaticResource.getOrLog(manager, ResType.ADVANCEMENTS.getPath(ImmersiveWeathering.res("recipes/decorations/crafting/oak_leaf_pile")));

        for (var e : ModBlocks.LEAF_PILES.entrySet()) {
            LeavesType leafType = e.getKey();
            if (!leafType.isVanilla()) {
                var v = e.getKey();

                String path = leafType.getNamespace() + "/" + leafType.getTypeName();
                String id = path + "_leaf_pile";

                String leavesId = Utils.getID(leafType.leaves).toString();

                //TODO: use new system
                try {
                    addLeafPileJson(Objects.requireNonNull(lootTable), id, leavesId);
                } catch (Exception ex) {
                    getLogger().error("Failed to generate Leaf Pile loot table for {} : {}", v, ex);
                }

                try {
                    addLeafPileJson(Objects.requireNonNull(recipe), id, leavesId);
                } catch (Exception ex) {
                    getLogger().error("Failed to generate Leaf Pile recipe for {} : {}", v, ex);
                }

                try {
                    addLeafPileJson(Objects.requireNonNull(advancement), id, leavesId);
                } catch (Exception ex) {
                    getLogger().error("Failed to generate Leaf Pile advancement for {} : {}", v, ex);
                }

            }
        }
    }

    @SuppressWarnings("deprecation")
    private byte[] createCompostables(Collection<BlockItem> leafPiles, Collection<Item> barks) {
        JsonObject root = new JsonObject();
        JsonObject values = new JsonObject();
        for (var pile : leafPiles) {
            pile.builtInRegistryHolder().unwrapKey().ifPresent(res -> values.addProperty(res.location().toString(), 0.1f));
        }
        for (var bark : barks) {
            bark.builtInRegistryHolder().unwrapKey().ifPresent(res -> values.addProperty(res.location().toString(), 0.8f));
        }
        root.add("values", values);
        return root.toString().getBytes(StandardCharsets.UTF_8);
    }

    public void addLeafPileJson(StaticResource resource, String id, String leafBlockId) {
        String string = new String(resource.data, StandardCharsets.UTF_8);

        String path = resource.location.getPath().replace("oak_leaf_pile", id);

        string = string.replace("oak_leaf_pile", id);
        string = string.replace("minecraft:oak_leaves", leafBlockId);

        //adds modified under my namespace
        ResourceLocation newRes = ImmersiveWeathering.res(path);
        dynamicPack.addBytes(newRes, string.getBytes(), ResType.GENERIC);
    }

    @SafeVarargs
    public final void addBarkJson(StaticResource resource, WoodType type, Pair<String, String>... replacements) {
        String string = new String(resource.data, StandardCharsets.UTF_8);
        String path = resource.location.getPath().replace("oak", type.getNamespace() + "/" + type.getTypeName());

        for (Pair<String, String> replacement : replacements) {
            string = string.replace(replacement.getFirst(), replacement.getSecond());
        }

        ResourceLocation newRes = ImmersiveWeathering.res(path);
        dynamicPack.addBytes(newRes, string.getBytes(), ResType.GENERIC);
    }
}
