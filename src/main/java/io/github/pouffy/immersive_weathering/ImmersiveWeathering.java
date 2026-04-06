package io.github.pouffy.immersive_weathering;

import io.github.pouffy.immersive_weathering.configs.ClientConfigs;
import io.github.pouffy.immersive_weathering.configs.CommonConfigs;
import io.github.pouffy.immersive_weathering.data.block_growths.BlockGrowthHandler;
import io.github.pouffy.immersive_weathering.data.fluid_generators.FluidGeneratorsHandler;
import io.github.pouffy.immersive_weathering.data.fluid_generators.ModFluidGenerators;
import io.github.pouffy.immersive_weathering.data.position_tests.ModPositionRuleTests;
import io.github.pouffy.immersive_weathering.datagen.ImmersiveWeatheringDataGen;
import io.github.pouffy.immersive_weathering.datamaps.DataMapHelpers;
import io.github.pouffy.immersive_weathering.dynamicpack.ServerDynamicResourcesHandler;
import io.github.pouffy.immersive_weathering.events.CommonEvents;
import io.github.pouffy.immersive_weathering.events.ModEvents;
import io.github.pouffy.immersive_weathering.items.CeilingAndWallBlockItem;
import io.github.pouffy.immersive_weathering.network.NetworkHandler;
import io.github.pouffy.immersive_weathering.reg.*;
import net.mehvahdjukaar.moonlight.api.events.IFireConsumeBlockEvent;
import net.mehvahdjukaar.moonlight.api.events.ILightningStruckBlockEvent;
import net.mehvahdjukaar.moonlight.api.events.MoonlightEventsHelper;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(ImmersiveWeathering.MOD_ID)
public class ImmersiveWeathering {
    private static ImmersiveWeathering INSTANCE;
    public static final String MOD_ID = "immersive_weathering";
    public static final Logger LOGGER = LogManager.getLogger("Immersive Weathering");
    private final IEventBus modEventBus;

    public static ResourceLocation res(String name) {
        if (name.contains(":")) {
            return ResourceLocation.tryParse(name);
        }
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, name);
    }

    public ImmersiveWeathering(IEventBus modEventBus, ModContainer modContainer) {
        this.modEventBus = modEventBus;
        INSTANCE = this;
        CommonConfigs.init();

        if (FMLEnvironment.dist == Dist.CLIENT) {
            ClientConfigs.init();
            ImmersiveWeatheringClient.init();
        }

        PlatHelper.addCommonSetup(ImmersiveWeathering::setup);

        //noinspection removal
        ServerDynamicResourcesHandler.INSTANCE.register();


        MoonlightEventsHelper.addListener(ModEvents::onFireConsume, IFireConsumeBlockEvent.class);
        MoonlightEventsHelper.addListener(ModEvents::onLightningHit, ILightningStruckBlockEvent.class);

        modEventBus.addListener(this::registerOverrides);
        NeoForge.EVENT_BUS.register(new CommonEvents());
        NeoForge.EVENT_BUS.addListener(DataMapHelpers::onDataMapsUpdated);
        modEventBus.register(ModDataMaps.class);

        //RegHelper.addLootTableInjects(ModLootInjects::onLootInject);

        ModCreativeTab.init();

        NetworkHandler.init();

        ModBlocks.staticInit();
        ModItems.staticInit();
        ModEntities.staticInit();
        ModParticles.staticInit();
        ModRuleTests.staticInit();
        //ModFeatures.init();
        ModSoundEvents.staticInit();

        modEventBus.addListener(ImmersiveWeatheringDataGen::gatherData);
        modEventBus.register(this);
    }

    public static IEventBus getEventBus() {
        return INSTANCE.modEventBus;
    }

    public static void setup() {
        ModPositionRuleTests.register();
        ModFluidGenerators.register();
    }

    public void registerOverrides(RegisterEvent event) {
        //override
        event.register(Registries.ITEM, helper -> {
            helper.register(ResourceLocation.parse("minecraft:hanging_roots"), new CeilingAndWallBlockItem(Blocks.HANGING_ROOTS, ModBlocks.HANGING_ROOTS_WALL.get(), new Item.Properties()));
        });
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void modifyComponents(ModifyDefaultComponentsEvent event) {
        event.modify(ModItems.ICICLE.get(), builder -> {
            if (CommonConfigs.ICICLE_FOOD.get()) {
                builder.set(DataComponents.FOOD, ModFoods.ICICLE);
            } else {
                builder.remove(DataComponents.FOOD);
            }
        });
        event.modify(ModItems.ICE_SICKLE.get(), builder -> {
            if (CommonConfigs.ICICLE_FOOD.get()) {
                builder.set(DataComponents.FOOD, ModFoods.ICICLE);
            } else {
                builder.remove(DataComponents.FOOD);
            }
        });
    }
}
