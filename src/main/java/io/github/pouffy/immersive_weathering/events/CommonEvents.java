package io.github.pouffy.immersive_weathering.events;

import io.github.pouffy.immersive_weathering.data.block_growths.BlockGrowthHandler;
import io.github.pouffy.immersive_weathering.data.fluid_generators.FluidGeneratorsHandler;
import net.minecraft.world.InteractionResult;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

public class CommonEvents {

    @SubscribeEvent
    public void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        var ret = ModEvents.onBlockCLicked(event.getItemStack(), event.getEntity(), event.getLevel(), event.getHand(), event.getHitVec());
        if (ret != InteractionResult.PASS) {
            event.setCanceled(true);
            event.setCancellationResult(ret);
        }
    }

    @SubscribeEvent
    public void registerListeners(AddReloadListenerEvent event) {
        event.addListener(BlockGrowthHandler.RELOAD_INSTANCE);
        event.addListener(FluidGeneratorsHandler.RELOAD_INSTANCE);
    }
}
