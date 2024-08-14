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

@SuppressWarnings("unused")
public class BeachHatModel<T extends Entity> extends EntityModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new BeachpartyIdentifier("beach_hat"), "main");
    private final ModelPart beach_hat;

    public BeachHatModel(ModelPart root) {
        this.beach_hat = root.getChild("beach_hat");
    }

    @SuppressWarnings("unused")
    public static LayerDefinition createBodyLayer() {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();
        PartDefinition beach_hat = modelPartData.addOrReplaceChild("beach_hat", CubeListBuilder.create().texOffs(-22, 15).addBox(-11.0F, 0.0F, -11.0F, 22.0F, 0.0F, 22.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-5.0F, -5.01F, -5.0F, 10.0F, 5.0F, 10.0F, new CubeDeformation(0.0F))
                .texOffs(0, 39).addBox(-5.25F, -5.6F, -5.25F, 10.0F, 5.0F, 10.0F, new CubeDeformation(0.0F))
                .texOffs(42, 6).addBox(1.1F, -0.25F, -11.0F, 2.0F, 0.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(49, 14).addBox(1.1F, -0.25F, -11.0F, 2.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));
        return LayerDefinition.create(modelData, 64, 64);
    }


    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        poseStack.pushPose();
        poseStack.scale(1F, 1F, 1F);
        poseStack.translate(0F, -0.5F, 0F);
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