package net.satisfy.beachparty.forge;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.satisfy.beachparty.registry.ObjectRegistry;

public class BeachpartyEvents {

    @SubscribeEvent
    public static void onBlockRightClick(PlayerInteractEvent.RightClickBlock event) {
        BlockState state = event.getLevel().getBlockState(event.getPos());
        if (state.getBlock() == ObjectRegistry.RADIO.get()) {
            event.setCanceled(true);
        }
    }
}