package io.github.pouffy.immersive_weathering.reg;

import io.github.pouffy.immersive_weathering.ImmersiveWeathering;
import io.github.pouffy.immersive_weathering.entities.FallingIcicleEntity;
import io.github.pouffy.immersive_weathering.entities.FallingLayerEntity;
import io.github.pouffy.immersive_weathering.entities.FallingPropaguleEntity;
import io.github.pouffy.immersive_weathering.entities.IcicleBlockEntity;
import io.github.pouffy.immersive_weathering.util.ModUtils;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = ModUtils.createRegister(Registries.ENTITY_TYPE);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = ModUtils.createRegister(Registries.BLOCK_ENTITY_TYPE);

    public static final DeferredHolder<EntityType<?>, EntityType<FallingIcicleEntity>> FALLING_ICICLE = ENTITY_TYPES.register("falling_icicle",
            () -> EntityType.Builder.of((EntityType.EntityFactory<FallingIcicleEntity>)FallingIcicleEntity::new, MobCategory.MISC).sized(0.98F, 0.98F).clientTrackingRange(10).updateInterval(20).build("falling_icicle"));

    public static final DeferredHolder<EntityType<?>, EntityType<FallingLayerEntity>> FALLING_LAYER = ENTITY_TYPES.register("falling_layer",
            () -> EntityType.Builder.of((EntityType.EntityFactory<FallingLayerEntity>)FallingLayerEntity::new, MobCategory.MISC).sized(0.98F, 0.98F).clientTrackingRange(10).updateInterval(20).build("falling_layer"));

    public static final DeferredHolder<EntityType<?>, EntityType<FallingPropaguleEntity>> FALLING_PROPAGULE = ENTITY_TYPES.register("falling_propagule",
            () -> EntityType.Builder.of((EntityType.EntityFactory<FallingPropaguleEntity>)FallingPropaguleEntity::new, MobCategory.MISC).sized(0.28F, 0.98F).clientTrackingRange(10).updateInterval(20).build("falling_propagule"));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<IcicleBlockEntity>> ICICLE_TILE = BLOCK_ENTITIES.register("icicle", () -> BlockEntityType.Builder.of(IcicleBlockEntity::new, ModBlocks.ICICLE.get()).build(null));

    public static void staticInit() {
        ImmersiveWeathering.LOGGER.info("[Immersive Weathering] Entity Registry");
        ImmersiveWeathering.LOGGER.info("[Immersive Weathering] Block Entity Registry");
    }
}
