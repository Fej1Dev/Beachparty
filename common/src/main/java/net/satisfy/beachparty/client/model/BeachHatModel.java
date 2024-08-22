package net.satisfy.beachparty.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.Entity;
import net.satisfy.beachparty.util.BeachpartyIdentifier;

public class BeachHatModel<T extends Entity> extends EntityModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new BeachpartyIdentifier("beach_hat"), "main");
    private final ModelPart beach_hat;

    public BeachHatModel(ModelPart root) {
        this.beach_hat = root.getChild("beach_hat");
    }

    @SuppressWarnings("unused")
    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition beach_hat = partdefinition.addOrReplaceChild("beach_hat", CubeListBuilder.create().texOffs(-18, 12).addBox(-9.0F, -1.0F, -9.0F, 18.0F, 0.0F, 18.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-4.0F, -5.0F, -4.0F, 8.0F, 4.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(24, -2).addBox(9.0F, -1.0F, 0.0F, 0.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 21.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        poseStack.pushPose();
        poseStack.scale(1.05F, 1.05F, 1.05F);
        beach_hat.render(poseStack, buffer, packedLight, packedOverlay);
        poseStack.popPose();
    }

    @Override
    public void setupAnim(T entity, float f, float g, float h, float i, float j) {

    }

    public void copyHead(ModelPart model) {
        beach_hat.copyFrom(model);
    }
}