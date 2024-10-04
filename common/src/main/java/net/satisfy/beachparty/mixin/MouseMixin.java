package net.satisfy.beachparty.mixin;

import dev.architectury.networking.NetworkManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.Options;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.satisfy.beachparty.block.furniture.RadioBlock;
import net.satisfy.beachparty.networking.BeachpartyMessages;
import net.satisfy.beachparty.registry.ObjectRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHandler.class)
public class MouseMixin {

    @Inject(method = "onScroll", at = @At("HEAD"), cancellable = true)
    private void MouseScrollOnRadio(long window, double horizontal, double vertical, CallbackInfo ci) {
        if (window == Minecraft.getInstance().getWindow().getWindow()) {
            Minecraft client = Minecraft.getInstance();
            if (client.hitResult instanceof BlockHitResult blockHitResult) {

                if (blockHitResult.getType() != HitResult.Type.BLOCK) return;

                BlockPos blockPos = blockHitResult.getBlockPos();
                if (client.level == null) return;
                BlockState blockState = client.level.getBlockState(blockPos);
                if (blockState.getBlock() != ObjectRegistry.RADIO.get() || !blockState.getValue(RadioBlock.ON)) return;

                int scrollValue = (int) beachparty$calculateScrollValue(vertical, client.options);
                beachparty$handleScrollEvent(blockPos, scrollValue);
                ci.cancel();
            }
        }
    }

    @Unique
    private double beachparty$calculateScrollValue(double vertical, Options options) {
        return options.discreteMouseScroll().get() ? Math.signum(vertical) : vertical * options.mouseWheelSensitivity().get();
    }

    @Unique
    private void beachparty$handleScrollEvent(BlockPos blockPos, int scrollValue) {
        if (scrollValue == 0) {
            return;
        }
        FriendlyByteBuf buffer = RadioBlock.createPacketBuf();
        buffer.writeBlockPos(blockPos);
        buffer.writeInt(scrollValue);
        NetworkManager.sendToServer(BeachpartyMessages.MOUSE_SCROLL_C2S, buffer);
    }
}
