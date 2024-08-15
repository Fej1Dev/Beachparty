package net.satisfy.beachparty.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.HangingSignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.satisfy.beachparty.registry.EntityTypeRegistry;
import org.jetbrains.annotations.NotNull;

public class BeachpartyHangingSignBlockEntity extends HangingSignBlockEntity {
    public BeachpartyHangingSignBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(blockPos, blockState);
    }

    @Override
    public @NotNull BlockEntityType<?> getType() {
        return EntityTypeRegistry.HANGING_SIGN.get();
    }
}