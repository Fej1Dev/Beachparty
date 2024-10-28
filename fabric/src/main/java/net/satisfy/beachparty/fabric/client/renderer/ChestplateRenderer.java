package net.satisfy.beachparty.fabric.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.satisfy.beachparty.item.armor.BeachpartyArmorItem;
import net.satisfy.beachparty.item.armor.DyeableBeachpartyArmorItem;
import net.satisfy.beachparty.registry.ArmorRegistry;

public class ChestplateRenderer implements ArmorRenderer {
    @Override
    public void render(PoseStack matrices, MultiBufferSource vertexConsumers, ItemStack stack, LivingEntity entity, EquipmentSlot slot, int light, HumanoidModel<LivingEntity> contextModel) {
        Model model;
        ResourceLocation texture;
        float red = 1.0F;
        float green = 1.0F;
        float blue = 1.0F;

        if (stack.getItem() instanceof DyeableBeachpartyArmorItem dyeableArmorItem) {
            model = ArmorRegistry.chestplateModel(dyeableArmorItem, contextModel.body, contextModel.leftArm, contextModel.rightArm);
            texture = dyeableArmorItem.getTexture();
            int color = dyeableArmorItem.getColor(stack);
            red = (color >> 16 & 255) / 255.0F;
            green = (color >> 8 & 255) / 255.0F;
            blue = (color & 255) / 255.0F;
        } else if (stack.getItem() instanceof BeachpartyArmorItem beachpartyArmorItem) {
            model = ArmorRegistry.chestplateModel(beachpartyArmorItem, contextModel.body, contextModel.leftArm, contextModel.rightArm);
            texture = beachpartyArmorItem.getTexture();
        } else {
            return;
        }

        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(model.renderType(texture));
        model.renderToBuffer(matrices, vertexConsumer, light, OverlayTexture.NO_OVERLAY, red, green, blue, 1.0F);
    }
}
