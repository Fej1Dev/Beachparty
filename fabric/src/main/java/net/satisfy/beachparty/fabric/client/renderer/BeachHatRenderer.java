package net.satisfy.beachparty.fabric.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.satisfy.beachparty.item.armor.BeachpartyArmorItem;
import net.satisfy.beachparty.registry.ArmorRegistry;

public class BeachHatRenderer implements ArmorRenderer {
    @Override
    public void render(PoseStack matrices, MultiBufferSource vertexConsumers, ItemStack stack, LivingEntity entity, EquipmentSlot slot, int light, HumanoidModel<LivingEntity> contextModel) {
        BeachpartyArmorItem hat = (BeachpartyArmorItem) stack.getItem();

        Model model = ArmorRegistry.getHatModel(hat, contextModel.getHead());

        assert model != null;
        model.renderToBuffer(matrices, vertexConsumers.getBuffer(model.renderType(hat.getTexture())), light, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1F);
    }
}