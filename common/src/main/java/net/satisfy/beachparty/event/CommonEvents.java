package net.satisfy.beachparty.event;

import dev.architectury.event.events.common.LootEvent;
import dev.architectury.event.events.common.PlayerEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootDataManager;
import net.satisfy.beachparty.block.RadioBlock;
import net.satisfy.beachparty.util.BeachpartyLoottableInjector;
import org.jetbrains.annotations.Nullable;

public class CommonEvents {

    public static void init() {
        LootEvent.MODIFY_LOOT_TABLE.register(CommonEvents::onModifyLootTable);
        PlayerEvent.PLAYER_JOIN.register(CommonEvents::onPlayerJoin);
    }

    private static void onModifyLootTable(@Nullable LootDataManager lootDataManager, ResourceLocation id, LootEvent.LootTableModificationContext ctx, boolean b) {
        BeachpartyLoottableInjector.InjectLoot(id, ctx);
    }

    private static void onPlayerJoin(ServerPlayer player) {

        ServerLevel world = player.serverLevel();
        for (BlockPos pos : RadioBlock.getAllRadioBlocks()) {
            BlockState state = world.getBlockState(pos);
            if (state.getBlock() instanceof RadioBlock && state.getValue(RadioBlock.ON)) {
                RadioBlock.sendPacket(state, world, pos, true);
            }
        }
    }
}
